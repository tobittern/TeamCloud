package com.euler.community.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.ReportBo;
import com.euler.community.domain.dto.ReportDto;
import com.euler.community.domain.entity.Comment;
import com.euler.community.domain.entity.Report;
import com.euler.community.domain.vo.ReportVo;
import com.euler.community.enums.DynamicFieldIncrEnum;
import com.euler.community.mapper.CommentMapper;
import com.euler.community.mapper.ReportMapper;
import com.euler.community.service.IDynamicService;
import com.euler.community.service.IReportService;
import com.euler.sdk.api.RemoteMemberService;
import com.euler.sdk.api.domain.MemberProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 举报Service业务层处理
 *
 * @author euler
 * @date 2022-06-09
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper,Report> implements IReportService {

    private final ReportMapper baseMapper;

    @Autowired
    private IDynamicService iDynamicService;
    @Autowired
    private CommentMapper commentMapper;
    @DubboReference
    private RemoteMemberService remoteMemberService;

    /**
     * 查询举报
     *
     * @param id 举报主键
     * @return 举报
     */
    @Override
    public ReportVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询举报列表
     *
     * @param dto 举报
     * @return 举报
     */
    @Override
    public TableDataInfo<ReportVo> queryPageList(ReportDto dto) {
        QueryWrapper<Report> lqw = buildQueryWrapper(dto);
        Page<ReportVo> result = null;
        if (StringUtils.equals("1", dto.getType())) {
            result = baseMapper.selectFrontDamicList(dto.build(), lqw);
        } else if (StringUtils.equals("2", dto.getType())) {
            result = baseMapper.selectFrontCommentsList(dto.build(), lqw);
        }
        if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            result.getRecords().forEach(a -> {
                // 举报人昵称
                MemberProfile member = remoteMemberService.getMemberByUserId(a.getMemberId());
                if (ObjectUtil.isNotNull(member)) {
                    // 动态的昵称
                    a.setReportUserName(member.getNickName());
                }

            });
        }
        return TableDataInfo.build(result);
    }

    private QueryWrapper<Report> buildQueryWrapper(ReportDto dto) {
        QueryWrapper<Report> lqw = Wrappers.<Report>query()
            // 举报类型 1动态 2评论
            .eq(StringUtils.isNotBlank(dto.getType()), "r.`type`", dto.getType())
            .eq(dto.getRelationId() != null, "r.`relation_id`", dto.getRelationId())
            .eq(dto.getDynamicId() != null, "r.`dynamic_id`", dto.getDynamicId())
            .eq(dto.getMemberId() != null, "r.`member_id`", dto.getMemberId());
        return lqw;
    }

    /**
     * 举报
     *
     * @param bo 举报
     * @return 结果
     */
    @Override
    public R insertByBo(ReportBo bo) {
        Report add = BeanUtil.toBean(bo, Report.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            if (bo.getType().equals("1")) {
                // 添加关联数据
                IdTypeDto<String, Integer> idTypeDto = new IdTypeDto<>();
                idTypeDto.setId(bo.getRelationId().toString());
                idTypeDto.setType(DynamicFieldIncrEnum.REPORT.getCode());
                iDynamicService.incrDynamicSomeFieldValue(idTypeDto);
            } else {
                // 举报评论 累加次数
                Comment comment = commentMapper.selectOne(Wrappers.<Comment>lambdaQuery().eq(Comment::getId, bo.getRelationId()));
                if (comment != null) {
                    Comment updateComment = new Comment();
                    updateComment.setId(bo.getRelationId());
                    updateComment.setReportNum(comment.getReportNum() + 1);
                    commentMapper.updateById(updateComment);
                }
            }
            return R.ok("举报成功！");
        }
        return R.fail("举报失败");
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(Report entity) {
        //TODO 做一些数据校验,如唯一约束
    }

}
