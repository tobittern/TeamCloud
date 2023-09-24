package com.euler.community.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.comparator.VersionComparator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HtmlUtil;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.community.config.CommonCommunityConfig;
import com.euler.community.domain.dto.VersionDto;
import com.euler.community.domain.dto.VersionPublishDto;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.euler.community.domain.bo.VersionBo;
import com.euler.community.domain.vo.VersionVo;
import com.euler.community.domain.entity.Version;
import com.euler.community.mapper.VersionMapper;
import com.euler.community.service.IVersionService;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 版本管理Service业务层处理
 *
 * @author euler
 * @date 2022-06-01
 */
@RequiredArgsConstructor
@Service
public class VersionServiceImpl extends ServiceImpl<VersionMapper,Version> implements IVersionService {

    private final VersionMapper baseMapper;

    @Autowired
    private CommonCommunityConfig commonCommunityConfig;

    /**
     * 查询版本管理
     *
     * @param id 版本管理主键
     * @return 版本管理
     */
    @Override
    public VersionVo queryById(Long id){

        return baseMapper.selectVoById(id);
    }

    /**
     * 查询版本管理列表
     *
     * @param dto 版本管理
     * @return 版本管理
     */
    @Override
    public TableDataInfo<VersionVo> queryPageList(VersionDto dto) {
        LambdaQueryWrapper<Version> lqw = buildQueryWrapper(dto);
        Page<VersionVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            result.getRecords().forEach(version -> {
                // 更新内容的前台展示，去除标签
                if (ObjectUtil.isNotNull(version.getContent())) {
                    version.setContent(HtmlUtil.cleanHtmlTag(version.getContent()));
                }
            });
        }
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<Version> buildQueryWrapper(VersionDto dto) {
        LambdaQueryWrapper<Version> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(dto.getVersionNo()), Version::getVersionNo, dto.getVersionNo());
        lqw.eq(StringUtils.isNotBlank(dto.getSystemType()), Version::getSystemType, dto.getSystemType());
        lqw.eq(StringUtils.isNotBlank(dto.getVersionStatus()), Version::getVersionStatus, dto.getVersionStatus());
        lqw.orderByDesc(Version::getCreateTime);
        return lqw;
    }

    /**
     * 新增版本管理
     *
     * @param bo 版本管理
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R insertByBo(VersionBo bo) {
        Version add = BeanUtil.toBean(bo, Version.class);
        // 版本号：统一不带V
        add.setVersionNo(add.getVersionNo().toUpperCase());

        String filePath = add.getFilePath();

        // 服务器上传地址：https://sdk-static.eulertu.cn/package/Android/
        // 文件路径，拼上又拍云前缀
        if(filePath != null && StringUtils.isNotBlank(filePath) && !filePath.contains("http")){
            if(filePath.startsWith("/")){
                filePath= commonCommunityConfig.getYunDomain() + filePath;
            }else{
                filePath= commonCommunityConfig.getYunDomain() + "/" + filePath;
            }
        }
        add.setFilePath(filePath);

        // 上传时间
        add.setUploadTime(new Date());
        // 待发布
        add.setVersionStatus(Constants.VERSION_STATUS_1);

        String result = validEntityBeforeSave(add, true);
        if (!result.equals("success")) {
            return R.fail(result);
        }

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return R.ok("新增版本成功");
    }

    /**
     * 修改版本管理
     *
     * @param bo 版本管理
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateByBo(VersionBo bo) {
        // 验证是否传输过来主键
        if (bo.getId() == null || bo.getId() <= 0) {
            return R.fail("参数缺失");
        }

        Version update = BeanUtil.toBean(bo, Version.class);

        // 版本号：统一不带V
        update.setVersionNo(update.getVersionNo().toUpperCase());

        String filePath = update.getFilePath();
        // 文件路径，拼上又拍云前缀
        if(filePath != null && StringUtils.isNotBlank(filePath) && !filePath.contains("http")){
            if(filePath.startsWith("/")){
                filePath = commonCommunityConfig.getYunDomain() + filePath;
            }else{
                filePath = commonCommunityConfig.getYunDomain() + "/" + filePath;
            }
        }
        update.setFilePath(filePath);

        // 上传时间
        update.setUploadTime(new Date());
        // 待发布
        update.setVersionStatus(Constants.VERSION_STATUS_1);

        String result = validEntityBeforeSave(update, false);
        if (!result.equals("success")) {
            return R.fail(result);
        }
        String key = StringUtils.format("{}app_version:{}:{}", Constants.BASE_KEY, update.getSystemType(), update.getVersionNo());
        RedisUtils.deleteObject(key);
        baseMapper.updateById(update);
        return R.ok("修改版本成功");
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     * @param isAdd 是否是新增
     * @return 校验结果
     */
    private String validEntityBeforeSave(Version entity, Boolean isAdd){
        // 校验版本号
        if (entity.getVersionNo() != null) {
            if (entity.getVersionNo().startsWith("v") || entity.getVersionNo().startsWith("V")) {
                return "版本号不能带V";
            }
        }
        // android
        if(StringUtils.equals(Constants.SYSTEM_TYPE_1, entity.getSystemType())){
            if (ObjectUtil.isNull(entity.getFileName())|| ObjectUtil.isNull(entity.getFilePath())) {
                return "上传的文件名和路径不能为空";
            }
        } else if(StringUtils.equals(Constants.SYSTEM_TYPE_2, entity.getSystemType())){
            // ios 需要填下载地址
            if(StringUtils.isEmpty(entity.getDownloadUrl())){
                return "ios系统的下载地址参数缺失";
            }
        }

        // 每个系统最多存在1个待发布的版本
        LambdaQueryWrapper<Version> waitLqw = Wrappers.lambdaQuery();
        waitLqw.eq(Version::getSystemType, entity.getSystemType());
        waitLqw.eq(Version::getVersionStatus, Constants.VERSION_STATUS_1);

        List<VersionVo> list = baseMapper.selectVoList(waitLqw);

        if(isAdd){
            // 创建版本
            if(ObjectUtil.isNotNull(list) && list.size() > 0){
                return "该系统下已存在一个待发布版本，请确认版本信息";
            }
            VersionVo vo = BeanUtil.toBean(entity, VersionVo.class);
            // 版本号的校验
            return checkVersionNo(entity.getSystemType(), vo);

        } else {
            // 修正版本
            if(ObjectUtil.isNull(list) || list.size() == 0){
                return "该系统下还没有待发布版本，还不能修正";
            } else if(ObjectUtil.isNotNull(list) && list.size() > 1) {
                return "该系统下待发布版本有多个，请确认";
            }
        }

        return "success";
    }

    /**
     * 版本号的校验
     *
     * @param systemType 应用系统，'1': android  '2': ios
     * @param waitVo 待发布的版本
     * @return
     */
    private String checkVersionNo(String systemType, VersionVo waitVo){
        // 查询线上的版本号
        LambdaQueryWrapper<Version> lqw = Wrappers.<Version>lambdaQuery()
            .eq(Version::getSystemType, systemType)
            .eq(Version::getVersionStatus, Constants.VERSION_STATUS_2)
            .orderByDesc(Version::getCreateTime).last("limit 1");
        VersionVo vo = baseMapper.selectVoOne(lqw);

        if(ObjectUtil.isNotNull(vo)){
            // 比较版本号，如果要发布某个版本，版本号必须>线上版本；
            int compare = VersionComparator.INSTANCE.compare(waitVo.getVersionNo().toUpperCase(), vo.getVersionNo().toUpperCase());
            if (compare <= 0) {
                return "创建的版本号 < 线上的版本号";
            }
        }
        return "success";
    }

    /**
     * 发布版本
     *
     * @param dto
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R publish(VersionPublishDto dto) {

        // 校验是否有待发布的版本
        LambdaQueryWrapper<Version> waitLqw = Wrappers.<Version>lambdaQuery()
            .eq(Version::getSystemType, dto.getSystemType())
            .eq(Version::getVersionStatus, Constants.VERSION_STATUS_1)
            .orderByDesc(Version::getCreateTime).last("limit 1");
        VersionVo waitVo = baseMapper.selectVoOne(waitLqw);
        if(ObjectUtil.isNull(waitVo)){
            return R.fail("该系统没有待发布的版本");
        }

        // 查询线上的版本号
        LambdaQueryWrapper<Version> lqw = Wrappers.<Version>lambdaQuery()
            .eq(Version::getSystemType, dto.getSystemType())
            .eq(Version::getVersionStatus, Constants.VERSION_STATUS_2)
            .orderByDesc(Version::getCreateTime).last("limit 1");
        VersionVo vo = baseMapper.selectVoOne(lqw);

        if(ObjectUtil.isNull(vo)){
            Boolean flag = updateVersionStatus(waitVo.getId(), 0L);
            if (flag) {
                return R.ok("发布成功");
            }
        } else {
            // 比较版本号，如果要发布某个版本，版本号必须>线上版本；
            int compare = VersionComparator.INSTANCE.compare(waitVo.getVersionNo().toUpperCase(), vo.getVersionNo().toUpperCase());
            if (compare > 0) {
                Boolean flag = updateVersionStatus(waitVo.getId(), vo.getId());
                if (flag) {
                    return R.ok("发布成功");
                }
            } else {
                return R.fail("待发布的版本号 < 线上的版本号");
            }
        }

        return R.fail("发布失败");
    }

    /**
     * 更新发布的版本信息
     *
     * @param id 待发布版本id
     * @param publishedId 发布版本id
     * @return true:发布成功 false:发布失败
     */
    private Boolean updateVersionStatus(Long id, Long publishedId){
        // 待发布版本->发布
        var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper)
            .eq(Version::getId, id)
            .eq(Version::getVersionStatus, Constants.VERSION_STATUS_1)
            .set(Version::getVersionStatus, Constants.VERSION_STATUS_2)
            .set(Version::getPublishTime, new Date());

        // 执行更新操作
        boolean update = updateChainWrapper.update();


        if (update) {
            var currentVersion=this.getById(id);
            String key = StringUtils.format("{}app_version:{}:{}", Constants.BASE_KEY, currentVersion.getSystemType(), currentVersion.getVersionNo());
            RedisUtils.deleteObject(key);

            if(publishedId > 0L) {
                var downVersion=this.getById(id);
                String downkey = StringUtils.format("{}app_version:{}:{}", Constants.BASE_KEY, downVersion.getSystemType(), downVersion.getVersionNo());
                RedisUtils.deleteObject(downkey);

                // 线上版本 -> 下架
                var onlineChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper)
                    .eq(Version::getId, publishedId)
                    .eq(Version::getVersionStatus, Constants.VERSION_STATUS_2)
                    .set(Version::getVersionStatus, Constants.VERSION_STATUS_3);
                return onlineChainWrapper.update();
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * 批量删除版本管理
     *
     * @param ids 需要删除的版本管理主键
     * @return 结果
     */
    @Override
    public R deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {

        List<VersionVo> list = baseMapper.selectVoById(ids);

        if (list != null && !list.isEmpty()) {
            for (VersionVo a : list) {
                if (StringUtils.equals(Constants.VERSION_STATUS_1, a.getVersionStatus())
                    || StringUtils.equals(Constants.VERSION_STATUS_3, a.getVersionStatus())) {

                    // 只能删除待发布/已下架的版本
                    baseMapper.deleteById(a.getId());
                    String key = StringUtils.format("{}app_version:{}:{}", Constants.BASE_KEY, a.getSystemType(), a.getVersionNo());
                    RedisUtils.deleteObject(key);
                    continue;
                }
            }
        } else {
            return R.fail("删除的版本不存在");
        }
        return R.ok("删除成功");
    }

    /**
     * 判断用户是否需要更新版本
     * @param dto
     * @return true:需要强更 false:不需要强更
     */
    public R isUpdate(VersionDto dto){

        // 应用系统和版本号不能为空
        if(ObjectUtil.isNull(dto.getSystemType()) || ObjectUtil.isNull(dto.getVersionNo())){
            return R.fail("应用系统和版本号不能为空");
        }

        VersionVo updateVersion = new VersionVo();
        // 是否需要强制更新
        Boolean isForceUpdateFlag = false;
        // 是否是第一次
        Boolean isFirst = true;

        // 获取版本记录
        LambdaQueryWrapper<Version> versionLqw = Wrappers.lambdaQuery();
        versionLqw.eq(Version::getSystemType, dto.getSystemType());
        versionLqw.and(a -> a.eq(Version::getVersionStatus, Constants.VERSION_STATUS_2)
            .or(b -> b.eq(Version::getVersionStatus, Constants.VERSION_STATUS_3)));
        versionLqw.orderByDesc(Version:: getId);
        List<VersionVo> list = baseMapper.selectVoList(versionLqw);

        // 用户更新版本记录
        if (list != null && !list.isEmpty() && list.size() > 0) {
            for (VersionVo version : list) {
                // 判断是否是用户后面的版本
                int compare = VersionComparator.INSTANCE.compare(version.getVersionNo().toUpperCase(), dto.getVersionNo().toUpperCase());
                if (compare > 0) {
                    // 需要更新版本
                    if(isFirst){
                        updateVersion = version;
                        isFirst = false;
                    }
                    // 判断是否需要强制更新
                    if (version.getUpdateType() != null
                        && StringUtils.equals(Constants.UPDATE_TYPE_2, version.getUpdateType())) {
                        // 需要强制更新
                        isForceUpdateFlag = true;
                        break;
                    }
                }
            }
        }

        if(isForceUpdateFlag){
            updateVersion.setUpdateType(Constants.UPDATE_TYPE_2);
        }

        return R.ok(updateVersion);
    }

    @Override
    public Version getCurrentVersion(VersionDto versionDto){
        LambdaQueryWrapper<Version> versionLqw = Wrappers.<Version>lambdaQuery().eq(Version::getVersionNo,versionDto.getVersionNo()).eq(Version::getSystemType,versionDto.getSystemType()).last("limit 1");
        return  this.getOne(versionLqw);

    }

}
