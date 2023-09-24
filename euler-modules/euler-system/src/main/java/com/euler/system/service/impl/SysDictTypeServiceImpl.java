package com.euler.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.common.core.utils.BeanCopyUtils;
import com.euler.common.core.utils.HttpRequestHeaderUtils;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.platform.api.RemoteGameManagerService;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.sdk.api.RemoteGameConfigService;
import com.euler.sdk.api.domain.GameConfigVo;
import com.euler.system.api.domain.SysDictData;
import com.euler.system.api.domain.SysDictType;
import com.euler.system.domain.dto.SdkMainMenuDto;
import com.euler.system.domain.dto.SdkMenuDto;
import com.euler.system.domain.dto.SdkSubMenuDto;
import com.euler.system.domain.vo.SdkMainMenuVo;
import com.euler.system.domain.vo.SdkSubMenuVo;
import com.euler.system.mapper.SysDictDataMapper;
import com.euler.system.mapper.SysDictTypeMapper;
import com.euler.system.service.ISdkMainMenuService;
import com.euler.system.service.ISdkSubMenuService;
import com.euler.system.service.ISysDictTypeService;
import com.euler.common.core.constant.UserConstants;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.dict.utils.DictUtils;
import com.euler.common.mybatis.core.page.PageQuery;
import com.euler.common.mybatis.core.page.TableDataInfo;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 字典 业务层处理
 *
 * @author euler
 */
@RequiredArgsConstructor
@Service
public class SysDictTypeServiceImpl implements ISysDictTypeService {

    private final SysDictTypeMapper baseMapper;
    private final SysDictDataMapper dictDataMapper;
    @Autowired
    private ISdkMainMenuService sdkMainMenuService;
    @Autowired
    private ISdkSubMenuService sdkSubMenuService;
    @DubboReference
    private RemoteGameManagerService remoteGameManagerService;
    @DubboReference
    private RemoteGameConfigService remoteGameConfigService;

    @Override
    public TableDataInfo<SysDictType> selectPageDictTypeList(SysDictType dictType, PageQuery pageQuery) {
        Map<String, Object> params = dictType.getParams();
        LambdaQueryWrapper<SysDictType> lqw = new LambdaQueryWrapper<SysDictType>()
            .likeRight(StringUtils.isNotBlank(dictType.getDictName()), SysDictType::getDictName, dictType.getDictName())
            .eq(StringUtils.isNotBlank(dictType.getStatus()), SysDictType::getStatus, dictType.getStatus())
            .likeRight(StringUtils.isNotBlank(dictType.getDictType()), SysDictType::getDictType, dictType.getDictType())
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                SysDictType::getCreateTime, params.get("beginTime"), params.get("endTime")).orderByDesc(SysDictType::getDictId);
        Page<SysDictType> page = baseMapper.selectPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    @Override
    public List<SysDictType> selectDictTypeList(SysDictType dictType) {
        Map<String, Object> params = dictType.getParams();
        return baseMapper.selectList(new LambdaQueryWrapper<SysDictType>()
            .likeRight(StringUtils.isNotBlank(dictType.getDictName()), SysDictType::getDictName, dictType.getDictName())
            .eq(StringUtils.isNotBlank(dictType.getStatus()), SysDictType::getStatus, dictType.getStatus())
            .likeRight(StringUtils.isNotBlank(dictType.getDictType()), SysDictType::getDictType, dictType.getDictType())
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                SysDictType::getCreateTime, params.get("beginTime"), params.get("endTime")));
    }

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    @Override
    public List<SysDictType> selectDictTypeAll() {
        return baseMapper.selectList();
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        List<SysDictData> dictDatas = DictUtils.getDictCache(dictType);
        if (CollUtil.isNotEmpty(dictDatas)) {
            return dictDatas;
        }
        dictDatas = dictDataMapper.selectDictDataByType(dictType);
        if (CollUtil.isNotEmpty(dictDatas)) {
            DictUtils.setDictCache(dictType, dictDatas);
            return dictDatas;
        }
        return null;
    }

    /**
     * 根据字典类型查询所有的字典数据（包含停用的字典数据）
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectAllDictDataByType(String dictType) {
        List<SysDictData> dictDatas = dictDataMapper.selectAllDictDataByType(dictType);
        return dictDatas;
    }

    /**
     * 获取sdk菜单信息
     *
     * @param dictType 字典类型
     * @param dictType 字典信息
     * @return sdk菜单信息
     */
    @Override
    public List<SdkMenuDto> getSdkMenuInfo(String dictType, List<SysDictData> data) {
        List<SdkMenuDto> list = new ArrayList<>();
        Long userId = LoginHelper.getUserId();
        boolean hasGameConfig = false;
        JSONObject jsonObject = new JSONObject();
        if (data != null && !data.isEmpty()) {
            // 判断查询的是否是sdk菜单
            if(StringUtils.equals("sdk_menu", dictType)) {
                RequestHeaderDto headerDto = HttpRequestHeaderUtils.getFromHttpRequest();
                if (headerDto != null && headerDto.getAppId() != null) {
                    // 通过appid查询游戏信息
                    OpenGameDubboVo openGameDubboVo = remoteGameManagerService.selectOpenGameInfo(headerDto.getAppId());
                    if (openGameDubboVo != null && openGameDubboVo.getId() > 0) {
                        // 查询是否有单游戏配置
                        // 游戏配置类型 1:SDK菜单 2:SDK钱包菜单 3:SDK虚拟钱包菜单 4:游戏支付方式 5:苹果应用类支付条件
                        GameConfigVo vo = remoteGameConfigService.selectGameConfigByParam(openGameDubboVo.getId(), "1", Convert.toStr(openGameDubboVo.getOperationPlatform()));
                        if (vo != null) {
                            hasGameConfig = true;
                            jsonObject = JSONUtil.parseObj(vo.getData());
                        }
                    }
                }
            }

            // 如果有单个游戏配置，返回游戏配置信息
            if(hasGameConfig) {
                JSONObject finalJsonObject = jsonObject;
                List<SysDictData> first = data.stream().filter(c -> Convert.toBool(finalJsonObject.get(c.getDictLabel()))).collect(Collectors.toList());
                data =  first;
            }
            data.forEach(a -> {
                // sdk菜单
                if(StringUtils.equals("sdk_menu", dictType)) {
                    SdkMainMenuDto mainDto = new SdkMainMenuDto();
                    mainDto.setDictValue(a.getDictValue());
                    mainDto.setIsUp("1");
                    List<SdkMainMenuVo> mainList = sdkMainMenuService.queryList(mainDto);
                    if (mainList != null && mainList.size() > 0) {
                        mainList.stream().forEach(m -> {
                            m.setPath(getJoinUrl(m.getPath(), userId));
                            SdkMenuDto sdkMenuDto = BeanUtil.toBean(m, SdkMenuDto.class);
                            SdkSubMenuDto subDto = new SdkSubMenuDto();
                            subDto.setMainMenuId(m.getId());
                            List<SdkSubMenuVo> subList = sdkSubMenuService.queryList(subDto);
                            if (subList != null && subList.size() > 0) {
                                subList.forEach(b -> {
                                    b.setPath(getJoinUrl(b.getPath(), userId));
                                });
                                List<SdkMenuDto> childList = BeanCopyUtils.copyList(subList, SdkMenuDto.class);
                                sdkMenuDto.setChildren(childList);
                            }
                            list.add(sdkMenuDto);
                        });
                    }
                } else {
                    if (StringUtils.isNotEmpty(a.getRemark())) {
                        SdkMenuDto sdkMenuDto = JsonUtils.parseObject(a.getRemark(), SdkMenuDto.class);
                        if (sdkMenuDto != null) {

                            sdkMenuDto.setPath(getJoinUrl(sdkMenuDto.getPath(), userId));
                            if (sdkMenuDto.getChildren() != null && !sdkMenuDto.getChildren().isEmpty()) {
                                sdkMenuDto.getChildren().forEach(b -> {
                                    b.setPath(getJoinUrl(b.getPath(), userId));
                                });
                            }
                        }
                        list.add(sdkMenuDto);
                    }
                }
            });
        }
        return list;
    }

    private String getJoinUrl(String path, Long userId) {
        if (StringUtils.isNotEmpty(path) && path.startsWith("http")) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(path);
            stringBuilder.append(path.contains("?") ? "&" : "?");

            stringBuilder.append("uid=");
            stringBuilder.append(userId);
            return stringBuilder.toString();

        }
        return path;
    }

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    @Override
    public SysDictType selectDictTypeById(Long dictId) {
        return baseMapper.selectById(dictId);
    }

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    @Override
    public SysDictType selectDictTypeByType(String dictType) {
        return baseMapper.selectById(new LambdaQueryWrapper<SysDictType>().eq(SysDictType::getDictType, dictType));
    }

    /**
     * 批量删除字典类型信息
     *
     * @param dictIds 需要删除的字典ID
     */
    @Override
    public void deleteDictTypeByIds(Long[] dictIds) {
        for (Long dictId : dictIds) {
            SysDictType dictType = selectDictTypeById(dictId);
            if (dictDataMapper.exists(new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getDictType, dictType.getDictType()))) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }
            DictUtils.removeDictCache(dictType.getDictType());
        }
        baseMapper.deleteBatchIds(Arrays.asList(dictIds));
    }

    /**
     * 加载字典缓存数据
     */
    @Override
    public void loadingDictCache() {
        List<SysDictData> dictDataList = dictDataMapper.selectList(
            new LambdaQueryWrapper<SysDictData>().eq(SysDictData::getStatus, UserConstants.DICT_NORMAL));
        Map<String, List<SysDictData>> dictDataMap = dictDataList.stream().collect(Collectors.groupingBy(SysDictData::getDictType));
        dictDataMap.forEach((k, v) -> {
            List<SysDictData> dictList = v.stream()
                .sorted(Comparator.comparing(SysDictData::getDictSort))
                .collect(Collectors.toList());
            DictUtils.setDictCache(k, dictList);
        });
    }

    /**
     * 清空字典缓存数据
     */
    @Override
    public void clearDictCache() {
        DictUtils.clearDictCache();
    }

    /**
     * 重置字典缓存数据
     */
    @Override
    public void resetDictCache() {
        clearDictCache();
        loadingDictCache();
    }

    /**
     * 新增保存字典类型信息
     *
     * @param dict 字典类型信息
     * @return 结果
     */
    @Override
    public int insertDictType(SysDictType dict) {
        int row = baseMapper.insert(dict);
        if (row > 0) {
            DictUtils.setDictCache(dict.getDictType(), null);
        }
        return row;
    }

    /**
     * 修改保存字典类型信息
     *
     * @param dict 字典类型信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDictType(SysDictType dict) {
        SysDictType oldDict = baseMapper.selectById(dict.getDictId());
        dictDataMapper.update(null, new LambdaUpdateWrapper<SysDictData>()
            .set(SysDictData::getDictType, dict.getDictType())
            .eq(SysDictData::getDictType, oldDict.getDictType()));
        int row = baseMapper.updateById(dict);
        if (row > 0) {
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(dict.getDictType());
            DictUtils.setDictCache(dict.getDictType(), dictDatas);
        }
        return row;
    }

    /**
     * 校验字典类型称是否唯一
     *
     * @param dict 字典类型
     * @return 结果
     */
    @Override
    public String checkDictTypeUnique(SysDictType dict) {
        boolean exist = baseMapper.exists(new LambdaQueryWrapper<SysDictType>()
            .eq(SysDictType::getDictType, dict.getDictType())
            .ne(ObjectUtil.isNotNull(dict.getDictId()), SysDictType::getDictId, dict.getDictId()));
        if (exist) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

}
