package com.euler.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.*;
import com.euler.common.core.utils.BeanCopyUtils;
import com.euler.common.core.utils.HttpRequestHeaderUtils;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.payment.api.RemoteOrderService;
import com.euler.platform.api.RemoteGameManagerService;
import com.euler.platform.api.domain.GameVersionHistoryVo;
import com.euler.platform.api.domain.OpenGame;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.platform.api.domain.OpenGameVo;
import com.euler.platform.domain.*;
import com.euler.platform.domain.bo.*;
import com.euler.platform.domain.dto.OpenGamePageDto;
import com.euler.platform.domain.dto.OpenGameTransferDto;
import com.euler.platform.domain.dto.OpenGameTransferLogDto;
import com.euler.platform.domain.dto.OpenGameVersionHistoryDto;
import com.euler.platform.domain.vo.*;
import com.euler.platform.enums.OperationActionTypeEnum;
import com.euler.platform.mapper.*;
import com.euler.platform.service.IOpenGameAuditRecordService;
import com.euler.platform.service.IOpenGameService;
import com.euler.platform.service.IUserService;
import com.euler.sdk.api.RemoteGameConfigService;
import com.euler.sdk.api.domain.GameConfigVo;
import com.euler.system.api.RemoteDictService;
import com.euler.system.api.RemoteSignService;
import com.euler.system.api.domain.AppConfig;
import com.euler.system.api.domain.SysDictData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 【游戏管理】Service业务层处理
 *
 * @author open
 * @date 2022-02-18
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OpenGameServiceImpl implements IOpenGameService {

    @Autowired
    private IOpenGameAuditRecordService iOpenGameAuditRecordService;
    @Autowired
    private OpenGameMapper baseMapper;
    @Autowired
    private OpenGameVersionHistoryMapper openGameVersionHistoryMapper;
    @Autowired
    private OpenGameAuditRecordMapper openGameAuditRecordMapper;
    @Autowired
    private OpenUserCertificationMapper openUserCertificationMapper;
    @Autowired
    private OpenGameTransferLogMapper openGameTransferLogMapper;
    @Autowired
    private IUserService iUserService;
    @DubboReference
    private RemoteDictService remoteDictService;
    @DubboReference
    private RemoteSignService remoteSignService;
    @DubboReference
    private RemoteOrderService remoteOrderService;
    @DubboReference
    private RemoteGameManagerService remoteGameManagerService;
    @DubboReference
    private RemoteGameConfigService remoteGameConfigService;

    /**
     * 资质审核基础数据统计
     */
    @Override
    public OpenGameDataCountVo selectCount() {
        List<KeyValueDto> keyValueDtos = baseMapper.selectGameAuditStatusCount();
        OpenGameDataCountVo vo = new OpenGameDataCountVo();
        for (var x : keyValueDtos) {
            if (x.getValue().equals("1")) {
                vo.setInCount(Convert.toInt(x.getKey()));
            } else if (x.getValue().equals("2")) {
                vo.setPassCount(Convert.toInt(x.getKey()));
            } else if (x.getValue().equals("3")) {
                vo.setRejectCount(Convert.toInt(x.getKey()));
            }
        }
        // 获取当前游戏列表中下架的游戏总数
        LambdaQueryWrapper<OpenGame> eqCount = Wrappers.<OpenGame>lambdaQuery().eq(OpenGame::getGameStatus, 2).eq(OpenGame::getOnlineStatus, 2);
        Long aLong = baseMapper.selectCount(eqCount);
        vo.setDownCount(Convert.toInt(aLong));

        return vo;
    }

    /**
     * 查询【游戏管理】
     *
     * @param idDto 【游戏管理】主键
     * @return 【游戏管理】
     */
    @Override
    public OpenGameVo selectInfo(IdDto idDto, Long userId) {
        LambdaQueryWrapper<OpenGame> eq = Wrappers.<OpenGame>lambdaQuery()
            .eq(OpenGame::getId, idDto.getId())
            .eq(LoginHelper.isFront(), OpenGame::getUserId, userId);

        OpenGameVo openGameVo = baseMapper.selectVoOne(eq);
        // 循环添加游戏类目名称
        List<SysDictData> platformGameType = remoteDictService.selectDictDataByType("platform_game_type");
        // 循环追加名称
        Optional<SysDictData> platformGameTypeAny = platformGameType.stream().filter(b -> b.getDictValue().equals(openGameVo.getGameCategory())).findAny();
        platformGameTypeAny.ifPresent(sysDictData -> openGameVo.setGameCategoryName(sysDictData.getDictLabel()));
        // 获取当前游戏的审核原因
        LambdaQueryWrapper<OpenGameAuditRecord> last = Wrappers.<OpenGameAuditRecord>lambdaQuery().eq(OpenGameAuditRecord::getGameId, openGameVo.getId())
            .orderByDesc(OpenGameAuditRecord::getId)
            .last("limit 1");
        OpenGameAuditRecord openGameAuditRecord = openGameAuditRecordMapper.selectOne(last);
        if (openGameAuditRecord != null) {
            openGameVo.setAuditRecord(openGameAuditRecord.getAuditRecord());
        }
        List<Integer> gameIds = new ArrayList<>();
        gameIds.add(openGameVo.getId());
        List<OpenGameVersionHistory> openGameVersionHistory = openGameVersionHistoryMapper.selectVersionNewOne(gameIds);
        if (openGameVersionHistory != null && openGameVersionHistory.size() > 0) {
            Optional<OpenGameVersionHistory> first = openGameVersionHistory.stream().filter(c -> c.getGameId().equals(openGameVo.getId())).findFirst();
            if (first.isPresent()) {
                openGameVo.setVersionHistoryNumber(first.get().getVersionNumberName());
                openGameVo.setVersionHistoryStatus(first.get().getAuditStatus());
                openGameVo.setVersionHistoryCreateTime(first.get().getCreateTime());
            }
        }
        // 获取游戏的历史版本内容
        OpenGameVersionHistoryVo openGameVersionHistoryVo = openGameVersionHistoryMapper.selectVoById(openGameVo.getVersionId());
        if (openGameVersionHistoryVo != null) {
            GameVersionHistoryVo copy = BeanCopyUtils.copy(openGameVersionHistoryVo, GameVersionHistoryVo.class);
            openGameVo.setGameVersionHistoryVo(copy);
        }
        // 追加游戏的公司名
        LambdaQueryWrapper<OpenUserCertification> lq = Wrappers.<OpenUserCertification>lambdaQuery();
        lq.eq(OpenUserCertification::getUserId, openGameVo.getUserId()).last(" limit 1");
        OpenUserCertification openUserCertification = openUserCertificationMapper.selectOne(lq);
        if (openUserCertification != null) {
            openGameVo.setCompanyName(openUserCertification.getCompanyName());
        }
        return openGameVo;
    }

    /**
     * 查询【游戏管理】列表
     *
     * @param 【游戏管理】
     * @return 【游戏管理】
     */
    @Override
    public TableDataInfo<OpenGameVo> queryPageList(OpenGamePageDto openGamePageDto) {
        // 进行数据查询
        LambdaQueryWrapper<OpenGame> baseWrapper = Wrappers.<OpenGame>lambdaQuery()
            .eq(LoginHelper.isFront(), OpenGame::getUserId, openGamePageDto.getUserId())
            .eq(openGamePageDto.getGameStatus() != null, OpenGame::getGameStatus, openGamePageDto.getGameStatus())
            .likeRight(StringUtils.isNotEmpty(openGamePageDto.getGameName()), OpenGame::getGameName, openGamePageDto.getGameName())
            .eq(Convert.toInt(openGamePageDto.getOperationPlatform(), 0) > 0, OpenGame::getOperationPlatform, openGamePageDto.getOperationPlatform());
        if (openGamePageDto.getIds() != null) {
            List<Integer> gameIds = Convert.toList(Integer.class, openGamePageDto.getIds());
            baseWrapper.in((gameIds != null && gameIds.size() > 0), OpenGame::getId, gameIds);
        }
        Page<OpenGameVo> result = baseMapper.selectVoPage(openGamePageDto.build(), baseWrapper);
        // 为空的时候直接返回
        if (result.getRecords() == null || result.getRecords().size() <= 0) {
            return TableDataInfo.build(result);
        }
        // 获取这个游戏的ID 用于查询这些游戏对应版本的状态
        List<Integer> gameIds = result.getRecords().stream().map(OpenGameVo::getId).distinct().collect(Collectors.toList());
        List<Long> userIds = result.getRecords().stream().map(OpenGameVo::getUserId).distinct().collect(Collectors.toList());
        List<OpenGameVersionHistory> openGameVersionHistory = null;
        List<OpenGameAuditRecord> openGameAuditRecords = null;
        if (gameIds.size() > 0) {
            openGameVersionHistory = openGameVersionHistoryMapper.selectVersionNewOne(gameIds);
            // 获取当前游戏的审核原因
            openGameAuditRecords = openGameAuditRecordMapper.selectAuditNewOne(gameIds);
        }
        // 循环添加游戏类目名称
        List<SysDictData> platformGameType = remoteDictService.selectDictDataByType("platform_game_type");
        // 获取这些游戏的公司名
        List<OpenUserCertificationVo> openUserCertificationVos = null;
        if (userIds.size() > 0) {
            LambdaQueryWrapper<OpenUserCertification> lq = Wrappers.<OpenUserCertification>lambdaQuery();
            lq.in(OpenUserCertification::getUserId, userIds);
            openUserCertificationVos = openUserCertificationMapper.selectVoList(lq);
        }
        // 循环追加名称
        List<OpenGameVersionHistory> finalOpenGameVersionHistory = openGameVersionHistory;
        List<OpenGameAuditRecord> finalOpenGameAuditRecords = openGameAuditRecords;
        List<OpenUserCertificationVo> finalOpenUserCertificationVos = openUserCertificationVos;
        result.getRecords().forEach(a -> {
            Optional<SysDictData> platformGameTypeAny = platformGameType.stream().filter(b -> b.getDictValue().equals(a.getGameCategory())).findFirst();
            platformGameTypeAny.ifPresent(sysDictData -> a.setGameCategoryName(sysDictData.getDictLabel()));
            if (finalOpenGameVersionHistory != null) {
                Optional<OpenGameVersionHistory> first = finalOpenGameVersionHistory.stream().filter(c -> c.getGameId().equals(a.getId())).findFirst();
                if (first.isPresent()) {
                    a.setVersionHistoryNumber(first.get().getVersionNumberName());
                    a.setVersionHistoryStatus(first.get().getAuditStatus());
                    a.setVersionHistoryCreateTime(first.get().getCreateTime());
                }
            }
            // 追加审核原因
            if (finalOpenGameAuditRecords.size() > 0) {
                Optional<OpenGameAuditRecord> second = finalOpenGameAuditRecords.stream().filter(d -> d.getGameId().equals(a.getId())).findFirst();
                second.ifPresent(openGameAuditRecord -> a.setAuditRecord(openGameAuditRecord.getAuditRecord()));
            }
            // 追加公司名
            if (finalOpenUserCertificationVos.size() > 0) {
                Optional<OpenUserCertificationVo> third = finalOpenUserCertificationVos.stream().filter(e -> e.getUserId().equals(a.getUserId())).findFirst();
                third.ifPresent(openUserCertificationVo -> a.setCompanyName(openUserCertificationVo.getCompanyName()));
            }
        });
        return TableDataInfo.build(result);
    }

    /**
     * 查询【游戏管理】列表
     *
     * @param 【游戏管理】
     * @return 【游戏管理】
     */
    @Override
    public List<OpenGameVo> queryList(OpenGameBo bo) {
        Long userId = LoginHelper.getUserId();
        LambdaQueryWrapper<OpenGame> eq = Wrappers.<OpenGame>lambdaQuery()
            .eq(LoginHelper.isFront(), OpenGame::getUserId, userId)
            .eq(OpenGame::getDelFlag, 0);
        return baseMapper.selectVoList(eq);
    }

    /**
     * 新增【游戏管理】
     *
     * @param 【游戏管理】
     * @return 结果
     */
    @Override
    @Transactional
    public R insertByBo(OpenGameBo bo) {
        OpenGame add = BeanUtil.toBean(bo, OpenGame.class);
        log.info("添加的字段列表->" + JsonUtils.toJsonString(add));
        // 保存前的数据校验
        String s = validEntityBeforeSave(add);
        if (!s.equals("success")) {
            return R.fail(s);
        }

        // 数据添加之前我们需要将当前数据的状态变更成为审核状态
        add.setGameStatus(1);
        // 为其增加app_id和app_secret
        String appId = "n9p" + IdUtil.simpleUUID().substring(16);
        String appSecret = IdUtil.simpleUUID();
        add.setAppId(appId);
        add.setAppSecret(appSecret);
        // 在app_config表中增加一条数据
        AppConfig appConfig = new AppConfig();
        appConfig.setAppName(bo.getGameName());
        appConfig.setAppId(appId);
        appConfig.setAppSecret(appSecret);
        appConfig.setIsShow(1);
        remoteSignService.addAppConfig(appConfig);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            // 添加游戏审批记录
            OpenGameAuditRecordBo openGameAuditRecordBo = new OpenGameAuditRecordBo();
            openGameAuditRecordBo.setGameId(add.getId());
            openGameAuditRecordBo.setOperationAction(OperationActionTypeEnum.Submit.getCode());
            iOpenGameAuditRecordService.insertByBo(openGameAuditRecordBo);
//            // 调用sdk dubbo服务创建默认的分包
//            remoteChannelService.createChannelPackageDefault(add.getGameName(), add.getId());
            return R.ok(add.getId());
        }
        return R.fail();
    }

    /**
     * 修改【游戏管理】
     *
     * @param 【游戏管理】
     * @return 结果
     */
    @Override
    public R updateByBo(OpenGameBo bo) {
        // 验证是否传输过来主键
        if (bo.getId() == null || bo.getId() <= 0) {
            return R.fail("参数缺失");
        }
        if (LoginHelper.isFront()) {
            // 检测 如果当前游戏正在处于审核中 是不会允许修改的
            IdDto idDto = new IdDto();
            idDto.setId(bo.getId());
            OpenGameVo openGameVo = this.selectInfo(idDto, bo.getUserId());
            // 游戏只要不处于审核中  或者 上线状态 或者为空 都不能进行修改
            if (openGameVo == null || openGameVo.getGameStatus().equals(1) || openGameVo.getOnlineStatus().equals(1)) {
                return R.fail("当前状态不允许进行修改");
            }
        }
        OpenGame update = BeanUtil.toBean(bo, OpenGame.class);
        // 保存前的数据校验
        String s = validEntityBeforeSave(update);
        if (!s.equals("success")) {
            return R.fail(s);
        }
        // 更新数据需要将当前数据变更成为最初的状态
        update.setGameStatus(1);
        update.setOnlineStatus(0);
        // 更新的数据进行一下日志记录
        log.info("游戏更新数据 code码【1F0AN63OAT】->", JsonUtils.toJsonString(update));
        int i = baseMapper.updateById(update);
        if (i > 0) {
            // 添加游戏审批记录
            OpenGameAuditRecordBo openGameAuditRecordBo = new OpenGameAuditRecordBo();
            openGameAuditRecordBo.setGameId(bo.getId());
            openGameAuditRecordBo.setOperationAction(OperationActionTypeEnum.Submit.getCode());
            iOpenGameAuditRecordService.insertByBo(openGameAuditRecordBo);
            return R.ok();
        }
        return R.fail("数据更新失败");
    }

    /**
     * 批量删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 撤销审核
     *
     * @param idDto
     * @param userId
     * @return
     */
    @Override
    public R revokeApproval(IdDto<Integer> idDto, Long userId) {
        LambdaQueryWrapper<OpenGame> eq = Wrappers.<OpenGame>lambdaQuery()
            .eq(OpenGame::getId, idDto.getId())
            .eq(OpenGame::getUserId, userId);
        OpenGame openGame = baseMapper.selectOne(eq);
        if (openGame != null) {
            // 不为空的时候  只有你提交的数据是待审核状态才能进行提交
            if (!openGame.getGameStatus().equals(1)) {
                return R.fail("审核中的才能进行撤销");
            }
            OpenGame update = new OpenGame();
            update.setId(openGame.getId());
            update.setGameStatus(0);
            update.setOnlineStatus(0);
            int i = baseMapper.updateById(update);
            if (i > 0) {
                return R.ok();
            }
        }
        return R.fail();
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private String validEntityBeforeSave(OpenGame entity) {
        //TODO 做一些数据校验,如唯一约束
        // 验证运行平台是否正确
        if (entity.getOperationPlatform() != null) {
            Integer[] operationPlatformList = new Integer[]{1, 2, 3};
            Optional<Integer> operationPlatformAny = Arrays.stream(operationPlatformList).filter(a -> a.equals(entity.getOperationPlatform())).findAny();
            if (!operationPlatformAny.isPresent()) {
                return "运行平台填写有误";
            }
        }
        // 验证游戏类目是否正确
        if (StringUtils.isNotEmpty(entity.getGameCategory())) {
            List<SysDictData> platformGameType = remoteDictService.selectDictDataByType("platform_game_type");
            Optional<SysDictData> platformGameTypeAny = platformGameType.stream().filter(a -> a.getDictValue().equals(entity.getGameCategory())).findAny();
            if (!platformGameTypeAny.isPresent()) {
                return "游戏类目填写有误";
            }
        }
        // 校验一下付费类型只可以有1和2才可以新增成功
        if (entity.getPayType() != null) {
            if (entity.getPayType() != 1 && entity.getPayType() != 2) {
                return "您输入的付费类型有误，请重新输入";
            }
        }
        // 付费类型选择有付费的时候 充值回调地址和回调秘钥不能为空
        if (entity.getPayType() != null
            && entity.getPayType().equals(1)
            && (StringUtils.isEmpty(entity.getRechargeCallback()) || StringUtils.isEmpty(entity.getCallbackSecretKey()))) {
            return "充值回调地址和回调秘钥不能为空";
        }
        // 游戏标签不为空的时候判断最多10个标签，每个标签最多4个字
        if (StringUtils.isNotEmpty(entity.getGameTags())) {
            // 标签不为空 我们就需要进行下一步标签信息的判断
            String[] split = entity.getGameTags().split(",");
            if (split.length > 10) {
                return "标签最多只能10个";
            }
            for (var s : split) {
                if (s.length() > 4) {
                    return "每个标签最多4个字";
                }
            }
        }
        // TODO 验证Icon的尺寸(640*640)和五图的尺寸(960*480)
        // 验证游戏图片的json是否超出
        if (StringUtils.isNotEmpty(entity.getPictureUrl())) {
            String[] split = entity.getPictureUrl().split(",");
            if (split.length > 5) {
                return "游戏图片不能超过五张";
            }
        }
        return "success";
    }

    /**
     * 游戏的上线操作
     *
     * @param idNameTypeDicDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R operation(IdNameTypeDicDto idNameTypeDicDTO, Long userId) {
        // 首先获取数据
        LambdaQueryWrapper<OpenGame> eq = Wrappers.<OpenGame>lambdaQuery()
            .eq(OpenGame::getId, idNameTypeDicDTO.getId())
            .eq(LoginHelper.isFront(), OpenGame::getUserId, userId);
        OpenGame openGame = baseMapper.selectOne(eq);
        if (openGame == null) {
            return R.fail("当前数据不存在");
        }
        if (!openGame.getGameStatus().equals(2)) {
            return R.fail("当前游戏审核状态错误");
        }
        // 当进行上架操作的时候需要判断一下当前游戏是否存在已经审核通过的版本包
        LambdaQueryWrapper<OpenGameVersionHistory> versionEq = Wrappers.<OpenGameVersionHistory>lambdaQuery().eq(OpenGameVersionHistory::getId, openGame.getVersionId());
        OpenGameVersionHistory openGameVersionHistory = openGameVersionHistoryMapper.selectOne(versionEq);
        if (openGameVersionHistory != null && !openGameVersionHistory.getAuditStatus().equals(2)) {
            return R.fail("当前游戏的包没有通过审核");
        }
        // 判断游戏中是否有上架的包
        if (openGame.getVersionId() == null
            || StringUtils.isEmpty(openGame.getVersionNumberName())
            || StringUtils.isEmpty(openGame.getGameInstallPackage())) {
            // 任何一个为空的时候我们都代表着当前游戏没有上架游戏包
            return R.fail("当前游戏并没有上架版本包，请先上架版本包");
        }
        // 数据存在 我们开始进行数据更新
        OpenGame updateOpenGame = new OpenGame();
        // 添加游戏审批记录
        OpenGameAuditRecordBo openGameAuditRecordBo = new OpenGameAuditRecordBo();
        openGameAuditRecordBo.setGameId(Convert.toInt(idNameTypeDicDTO.getId()));
        if (idNameTypeDicDTO.getType().equals(1)) {
            updateOpenGame.setOnlineStatus(1);
            updateOpenGame.setOnTime(new Date());
            openGameAuditRecordBo.setOperationAction(OperationActionTypeEnum.On.getCode());
        } else if (idNameTypeDicDTO.getType().equals(2)) {
            updateOpenGame.setOnlineStatus(2);
            updateOpenGame.setOffTime(new Date());
            openGameAuditRecordBo.setOperationAction(OperationActionTypeEnum.Off.getCode());
        } else {
            return R.fail("参数错误");
        }
        updateOpenGame.setId(Convert.toInt(idNameTypeDicDTO.getId()));
        int i = baseMapper.updateById(updateOpenGame);
        Boolean aBoolean = iOpenGameAuditRecordService.insertByBo(openGameAuditRecordBo);
        if (i > 0 && aBoolean) {
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 游戏审核
     *
     * @param bo
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R auditGame(OpenGameAuditRecordBo bo, Long userId) {
        if (LoginHelper.isFront()) {
            return R.fail("您当前没有权限");
        }
        // 首先查询当前游戏是否存在
        OpenGameVo openGameVo = baseMapper.selectVoById(bo.getGameId());
        if (openGameVo == null) {
            return R.fail("当前数据不存在");
        }
        if (openGameVo.getGameStatus().equals(0)) {
            return R.fail("待提交数据不能进行审核");
        }
        if (openGameVo.getGameStatus().equals(2)) {
            return R.fail("当前游戏已经审核成功了");
        }
        // 进行更新操作
        OpenGame openGame = new OpenGame();
        openGame.setId(bo.getGameId());
        openGame.setAuditTime(new Date());
        if (bo.getAuditStatus().equals(1)) {
            openGame.setGameStatus(2);
            bo.setOperationAction(OperationActionTypeEnum.Pass.getCode());
        } else if (bo.getAuditStatus().equals(2)) {
            openGame.setGameStatus(3);
            bo.setOperationAction(OperationActionTypeEnum.Reject.getCode());
        } else {
            return R.fail("参数错误");
        }
        int i = baseMapper.updateById(openGame);
        // 补充一下入库前的数据
        bo.setAuditUserId(userId);
        bo.setCreateTime(new Date());
        // 进行一下审核数据的记录
        Boolean aBoolean = iOpenGameAuditRecordService.insertByBo(bo);
        if (i > 0 && aBoolean) {
            return R.ok();
        }
        return R.fail();
    }

    /**
     * @return
     */
    @Override
    public List<OpenGameDubboVo> selectByIds(List<Integer> collect) {
        LambdaQueryWrapper<OpenGame> lqw = new LambdaQueryWrapper<OpenGame>()
            .in(ArrayUtil.isNotEmpty(collect) && collect.size() > 0, OpenGame::getId, collect)
            .orderByDesc(OpenGame::getId);

        List<OpenGame> openGames = baseMapper.selectList(lqw);
        List<OpenGameDubboVo> openGameDubboVos = BeanCopyUtils.copyList(openGames, OpenGameDubboVo.class);
        // 数据获取完毕之后我们需要获取游戏的分类
        List<SysDictData> platformGameType = remoteDictService.selectDictDataByType("platform_game_type");
        if (openGameDubboVos.size() > 0) {
            openGameDubboVos.forEach(a -> {
                Optional<SysDictData> first = platformGameType.stream().filter(b -> b.getDictValue().equals(a.getGameCategory())).findFirst();
                first.ifPresent(sysDictData -> a.setGameCategoryName(sysDictData.getDictLabel()));
            });
        }
        return openGameDubboVos;
    }

    /**
     * @return
     */
    @Override
    public TableDataInfoCoreDto<OpenGameDubboVo> selectByParams(List<Integer> collect, SelectGameDto dto) {
        // 手动计算分页
        if (dto.getPageNum() == null) {
            dto.setPageNum(1);
        }
        if (dto.getPageSize() == null) {
            dto.setPageSize(10);
        }
        Integer startNum = (dto.getPageNum() == 1) ? 0 : ((dto.getPageNum() - 1) * dto.getPageSize());
        // 开始拼接分页
        LambdaQueryWrapper<OpenGame> lqw = new LambdaQueryWrapper<OpenGame>()
            .in(ArrayUtil.isNotEmpty(collect) && collect.size() > 0, OpenGame::getId, collect)
            .likeRight(StringUtils.isNotBlank(dto.getGameName()), OpenGame::getGameName, dto.getGameName())
            .eq(dto.getOperationPlatform() != null, OpenGame::getOperationPlatform, dto.getOperationPlatform());
        // 在获取一下总数
        Long aLong = baseMapper.selectCount(lqw);
        lqw.last("limit " + startNum + ", " + dto.getPageSize())
            .orderByDesc(OpenGame::getId);
        List<OpenGame> openGames = baseMapper.selectList(lqw);
        List<OpenGameDubboVo> openGameDubboVos = BeanCopyUtils.copyList(openGames, OpenGameDubboVo.class);
        TableDataInfoCoreDto<OpenGameDubboVo> build = TableDataInfoCoreDto.build(openGameDubboVos, Convert.toInt(aLong));
        return build;
    }

    /**
     * 通过id查询游戏基础信息, 不分页
     *
     * @return
     */
    @Override
    public List<OpenGameDubboVo> selectByChannel(List<Integer> collect, SelectGameDto dto) {
        LambdaQueryWrapper<OpenGame> lqw = new LambdaQueryWrapper<OpenGame>()
            .in(ArrayUtil.isNotEmpty(collect) && collect.size() > 0, OpenGame::getId, collect)
            .likeRight(StringUtils.isNotBlank(dto.getGameName()), OpenGame::getGameName, dto.getGameName())
            .eq(dto.getOperationPlatform() != null, OpenGame::getOperationPlatform, dto.getOperationPlatform())
            .orderByDesc(OpenGame::getId);

        List<OpenGame> openGames = baseMapper.selectList(lqw);
        List<OpenGameDubboVo> openGameDubboVos = BeanCopyUtils.copyList(openGames, OpenGameDubboVo.class);
        return openGameDubboVos;
    }

    @Override
    public OpenGameDubboVo selectInfoByAppId(String appId) {
        LambdaQueryWrapper<OpenGame> lqw = new LambdaQueryWrapper<OpenGame>()
            .eq(OpenGame::getAppId, appId)
            .last("limit 1");

        OpenGame openGames = baseMapper.selectOne(lqw);
        OpenGameDubboVo openGameDubboVo = BeanCopyUtils.copy(openGames, OpenGameDubboVo.class);
        return openGameDubboVo;
    }

    /**
     * 游戏版本历史添加
     *
     * @param bo
     * @return
     */
    public R addVersion(OpenGameVersionHistoryBo bo) {
        OpenGameVersionHistory add = BeanUtil.toBean(bo, OpenGameVersionHistory.class);
        log.info("添加的字段列表->" + JsonUtils.toJsonString(add));
        // 保存前的数据校验
        if (StringUtils.isNotEmpty(add.getPictureUrl())) {
            String[] split = add.getPictureUrl().split(",");
            if (split.length > 5) {
                return R.fail("游戏图片不能超过五张");
            }
        }
        // 如果已经存在之前的版本 那么再次提交的版本号不能低于上一个版本
        LambdaQueryWrapper<OpenGameVersionHistory> lqw = Wrappers.<OpenGameVersionHistory>lambdaQuery()
            .eq(OpenGameVersionHistory::getGameId, bo.getGameId())
            .orderByDesc(OpenGameVersionHistory::getId)
            .last("limit 1");
        OpenGameVersionHistory openGameVersionHistory = openGameVersionHistoryMapper.selectOne(lqw);
        if (openGameVersionHistory != null && openGameVersionHistory.getVersionNumber() >= bo.getVersionNumber()) {
            return R.fail("新版本号必须大于之前提交的版本号");
        }
        // 查询一下当前添加版本的游戏是否审核通过
        LambdaQueryWrapper<OpenGame> lqwGameInfo = Wrappers.<OpenGame>lambdaQuery()
            .eq(OpenGame::getId, bo.getGameId());
        OpenGame openGame = baseMapper.selectOne(lqwGameInfo);
        if (openGame != null && !openGame.getGameStatus().equals(2)) {
            return R.fail("当前游戏没有审核通过");
        }
        // 数据添加
        add.setAuditStatus(1);
        boolean flag = openGameVersionHistoryMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 游戏版本历史查询
     *
     * @return
     */
    @Override
    public TableDataInfo<OpenGameVersionHistoryVo> gameVersionList(OpenGameVersionHistoryDto dto) {
        LambdaQueryWrapper<OpenGameVersionHistory> lqw = Wrappers.<OpenGameVersionHistory>lambdaQuery()
            .eq(OpenGameVersionHistory::getGameId, dto.getGameId())
            .orderByDesc(OpenGameVersionHistory::getId);
        IPage<OpenGameVersionHistoryVo> result = openGameVersionHistoryMapper.selectVoPage(dto.build(), lqw);

        // 查询一下当前添加的包列表中那一条处于上线状态
        LambdaQueryWrapper<OpenGame> eq = Wrappers.<OpenGame>lambdaQuery().eq(OpenGame::getId, dto.getGameId());
        OpenGame openGame = baseMapper.selectOne(eq);
        result.getRecords().forEach(a -> {
            if (a.getId() >= openGame.getVersionId()) {
                if (a.getId().equals(openGame.getVersionId())) {
                    a.setIsUp(1);
                } else {
                    a.setIsUp(2);
                }
            }
        });
        return TableDataInfo.build(result);
    }

    /**
     * 不同版本的操作行为
     */
    @Override
    public R operationVersion(IdNameTypeDicDto dto, Long userId) {
        // 首先获取数据
        LambdaQueryWrapper<OpenGameVersionHistory> eq = Wrappers.<OpenGameVersionHistory>lambdaQuery()
            .eq(OpenGameVersionHistory::getId, dto.getId())
            .eq(LoginHelper.isFront(), OpenGameVersionHistory::getUserId, userId);
        OpenGameVersionHistory openGameVersionHistory = openGameVersionHistoryMapper.selectOne(eq);
        if (openGameVersionHistory == null) {
            return R.fail("当前数据不存在");
        }
        if (!openGameVersionHistory.getAuditStatus().equals(2)) {
            return R.fail("当前版本没有审核通过");
        }
        // 判断低版本不能替换高版本
        LambdaQueryWrapper<OpenGame> getGameInfo = Wrappers.<OpenGame>lambdaQuery().eq(OpenGame::getId, openGameVersionHistory.getGameId());
        OpenGame searchOpenGame = baseMapper.selectOne(getGameInfo);
        if (searchOpenGame.getVersionId() > openGameVersionHistory.getId()) {
            return R.fail("低版本不能替换高版本");
        }
        // 数据存在根据类型进行更换版本操作
        OpenGame openGame = new OpenGame();
        openGame.setVersionId(Convert.toInt(dto.getId()));
        openGame.setVersionNumberName(openGameVersionHistory.getVersionNumberName());
        openGame.setGameInstallPackage(openGameVersionHistory.getGameInstallPackage());
        openGame.setVersionOnlineTime(new Date());
        openGame.setId(openGameVersionHistory.getGameId());
        int i = baseMapper.updateById(openGame);
        if (i > 0) {
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 审核添加的游戏版本
     */
    @Override
    public R auditVersion(IdNameTypeDicDto dto, Long userId) {
        // 首先获取数据
        LambdaQueryWrapper<OpenGameVersionHistory> eq = Wrappers.<OpenGameVersionHistory>lambdaQuery()
            .eq(OpenGameVersionHistory::getId, dto.getId());
        OpenGameVersionHistory openGameVersionHistory = openGameVersionHistoryMapper.selectOne(eq);
        if (openGameVersionHistory == null) {
            return R.fail("当前数据不存在");
        }
        // 数据存在根据类型进行更新
        OpenGameVersionHistory updateOperationVersion = new OpenGameVersionHistory();
        if (dto.getType().equals(1)) {
            updateOperationVersion.setAuditStatus(2);
            updateOperationVersion.setAuditTime(new Date());
        } else if (dto.getType().equals(2)) {
            updateOperationVersion.setAuditStatus(3);
            updateOperationVersion.setAuditTime(new Date());
        } else {
            return R.fail("参数错误");
        }
        updateOperationVersion.setId(Convert.toInt(dto.getId()));
        int i = openGameVersionHistoryMapper.updateById(updateOperationVersion);
        if (i > 0) {
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 批量删除
     */
    @Override
    public R removeVersionByIds(KeyValueDto<String> keyValueDto) {
        // 查询设置用户
        String[] split = keyValueDto.getKey().split(",");
        Integer[] ids = Convert.toIntArray(split);
        Integer gameId = Convert.toInt(keyValueDto.getValue());
        // 判断一下当前删除的版本是否已经上架到游戏中
        LambdaQueryWrapper<OpenGame> eq = Wrappers.<OpenGame>lambdaQuery().eq(OpenGame::getId, gameId)
            .eq(LoginHelper.isFront(), OpenGame::getUserId, LoginHelper.getUserId());
        OpenGame openGame = baseMapper.selectOne(eq);
        if (openGame != null) {
            // 判断当前删除的版本是否在上架游戏中
            Optional<Integer> first = Arrays.stream(ids).filter(a -> a.equals(openGame.getVersionId())).findFirst();
            if (first.isPresent()) {
                return R.fail("删除版本已经上架了");
            }
            // 不存在我们就进行删除操作
            Collection<Integer> delIds = Arrays.asList(ids);
            int i = openGameVersionHistoryMapper.deleteBatchIds(delIds);
            if (i > 0) {
                return R.ok();
            }
        }
        return R.fail();
    }

    /**
     * 根据条件查询openGame
     */
    public TableDataInfoCoreDto<OpenGameDubboVo> selectGameByParam(Map<String, Object> map) {
        int pageNum = map.get("pageNum") == null ? 0 : Integer.parseInt(map.get("pageNum").toString());
        int pageSize = map.get("pageSize") == null ? 10 : Integer.parseInt(map.get("pageSize").toString());
        List<OpenGame> openGames = baseMapper.selectGameByParam(map);
        int total = baseMapper.selectGameByParamCount(map);
        // 创建分页参数
        List<OpenGameDubboVo> openGameDubboVos = BeanCopyUtils.copyList(openGames, OpenGameDubboVo.class);
        return TableDataInfoCoreDto.build(openGameDubboVos, total);
    }

    @Override
    public OpenGameVo getGameInfo(IdDto idDto) {
        LambdaQueryWrapper<OpenGame> eq = Wrappers.<OpenGame>lambdaQuery()
            .eq(OpenGame::getId, idDto.getId());
        OpenGameVo openGameVo = baseMapper.selectVoOne(eq);
        if (openGameVo == null) {
            return null;
        }
        // log.info("app端调用获取游戏详情接口出参:{}", JsonUtils.toJsonString(openGameVo));
        // 循环添加游戏类目名称
        List<SysDictData> platformGameType = remoteDictService.selectDictDataByType("platform_game_type");
        // 循环追加名称
        Optional<SysDictData> platformGameTypeAny = platformGameType.stream().filter(b -> b.getDictValue().equals(openGameVo.getGameCategory())).findAny();
        platformGameTypeAny.ifPresent(sysDictData -> openGameVo.setGameCategoryName(sysDictData.getDictLabel()));
        LambdaQueryWrapper<OpenUserCertification> lq = Wrappers.<OpenUserCertification>lambdaQuery();
        lq.eq(OpenUserCertification::getUserId, openGameVo.getUserId()).last(" limit 1");
        OpenUserCertification openUserCertification = openUserCertificationMapper.selectOne(lq);
        if (openUserCertification != null) {
            openGameVo.setCompanyName(openUserCertification.getCompanyName());
        }
        return openGameVo;
    }

    /**
     * 游戏拥有权转移
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R transfer(OpenGameTransferDto dto) {
        // 首先查询当前转移给的游戏是否属于当前用户
        Long userId = LoginHelper.getUserId();
        OpenGameVo openGameVo = baseMapper.selectVoById(dto.getId());
        if (openGameVo == null) {
            return R.fail("游戏不存在");
        }
        R<UserVo> me = iUserService.getInfo(userId);
        R<UserVo> transfer = iUserService.getInfo(dto.getTransferUserId());
        if (me.getCode() == R.FAIL) {
            return R.fail("当前用户不存在");
        }
        UserVo meData = me.getData();
        // 查询转移给的用户是否存在
        if (transfer.getCode() == R.FAIL) {
            return R.fail("转移给的用户不存在");
        }
        UserVo transferData = transfer.getData();
        if (!transferData.getAuditStatus().equals(2)) {
            return R.fail("转移的用户并未审核通过");
        }
        // 开始进行游戏所属权的转移
        OpenGame openGame = new OpenGame();
        openGame.setId(openGameVo.getId());
        openGame.setUserId(dto.getTransferUserId());
        baseMapper.updateById(openGame);
        // 添加转移记录
        OpenGameTransferLog openGameTransferLog = new OpenGameTransferLog();
        openGameTransferLog.setGameId(openGameVo.getId());
        openGameTransferLog.setOriginalOpid(userId);
        openGameTransferLog.setOriginalCompanyName(meData.getCompanyName());
        openGameTransferLog.setOriginalUsername(meData.getUserName());
        openGameTransferLog.setTransferOpid(dto.getTransferUserId());
        openGameTransferLog.setTransferCompanyName(transferData.getCompanyName());
        openGameTransferLog.setTransferUsername(transferData.getUserName());
        openGameTransferLogMapper.insert(openGameTransferLog);
        return R.ok();
    }

    /**
     * 查询【游戏管理】列表
     *
     * @param 【游戏管理】
     * @return 【游戏管理】
     */
    @Override
    public TableDataInfo<OpenGameTransferLogVo> transferList(OpenGameTransferLogDto dto) {
        // 进行数据查询
        LambdaQueryWrapper<OpenGameTransferLog> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getGameId() != null, OpenGameTransferLog::getGameId, dto.getGameId());
        lqw.eq(dto.getOriginalOpid() != null, OpenGameTransferLog::getOriginalOpid, dto.getOriginalOpid());
        lqw.likeRight(StringUtils.isNotBlank(dto.getOriginalUsername()), OpenGameTransferLog::getOriginalUsername, dto.getOriginalUsername());
        lqw.likeRight(StringUtils.isNotBlank(dto.getOriginalCompanyName()), OpenGameTransferLog::getOriginalCompanyName, dto.getOriginalCompanyName());
        lqw.eq(dto.getTransferOpid() != null, OpenGameTransferLog::getTransferOpid, dto.getTransferOpid());
        lqw.likeRight(StringUtils.isNotBlank(dto.getTransferCompanyName()), OpenGameTransferLog::getTransferCompanyName, dto.getTransferCompanyName());
        lqw.likeRight(StringUtils.isNotBlank(dto.getTransferUsername()), OpenGameTransferLog::getTransferUsername, dto.getTransferUsername());

        IPage<OpenGameTransferLogVo> result = openGameTransferLogMapper.selectVoPage(dto.build(), lqw);
        // 为空的时候直接返回
        if (result.getRecords() == null || result.getRecords().size() <= 0) {
            return TableDataInfo.build(result);
        }
        return TableDataInfo.build(result);
    }

    /**
     * 根据用户id查询出审核过且在线的游戏列表
     *
     * @return
     */
    @Override
    public List<OpenGameVo> queryGameListByUserId(IdDto<Long> idDto) {
        LambdaQueryWrapper<OpenGame> lqw = Wrappers.lambdaQuery();
        lqw.eq(idDto.getId() != null, OpenGame::getUserId, idDto.getId());
        // 审核成功
        lqw.eq(OpenGame::getGameStatus, "2");
        // 上线
        lqw.eq(OpenGame::getOnlineStatus, "1");

        return baseMapper.selectVoList(lqw);
    }

    /**
     * 根据渠道id查询游戏列表
     */
    @Override
    public List<OpenGame> getGameList(Integer channelId) {
        QueryWrapper<OpenGame> lqw = Wrappers.query();
        lqw.eq("c.id", channelId);
        return baseMapper.getGameList(lqw);
    }

    /**
     * 获取支付方式
     *
     * @param keyValueDto
     * @return
     */
    @Override
    public List getPayTypeData(KeyValueDto<String> keyValueDto) {
        String dicKey = StringUtils.format("global_game_pay_type_{}{}{}", keyValueDto.getKey(), StringUtils.isNotEmpty(keyValueDto.getValue()) ? "_" : "", Convert.toStr(keyValueDto.getValue(), ""));
        List list = new ArrayList();
        AtomicBoolean isOpen = new AtomicBoolean(false);
        AtomicInteger paymentCountLimit = new AtomicInteger();
        Long gameId = 0l;
        boolean hasGameConfig = false;
        boolean hasIosGameConfig = false;
        JSONObject jsonObject = new JSONObject();
        JSONObject iosJsonObject = new JSONObject();
        RequestHeaderDto headerDto = HttpRequestHeaderUtils.getFromHttpRequest();
        if (headerDto != null && headerDto.getAppId() != null) {
            // 通过app_id查询游戏基础信息
            OpenGameDubboVo gameDubboVo = remoteGameManagerService.selectOpenGameInfo(headerDto.getAppId());
            if (gameDubboVo != null) {
                gameId = Convert.toLong(gameDubboVo.getId());
                // 查询是否有单游戏配置
                // 游戏配置类型 4:游戏支付方式
                GameConfigVo vo = remoteGameConfigService.selectGameConfigByParam(gameDubboVo.getId(), "4", Convert.toStr(gameDubboVo.getOperationPlatform()));
                if (vo != null) {
                    hasGameConfig = true;
                    jsonObject = JSONUtil.parseObj(vo.getData());
                }
                // 游戏配置类型 5:苹果应用类支付条件
                GameConfigVo iosVo = remoteGameConfigService.selectGameConfigByParam(gameDubboVo.getId(), "5", Convert.toStr(gameDubboVo.getOperationPlatform()));
                if (iosVo != null) {
                    hasIosGameConfig = true;
                    iosJsonObject = JSONUtil.parseObj(iosVo.getData());
                }
            }
        }
        if(StringUtils.equals("app_ios", keyValueDto.getKey())) {
            // 如果有单个游戏配置，返回游戏配置信息
            if(hasIosGameConfig) {
                // 使用单个游戏配置里的值
                JSONObject finalJsonObject = iosJsonObject;
                if (Convert.toBool(finalJsonObject.get("苹果应用类支付开关"))) {
                    isOpen.set(true);
                }

                if (Convert.toInt(finalJsonObject.get("支付笔数限制")) > 0) {
                    paymentCountLimit.set(Convert.toInt(finalJsonObject.get("支付笔数限制")));
                }
            } else {
                // 查询字典，获取苹果应用类支付条件
                List<SysDictData> data = remoteDictService.selectDictDataByType("app_ios_payment_term");
                if (data != null && !data.isEmpty()) {
                    data.forEach(a -> {
                        // 判断开关是否开启
                        if (a.getDictLabel().equals("苹果应用类支付开关") && StringUtils.equals("0", a.getStatus())) {
                            isOpen.set(true);
                        }

                        if (a.getDictLabel().equals("支付笔数限制") && Convert.toInt(a.getDictValue()) > 0) {
                            paymentCountLimit.set(Convert.toInt(a.getDictValue()));
                        }
                    });
                }
            }
        }

        // 查询用户已支付的订单数
        int count = remoteOrderService.getOrderCountByUser(LoginHelper.getUserId(), gameId);
        // 判断已支付订单是否在该限制范围内
        if(isOpen.get() && StringUtils.equals("2", headerDto.getDevice()) && count < paymentCountLimit.get()) {
            // 查询字典【支付渠道 pay_channel】, 获取【苹果内购】对应的字典键值
            List<SysDictData> data = remoteDictService.selectDictDataByType("pay_channel");
            AtomicReference<String> value = new AtomicReference<>();
            if(data != null && data.size() > 0) {
                data.stream().forEach(d -> {
                    if(StringUtils.equals("苹果内购", d.getDictLabel())) {
                        value.set(d.getDictValue());
                    }
                });
            }
            Map<String, String> map = new HashMap<>();
            map.put("id", value.get());
            list.add(map);
        } else {
            List<SysDictData> gamePayType = remoteDictService.selectDictDataByType(dicKey);
            // 如果有单个游戏配置，返回游戏配置信息
            if(hasGameConfig) {
                JSONObject finalJsonObject = jsonObject;
                List<SysDictData> first = gamePayType.stream().filter(c -> Convert.toBool(finalJsonObject.get(c.getDictLabel()))).collect(Collectors.toList());
                gamePayType =  first;
            }
            if (gamePayType != null && !gamePayType.isEmpty()) {
                gamePayType.forEach(a -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("icon", a.getCssClass());
                    map.put("id", a.getDictValue());
                    map.put("name", a.getDictLabel());
                    list.add(map);
                });
            }
        }
        return list;
    }

    /**
     * 通过游戏名称和平台查询游戏信息
     */
    @Override
    public OpenGameVo getGameInfo(String gameName, String platform) {
        LambdaQueryWrapper<OpenGame> lqw = Wrappers.lambdaQuery();
        lqw.eq(OpenGame::getGameName, gameName);
        lqw.eq(platform != null, OpenGame::getOperationPlatform, platform);
        lqw.eq(OpenGame::getDelFlag, "0");
        return baseMapper.selectVoOne(lqw);
    }

}
