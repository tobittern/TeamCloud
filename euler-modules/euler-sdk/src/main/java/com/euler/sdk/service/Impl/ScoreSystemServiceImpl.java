package com.euler.sdk.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.sdk.api.enums.RechargeTypeEnum;
import com.euler.sdk.api.domain.ScoreSystemBo;
import com.euler.sdk.domain.dto.ScoreSystemDto;
import com.euler.sdk.domain.entity.ScoreSystem;
import com.euler.sdk.domain.vo.ScoreSystemVo;
import com.euler.sdk.mapper.ScoreSystemMapper;
import com.euler.sdk.service.IScoreSystemService;
import com.euler.sdk.service.IWalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 积分体系Service业务层处理
 *
 * @author euler
 * @date 2022-03-22
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ScoreSystemServiceImpl implements IScoreSystemService {

    private final ScoreSystemMapper baseMapper;
    private final IWalletService walletService;

    /**
     * 查询积分体系
     *
     * @param id 积分体系主键
     * @return 积分体系
     */
    @Override
    public ScoreSystemVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询积分体系列表
     *
     * @param dto 积分体系
     * @return 积分体系
     */
    @Override
    public TableDataInfo<ScoreSystemVo> queryPageList(ScoreSystemDto dto) {
        LambdaQueryWrapper<ScoreSystem> lqw = buildQueryWrapper(dto);
        Page<ScoreSystemVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<ScoreSystem> buildQueryWrapper(ScoreSystemDto dto) {
        LambdaQueryWrapper<ScoreSystem> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getUserId() != null, ScoreSystem::getUserId, dto.getUserId());
        lqw.eq(dto.getScore() != null, ScoreSystem::getScore, dto.getScore());
        return lqw;
    }

    /**
     * 计算积分，并新增到积分体系和钱包中
     *
     * @param bo 积分体系
     * @return 结果
     */
    @Override
    public R calculateScore(ScoreSystemBo bo) {
        ScoreSystem add = BeanUtil.toBean(bo, ScoreSystem.class);
        log.debug("添加的字段列表->" + JsonUtils.toJsonString(add));

        // 总积分
        Long score = 0L;
        score = add.getScore();
        // 总积分=首次注册积分+签到积分
        add.setScore(score);

        if (score > 0) {
            // 新增到积分体系
            boolean flag = baseMapper.insert(add) > 0;
            // 更新钱包表里的积分
            String addDesc = "增加积分";
            if (StringUtils.isNotBlank(bo.getDesc())) {
                addDesc = bo.getDesc();
            }
            boolean walletFlag = walletService.modifyWallet(add.getUserId(), 0, 1, score, RechargeTypeEnum.score, 1, addDesc);


            if (flag && walletFlag) {
                bo.setId(add.getId());
                return R.ok();
            }
        }
        return R.fail("新增失败");
    }

    /**
     * 修改积分体系
     *
     * @param bo 积分体系
     * @return 结果
     */
    @Override
    public R updateByBo(ScoreSystemBo bo) {
        // 验证是否传输过来主键
        if (bo.getId() == null || bo.getId() <= 0) {
            return R.fail("参数缺失");
        }
        ScoreSystem update = BeanUtil.toBean(bo, ScoreSystem.class);
        String result = validEntityBeforeSave(update);
        if (!result.equals("success")) {
            return R.fail(result);
        }

        int i = baseMapper.updateById(update);

        // 更新钱包表里的积分
        boolean walletFlag = walletService.modifyWallet(update.getUserId(),LoginHelper.getSdkChannelPackage().getGameId(), 1,update.getScore(), RechargeTypeEnum.score, 1, "增加积分");

        if (i > 0 && walletFlag) {
            return R.ok();
        }
        return R.fail("更新失败");
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     * @return 校验结果
     */
    private String validEntityBeforeSave(ScoreSystem entity) {
        //TODO 做一些数据校验,如唯一约束
        return "success";
    }

}
