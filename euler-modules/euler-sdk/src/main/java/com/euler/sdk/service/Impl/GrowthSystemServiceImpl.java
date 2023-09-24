package com.euler.sdk.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.euler.sdk.domain.dto.GrowthSystemDto;
import com.euler.sdk.domain.vo.GrowthConfigVo;
import com.euler.sdk.mapper.GrowthConfigMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.euler.sdk.domain.bo.GrowthSystemBo;
import com.euler.sdk.domain.vo.GrowthSystemVo;
import com.euler.sdk.domain.entity.GrowthSystem;
import com.euler.sdk.mapper.GrowthSystemMapper;
import com.euler.sdk.service.IGrowthSystemService;

/**
 * 成长体系Service业务层处理
 *
 * @author euler
 * @date 2022-03-22
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class GrowthSystemServiceImpl implements IGrowthSystemService {

    private final GrowthSystemMapper baseMapper;
    private final GrowthConfigMapper growthConfigMapper;

    /**
     * 查询成长体系
     *
     * @param id 成长体系主键
     * @return 成长体系
     */
    @Override
    public GrowthSystemVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询成长体系列表
     *
     * @param dto 成长体系
     * @return 成长体系
     */
    @Override
    public TableDataInfo<GrowthSystemVo> queryPageList(GrowthSystemDto dto) {
        LambdaQueryWrapper<GrowthSystem> lqw = buildQueryWrapper(dto);
        Page<GrowthSystemVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<GrowthSystem> buildQueryWrapper(GrowthSystemDto dto) {
        LambdaQueryWrapper<GrowthSystem> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getUserId() != null, GrowthSystem::getUserId, dto.getUserId());
        lqw.eq(dto.getGradeType() != null, GrowthSystem::getGradeType, dto.getGradeType());
        lqw.eq(dto.getGrowthGrade() != null, GrowthSystem::getGrowthGrade, dto.getGrowthGrade());
        lqw.eq(dto.getMoney() != null, GrowthSystem::getMoney, dto.getMoney());
        return lqw;
    }

    /**
     * 新增成长体系
     *
     * @param bo 成长体系
     * @return 结果
     */
    @Override
    public R insertByBo(GrowthSystemBo bo) {

        // 充值所兑换的成长值 = 用户充值的钱 * 一元能够兑换的成长值
        Long moneyGrowthValue = bo.getMoney() * bo.getGrowthValue();
        bo.setMoneyGrowthValue(moneyGrowthValue);

        // TODO 总的成长值的计算方式 还没有确定
        bo.setGrowthTotal(moneyGrowthValue);

        GrowthSystem add = BeanUtil.toBean(bo, GrowthSystem.class);
        log.info("添加的字段列表->" + JsonUtils.toJsonString(add));
        String result = validEntityBeforeSave(add, false);
        if (!result.equals("success")) {
            return R.fail(result);
        }
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            return R.ok();
        }
        return R.fail("新增失败");
    }

    /**
     * 成长值升级
     *
     * @param bo 成长体系
     * @return 结果
     */
    public R upgradeGrowth(GrowthSystemBo bo){
        // 验证是否传输过来主键
        if (bo.getId() == null || bo.getId() <= 0) {
            return R.fail("参数缺失");
        }
        // 充值所兑换的成长值 = 用户充值的钱 * 一元能够兑换的成长值
        Long moneyGrowthValue = bo.getMoney() * bo.getGrowthValue();
        bo.setMoneyGrowthValue(moneyGrowthValue);
        // 总的成长值
        bo.setGrowthTotal(moneyGrowthValue);

        GrowthSystem update = BeanUtil.toBean(bo, GrowthSystem.class);
        String result = validEntityBeforeSave(update, true);
        if (!result.equals("success")) {
            return R.fail(result);
        }
        int i = baseMapper.updateById(update);
        if (i > 0) {
            return R.ok();
        }
        return R.fail("升级失败");
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     * @param isUpdate 是否是更新
     * @return 校验结果
     */
    private String validEntityBeforeSave(GrowthSystem entity, Boolean isUpdate){
        //TODO 做一些数据校验,如唯一约束

        // 拟定用户充值10万人民币可获得平台最高称号；
        if(entity.getMoney() > 100000){
            return "已获得最高等级称号，请确认充值金额";
        }

        // 升级时的校验
        if(isUpdate){
            // 获取原来的成长值体系
            GrowthSystemVo vo = baseMapper.selectVoById(entity.getId());

            if(ObjectUtil.isNull(vo)){
                return "该用户的成长值等级不存在，无法升级";
            } else {
                if (entity.getGradeType() <= vo.getGradeType()
                    || entity.getGrowthGrade() <= vo.getGrowthGrade()) {
                    return "该用户升级的成长值不能比原来还低";
                }
            }

            // 读取配置文件，获取升级所需的成长值
            GrowthConfigVo configVo = growthConfigMapper.selectVoByPrams(entity.getGradeType(), entity.getGrowthGrade());
            if(ObjectUtil.isNull(configVo)){
                return "成长值类型或等级有误";
            }

            // 升级所需的成长值
            Long upgradeValue = configVo.getUpgradeValue();
            if(entity.getGrowthTotal() < upgradeValue){
                return "还未达到升级的成长值...";
            }

        }
        return "success";
    }

}
