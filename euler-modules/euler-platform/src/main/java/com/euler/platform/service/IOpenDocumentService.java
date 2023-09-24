package com.euler.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.platform.domain.OpenDocument;
import com.euler.platform.domain.bo.OpenDocumentBo;
import com.euler.platform.domain.vo.OpenDocumentListVo;
import com.euler.platform.domain.vo.OpenDocumentVo;

import java.util.Collection;
import java.util.List;

/**
 * 文档资源中心Service接口
 *
 * @author open
 * @date 2022-02-21
 */
public interface IOpenDocumentService extends IService<OpenDocument> {

    /**
     * 查询文档资源中心
     *
     * @param id 文档资源中心主键
     * @return 文档资源中心
     */
    OpenDocumentVo queryById(Long id);

    /**
     * 查询文档资源中心列表
     *
     * @param bo 文档资源中心
     * @return 文档资源中心集合
     */
    List<OpenDocumentListVo> documentList(IdDto<Integer> idDto);



    /**
     * 修改文档资源中心
     *
     * @param bo 文档资源中心
     * @return 结果
     */
    Boolean insertByBo(OpenDocumentBo bo);

    /**
     * 修改文档资源中心
     *
     * @param bo 文档资源中心
     * @return 结果
     */
    Boolean updateByBo(OpenDocumentBo bo);

    /**
     * 校验并批量删除文档资源中心信息
     *
     * @param ids 需要删除的文档资源中心主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
