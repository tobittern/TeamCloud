package com.euler.community.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.community.enums.DynamicFieldIncrEnum;
import com.euler.community.service.IDynamicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.euler.community.domain.bo.DynamicForwardBo;
import com.euler.community.domain.entity.DynamicForward;
import com.euler.community.mapper.DynamicForwardMapper;
import com.euler.community.service.IDynamicForwardService;

/**
 * 动态转发Service业务层处理
 *
 * @author euler
 * @date 2022-06-20
 */
@RequiredArgsConstructor
@Service
public class DynamicForwardServiceImpl extends ServiceImpl<DynamicForwardMapper,DynamicForward> implements IDynamicForwardService {

    private final DynamicForwardMapper baseMapper;

    @Autowired
    private IDynamicService iDynamicService;

    /**
     * 新增动态转发
     *
     * @param bo 动态转发
     * @return 结果
     */
    @Override
    public R insertByBo(DynamicForwardBo bo) {
        DynamicForward add = BeanUtil.toBean(bo, DynamicForward.class);
        validEntityBeforeSave(add);

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            // 分享成功后，把数据添加到记录表里
            IdTypeDto<String, Integer> objectObjectIdTypeDto = new IdTypeDto<>();
            objectObjectIdTypeDto.setId(bo.getDynamicId().toString());
            objectObjectIdTypeDto.setType(DynamicFieldIncrEnum.FORWARD.getCode());
            iDynamicService.incrDynamicSomeFieldValue(objectObjectIdTypeDto);
        }
        return R.ok();
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(DynamicForward entity){
        //TODO 做一些数据校验,如唯一约束
    }

}
