package com.euler.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.utils.BeanCopyUtils;
import com.euler.platform.mapper.OpenDocumentMapper;
import com.euler.platform.service.IOpenDocumentService;
import com.euler.platform.domain.OpenDocument;
import com.euler.platform.domain.bo.OpenDocumentBo;
import com.euler.platform.domain.vo.OpenDocumentListVo;
import com.euler.platform.domain.vo.OpenDocumentVo;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文档资源中心Service业务层处理
 *
 * @author open
 * @date 2022-02-21
 */
@RequiredArgsConstructor
@Service
public class OpenDocumentServiceImpl extends ServiceImpl<OpenDocumentMapper, OpenDocument> implements IOpenDocumentService {

    private final OpenDocumentMapper baseMapper;

    /**
     * 查询文档资源中心
     *
     * @param id 文档资源中心主键
     * @return 文档资源中心
     */
    @Override
    public OpenDocumentVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询文档资源中心列表
     *
     * @param parentId 文档资源中心
     * @return 文档资源中心
     */
    @Override
    public List<OpenDocumentListVo> documentList(IdDto<Integer> parentId) {


        var wrapper = Wrappers.<OpenDocument>lambdaQuery().select(OpenDocument::getId, OpenDocument::getOrderNum, OpenDocument::getTitle, OpenDocument::getParentId, OpenDocument::getType, OpenDocument::getPath);
        List<OpenDocument> list = list(wrapper);
        var res = BeanCopyUtils.copyList(list, OpenDocumentListVo.class);

        if (parentId.getId() == 0) {
            var mainRes = res.stream().filter(a -> a.getParentId().equals(0)).collect(Collectors.toList());
            if (mainRes != null && !mainRes.isEmpty())
                mainRes.sort(Comparator.comparingInt(OpenDocumentListVo::getOrderNum));
            return mainRes;
        }
        if (parentId.getId().equals(-1))
            parentId.setId(0);

        var mainRes = res.stream().filter(a -> a.getParentId().equals(parentId.getId())).collect(Collectors.toList());


        setChildrenList(res, mainRes);
        return mainRes;


    }


    private void setChildrenList(List<OpenDocumentListVo> list, List<OpenDocumentListVo> resList) {

        if (resList == null || resList.isEmpty())
            return;
        for (var item : resList) {

            var childrenList = list.stream().filter(c -> c.getParentId().equals(item.getId())).collect(Collectors.toList());

            if (childrenList != null && !childrenList.isEmpty())
                childrenList.sort(Comparator.comparingInt(OpenDocumentListVo::getOrderNum));
            setChildrenList(list, childrenList);
            item.setChildren(childrenList);
        }


    }


    /**
     * 新增文档资源中心
     *
     * @param bo 文档资源中心
     * @return 结果
     */
    @Override
    public Boolean insertByBo(OpenDocumentBo bo) {
        OpenDocument add = BeanUtil.toBean(bo, OpenDocument.class);
        if (!bo.getParentId().equals(0))
            add.setPath(add.getPath() + "," + bo.getParentId());

        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改文档资源中心
     *
     * @param bo 文档资源中心
     * @return 结果
     */
    @Override
    public Boolean updateByBo(OpenDocumentBo bo) {
        OpenDocument update = BeanUtil.toBean(bo, OpenDocument.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(OpenDocument entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除文档资源中心
     *
     * @param ids 需要删除的文档资源中心主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
