package com.euler.sdk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.sdk.domain.bo.GameUseRecordBo;
import com.euler.sdk.domain.bo.PopupGameRelationBo;
import com.euler.sdk.domain.entity.GameUseRecord;
import com.euler.sdk.domain.entity.PopupGameRelation;

import java.util.List;

/**
 * 弹窗关联游戏Service接口
 *
 * @author euler
 * @date 2022-09-05
 */
public interface IPopupGameRelationService extends IService<PopupGameRelation> {

    /**
     * 关联数据添加
     */
    int insertRelation(PopupGameRelationBo bo);

    /**
     * 关联数据修改
     */
    int updateRelation(PopupGameRelationBo bo);

    /**
     * 按照条件查询出关联的游戏
     * @param bo
     * @return
     */
    List<PopupGameRelation> selectInfoByParams(PopupGameRelationBo bo);
}
