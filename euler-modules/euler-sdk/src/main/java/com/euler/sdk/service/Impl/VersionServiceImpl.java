package com.euler.sdk.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.constant.VersionConstant;
import com.euler.sdk.domain.bo.VersionBo;
import com.euler.sdk.domain.dto.VersionDto;
import com.euler.sdk.domain.entity.Version;
import com.euler.sdk.domain.vo.VersionVo;
import com.euler.sdk.mapper.VersionMapper;
import com.euler.sdk.service.IVersionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * sdk版本管理Service业务层处理
 *
 * @author euler
 * 2022-07-08
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class VersionServiceImpl extends ServiceImpl<VersionMapper, Version> implements IVersionService {

    private final VersionMapper baseMapper;

    @Resource
    private com.euler.sdk.config.WebConfig WebConfig;

    /**
     * 查询sdk版本管理
     *
     * @param id sdk版本管理主键
     * @return sdk版本管理
     */
    @Override
    public VersionVo queryById(Long id) {
        VersionVo versionVo = baseMapper.selectVoById(id);
        if (versionVo != null) {
            versionVo.setDate1(versionVo.getCreateTime());
            versionVo.setDate2(versionVo.getCreateTime());
        }
        return versionVo;
    }

    /**
     * 查询sdk版本管理列表
     *
     * @param versionDto sdk版本管理
     * @return sdk版本管理
     */
    @Override
    public TableDataInfo<VersionVo> queryPageList(VersionDto versionDto) {
        LambdaQueryWrapper<Version> lqw = buildQueryWrapper(versionDto);
        Page<VersionVo> result = baseMapper.selectVoPage(versionDto.build(), lqw);
        if (result != null&&result.getRecords()!=null&&!result.getRecords().isEmpty()) {
            result.getRecords().forEach(v->{
                String fileUrl = v.getFileUrl();
                v.setFileUrl(WebConfig.getYunDomain() + fileUrl);
                v.setDate1(v.getCreateTime());
                v.setDate2(v.getCreateTime());
            });

        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询sdk版本管理列表
     *
     * @param versionDto sdk版本管理
     * @return sdk版本管理
     */
    @Override
    public List<VersionVo> queryList(VersionDto versionDto) {
        LambdaQueryWrapper<Version> lqw = buildQueryWrapper(versionDto);
        List<VersionVo> versionVos = baseMapper.selectVoList(lqw);
        for (VersionVo v : versionVos) {
            String fileUrl = v.getFileUrl();
            v.setFileUrl(WebConfig.getYunDomain() + fileUrl);
            v.setDate1(v.getCreateTime());
            v.setDate2(v.getCreateTime());
        }
        return versionVos;
    }

    private LambdaQueryWrapper<Version> buildQueryWrapper(VersionDto versionDto) {
        LambdaQueryWrapper<Version> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(versionDto.getNumber()), Version::getNumber, versionDto.getNumber());
        lqw.eq(versionDto.getPlatform() != null, Version::getPlatform, versionDto.getPlatform());
        lqw.eq(versionDto.getType() != null, Version::getType, versionDto.getType());
        lqw.eq(versionDto.getIsNew() != null, Version::getIsNew, versionDto.getIsNew());
        lqw.like(StringUtils.isNotBlank(versionDto.getContent()), Version::getContent, versionDto.getContent());
        lqw.eq(StringUtils.isNotBlank(versionDto.getFileUrl()), Version::getFileUrl, versionDto.getFileUrl());
        //时间
        lqw.ge(versionDto.getStartTime() != null, Version::getCreateTime, versionDto.getStartTime());
        lqw.le(versionDto.getEndTime() != null, Version::getCreateTime, versionDto.getEndTime());
        lqw.orderByDesc(Version::getCreateTime);
        return lqw;
    }

    /**
     * 新增sdk版本管理
     *
     * @param bo sdk版本管理
     * @return 结果
     */
    @Override
    public R<Void> insertByBo(VersionBo bo) {
        if (bo.getPlatform() == null) {
            return R.fail("运行平台不能为空");
        }
        if (bo.getType() == null) {
            return R.fail("版本类型不能为空");
        }
        if (bo.getIsNew() == null) {
            return R.fail("是否是新版本不能为空");
        }
        HashMap<Boolean, R<Void>> validateMap = validateParam(bo);
        for (Map.Entry<Boolean, R<Void>> entry : validateMap.entrySet()) {
            Boolean validateFlag = entry.getKey();
            R<Void> r = entry.getValue();
            if (!validateFlag) {
                return r;
            }
        }
        Version add = BeanUtil.toBean(bo, Version.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return R.ok();
    }

    public HashMap<Boolean, R<Void>> validateParam(VersionBo bo) {
        HashMap<Boolean, R<Void>> map = new HashMap<>();
        //校验附件
        String fileUrl = bo.getFileUrl();
        if (StringUtils.isBlank(fileUrl)) {
            map.put(false, R.fail("请上传文件"));
            return map;
        } else {
            String suffix = fileUrl.substring(fileUrl.lastIndexOf("."));
            //运行平台 (1 安卓  2 ios  3 h5)
            if (bo.getPlatform() == 1) {
                String[] arr = VersionConstant.ANDROID_SUFFIX.split(",");
                List<String> arrList = Stream.of(arr).collect(Collectors.toList());
                if (!arrList.contains(suffix)) {
                    map.put(false, R.fail("安卓文件上传格式错误"));
                    return map;
                }
            } else if (bo.getPlatform() == 2) {
                String[] arr = VersionConstant.IOS_SUFFIX.split(",");
                List<String> arrList = Stream.of(arr).collect(Collectors.toList());
                if (!arrList.contains(suffix)) {
                    map.put(false, R.fail("IOS文件上传格式错误"));
                    return map;
                }
            } else if (bo.getPlatform() == 3) {
                String[] arr = VersionConstant.H5_SUFFIX.split(",");
                List<String> arrList = Stream.of(arr).collect(Collectors.toList());
                if (!arrList.contains(suffix)) {
                    map.put(false, R.fail("H5文件上传格式错误"));
                    return map;
                }
            }
        }


        map.put(true, R.ok());
        return map;
    }

    /**
     * 修改sdk版本管理
     *
     * @param bo sdk版本管理
     * @return 结果
     */
    @Override
    public R<Void> updateByBo(VersionBo bo) {
        if (bo.getId() == null) {
            return R.fail("id不能为空");
        }
        Version v = baseMapper.selectById(bo.getId());
        bo.setPlatform(v.getPlatform());//修改设置成原平台
        HashMap<Boolean, R<Void>> validateMap = validateParam(bo);
        for (Map.Entry<Boolean, R<Void>> entry : validateMap.entrySet()) {
            Boolean validateFlag = entry.getKey();
            R<Void> r = entry.getValue();
            if (!validateFlag) {
                return r;
            }
        }
        Version update = new Version();
        update.setId(v.getId());
        //判断如果 版本号、更新内容、文件路径 三者都不一样的情况下，将原来的数据更新为历史数据,同时新增最新数据
        if (v.getNumber().equals(bo.getNumber()) && v.getContent().equals(bo.getContent()) && v.getFileUrl().equals(bo.getFileUrl())) {
            log.info("版本号、更新内容、文件路径没有改变,不更新数据");
            return R.ok();
        } else {
            update.setIsNew(1);
        }
        int updateFlag = baseMapper.updateById(update);
        //同时插入新数据
        bo.setType(v.getType());
        bo.setIsNew(0);
        //将id设置为null，以便插入数据
        bo.setId(null);
        R<Void> insert = insertByBo(bo);
        int code = insert.getCode();
        if (R.SUCCESS == code && updateFlag > 0) {
            return R.ok();
        } else {
            return R.fail("更新异常");
        }
    }

    /**
     * 批量删除sdk版本管理
     *
     * @param ids 需要删除的sdk版本管理主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public R<Void> delete(Long id) {
        if (id == null) {
            return R.fail("id不能为空");
        }
        return baseMapper.deleteById(id) > 0 ? R.ok() : R.fail("删除失败");
    }

    @Override
    public Object getNewVersions() {
        Map<String, Object> map = new HashMap<>();
        LambdaQueryWrapper<Version> lqw1 = Wrappers.lambdaQuery();
        lqw1.eq(true, Version::getType, 0);//稳定版
        lqw1.eq(true, Version::getIsNew, 0);
        lqw1.orderByAsc(true, Version::getPlatform);
        List<VersionVo> versionVo1 = baseMapper.selectVoList(lqw1);
        for (VersionVo v : versionVo1) {
            v.setFileUrl(WebConfig.getYunDomain() + v.getFileUrl());
            v.setDate1(v.getCreateTime());
            v.setDate2(v.getCreateTime());
        }

        LambdaQueryWrapper<Version> lqw2 = Wrappers.lambdaQuery();
        lqw2.eq(true, Version::getType, 1);//最新版
        lqw2.eq(true, Version::getIsNew, 0);
        lqw2.orderByAsc(true, Version::getPlatform);
        List<VersionVo> versionVo2 = baseMapper.selectVoList(lqw2);
        for (VersionVo v : versionVo2) {
            v.setFileUrl(WebConfig.getYunDomain() + v.getFileUrl());
            v.setDate1(v.getCreateTime());
            v.setDate2(v.getCreateTime());
        }
        map.put("release", versionVo1);
        map.put("newest", versionVo2);
        return map;
    }

    @Override
    public Object getSdkVersions() {
        Map<String, Object> map = new HashMap<>();
        LambdaQueryWrapper<Version> lqw = Wrappers.lambdaQuery();
        lqw.eq(true, Version::getIsNew, 0);
        List<VersionVo> versionVoList = baseMapper.selectVoList(lqw);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        Map<String, Object> map3 = new HashMap<>();

        Map<String, Object> androidMap2 = new HashMap<>();
        Map<String, Object> iosMap2 = new HashMap<>();
        Map<String, Object> h5Map2 = new HashMap<>();


        for (VersionVo v : versionVoList) {
            v.setFileUrl(WebConfig.getYunDomain() + v.getFileUrl());
            v.setDate1(v.getCreateTime());
            v.setDate2(v.getCreateTime());

            Map<String, Object> map1 = new HashMap<>();
            map1.put("version", v.getNumber());
            map1.put("date", sdf.format(v.getDate2()));
            map1.put("content", v.getContent());
            map1.put("link", v.getFileUrl());


            if (v.getPlatform() == 1) {
                if (0 == v.getType()) {
                    androidMap2.put("stable", map1);
                } else if (1 == v.getType()) {
                    androidMap2.put("latest", map1);
                }


            } else if (v.getPlatform() == 2) {
                if (0 == v.getType()) {
                    iosMap2.put("stable", map1);
                } else if (1 == v.getType()) {
                    iosMap2.put("latest", map1);
                }
            } else if (v.getPlatform() == 3) {
                if (0 == v.getType()) {
                    h5Map2.put("stable", map1);
                } else if (1 == v.getType()) {
                    h5Map2.put("latest", map1);
                }
            }
        }

        map3.put("Android", androidMap2);
        map3.put("iOS", iosMap2);
        map3.put("H5", h5Map2);

        map.put("datasource", map3);
        return map;
    }
}
