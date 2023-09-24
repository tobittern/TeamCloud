package com.euler.community.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdNameDto;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.utils.BeanCopyUtils;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.ServletUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.domain.bo.DiscoverBo;
import com.euler.community.domain.bo.HotGameBo;
import com.euler.community.domain.dto.DiscoverDto;
import com.euler.community.domain.entity.Discover;
import com.euler.community.domain.vo.*;
import com.euler.community.mapper.DiscoverMapper;
import com.euler.community.service.IBannerGroupService;
import com.euler.community.service.IBannerService;
import com.euler.community.service.IDiscoverService;
import com.euler.community.service.IHotGameService;
import com.euler.platform.api.RemoteGameManagerService;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.platform.api.domain.OpenGameVo;
import com.euler.sdk.api.RemoteChannelService;
import com.euler.sdk.api.domain.ChannelPackageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 发现配置Service业务层处理
 *
 * @author euler
 * @date 2022-06-06
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DiscoverServiceImpl extends ServiceImpl<DiscoverMapper, Discover> implements IDiscoverService {

    private final DiscoverMapper baseMapper;

    @Autowired
    private IBannerGroupService bannerGroupService;
    @Autowired
    private IBannerService iBannerService;
    @DubboReference
    private RemoteGameManagerService remoteGameManagerService;
    @DubboReference
    private RemoteChannelService remoteChannelService;
    @Autowired
    private IHotGameService iHotGameService;

    /**
     * 查询发现配置
     *
     * @param id 发现配置主键
     * @return 发现配置
     */
    @Override
    public DiscoverVo queryById(Long id) {

        DiscoverVo vo = baseMapper.selectVoById(id);
        if (vo != null) {
            if (vo.getModuleType().equals("3")) {
                List<Dict> contentArr = JsonUtils.parseArrayMap(vo.getContent());
                if (contentArr != null && contentArr.get(0) != null) {
                    Long bannerGroupId = Convert.toLong(contentArr.get(0).get("bannerGroupId"));
                    BannerGroupVo bannerGroupVo = bannerGroupService.queryById(bannerGroupId);
                    if (bannerGroupVo != null) {
                        IdNameDto<Long> objectIdNameDto = new IdNameDto<>();
                        objectIdNameDto.setId(bannerGroupVo.getId());
                        objectIdNameDto.setName(bannerGroupVo.getGroupName());
                        vo.setIdNameDto(objectIdNameDto);
                    }
                }
            } else {
                List<Dict> contentArr = JsonUtils.parseArrayMap(vo.getContent());
                List<Integer> gameIds = new ArrayList<>();
                // 游戏的
                contentArr.forEach(a -> {
                    // 游戏的
                    List<Integer> gameId = contentArr.stream().map(m -> Convert.toInt(m.get("gameId"))).collect(Collectors.toList());
                    gameIds.addAll(gameId);
                });
                List<ChannelPackageVo> gamePackageInfoByIds = remoteChannelService.getGamePackageInfoByIds(gameIds);
                contentArr.forEach(a -> {
                    Optional<ChannelPackageVo> first = gamePackageInfoByIds.stream().filter(cc -> cc.getGameId().equals(a.get("gameId"))).findFirst();
                    if (first.isPresent()) {
                        a.put("gameName", first.get().getNewGameName());
                    }
                });
                vo.setContent(JsonUtils.toJsonString(contentArr));
            }
            vo.setShowContent(JsonUtils.parseArrayMap(vo.getContent()));
        }
        return vo;
    }

    /**
     * 查询发现配置列表
     *
     * @param dto 发现配置
     * @return 发现配置
     */
    @Override
    public TableDataInfo<DiscoverVo> queryPageList(DiscoverDto dto) {
        LambdaQueryWrapper<Discover> lqw = buildQueryWrapper(dto);
        Page<DiscoverVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        result.getRecords().forEach(a -> {
            a.setShowContent(JsonUtils.parseArrayMap(a.getContent()));
        });
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<Discover> buildQueryWrapper(DiscoverDto dto) {
        LambdaQueryWrapper<Discover> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getMemberId() != null, Discover::getMemberId, dto.getMemberId());
        lqw.eq(StringUtils.isNotBlank(dto.getTitle()), Discover::getTitle, dto.getTitle());
        lqw.eq(StringUtils.isNotBlank(dto.getModuleType()), Discover::getModuleType, dto.getModuleType());
        lqw.eq(StringUtils.isNotBlank(dto.getStatus()), Discover::getStatus, dto.getStatus());
        lqw.eq(StringUtils.isNotBlank(dto.getApplicationSystem()), Discover::getApplicationSystem, dto.getApplicationSystem());
        // 按照排序优先级，越小越靠上
        lqw.orderByAsc(Discover::getLevel).orderByAsc(Discover::getId);
        return lqw;
    }

    /**
     * 查询发现配置列表
     *
     * @param dto 发现配置
     * @return 发现配置
     */
    @Override
    public List<DiscoverVo> queryList(DiscoverDto dto) {
        // 判断Redis中是否存在数据
        String dateTime = DateUtil.format(new Date(), "yyyy-MM-dd");
        String key = Constants.COMMUNITY_KEY + "discover:" + ":" + dto.getApplicationSystem() + ":" + dateTime;
        // 首先判断
        List<DiscoverVo> cacheList = RedisUtils.getCacheList(key);
        // 判断是否存在数据
        if (cacheList.size() <= 0) {
            // 数据不存在我们需要从数据库中获取到数据
            LambdaQueryWrapper<Discover> lqw = buildQueryWrapper(dto);
            List<DiscoverVo> discoverVos = baseMapper.selectVoList(lqw);
            // 首先我们根据类型判断我们需要获取的东西
            List<Integer> gameIds = new ArrayList<>();
            List<Long> bannerGroupIds = new ArrayList<>();
            discoverVos.forEach(a -> {
                List<Dict> dicts = JsonUtils.parseArrayMap(a.getContent());
                if (a.getModuleType().equals("3")) {
                    // banner的
                    Long bannerGroupId = Convert.toLong(dicts.get(0).get("bannerGroupId"));
                    bannerGroupIds.add(bannerGroupId);
                } else {
                    // 游戏的
                    List<Integer> gameId = dicts.stream().map(m -> Convert.toInt(m.get("gameId"))).collect(Collectors.toList());
                    gameIds.addAll(gameId);
                }
            });
            List<BannerGroupListVo> groupBannerList = null;
            List<OpenGameDubboVo> openGameDubboVos = null;
            List<ChannelPackageVo> gamePackageInfoByIds = null;
            // 数据循环完毕之后我们需要获取到游戏的信息和banner组中的信息
            if (bannerGroupIds.size() > 0) {
                groupBannerList = bannerGroupService.getGroupBannerList(bannerGroupIds);
            }
            if (gameIds.size() > 0) {
                // 查询这批游戏的基础数据
                openGameDubboVos = remoteGameManagerService.selectByIds(gameIds);
                // 从分包列表中查询游戏的其他数据
                gamePackageInfoByIds = remoteChannelService.getGamePackageInfoByIds(gameIds);
            }
            // 再次循环将数据添加到字段中
            List<BannerGroupListVo> finalGroupBannerList = groupBannerList;
            List<OpenGameDubboVo> finalOpenGameDubboVos = openGameDubboVos;
            List<ChannelPackageVo> finalGamePackageInfoByIds = gamePackageInfoByIds;
            discoverVos.forEach(l -> {
                List<Dict> newDict = JsonUtils.parseArrayMap(l.getContent());
                if (l.getModuleType().equals("3")) {
                    Long bannerGroupId = Convert.toLong(newDict.get(0).get("bannerGroupId"));
                    Optional<BannerGroupListVo> first = finalGroupBannerList.stream().filter(aa -> aa.getId().equals(bannerGroupId)).findFirst();
                    first.ifPresent(bannerGroupListVo -> l.setFrontShowBanner(bannerGroupListVo.getBannerVoList()));
                } else {
                    // 循环游戏列表获取数据
                    List<DiscoverShowGameVo> showGameVoList = new ArrayList<>();
                    for (Dict dict : newDict) {
                        DiscoverShowGameVo discoverShowGameVo = new DiscoverShowGameVo();
                        Integer gameId = Convert.toInt(dict.get("gameId"));
                        Optional<OpenGameDubboVo> second = finalOpenGameDubboVos.stream().filter(bb -> bb.getId().equals(gameId)).findFirst();
                        Optional<ChannelPackageVo> third = finalGamePackageInfoByIds.stream().filter(cc -> cc.getGameId().equals(gameId)).findFirst();
                        discoverShowGameVo.setGameId(gameId);
                        discoverShowGameVo.setTag(Convert.toStr(dict.get("tag")));
                        if (second.isPresent()) {
                            discoverShowGameVo.setGameCategoryName(second.get().getGameCategoryName());
                        }
                        if (third.isPresent()) {
                            discoverShowGameVo.setDownloadAddress(third.get().getRealDownAddress());
                            discoverShowGameVo.setGameIcon(third.get().getIcon());
                            discoverShowGameVo.setGameName(third.get().getNewGameName());
                            discoverShowGameVo.setPackageName(third.get().getPackageName());
                            discoverShowGameVo.setStartupClass(third.get().getStartupClass());
                        }
                        showGameVoList.add(discoverShowGameVo);
                    }
                    l.setFrontShowGame(showGameVoList);
                }
                // 循环完之后将一些没有比较的字段设置空
                l.setContent(null);
            });
            // 数据存储到redis中
            RedisUtils.setCacheList(key, discoverVos);
            RedisUtils.expire(key, Duration.ofDays(2));
            cacheList = discoverVos;
        }
        return cacheList;
    }

    /**
     * 新增发现配置
     *
     * @param bo 发现配置
     * @return 结果
     */
    @Override
    public R insertByBo(DiscoverBo bo) {
        Discover add = BeanUtil.toBean(bo, Discover.class);
        R<Discover> discoverR = validEntityBeforeSave(add);
        if (discoverR.getCode() == R.FAIL) {
            return discoverR;
        }
        // 验证成功之后我们需要将数据从新获取一下
        Discover data = discoverR.getData();
        // 状态，默认是待启用
        data.setStatus("0");
        boolean flag = baseMapper.insert(data) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return R.ok();
    }

    /**
     * 修改发现配置
     *
     * @param bo 发现配置
     * @return 结果
     */
    @Override
    public R updateByBo(DiscoverBo bo) {
        Discover update = BeanUtil.toBean(bo, Discover.class);
        R<Discover> discoverR = validEntityBeforeSave(update);
        if (discoverR.getCode() == R.FAIL) {
            return discoverR;
        }
        Discover data = discoverR.getData();
        int i = baseMapper.updateById(data);
        if (i > 0) {
            return R.ok();
        }
        return R.fail("修改失败");
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private R<Discover> validEntityBeforeSave(Discover entity) {
        //判断应用系统类型是否正确
        String[] typeList = new String[]{"1", "2", "3"};
        Optional<String> applicationSystemPlatformAnyAny = Arrays.stream(typeList).filter(a -> a.equals(entity.getApplicationSystem())).findAny();
        if (!applicationSystemPlatformAnyAny.isPresent()) {
            return R.fail("应用系统类型错误");
        }
        //判断模型类型是否正确
        String[] moduleTypeList = new String[]{"1", "2", "3"};
        Optional<String> moduleTypePlatformAnyAny = Arrays.stream(moduleTypeList).filter(a -> a.equals(entity.getModuleType())).findAny();
        if (!moduleTypePlatformAnyAny.isPresent()) {
            return R.fail("模型类型错误");
        }
        // 校验具体内容，格式为json格式
        // 类型是1和2的时候，[{"gameId":"101","tag":"热门游戏1"}]
        // 类型是3的时候，[{"bannerGroupId":"201"}]
        if (StringUtils.isNotBlank(entity.getContent())) {
            // 判断是否是json格式
            if (!isJson(entity.getContent())) {
                return R.fail("内容的格式不是json格式，请确认");
            }
            List<Dict> contentArr = JsonUtils.parseArrayMap(entity.getContent());
            // 模块类型, 1纵向编组，2横向编组
            if (StringUtils.equals("1", entity.getModuleType())
                || StringUtils.equals("2", entity.getModuleType())) {

                if (contentArr != null && contentArr.size() > 10) {
                    return R.fail("至多只能添加10款游戏");
                }
                List<Integer> list = new ArrayList<>();
                for (int i = 0; i < contentArr.size(); i++) {
                    // 游戏id
                    Integer gameId = Convert.toInt(contentArr.get(i).get("gameId"));
                    list.add(i, gameId);
                }
                // 判断游戏id是否存在
                // 这个地方就需要调一下远程的dubbo服务  查看一下那些游戏是可以正常使用的
                List<OpenGameDubboVo> openGameDubboVos = remoteGameManagerService.selectByIds(list);
                if (ObjectUtil.isNull(openGameDubboVos) || openGameDubboVos.size() != list.size()) {
                    return R.fail("添加的游戏中有不存在的游戏");
                }
                // 循环为我们数据体重追加游戏数据
                for (Dict dict : contentArr) {
                    Integer gameId = Convert.toInt(dict.get("gameId"));
                    // 获取到当前游戏的基础数据
                    Optional<OpenGameDubboVo> first = openGameDubboVos.stream().filter(b -> b.getId().equals(gameId)).findFirst();
                    if (first.isPresent()) {
                        String tag = Convert.toStr(dict.get("tag"));
                        if (StringUtils.isNotEmpty(tag)) {
                            //判断tag标签至多只有3个
                            String[] tags = tag.split(",");
                            if (tags.length > 3) {
                                return R.fail("至多只能添加3个标签");
                            }
                            dict.put("tag", tag);
                        } else {
                            // 当前游戏没有设置标签
                            dict.put("tag", first.get().getGameTags());
                        }
                    }
                    dict.remove("gameName");
                }
                entity.setContent(JsonUtils.toJsonString(contentArr));
                return R.ok(entity);
            } else if (StringUtils.equals("3", entity.getModuleType())) {
                // 模块类型, 3banner编组
                if (contentArr != null && contentArr.size() != 1) {
                    return R.fail("至多只能添加1个banner组");
                }
                // banner组id
                Long bannerGroupId = Convert.toLong(contentArr.get(0).get("bannerGroupId"));
                // 判断banner组id是否存在
                BannerGroupVo bannerGroupVos = bannerGroupService.queryById(bannerGroupId);
                if (bannerGroupVos == null) {
                    return R.fail("添加的banner组id不存在");
                }
                //判断当banner组的id为空的时候
                if (bannerGroupId == null) {
                    return R.fail("添加的banner组id不能为空");
                }
                return R.ok(entity);
            }
        }
        return R.fail();
    }

    /**
     * 删除发现配置
     *
     * @param ids
     * @return 结果
     */
    @Override
    public R deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (baseMapper.deleteBatchIds(ids) > 0) {
            return R.ok("删除成功");
        }
        return R.fail("删除失败");
    }

    /**
     * 操作上下线
     *
     * @return 结果
     */
    @Override
    public R operation(IdNameTypeDicDto idNameTypeDicDto, Long userId) {
        // 数据存在 我们开始进行数据更新
        Integer[] typeList = new Integer[]{1, 2};
        Optional<Integer> operationPlatformAny = Arrays.stream(typeList).filter(a -> a.equals(idNameTypeDicDto.getType())).findAny();
        if (!operationPlatformAny.isPresent()) {
            return R.fail("参数错误");
        }
        DiscoverVo vo = queryById(Convert.toLong(idNameTypeDicDto.getId()));
        if (ObjectUtil.isNull(vo)) {
            return R.fail("数据不存在");
        }
        // 执行更新操作
        var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper)
            .eq(Discover::getId, idNameTypeDicDto.getId())
            .set(Discover::getStatus, idNameTypeDicDto.getType());
        boolean update = updateChainWrapper.update();
        if (update) {
            return R.ok("修改成功");
        }
        return R.fail("数据修改失败");
    }

    /**
     * 判断字符串是否为json格式
     *
     * @param str 字符串
     * @return true:是 false:不是
     */
    private Boolean isJson(String str) {
        try {
            JSONArray jsonArray = JSONArray.parseArray(str);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    /**
     * app端查询游戏详情
     *
     * @param idDto
     * @return
     */
    @Override
    public OpenGameForAppVo getGameInfo(IdDto<Integer> idDto) {
        OpenGameVo gameInfo = remoteGameManagerService.getGameInfo(idDto);
        OpenGameForAppVo returnCopy = BeanCopyUtils.copy(gameInfo, OpenGameForAppVo.class);
        // 查询其他信息
        List<Integer> gameIds = new ArrayList<>();
        gameIds.add(gameInfo.getId());
        List<ChannelPackageVo> gamePackageInfoByIds = remoteChannelService.getGamePackageInfoByIds(gameIds);
        if (gamePackageInfoByIds.size() > 0) {
            Optional<ChannelPackageVo> first = gamePackageInfoByIds.stream().filter(a -> a.getGameId().equals(gameInfo.getId())).findFirst();
            if (first.isPresent()) {
                returnCopy.setDownloadAddress(first.get().getRealDownAddress());
                returnCopy.setPackageName(first.get().getPackageName());
                returnCopy.setStartupClass(first.get().getStartupClass());
            }
        }
        String device = ServletUtils.getHeader(ServletUtils.getRequest(), "device");
        // 增加用户访问游戏的次数记录
        HotGameBo hotGameBo = new HotGameBo();
        hotGameBo.setMemberId(LoginHelper.getUserIdOther());
        hotGameBo.setGameId(Convert.toLong(gameInfo.getId()));
        hotGameBo.setGameName(gameInfo.getGameName());
        hotGameBo.setGamePic(gameInfo.getIconUrl());
        hotGameBo.setOperationPlatform(Convert.toInt(device));
        iHotGameService.insertByBo(hotGameBo);

        return returnCopy;
    }


    @Override
    public void clearCache() {
        String dateTime = DateUtil.format(new Date(), "yyyy-MM-dd");
        String key1 = Constants.COMMUNITY_KEY + "discover:" + ":1:" + dateTime;
        String key2 = Constants.COMMUNITY_KEY + "discover:" + ":2:" + dateTime;
        String key3 = Constants.COMMUNITY_KEY + "discover:" + ":3:" + dateTime;
        // 删除指定key
        RedisUtils.deleteObject(key1);
        RedisUtils.deleteObject(key2);
        RedisUtils.deleteObject(key3);
    }


    /**
     * 通过游戏名获取基础数据中的游戏列表
     *
     * @param dto
     * @return
     */
    @Override
    public R getGameListByName(DiscoverDto dto) {
        // 首先从渠道表中获取渠道数据
        List<ChannelPackageVo> gameListByName = remoteChannelService.getGameListByName();
        // 获取到这些游戏id 去除掉不是当前平台的
        Integer platform = 1;
        if (dto.getApplicationSystem() != null) {
            platform = Convert.toInt(dto.getApplicationSystem());
        }

        List<IdNameDto<Integer>> idNameDtos = new ArrayList<>();

        if (gameListByName.size() > 0) {
            List<Integer> gameIds = gameListByName.stream().map(ChannelPackageVo::getGameId).collect(Collectors.toList());
            List<OpenGameDubboVo> openGameDubboVos = remoteGameManagerService.selectByIds(gameIds);
            // 过滤平台
            if (openGameDubboVos.size() > 0) {
                Integer finalPlatform = platform;
                List<OpenGameDubboVo> filterGameList = openGameDubboVos.stream().filter(a -> a.getOperationPlatform().equals(finalPlatform)).collect(Collectors.toList());
                if (filterGameList.size() > 0) {
                    List<Integer> needSearchGameId = filterGameList.stream().map(OpenGameDubboVo::getId).collect(Collectors.toList());
                    gameListByName.forEach(c -> {
                        if (needSearchGameId.contains(c.getGameId())) {
                            IdNameDto<Integer> objectIdNameDto = new IdNameDto<>();
                            objectIdNameDto.setId(c.getGameId());
                            objectIdNameDto.setName(c.getNewGameName());
                            idNameDtos.add(objectIdNameDto);
                        }
                    });
                }
            }
        }
        return R.ok(idNameDtos);
    }
}



