package com.euler.sdk.service.Impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.constant.UserConstants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.domain.dto.RoleDTO;
import com.euler.common.core.enums.UserTypeEnum;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.platform.api.RemoteGameManagerService;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.sdk.api.domain.ChannelPackageVo;
import com.euler.sdk.domain.bo.ChannelBo;
import com.euler.sdk.domain.bo.ChannelPackageBo;
import com.euler.sdk.domain.bo.GameUseRecordBo;
import com.euler.sdk.domain.dto.ChannelDto;
import com.euler.sdk.domain.dto.ChannelPackageDto;
import com.euler.sdk.domain.entity.Channel;
import com.euler.sdk.domain.entity.ChannelPackage;
import com.euler.sdk.domain.entity.ChannelPackageTask;
import com.euler.sdk.domain.vo.ChannelVo;
import com.euler.sdk.enums.GameUseRecordTypeEnum;
import com.euler.sdk.mapper.ChannelMapper;
import com.euler.sdk.mapper.ChannelPackageMapper;
import com.euler.sdk.mapper.ChannelPackageTaskMapper;
import com.euler.sdk.service.IChannelService;
import com.euler.sdk.service.IGameUseRecordService;
import com.euler.system.api.RemoteUserService;
import com.euler.system.api.domain.SysUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 主渠道Service业务层处理
 *
 * @author euler
 * @date 2022-04-01
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ChannelServiceImpl implements IChannelService {

    private final ChannelMapper baseMapper;

    @Autowired
    private ChannelPackageMapper channelPackageMapper;
    @Autowired
    private ChannelPackageTaskMapper channelPackageTaskMapper;

    @DubboReference
    private RemoteUserService remoteUserService;
    @DubboReference
    private RemoteGameManagerService remoteGameManagerService;

    @Autowired
    private IGameUseRecordService iGameUseRecordService;


    /**
     * 查询主渠道
     *
     * @param id 主渠道主键
     * @return 主渠道
     */
    @Override
    public ChannelVo queryById(Integer id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询主渠道列表
     *
     * @return 主渠道
     */
    @Override
    public TableDataInfo<ChannelVo> queryPageList(ChannelDto dto) {
        LambdaQueryWrapper<Channel> lqw = buildQueryWrapper(dto);
        Page<ChannelVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询主渠道列表, 不分页
     *
     * @return 主渠道
     */
    @Override
    public List<ChannelVo> queryList(ChannelDto dto) {
        LambdaQueryWrapper<Channel> lqw = buildQueryWrapper(dto);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Channel> buildQueryWrapper(ChannelDto dto) {
        LambdaQueryWrapper<Channel> lqw = Wrappers.lambdaQuery();
        lqw.likeRight(StringUtils.isNotBlank(dto.getChannelName()), Channel::getChannelName, dto.getChannelName());
        Optional<RoleDTO> channel = LoginHelper.getLoginUser().getRoles().stream().filter(a -> a.getRoleKey().contains("channel")).findFirst();
        if (channel.isPresent()) {
            lqw.eq(Channel::getUserId, LoginHelper.getUserId());
        }
        lqw.eq(dto.getId() != null, Channel::getId, dto.getId());
        lqw.orderByDesc(Channel::getId);
        return lqw;
    }

    /**
     * 新增主渠道
     *
     * @return 结果
     */
    @Override
    @Transactional
    public R insertByBo(ChannelBo bo) {
        Channel add = BeanUtil.toBean(bo, Channel.class);
        // 添加的时候渠道主账号名称不能含有中文
        Boolean containChinese = StringUtils.isContainChinese(bo.getAdminName());
        if (containChinese) {
            return R.fail("渠道主账号名称不能含有中文");
        }
        // 获取一下添加绑定了多少游戏
        Integer[] gameNums = Convert.toIntArray(bo.getChannelGame().split(","));
        add.setGameNum(Convert.toInt(Arrays.stream(gameNums).distinct().count()));
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            // 针对绑定的游戏进行一下处理
            GameUseRecordBo gameUseRecordBo = new GameUseRecordBo();
            gameUseRecordBo.setPartyAId(add.getId());
            gameUseRecordBo.setPartyBId(bo.getChannelGame());
            gameUseRecordBo.setType(GameUseRecordTypeEnum.CHANNEL_ASSOCIATION.getCode());
            iGameUseRecordService.insertGameUseRecord(gameUseRecordBo);
            // 添加成功之后需要生成一个用户
            var sysUser = new SysUser();
            // 检测用户
            if (UserConstants.NOT_UNIQUE.equals(remoteUserService.checkUserNameUnique(add.getAdminName()))) {
                throw new ServiceException("注册的渠道主账号名字重复");
            }
            sysUser.setPassword(BCrypt.hashpw(add.getAdminName()));
            sysUser.setUserName(add.getAdminName());
            sysUser.setNickName(add.getAdminName());
            sysUser.setUserType(UserTypeEnum.SYS_USER.getUserType());
            sysUser.setRegisterChannelId(add.getId());
            sysUser.setRoleIds(new Long[]{UserConstants.DEFAULT_CHANNEL_ROLE_ID});
            Long insertUserId = remoteUserService.registerUserInfo(sysUser);
            // 再次更新一下当前渠道的所属用户
            var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper).eq(Channel::getId, add.getId())
                .set(Channel::getUserId, insertUserId);
            updateChainWrapper.update();
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 修改主渠道
     *
     * @param bo 主渠道
     * @return 结果
     */
    @Override
    public R updateByBo(ChannelBo bo) {
        Channel update = new Channel();
        update.setId(bo.getId());
        update.setChannelName(bo.getChannelName());
        // 获取一下添加绑定了多少游戏
        Integer[] gameNums = Convert.toIntArray(bo.getChannelGame().split(","));
        update.setGameNum(Convert.toInt(Arrays.stream(gameNums).distinct().count()));

        int i = baseMapper.updateById(update);
        if (i > 0) {
            // 针对绑定的游戏进行一下处理
            GameUseRecordBo gameUseRecordBo = new GameUseRecordBo();
            gameUseRecordBo.setPartyAId(bo.getId());
            gameUseRecordBo.setPartyBId(bo.getChannelGame());
            gameUseRecordBo.setType(GameUseRecordTypeEnum.CHANNEL_ASSOCIATION.getCode());
            iGameUseRecordService.updateRGameUseRecord(gameUseRecordBo);

            // channelPackage表里的游戏数据最新化: 该渠道里没有绑定的游戏，都需要删除
            var updateChainWrapper = new LambdaUpdateChainWrapper<>(channelPackageMapper)
                .eq(ChannelPackage:: getChannelId, bo.getId())
                .notIn(ChannelPackage::getGameId, gameNums)
                .set(ChannelPackage::getDelFlag, '1');
            updateChainWrapper.update();

            return R.ok();
        }
        return R.fail();
    }

    /**
     * 渠道开启停用
     */
    @Override
    public R operation(IdNameTypeDicDto dto, Long userId) {
        // 首先获取数据
        LambdaQueryWrapper<Channel> eq = Wrappers.<Channel>lambdaQuery()
            .eq(Channel::getId, dto.getId())
            .eq(LoginHelper.isFront(), Channel::getUserId, userId);
        Channel channel = baseMapper.selectOne(eq);
        if (channel == null) {
            return R.fail("当前数据不存在");
        }
        // 数据存在根据类型进行更换版本操作
        Channel updateChannel = new Channel();
        if (dto.getType().equals(1)) {
            updateChannel.setStatus(0);
        } else if (dto.getType().equals(2)) {
            updateChannel.setStatus(1);
        } else {
            return R.fail("参数错误");
        }
        updateChannel.setId(Convert.toInt(dto.getId()));
        int i = baseMapper.updateById(updateChannel);
        if (i > 0) {
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 分包列表信息获取
     *
     * @return
     */
    @Override
    public TableDataInfo<ChannelPackageVo> groupList(ChannelPackageDto dto) {
        LambdaQueryWrapper<ChannelPackage> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getChannelId() != null, ChannelPackage::getChannelId, dto.getChannelId());
        lqw.eq(dto.getGameId() != null, ChannelPackage::getGameId, dto.getGameId());
        lqw.likeRight(StringUtils.isNotBlank(dto.getLabel()), ChannelPackage::getLabel, dto.getLabel());
        lqw.likeRight(StringUtils.isNotBlank(dto.getNewGameName()), ChannelPackage::getNewGameName, dto.getNewGameName());
        lqw.likeRight(StringUtils.isNotBlank(dto.getVersion()), ChannelPackage::getVersion, dto.getVersion());
        if (dto.getStartTime() != null && dto.getEndTime() != null) {
            lqw.between(ChannelPackage::getCreateTime, dto.getStartTime(), dto.getEndTime());
        }
        lqw.orderByDesc(ChannelPackage::getId);

        Page<ChannelPackageVo> result = channelPackageMapper.selectVoPage(dto.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 导出渠道分组列表
     */
    @Override
    public List<ChannelPackageVo> queryExportList(ChannelPackageDto dto) {
        LambdaQueryWrapper<ChannelPackage> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getChannelId() != null, ChannelPackage::getChannelId, dto.getChannelId());
        lqw.eq(dto.getGameId() != null, ChannelPackage::getGameId, dto.getGameId());
        lqw.orderByDesc(ChannelPackage::getId);
        List<ChannelPackageVo> channelPackageVos = channelPackageMapper.selectVoList(lqw);
        return channelPackageVos;
    }

    /**
     * 添加渠道分包
     *
     * @param bo 主渠道
     * @return 结果
     */
    @Override
    @Transactional
    public R addGroup(ChannelPackageBo bo) {
        try {
            // 判断一下分包数量
            if (bo.getNumberOfSubcontracts() > 200) {
                return R.fail("每次分包上线是200");
            }
            // 首先查询当前渠道已经创建了多少分包
            ChannelVo channelVo = baseMapper.selectVoById(bo.getChannelId());
            if (channelVo == null) {
                return R.fail("当前渠道数据不存在");
            }
            if (!channelVo.getStatus().equals(0)) {
                return R.fail("当前渠道已停用");
            }
            // 判断分包前缀名只能是英文
            Boolean containChinese = StringUtils.isContainChinese(bo.getPackagePrefixName());
            if (containChinese) {
                return R.fail("分包名前缀不能含有中文");
            }
//            // 判断新创建的渠道分包名字是否存在重复的  前提不是当前渠道
//            LambdaQueryWrapper<ChannelPackage> neq = Wrappers.<ChannelPackage>lambdaQuery()
//                .ne(ChannelPackage::getChannelId, bo.getChannelId())
//                .eq(ChannelPackage::getPackagePrefixName, bo.getPackagePrefixName());
//            Long prefixNameCount = channelPackageMapper.selectCount(neq);
//            if (prefixNameCount > 0) {
//                return R.fail("当前渠道前缀名已经存在");
//            }
            // 首先创建一个分包任务
            ChannelPackageTask channelPackageTask = new ChannelPackageTask();
            channelPackageTask.setChannelId(bo.getChannelId());
            channelPackageTask.setGameId(bo.getGameId());
            channelPackageTaskMapper.insert(channelPackageTask);
            Integer insertChannelPackageTaskId = channelPackageTask.getId();
            // 现在已经分包的数量
            LambdaQueryWrapper<ChannelPackage> count = Wrappers.<ChannelPackage>lambdaQuery()
                .eq(ChannelPackage::getChannelId, bo.getChannelId())
                .eq(ChannelPackage::getPackagePrefixName, bo.getPackagePrefixName())
                .eq(ChannelPackage::getGameId, bo.getGameId());
            Long aLong = channelPackageMapper.selectCount(count);
            Integer nowNums = Convert.toInt(aLong);
            // 按照分包数量进行循环设置list
            int rows = 0;
            List<ChannelPackage> list = new ArrayList<ChannelPackage>();
            String uuId = UUID.randomUUID().toString();
            for (Integer i = nowNums + 1; i <= nowNums + bo.getNumberOfSubcontracts(); i++) {
                ChannelPackage channelGroup = new ChannelPackage();
                channelGroup.setChannelId(bo.getChannelId());
                channelGroup.setGameId(bo.getGameId());
                channelGroup.setPackageTaskId(insertChannelPackageTaskId);
                channelGroup.setNewGameName(bo.getNewGameName());
                channelGroup.setIcon(bo.getIcon());
                channelGroup.setPackagePrefixName(bo.getPackagePrefixName());
                channelGroup.setPackageCode(channelVo.getAdminName() + bo.getPackagePrefixName() + "" + i);
                channelGroup.setLabel(bo.getLabel());
                channelGroup.setVersionId(bo.getVersionId());
                channelGroup.setVersion(bo.getVersion());
                channelGroup.setEdition(uuId);
                list.add(channelGroup);
            }
            if (list.size() > 0) {
                rows = channelPackageMapper.insertBatch(list) ? list.size() : 0;
            }
            if (rows > 0) {
                return R.ok();
            }
        } catch (Exception e) {
            log.error("渠道分包报错", e);
        }
        return R.fail();
    }


    /**
     * 修改渠道分包
     *
     * @param bo 主渠道
     * @return 结果
     */
    @Override
    @Transactional
    public R editGroup(ChannelPackageBo bo) {
        try {
            // 首先查询当前渠道已经创建了多少分包
            ChannelVo channelVo = baseMapper.selectVoById(bo.getChannelId());
            if (channelVo == null) {
                return R.fail("当前渠道数据不存在");
            }
            if (!channelVo.getStatus().equals(0)) {
                return R.fail("当前渠道已停用");
            }
            // 修改当前渠道下面所有分包的icon和游戏名，标签
            ChannelPackage channelPackage = new ChannelPackage();
            channelPackage.setIcon(bo.getIcon());
            channelPackage.setNewGameName(bo.getNewGameName());
            channelPackage.setLabel(bo.getLabel());
            channelPackage.setStatus(0);
            LambdaQueryWrapper<ChannelPackage> eq = Wrappers.<ChannelPackage>lambdaQuery()
                .eq(ChannelPackage::getChannelId, bo.getChannelId())
                .eq(ChannelPackage::getGameId, bo.getGameId());
            int update = channelPackageMapper.update(channelPackage, eq);
            if (update > 0) {
                // 查询出当前进行二次分包中存在那些分包任务ID
                LambdaQueryWrapper<ChannelPackage> channelPackageLambdaQueryWrapper = Wrappers.<ChannelPackage>lambdaQuery().select(ChannelPackage::getPackageTaskId)
                    .eq(ChannelPackage::getChannelId, bo.getChannelId())
                    .eq(ChannelPackage::getGameId, bo.getGameId())
                    .groupBy(ChannelPackage::getPackageTaskId);
                List<ChannelPackage> channelPackages = channelPackageMapper.selectList(channelPackageLambdaQueryWrapper);
                List<Integer> ids = new ArrayList<>();
                channelPackages.forEach(a -> {
                    if (a != null) {
                        ids.add(a.getPackageTaskId());
                    }
                });
                // 执行更新操作
                var updateChainWrapper = new LambdaUpdateChainWrapper<>(channelPackageTaskMapper).in(ChannelPackageTask::getId, ids)
                    .set(ChannelPackageTask::getStatus, 0);
                updateChainWrapper.update();
                return R.ok();
            }
        } catch (Exception e) {
            log.error("渠道二次分包报错", e);
        }
        return R.fail();
    }


    /**
     * 根据渠道code查询出分包渠道的基础信息
     *
     * @return
     */
    @Override
    public ChannelPackageVo selectChannelPackageByCode(String codeKey, String appId) {
        boolean isQuary = false;
        if (StringUtils.isNotEmpty(codeKey)) {
            if (codeKey.equals("default")) {
                isQuary = true;
            } else {
                LambdaQueryWrapper<ChannelPackage> eq = Wrappers.<ChannelPackage>lambdaQuery()
                    .eq(ChannelPackage::getPackageCode, codeKey)
                    .last("limit 1");
                ChannelPackageVo channelPackageVo = channelPackageMapper.selectVoOne(eq);
                if (channelPackageVo != null) {
                    // 数据查询到之后我们需要获取一下渠道名
                    LambdaQueryWrapper<Channel> channelLqw = Wrappers.<Channel>lambdaQuery().select(Channel::getChannelName).eq(Channel::getId, channelPackageVo.getChannelId());
                    ChannelVo channelVo = baseMapper.selectVoById(channelPackageVo.getChannelId());
                    if (channelVo != null) {
                        channelPackageVo.setChannelName(channelVo.getChannelName());
                    }

                    OpenGameDubboVo openGameDubboVo = remoteGameManagerService.selectOpenGameInfo(appId);
                    if (openGameDubboVo != null) {
                        channelPackageVo.setGameId(openGameDubboVo.getId());
                        channelPackageVo.setNewGameName(openGameDubboVo.getGameName());
                    }
                    return channelPackageVo;
                } else {
                    isQuary = true;
                }
            }
            // 默认渠道/渠道信息不存在的情况
            if (isQuary) {
                // 默认渠道我们通过appId获取到游戏的ID
                ChannelPackageVo channelPackageVo = new ChannelPackageVo();
                channelPackageVo.setChannelId(0);
                channelPackageVo.setPackageCode("default");
                channelPackageVo.setChannelName("默认渠道");
                channelPackageVo.setVersionId(0);
                channelPackageVo.setVersion("v0.0.0");
                OpenGameDubboVo openGameDubboVo = remoteGameManagerService.selectOpenGameInfo(appId);
                if (openGameDubboVo != null) {
                    channelPackageVo.setGameId(openGameDubboVo.getId());
                    channelPackageVo.setNewGameName(openGameDubboVo.getGameName());
                }
                return channelPackageVo;
            }
        }
        return null;
    }

//
//    /**
//     * 根据渠道code查询出分包渠道的基础信息
//     *
//     * @param gameName
//     * @param gameId
//     * @return
//     */
//    @Override
//    public Boolean createChannelPackageDefault(String gameName, Integer gameId) {
//        // 首先查询当前分包列表中是否存在
//        LambdaQueryWrapper<ChannelPackage> aDefault = Wrappers.<ChannelPackage>lambdaQuery().eq(ChannelPackage::getPackageCode, "default")
//            .eq(ChannelPackage::getGameId, gameId);
//        Long aLong = channelGroupMapper.selectCount(aDefault);
//        if (aLong > 0) {
//            // 代表当前游戏已经存在默认的渠道
//            return false;
//        }
//        // 不存在 我们需要创建当前游戏的默认分包渠道
//        ChannelPackage channelPackage = new ChannelPackage();
//        channelPackage.setChannelId(0);
//        channelPackage.setGameId(gameId);
//        channelPackage.setNewGameName(gameName);
//        channelPackage.setPackageCode("default");
//        channelPackage.setVersion("默认版本");
//        channelPackage.setLabel("游戏默认渠道");
//        channelPackage.setEdition(UUID.randomUUID().toString());
//        int insert = channelGroupMapper.insert(channelPackage);
//        if (insert > 0) {
//            return true;
//        }
//        return false;
//    }

    /**
     * 获取一批游戏的分包基础信息
     *
     * @param gameIds
     * @return
     */
    @Override
    public List<ChannelPackageVo> getGamePackageInfoByIds(List<Integer> gameIds) {
        // 查询
        LambdaQueryWrapper<ChannelPackage> eq = Wrappers.<ChannelPackage>lambdaQuery()
            .in(gameIds.size() > 0, ChannelPackage::getGameId, gameIds)
            .eq(ChannelPackage::getPackageType, 1);
        return channelPackageMapper.selectVoList(eq);
    }

    /**
     * 获取一批游戏的分包基础信息
     *
     * @return
     */
    @Override
    public List<ChannelPackageVo> getGameListByName() {
        // 查询
        LambdaQueryWrapper<ChannelPackage> eq = Wrappers.<ChannelPackage>lambdaQuery()
            .select(ChannelPackage::getGameId, ChannelPackage::getNewGameName)
            .eq(ChannelPackage::getPackageType, 1)
            .groupBy(ChannelPackage::getNewGameName)
            .orderByAsc(ChannelPackage::getNewGameName);
        return channelPackageMapper.selectVoList(eq);
    }


}
