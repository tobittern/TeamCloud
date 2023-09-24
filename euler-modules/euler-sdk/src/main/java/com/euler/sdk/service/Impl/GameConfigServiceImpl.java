package com.euler.sdk.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.platform.api.RemoteGameManagerService;
import com.euler.platform.api.domain.OpenGameVo;
import com.euler.sdk.api.domain.GameConfigData;
import com.euler.sdk.api.domain.GameConfigVo;
import com.euler.sdk.enums.GameConfigEnum;
import com.euler.system.api.RemoteDictService;
import com.euler.system.api.domain.SysDictData;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.euler.sdk.domain.bo.GameConfigBo;
import com.euler.sdk.domain.dto.GameConfigDto;
import com.euler.sdk.domain.entity.GameConfig;
import com.euler.sdk.mapper.GameConfigMapper;
import com.euler.sdk.service.IGameConfigService;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 游戏配置Service业务层处理
 *
 * @author euler
 * @date 2023-03-23
 */
@RequiredArgsConstructor
@Service
public class GameConfigServiceImpl extends ServiceImpl<GameConfigMapper, GameConfig> implements IGameConfigService {

    @Autowired
    private GameConfigMapper baseMapper;
    @DubboReference
    private RemoteGameManagerService remoteGameManagerService;
    @DubboReference
    private RemoteDictService remoteDictService;

    /**
     * 查询游戏配置
     *
     * @param id 游戏配置主键
     * @return 游戏配置
     */
    @Override
    public GameConfigVo queryById(Integer id) {
        GameConfigVo vo = baseMapper.selectVoById(id);
        if(vo != null) {
            vo = parseJsonData(vo, true);
        }
        return vo;
    }

    /**
     * 根据条件查询游戏配置信息
     *
     * @return 游戏配置信息
     */
    @Override
    public GameConfigVo selectGameConfigByParam(Integer gameId, String type, String platform) {
        LambdaQueryWrapper<GameConfig> lqw = Wrappers.lambdaQuery();
        lqw.eq(gameId != null && gameId > 0, GameConfig::getGameId, gameId);
        lqw.eq(GameConfig::getType, type);
        lqw.and(a -> a.eq(StringUtils.isNotBlank(platform), GameConfig::getPlatform, platform)
            .or(b -> b.eq(GameConfig::getPlatform, "0")));
        return baseMapper.selectVoOne(lqw);
    }

    /**
     * 查询游戏配置列表
     *
     * @param dto 游戏配置
     * @return 游戏配置
     */
    @Override
    public TableDataInfo<GameConfigVo> queryPageList(GameConfigDto dto) {
        LambdaQueryWrapper<GameConfig> lqw = buildQueryWrapper(dto);
        Page<GameConfigVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        if(result != null && result.getRecords() != null && result.getRecords().size() > 0) {
            result.getRecords().stream().forEach(a -> {
                a = parseJsonData(a, false);
            });
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询游戏配置列表
     *
     * @param dto 游戏配置
     * @return 游戏配置
     */
    @Override
    public List<GameConfigVo> queryList(GameConfigDto dto) {
        LambdaQueryWrapper<GameConfig> lqw = buildQueryWrapper(dto);
        List<GameConfigVo> list = baseMapper.selectVoList(lqw);
        if(list != null && list.size() > 0) {
            list.stream().forEach(a -> {
                a = parseJsonData(a, false);
            });
        }
        return list;
    }

    private LambdaQueryWrapper<GameConfig> buildQueryWrapper(GameConfigDto dto) {
        LambdaQueryWrapper<GameConfig> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getGameId() != null, GameConfig::getGameId, dto.getGameId());
        lqw.and(a -> a.eq(StringUtils.isNotBlank(dto.getPlatform()), GameConfig::getPlatform, dto.getPlatform())
            .or(b -> b.eq(GameConfig::getPlatform, "0")));
        lqw.eq(StringUtils.isNotBlank(dto.getType()), GameConfig::getType, dto.getType());
        return lqw;
    }

    /**
     * json数据解析
     *
     * @param vo
     * @return
     */
    private GameConfigVo parseJsonData(GameConfigVo vo, Boolean isQueryDetail) {
        // json数据解析
        JSONObject jsonObject = JSONUtil.parseObj(vo.getData(), false, true);
        List<GameConfigData> tempList = new ArrayList<>();
        // 遍历json数据
        if(jsonObject != null && !jsonObject.isEmpty()) {
            jsonObject.entrySet().forEach(a -> {
                // 是否是苹果应用类支付条件
                if (!StringUtils.equals("5", vo.getType())) {
                    GameConfigData gameConfigData = new GameConfigData();
                    gameConfigData.setName(a.getKey());
                    gameConfigData.setValue(Convert.toBool(a.getValue()));
                    tempList.add(gameConfigData);
                } else {
                    if (StringUtils.equals("苹果应用类支付开关", a.getKey())) {
                        GameConfigData gameConfigData = new GameConfigData();
                        gameConfigData.setName(a.getKey());
                        gameConfigData.setValue(Convert.toBool(a.getValue()));
                        tempList.add(gameConfigData);
                    } else {
                        GameConfigData gameConfigData = new GameConfigData();
                        gameConfigData.setName(a.getKey());
                        gameConfigData.setValue(Convert.toInt(a.getValue()));
                        tempList.add(gameConfigData);
                    }
                }
            });
        }

        GameConfigEnum gameConfigEnum =  GameConfigEnum.find(vo.getType());
        switch (gameConfigEnum) {
            case SDK_MENU:
                vo.setSdkMenuList(tempList);
                // 进入详情页面后，把其他字典列表值也加载进来
                if(isQueryDetail) {
                    setListData(vo, false, true, true, true, true, true);
                }
                break;
            case WALLET_MENU:
                vo.setSdkWalletMenuList(tempList);
                if(isQueryDetail) {
                    setListData(vo, true, false, true, true, true, true);
                }
                break;
            case VIRTUAL_WALLET_MENU:
                vo.setSdkVirtualWalletMenuList(tempList);
                if(isQueryDetail) {
                    setListData(vo, true, true, false, true, true, true);
                }
                break;
            case GAME_PAY_TYPE:
                vo.setGamePayTypeList(tempList);
                if(isQueryDetail) {
                    setListData(vo, true, true, true, false, true, true);
                }
                break;
            case APP_IOS_PAYMENT_TERM:
                vo.setAppIosPaymentTermList(tempList);
                if(isQueryDetail) {
                    setListData(vo, true, true, true, true, false, true);
                }
                break;
            default:
                vo.setEventBroadcastList(tempList);
                if(isQueryDetail) {
                    setListData(vo, true, true, true, true, true, false);
                }
                break;
        }
        // 游戏名称设置
        IdDto<Integer> idDto = new IdDto<>();
        idDto.setId(vo.getGameId());
        OpenGameVo gameVo = remoteGameManagerService.getGameInfo(idDto);
        if (gameVo != null) {
            vo.setGameName(gameVo.getGameName());
        }
        return vo;
    }

    private GameConfigVo setListData(GameConfigVo vo, Boolean isSdkMenu, Boolean isSdkWalletMenu, Boolean isSdkVirtualWalletMenu, Boolean isGamePayType, Boolean isAppIosPaymentTerm, Boolean isEventBroadcast) {
        // 查询字典
        List<GameConfigData> sdkMenuList = selectDictData("sdk_menu");
        List<GameConfigData> sdkWalletMenuList = selectDictData("sdk_wallet_menu");
        List<GameConfigData> sdkVirtualWalletMenuList = selectDictData("sdk_virtual_wallet_menu");
        List<GameConfigData> gamePayTypeList = selectDictData("global_game_pay_type_app_android_4");
        List<GameConfigData> iosGamePayTypeList = selectDictData("global_game_pay_type_app_ios_4");
        List<GameConfigData> h5GamePayTypeList = selectDictData("app_ios_payment_term");
        List<GameConfigData> appIosPaymentTermList = selectDictData("app_ios_payment_term");
        List<GameConfigData> eventBroadcastList = selectDictData("event_broadcast");

        if(isSdkMenu) {
            vo.setSdkMenuList(sdkMenuList);
        }
        if(isSdkWalletMenu) {
            vo.setSdkWalletMenuList(sdkWalletMenuList);
        }
        if(isSdkVirtualWalletMenu) {
            vo.setSdkVirtualWalletMenuList(sdkVirtualWalletMenuList);
        }
        if(isGamePayType) {
            if(StringUtils.equals("1", vo.getPlatform())) {
                // 安卓
                vo.setGamePayTypeList(gamePayTypeList);
            } else if(StringUtils.equals("2", vo.getPlatform())) {
                // IOS
                vo.setGamePayTypeList(iosGamePayTypeList);
            } else if(StringUtils.equals("3", vo.getPlatform())) {
                // H5
                vo.setGamePayTypeList(h5GamePayTypeList);
            }
        }
        if(isAppIosPaymentTerm) {
            vo.setAppIosPaymentTermList(appIosPaymentTermList);
        }
        if(isEventBroadcast) {
            vo.setEventBroadcastList(eventBroadcastList);
        }
        return vo;
    }

    private List<GameConfigData> selectDictData(String type) {
        List<GameConfigData> tempList = new ArrayList<>();
        List<SysDictData> data = remoteDictService.selectAllDictDataByType(type);
        if(data != null && data.size() > 0) {
            if(!StringUtils.equals("app_ios_payment_term", type)) {
                data.stream().forEach(d -> {
                    GameConfigData gameConfigData = new GameConfigData();
                    gameConfigData.setName(d.getDictLabel());
                    gameConfigData.setValue(false);
                    tempList.add(gameConfigData);
                });
            } else {
                data.stream().forEach(d -> {
                    if(StringUtils.equals("苹果应用类支付开关", d.getDictLabel())) {
                        GameConfigData gameConfigData = new GameConfigData();
                        gameConfigData.setName(d.getDictLabel());
                        gameConfigData.setValue(false);
                        tempList.add(gameConfigData);
                    } else {
                        GameConfigData gameConfigData = new GameConfigData();
                        gameConfigData.setName(d.getDictLabel());
                        gameConfigData.setValue(0);
                        tempList.add(gameConfigData);
                    }
                });

            }
        }
        return tempList;
    }

    /**
     * 新增游戏配置
     *
     * @param bo 游戏配置
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R insertByBo(GameConfigBo bo) {
        // 判断该游戏是否配置过
        LambdaQueryWrapper<GameConfig> lqw = Wrappers.lambdaQuery();
        lqw.eq(GameConfig::getGameId, bo.getGameId());
        lqw.eq(GameConfig::getPlatform, bo.getPlatform());
        lqw.eq(GameConfig::getType, bo.getType());
        GameConfigVo gameConfigVo = baseMapper.selectVoOne(lqw);
        if(gameConfigVo != null) {
            return R.fail("该游戏已配置过");
        }
        setJSonData(bo);
        GameConfig add = BeanUtil.toBean(bo, GameConfig. class);
        String result = validEntityBeforeSave(add);
        if (!result.equals("success")) {
            return R.fail(result);
        }
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            return R.ok("新增成功");
        }
        return R.fail("新增失败");
    }

    /**
     * 修改游戏配置
     *
     * @param bo 游戏配置
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateByBo(GameConfigBo bo) {
        // 验证是否传输过来主键
        if (bo.getId() == null || bo.getId() <= 0) {
            return R.fail("参数缺失");
        }
        setJSonData(bo);
        GameConfig update = BeanUtil.toBean(bo, GameConfig. class);
        String result = validEntityBeforeSave(update);
        if (!result.equals("success")) {
            return R.fail(result);
        }
        int i = baseMapper.updateById(update);
        if(i > 0) {
            return R.ok("修改成功");
        }
        return R.fail("修改失败");
    }

    private GameConfigBo setJSonData(GameConfigBo bo) {
        JSONObject jsonObj = new JSONObject(new LinkedHashMap());
        List<GameConfigData> tempList = new ArrayList<>();
        GameConfigEnum gameConfigEnum =  GameConfigEnum.find(bo.getType());
        switch (gameConfigEnum) {
            case SDK_MENU:
                tempList = bo.getSdkMenuList();
                break;
            case WALLET_MENU:
                tempList = bo.getSdkWalletMenuList();
                break;
            case VIRTUAL_WALLET_MENU:
                tempList = bo.getSdkVirtualWalletMenuList();
                break;
            case GAME_PAY_TYPE:
                tempList = bo.getGamePayTypeList();
                break;
            case APP_IOS_PAYMENT_TERM:
                tempList = bo.getAppIosPaymentTermList();
                break;
            default:
                tempList = bo.getEventBroadcastList();
                break;
        }
        if(tempList != null && tempList.size() > 0) {
            if(!StringUtils.equals("5", bo.getType())) {
                tempList.stream().forEach(t -> {
                    if(t.getValue() == null) {
                        t.setValue(false);
                    } else {
                        t.setValue(Convert.toBool(t.getValue()));
                    }
                    jsonObj.put(t.getName(), t.getValue().toString());
                });
            } else {
                if(tempList.size() != 2) {
                    jsonObj.put("苹果应用类支付开关", "false");
                    jsonObj.put("支付笔数限制", tempList.get(0).getValue().toString());
                } else {
                    tempList.stream().forEach(t -> {
                        if(StringUtils.equals("苹果应用类支付开关", t.getName())) {
                            jsonObj.put(t.getName(), Convert.toBool(t.getValue()).toString());
                        } else {
                            jsonObj.put(t.getName(), Convert.toInt(t.getValue()).toString());
                        }
                    });
                }
            }
            bo.setData(jsonObj.toString());
        }
        return bo;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     * @return 校验结果
     */
    private String validEntityBeforeSave(GameConfig entity) {
        //判断应用系统类型是否正确
        String[] typeList = new String[]{"1", "2", "3", "4", "5", "6"};
        Optional<String> typeAny = Arrays.stream(typeList).filter(a -> a.equals(entity.getType())).findAny();
        if (!typeAny.isPresent()) {
            return "游戏配置类型错误";
        }
        // 平台校验
        if(StringUtils.isNotBlank(entity.getType()) &&
            (StringUtils.equals("4", entity.getType()) || StringUtils.equals("5", entity.getType()))) {
            // 游戏配置类型 4:游戏支付方式, 平台必须选择具体平台
            if(StringUtils.equals("4", entity.getType())) {
                if(StringUtils.isBlank(entity.getPlatform()) || StringUtils.equals("0", entity.getPlatform())) {
                    return "选择游戏支付方式时，平台必须选择具体平台";
                }
            }
            // 游戏配置类型 5:苹果应用类支付条件，平台必须是IOS
            if(StringUtils.equals("5", entity.getType()) && !StringUtils.equals("2", entity.getPlatform())) {
                return "选择苹果应用类支付条件时，平台必须选择IOS";
            }
        }
        return "success";
    }

    /**
     * 批量删除游戏配置
     *
     * @param ids 需要删除的游戏配置主键
     * @return 结果
     */
    @Override
    public R deleteWithValidByIds(Collection<Integer> ids) {
        int i = baseMapper.deleteBatchIds(ids);
        if(i > 0) {
            return R.ok("删除成功");
        }
        return R.fail("删除失败");
    }

}
