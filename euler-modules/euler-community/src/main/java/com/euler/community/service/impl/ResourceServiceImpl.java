package com.euler.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.ResourceBo;
import com.euler.community.domain.dto.ResourceDto;
import com.euler.community.domain.entity.Resource;
import com.euler.community.domain.vo.ResourceVo;
import com.euler.community.mapper.ResourceMapper;
import com.euler.community.service.IResourceService;
import com.euler.community.utils.CommonForCommunityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 动态所有资源Service业务层处理
 *
 * @author euler
 * @date 2022-06-09
 */
@RequiredArgsConstructor
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper,Resource> implements IResourceService {

    private final ResourceMapper baseMapper;

    /**
     * 查询动态所有资源
     *
     * @param id 动态所有资源主键
     * @return 动态所有资源
     */
    @Override
    public ResourceVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询动态所有资源列表
     *
     * @param dto 动态所有资源
     * @return 动态所有资源
     */
    @Override
    public TableDataInfo<ResourceVo> queryPageList(ResourceDto dto) {
        LambdaQueryWrapper<Resource> lqw = buildQueryWrapper(dto);
        Page<ResourceVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<Resource> buildQueryWrapper(ResourceDto dto) {
        LambdaQueryWrapper<Resource> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getMemberId() != null, Resource::getMemberId, dto.getMemberId());
        lqw.eq(dto.getDynamicId() != null, Resource::getDynamicId, dto.getDynamicId());
        lqw.eq(dto.getFileType() != null, Resource::getFileType, dto.getFileType());
        lqw.likeRight(StringUtils.isNotBlank(dto.getFileName()), Resource::getFileName, dto.getFileName());
        lqw.eq(dto.getAuditStatus() != null, Resource::getAuditStatus, dto.getAuditStatus());
        lqw.orderByDesc(Resource::getId);
        return lqw;
    }

    /**
     * 新增动态所有资源
     *
     * @param bo 动态所有资源
     * @return 结果
     */
    @Override
    public R insertByBo(ResourceBo bo) {
        if (!StringUtils.isNotBlank(bo.getFilePath())) {
            return R.fail();
        }
        String[] split = bo.getFilePath().split(",");
        int rows = 0;
        List<Resource> list = new ArrayList<Resource>();
        for (String s : split) {
            Resource resource = new Resource();
            resource.setMemberId(bo.getMemberId());
            resource.setDynamicId(bo.getDynamicId());
            Integer resourceType = CommonForCommunityUtils.getResourceType(s);
            resource.setFileType(resourceType);
            String resourceName = CommonForCommunityUtils.getResourceName(s);
            resource.setFileName(resourceName);
            resource.setFilePath(s);
            resource.setAuditStatus(1);
            list.add(resource);
        }
        if (list.size() > 0) {
            rows = baseMapper.insertBatch(list) ? list.size() : 0;
        }
        if (rows > 0) {
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(Resource entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除动态所有资源
     *
     * @param ids 需要删除的动态所有资源主键
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
