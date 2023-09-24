package com.euler.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.utils.StringUtils;
import com.euler.system.api.domain.NoticeToUsers;
import com.euler.system.domain.bo.NoticeToUsersBo;
import com.euler.system.domain.dto.NoticeToUsersDto;
import com.euler.system.domain.vo.NoticeToUsersVo;
import com.euler.system.mapper.NoticeToUsersMapper;
import com.euler.system.service.INoticeToUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 公告接收Service业务层处理
 *
 * @author euler
 * @date 2022-04-08
 */
@RequiredArgsConstructor
@Service
public class NoticeToUsersServiceImpl extends ServiceImpl<NoticeToUsersMapper,NoticeToUsers> implements INoticeToUsersService {

    private final NoticeToUsersMapper baseMapper;

    @Override
    public com.euler.system.api.domain.NoticeToUsers selectUnreadNotice(NoticeToUsersDto dto) {
        NoticeToUsers users = baseMapper.selectOne(new LambdaQueryWrapper<NoticeToUsers>()
            .eq(dto.getNoticeId()!= null, NoticeToUsers::getNoticeId, dto.getNoticeId())
            .eq(NoticeToUsers::getToUserId, dto.getToUserId())
            .eq(StringUtils.isNotBlank(dto.getReadStatus()), NoticeToUsers::getReadStatus, dto.getReadStatus()).last("limit 1"));

        return users;
    }

    /**
     * 查询公告接收
     *
     * @param id 公告接收主键
     * @return 公告接收
     */
    @Override
    public NoticeToUsersVo queryById(Integer id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 根据公告id查询公告接收者列表
     *
     * @param noticeId 公告id
     * @return 公告接收
     */
    @Override
    public List<NoticeToUsersVo> selectVoByNoticeId(Integer noticeId){
        LambdaQueryWrapper<NoticeToUsers> lqw = Wrappers.lambdaQuery();
        lqw.eq(noticeId > 0, NoticeToUsers::getNoticeId, noticeId);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询公告接收列表
     *
     * @param dto 公告接收
     * @return 公告接收
     */
    @Override
    public List<NoticeToUsersVo> queryList(NoticeToUsersDto dto) {
        LambdaQueryWrapper<NoticeToUsers> lqw = buildQueryWrapper(dto);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<NoticeToUsers> buildQueryWrapper(NoticeToUsersDto dto) {
        LambdaQueryWrapper<NoticeToUsers> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getNoticeId() != null, NoticeToUsers::getNoticeId, dto.getNoticeId());
        lqw.eq(dto.getToUserId() != null, NoticeToUsers::getToUserId, dto.getToUserId());
        lqw.eq(StringUtils.isNotBlank(dto.getReadStatus()), NoticeToUsers::getReadStatus, dto.getReadStatus());
        return lqw;
    }

    /**
     * 新增公告接收
     *
     * @param bo 公告接收
     * @return 结果
     */
    @Override
    public Boolean insertByBo(NoticeToUsersBo bo) {
        NoticeToUsers add = BeanUtil.toBean(bo, NoticeToUsers.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改公告接收
     *
     * @param bo 公告接收
     * @return 结果
     */
    @Override
    public Boolean updateByBo(NoticeToUsersBo bo) {
        NoticeToUsers update = BeanUtil.toBean(bo, NoticeToUsers.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(NoticeToUsers entity){
        //TODO 做一些数据校验,如唯一约束
    }

}
