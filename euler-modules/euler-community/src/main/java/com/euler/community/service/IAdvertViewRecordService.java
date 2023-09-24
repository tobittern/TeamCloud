package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.AdvertViewRecordBo;
import com.euler.community.domain.dto.AdvertViewRecordDto;
import com.euler.community.domain.entity.AdvertViewRecord;
import com.euler.community.domain.vo.AdvertViewRecordVo;

import java.util.Collection;
import java.util.List;

/**
 * Service接口
 *
 * @author euler
 * @date 2022-06-17
 */
public interface IAdvertViewRecordService extends IService<AdvertViewRecord> {

    /**
     * 查询
     *
     * @param id 主键
     * @return
     */
    AdvertViewRecordVo queryById(Long id);

    /**
     * 查询列表
     *
     * @param advertViewRecordDto
     * @return 集合
     */
    TableDataInfo<AdvertViewRecordVo> queryPageList(AdvertViewRecordDto advertViewRecordDto);

    /**
     * 查询列表
     *
     * @param advertViewRecordDto
     * @return 集合
     */
    List<AdvertViewRecordVo> queryList(AdvertViewRecordDto advertViewRecordDto);

    /**
     * 修改
     *
     * @param bo
     * @return 结果
     */
    Boolean insertByBo(AdvertViewRecordBo bo);

    /**
     * 修改
     *
     * @param bo
     * @return 结果
     */
    Boolean updateByBo(AdvertViewRecordBo bo);

    /**
     * 校验并批量删除信息
     *
     * @param ids 需要删除的主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 广告浏览
     * @param bo
     * @return
     */
    R<Void> view(AdvertViewRecordBo bo);
}
