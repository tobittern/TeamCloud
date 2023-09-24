package com.euler.sdk.service.Impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.utils.StringUtils;
import com.euler.platform.api.RemoteGameManagerService;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.sdk.domain.bo.PopupGameRelationBo;
import com.euler.sdk.domain.entity.PopupGameRelation;
import com.euler.sdk.mapper.PopupGameRelationMapper;
import com.euler.sdk.service.IPopupGameRelationService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 弹窗关联游戏Service业务层处理
 *
 * @author euler
 * @date 2022-09-05
 */
@RequiredArgsConstructor
@Service
public class PopupGameRelationServiceImpl extends ServiceImpl<PopupGameRelationMapper, PopupGameRelation> implements IPopupGameRelationService {

    @Autowired
    private PopupGameRelationMapper baseMapper;
    @DubboReference
    private RemoteGameManagerService remoteGameManagerService;

    /**
     * 关联数据添加
     */
    @Override
    public int insertRelation(PopupGameRelationBo bo) {
        if (StringUtils.isBlank(bo.getGameIds())) {
            return 0;
        }
        int rows = 1;
        // 新增关联数据
        if (bo.getPopupId() == null) {
            return rows;
        }
        Integer[] insertData = Convert.toIntArray(bo.getGameIds().split(","));
        // 这个地方就需要调一下远程的dubbo服务  查看一下那些游戏是可以正常使用的
        List<Integer> searchIds = Arrays.asList(insertData);
        if (searchIds.size() > 0) {
            List<OpenGameDubboVo> openGameDubboVos = remoteGameManagerService.selectByIds(searchIds);
            List<PopupGameRelation> list = new ArrayList<PopupGameRelation>();
            for (var x : openGameDubboVos) {
                // 过滤掉没有上线的不能使用的游戏
                PopupGameRelation popupGameRelation = new PopupGameRelation();
                popupGameRelation.setPopupId(bo.getPopupId());
                popupGameRelation.setGameId(x.getId());
                list.add(popupGameRelation);
            }
            if (list.size() > 0) {
                rows = baseMapper.insertBatch(list) ? list.size() : 0;
            }
        }
        return rows;
    }


    /**
     * 关联数据修改
     */
    @Override
    public int updateRelation(PopupGameRelationBo bo) {
        // 删除相对应的关联数据
        baseMapper.delete(new LambdaQueryWrapper<PopupGameRelation>()
            .eq(PopupGameRelation::getPopupId, bo.getPopupId()));
        // 新增
        return insertRelation(bo);
    }

    /**
     * 按照条件查询出关联的游戏
     *
     * @return
     */
    @Override
    public List<PopupGameRelation> selectInfoByParams(PopupGameRelationBo bo) {
        LambdaQueryWrapper<PopupGameRelation> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getPopupId() != null, PopupGameRelation::getPopupId, bo.getPopupId());
        lqw.in(bo.getPopupIds() != null, PopupGameRelation::getPopupId, bo.getPopupIds());
        lqw.orderByDesc(PopupGameRelation::getId);
        return baseMapper.selectVoList(lqw);
    }


}
