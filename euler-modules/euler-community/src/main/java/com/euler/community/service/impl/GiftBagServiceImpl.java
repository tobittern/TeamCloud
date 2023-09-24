package com.euler.community.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.config.CommonCommunityConfig;
import com.euler.community.config.GiftRabbitMqProducer;
import com.euler.community.constant.GitBagConstant;
import com.euler.community.domain.bo.GiftBagBo;
import com.euler.community.domain.bo.GiftBagCdkBo;
import com.euler.community.domain.dto.CodeDto;
import com.euler.community.domain.dto.GiftBagDto;
import com.euler.community.domain.entity.GiftBag;
import com.euler.community.domain.entity.GiftBagCdk;
import com.euler.community.domain.vo.GiftBagVo;
import com.euler.community.mapper.GiftBagCdkMapper;
import com.euler.community.mapper.GiftBagMapper;
import com.euler.community.service.IGiftBagService;
import com.euler.platform.api.domain.OpenGameDubboVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.*;

/**
 * 礼包配置Service业务层处理
 *
 * @author euler
 * 2022-06-02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GiftBagServiceImpl extends ServiceImpl<GiftBagMapper, GiftBag> implements IGiftBagService {

    private final GiftBagMapper baseMapper;

    private final GiftBagCdkMapper giftBagCdkMapper;

    private final GiftRabbitMqProducer giftRabbitMqProducer;

    @DubboReference
    private com.euler.platform.api.RemoteGameManagerService RemoteGameManagerService;

    @Resource
    private CommonCommunityConfig commonCommunityConfig;

    /**
     * 查询礼包配置
     *
     * @param id 礼包配置主键
     * @return 礼包配置
     */
    @Override
    public GiftBagVo queryById(Long id) {
        GiftBagVo giftBagVo = baseMapper.selectVoById(id);
        if (giftBagVo == null) {
            return null;
        }
        // 礼包领取数目=礼包总数，不可领取
        if (giftBagVo.getDrawNum() == giftBagVo.getTotalNum()) {
            giftBagVo.setIsPicked("2");
        }
        if (LoginHelper.isLogin()) {
            LambdaQueryWrapper<GiftBagCdk> lqw = Wrappers.lambdaQuery();
            lqw.eq(GiftBagCdk::getMemberId, LoginHelper.getUserId());
            lqw.eq(id != null, GiftBagCdk::getGiftBagId, id);
            // 判断该用户是否领取过该礼包
            List<GiftBagCdk> giftBagCdkList = giftBagCdkMapper.selectList(lqw);
            if (giftBagCdkList != null && !giftBagCdkList.isEmpty()) {
                giftBagVo.setActivationCode(giftBagCdkList.get(0).getCode());
                giftBagVo.setIsPicked("1");
            } else {
                giftBagVo.setIsPicked(giftBagVo.getDrawNum() == giftBagVo.getTotalNum() ? "2" : "0");
            }
        } else {
            giftBagVo.setIsPicked("0");
        }

        if (giftBagVo.getGameId() != null) {
            List<Integer> idList = new ArrayList<>();
            idList.add(giftBagVo.getGameId().intValue());
            List<OpenGameDubboVo> openGameDubboVos = RemoteGameManagerService.selectByIds(idList);
            if (openGameDubboVos != null && !openGameDubboVos.isEmpty()) {
                OpenGameDubboVo openGameDubboVo = openGameDubboVos.get(0);
                giftBagVo.setUniversalLink(openGameDubboVo.getUniversalLink());
                giftBagVo.setUrlScheme(openGameDubboVo.getUrlScheme());
                giftBagVo.setVersionOnlineTime(openGameDubboVo.getVersionOnlineTime());
            }
        }

        setUrlPrefix(giftBagVo);
        return giftBagVo;
    }

    /**
     * 设置附件前缀
     *
     * @param giftBagVo 参数
     */
    public void setUrlPrefix(GiftBagVo giftBagVo) {
        if (giftBagVo != null) {
            String yunDomain = commonCommunityConfig.getYunDomain();
            //礼包图片url
            String picPath = giftBagVo.getPicPath();
            giftBagVo.setPicPath(StringUtils.isBlank(picPath) ? null : (picPath.startsWith(GitBagConstant.HTTP) ? picPath : yunDomain + picPath));
            //cdk礼包附件
            String cdkFilePath = giftBagVo.getCdkFilePath();
            giftBagVo.setCdkFilePath(StringUtils.isBlank(cdkFilePath) ? null : (cdkFilePath.startsWith(GitBagConstant.HTTP) ? cdkFilePath : yunDomain + cdkFilePath));
            // 礼包兑换期限
            String startTime = DateUtil.format(giftBagVo.getStartTime(), "yyyy/MM/dd") + " " + "00:00";
            String endTime = DateUtil.format(giftBagVo.getEndTime(), "yyyy/MM/dd") + " " + "23:59";
            giftBagVo.setShowTime(startTime + " - " + endTime);
        }
    }

    /**
     * 查询礼包配置列表
     *
     * @param giftBagDto 礼包配置
     * @return 礼包配置
     */
    @Override
    public TableDataInfo<GiftBagVo> queryPageList(GiftBagDto giftBagDto) {
        LambdaQueryWrapper<GiftBag> lqw = buildQueryWrapper(giftBagDto);
        Page<GiftBagVo> result = baseMapper.selectVoPage(giftBagDto.build(), lqw);
        if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            result.getRecords().forEach(a -> {
                setUrlPrefix(a);
            });
        }

        return TableDataInfo.build(result);
    }

    /**
     * 查询礼包配置列表
     *
     * @param giftBagDto 礼包配置
     * @return 礼包配置
     */
    @Override
    public List<GiftBagVo> queryList(GiftBagDto giftBagDto) {
        LambdaQueryWrapper<GiftBag> lqw = buildQueryWrapper(giftBagDto);
        List<GiftBagVo> result = baseMapper.selectVoList(lqw);
        if (result != null && !result.isEmpty()) {
            result.forEach(a -> {
                setUrlPrefix(a);
            });
        }
        return result;
    }

    private LambdaQueryWrapper<GiftBag> buildQueryWrapper(GiftBagDto giftBagDto) {
        LambdaQueryWrapper<GiftBag> lqw = Wrappers.lambdaQuery();
        lqw.eq(giftBagDto.getGameId() != null, GiftBag::getGameId, giftBagDto.getGameId());
        lqw.like(StringUtils.isNotBlank(giftBagDto.getGameName()), GiftBag::getGameName, giftBagDto.getGameName());
        lqw.like(StringUtils.isNotBlank(giftBagDto.getGiftName()), GiftBag::getGiftName, giftBagDto.getGiftName());
        lqw.le(giftBagDto.getStartTime() != null, GiftBag::getStartTime, giftBagDto.getStartTime());
        lqw.ge(giftBagDto.getEndTime() != null, GiftBag::getEndTime, giftBagDto.getEndTime());
        lqw.eq(giftBagDto.getStatus() != null, GiftBag::getStatus, giftBagDto.getStatus());
        lqw.like(giftBagDto.getApplicationType() != null, GiftBag::getApplicationType, giftBagDto.getApplicationType());
        //按创建时间倒序排列
        lqw.orderByDesc(GiftBag::getCreateTime);
        return lqw;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Void> insertByBo(GiftBagBo bo) {
        //解析文件，获取激活码
        List<CodeDto> codeList = redCdkCode(bo.getCdkFilePath());
        //文件解析失败直接返回
        if (codeList.isEmpty()) {
            return R.fail("解析文件没有获取到激活码数据");
        }
        bo.setTotalNum(codeList.size());//设置礼包总数
        //插入礼包基础数据
        GiftBag add = BeanUtil.toBean(bo, GiftBag.class);
        String result = validEntityBeforeSave(add);
        if (!result.equals("success")) {
            return R.fail(result);
        }
        boolean insertFlag = baseMapper.insert(add) > 0;
        if (insertFlag) {
            bo.setId(add.getId());
            //插入礼包激活码数据
            batchCdkData(codeList, bo);
        }

        return insertFlag ? R.ok() : R.fail("保存失败");
    }

    /**
     * 激活码数据入库
     *
     * @param codeList 激活吗列表
     * @param bo       参数
     */
    public boolean batchCdkData(List<CodeDto> codeList, GiftBagBo bo) {
        List<GiftBagCdk> giftBagCdkList = new ArrayList<>();
        for (var codeDto : codeList) {
            GiftBagCdk giftBagCdk = new GiftBagCdk();
            giftBagCdk.setGiftBagId(bo.getId());
            giftBagCdk.setGameId(bo.getGameId());
            giftBagCdk.setCode(codeDto.getCode());
            giftBagCdk.setStatus(0);
            giftBagCdkList.add(giftBagCdk);
        }
        return giftBagCdkMapper.insertBatch(giftBagCdkList);
    }

    /**
     * 读取文件，获取礼包激活码
     *
     * @param filePath 附件路径
     */
    public List<CodeDto> redCdkCode(String filePath) {
        List<CodeDto> codeList = new ArrayList<>();
        if (StringUtils.isNotBlank(filePath)) {
            try {
                filePath = commonCommunityConfig.getYunDomain() + filePath;
                //对文件进行解析
                byte[] fileByte = HttpUtil.downloadBytes(filePath);
                InputStream input = new ByteArrayInputStream(fileByte);
                codeList = ExcelUtil.importExcel(input, CodeDto.class);
                if (input != null)
                    input.close();
            } catch (Exception e) {
                log.error("读取文件异常", e);
            }
        }
        return codeList;
    }

    /**
     * 修改礼包配置
     *
     * @param bo 礼包配置
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Void> updateByBo(GiftBagBo bo) {
        if (bo.getId() == null) {
            return R.fail("主键id为空");
        }
        GiftBag giftBag = baseMapper.selectById(bo.getId());
        if (giftBag == null) {
            return R.fail("当前数据不存在");
        }
        if (giftBag.getStatus() == 1 || giftBag.getStatus() == 2) {
            return R.fail("已上架和已下架礼包不能修改");
        }
        GiftBag update = BeanUtil.toBean(bo, GiftBag.class);
        String result = validEntityBeforeSave(update);
        if (!result.equals("success")) {
            return R.fail(result);
        }
        //判断下附件有没有重新上传
        if (!giftBag.getCdkFilePath().equals(bo.getCdkFilePath())) {
            //重新上传了文件
            List<CodeDto> codeList = redCdkCode(bo.getCdkFilePath());
            if (codeList.isEmpty()) {
                return R.fail("解析文件没有获取到激活码数据");
            }
            //先删除原有的礼包激活码数据,再重新插入
            LambdaQueryWrapper<GiftBagCdk> lqw = Wrappers.lambdaQuery();
            lqw.eq(GiftBagCdk::getGiftBagId, bo.getId());
            List<GiftBagCdk> giftBagCdkList = giftBagCdkMapper.selectList(lqw);
            List<Long> cdkIdsList = new ArrayList<>();
            for (GiftBagCdk giftBagCdk : giftBagCdkList) {
                cdkIdsList.add(giftBagCdk.getId());
            }
            //物理删除
            giftBagCdkMapper.physicallyDeleteBatchIds(cdkIdsList);
            //重新插入
            boolean batchFlag = batchCdkData(codeList, bo);
            if (!batchFlag) {
                return R.fail("更新激活码数据异常");
            }
            update.setTotalNum(codeList.size());//设置总的数目
            update.setDrawNum(0);//设置默认的已领取数目为0
        }
        boolean flag = baseMapper.updateById(update) > 0;
        if (!flag) {
            return R.fail("更新礼包异常");
        }
        //对于已上架的礼包，将数据提前放入redis缓存
        //缓存key=礼包id,游戏id
        String cashKey = StringUtils.format("{}{}{}", Constants.BASE_KEY, GitBagConstant.GIT_BAG_PREFIX, update.getId());
        if (bo.getStatus() == 1) {
            //缓存value=总数,已领数目
            String value = update.getTotalNum() + "," + update.getDrawNum();
            //先删除缓存
            if (RedisUtils.hasKey(cashKey)) {
                RedisUtils.deleteObject(cashKey);
            }
            long time = DateUtil.betweenMs(update.getEndTime(), new Date());
            RedisUtils.setCacheObject(cashKey, value, Duration.ofMillis(time));
        } else if (bo.getStatus() == 2) {//下架的时候，将数据从缓存里清除
            //删除缓存
            if (RedisUtils.hasKey(cashKey)) {
                RedisUtils.deleteObject(cashKey);
            }
        }
        return R.ok();
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private String validEntityBeforeSave(GiftBag entity) {
        // 判断传输过来的应用系统是否包含多个
        String[] typeList = new String[]{"1", "2", "3"};
        String[] strArr = entity.getApplicationType().split(",");
        String[] types = Convert.toStrArray(strArr);
        // 对应的下载地址校验
        for (int i = 0; i < types.length; i++) {
            // 应用平台校验 1：android 2：ios 3：h5
            String type = types[i];
            Optional<String> typeAny = Arrays.stream(typeList).filter(a -> a.equals(type)).findAny();
            if (!typeAny.isPresent()) {
                return "应用平台参数错误";
            }
            // 下载地址校验
            if (StringUtils.equals(Constants.SYSTEM_TYPE_1, type)) {
                if (ObjectUtil.isEmpty(entity.getAndroidDownloadUrl())) {
                    return "android的下载地址未输入，请确认";
                }
            } else if (StringUtils.equals(Constants.SYSTEM_TYPE_2, type)) {
                if (ObjectUtil.isEmpty(entity.getIosDownloadUrl())) {
                    return "ios的下载地址未输入，请确认";
                }
            } else if (StringUtils.equals(Constants.SYSTEM_TYPE_3, type)) {
                if (ObjectUtil.isEmpty(entity.getH5DownloadUrl())) {
                    return "h5的下载地址未输入，请确认";
                }
            }
        }
        return "success";
    }

    /**
     * 批量删除礼包配置
     *
     * @param ids 需要删除的礼包配置主键
     * @return 结果
     */
    @Override
    public R<Void> deleteWithValidByIds(Collection<Long> ids) {
        for (Long id : ids) {
            GiftBag giftBag = baseMapper.selectById(id);
            if (giftBag == null) {
                return R.fail("数据不存在");
            }
            if (giftBag.getStatus() == 1) {
                return R.fail("已上架的不能删除");
            }
        }
        if (baseMapper.deleteBatchIds(ids) > 0) {
            return R.ok("删除成功");
        }
        return R.fail("删除失败");
    }

    /**
     * 已领取的礼包
     *
     * @param giftBagCdkBo cdk
     * @return
     */
    @Override
    public R<Object> receivedBags(GiftBagCdkBo giftBagCdkBo) {
        LambdaQueryWrapper<GiftBagCdk> lqw = Wrappers.lambdaQuery();
        lqw.eq(GiftBagCdk::getMemberId, giftBagCdkBo.getMemberId());
        //加入礼包id 判断
        lqw.eq(giftBagCdkBo.getGiftBagId() != null, GiftBagCdk::getGiftBagId, giftBagCdkBo.getGiftBagId());
        // 按照领取时间倒序排列
        lqw.orderByDesc(GiftBagCdk::getReceiveTime);
        List<GiftBagCdk> giftBagCdkList = giftBagCdkMapper.selectList(lqw);
        if (giftBagCdkList == null || giftBagCdkList.size() <= 0) {
            return R.ok(Collections.EMPTY_LIST);
        }
        // 领取过的都查出来，不用关注礼包状态
        List<Map<String, Object>> rList = new ArrayList<>();
        for (GiftBagCdk v : giftBagCdkList) {
            LambdaQueryWrapper<GiftBag> giftBagLqw = Wrappers.lambdaQuery();
            giftBagLqw.eq(GiftBag::getId, v.getGiftBagId());//领取的礼包
            List<GiftBagVo> giftBagVos = baseMapper.selectVoList(giftBagLqw);
            if (giftBagVos.isEmpty()) {
                continue;
            }
            GiftBagVo giftBagVo = giftBagVos.get(0);
            // 已领取
            giftBagVo.setIsPicked("1");
            giftBagVo.setActivationCode(v.getCode());
            Date endTime = giftBagVo.getEndTime();//结束时间
            long time = endTime.getTime() - new Date().getTime();
            // 礼包是否失效 0：已失效 1：未失效
            int flag = time <= 0 ? 0 : 1;
            //设置附件url
            setUrlPrefix(giftBagVo);
            Map<String, Object> map = new HashMap<>();
            map.put("giftBag", giftBagVo);
            map.put("code", v.getCode());
            map.put("isInvalid", flag);
            rList.add(map);
        }
        // 根据礼包是否失效来降序排列
        // rList.stream().sorted(Comparator.comparing(a-> Convert.toInt(a.get("isInvalid")))).collect(Collectors.toList());
        // Collections.reverse(rList);
        listMapSort(rList, "desc", "isInvalid");

        return R.ok(rList);
    }

    /**
     * 待领取的礼包
     *
     * @param bo cdk
     * @return
     */
    @Override
    public R<Object> waitBags(GiftBagCdkBo bo) {
        List<GiftBag> giftBagIdList = new ArrayList<>();
        boolean isLogin = LoginHelper.isLogin();
        if (isLogin) {
            LambdaQueryWrapper<GiftBagCdk> lqw = Wrappers.lambdaQuery();
            lqw.eq(bo.getGameId() != null, GiftBagCdk::getGameId, bo.getGameId());
            lqw.eq(GiftBagCdk::getMemberId, LoginHelper.getUserId());
            List<GiftBagCdk> giftBagCdkList = giftBagCdkMapper.selectList(lqw);
            for (GiftBagCdk v : giftBagCdkList) {
                GiftBag bag = new GiftBag();
                bag.setActivationCode(v.getCode());
                bag.setId(v.getGiftBagId());
                giftBagIdList.add(bag);
            }
        }

        LambdaQueryWrapper<GiftBag> giftBagLqw = Wrappers.lambdaQuery();
        Date currentDate = new Date();
        giftBagLqw.eq(GiftBag::getStatus, 1);//已上架的
        giftBagLqw.le(GiftBag::getStartTime, currentDate);//开始时间小于等于当前时间
        giftBagLqw.ge(GiftBag::getEndTime, currentDate);//结束时间大于等于当前时间
        //giftBagLqw.notIn(!giftBagIdList.isEmpty(), GiftBag::getId, giftBagIdList);//排除掉领取过的礼包
        giftBagLqw.eq(GiftBag::getDelFlag, 0);//未删除的
        giftBagLqw.eq(bo.getGameId() != null, GiftBag::getGameId, bo.getGameId());//查询当前游戏
        // 应用平台 1：android 2：ios 3：h5
        giftBagLqw.like(bo.getApplicationType() != null, GiftBag::getApplicationType, bo.getApplicationType());
        List<GiftBagVo> giftBagsList = baseMapper.selectVoList(giftBagLqw);

        if (giftBagsList != null && !giftBagsList.isEmpty()) {
            giftBagsList.forEach(a -> {
                setUrlPrefix(a);
                // 礼包领取数目=礼包总数，2不可领取
                if (a.getDrawNum() == a.getTotalNum()) {
                    a.setIsPicked(isLogin ? "2" : "0");
                }
                for (int i = 0; i < giftBagIdList.size(); i++) {
                    // 礼包是否已领取：1(已领取) 0(待领取)
                    if (giftBagIdList.get(i).getId().equals(a.getId())) {
                        a.setIsPicked("1");
                        // 已领取的礼包，需要设置激活码
                        a.setActivationCode(giftBagIdList.get(i).getActivationCode());
                    }
                }

            });
        }

        return R.ok(giftBagsList);
    }

    /**
     * 领取礼包
     *
     * @param bo cdk
     * @return
     */
    @Override
    public R<Void> pickBag(GiftBagCdkBo bo) {
        if (bo.getGiftBagId() == null) {
            return R.fail("礼包id不能为空");
        }

        //将改用户领取行为放入redis里，5分钟之内不允许再领取
        String lockKey = StringUtils.format("{}{}{}:{}", Constants.BASE_KEY, GitBagConstant.GIT_BAG_LOCK_USER_PREFIX, bo.getGiftBagId(), bo.getMemberId());
        if (RedisUtils.hasKey(lockKey)) {
            return R.fail("你已经领取过该礼包");
        }

        //缓存中不存在，将数据放置在缓存里
        GiftBag giftBag = baseMapper.selectById(bo.getGiftBagId());
        if(ObjectUtil.isNull(giftBag)){
            return R.fail("该礼包不存在");
        }
        Long time = DateUtil.betweenMs(new Date(), giftBag.getEndTime());
        if (time <= 0) {
            return R.fail("礼包已失效");
        }

        //从缓存里读取数据
        String cashKey = StringUtils.format("{}{}{}", Constants.BASE_KEY, GitBagConstant.GIT_BAG_PREFIX, bo.getGiftBagId());
        if (!RedisUtils.hasKey(cashKey)) {
            String value = giftBag.getTotalNum() + "," + giftBag.getDrawNum();
            //缓存value=总数,已领数目
            RedisUtils.setCacheObject(cashKey, value, Duration.ofMillis(time));
        }
        String value = RedisUtils.getCacheObject(cashKey);
        if (StringUtils.isBlank(value)) {
            value = giftBag.getTotalNum() + "," + giftBag.getDrawNum();
        }
        int cashTotalNum = Integer.parseInt(value.split(",")[0]);//总数

        String incrementKey = StringUtils.format("{}{}{}", Constants.BASE_KEY, GitBagConstant.GIT_BAG_INCR, bo.getGiftBagId());
        Long increment = RedisUtils.getAtomicValue(incrementKey);


        log.info("领取礼包，使用redis自增判断是否存入队列，自增值:{}，礼包id:{}", increment, bo.getGiftBagId());
        if ((increment == null ? 0 : increment) < cashTotalNum) {//可以往消息队列里存放数据
            String msgId = GitBagConstant.GIT_BAG_RABBIT_PREFIX + bo.getGiftBagId();
            Map<String, Object> map = new HashMap<>();
            map.put("giftBagId", bo.getGiftBagId());
            map.put("memberId", bo.getMemberId());
            giftRabbitMqProducer.giftPick(map, msgId);

            RedisUtils.incrAtomicValue(incrementKey);
            RedisUtils.expire(incrementKey, Duration.ofMillis(time));
            RedisUtils.setCacheObject(lockKey, lockKey, Duration.ofMinutes(GitBagConstant.GIT_BAG_LOCK_USER_TIME));
        } else {
            return R.fail("礼包已被领完");
        }
        return R.ok();
    }

    @Override
    public R<Void> exchangeBag(GiftBagCdkBo bo) {
        if (bo.getGiftBagId() == null) {
            return R.fail("礼包id不能为空");
        }
        if (bo.getMemberId() == null) {
            return R.fail("用户id不能为空");
        }
        if (StringUtils.isBlank(bo.getCode())) {
            return R.fail("兑换码不能为空");
        }
        LambdaQueryWrapper<GiftBagCdk> lqw = Wrappers.lambdaQuery();
        lqw.eq(GiftBagCdk::getMemberId, bo.getMemberId());
        lqw.eq(GiftBagCdk::getGiftBagId, bo.getGiftBagId());
        lqw.eq(GiftBagCdk::getCode, bo.getCode());
        List<GiftBagCdk> giftBagCdkList = giftBagCdkMapper.selectList(lqw);
        if (giftBagCdkList == null || giftBagCdkList.isEmpty()) {
            return R.fail("兑换码错误");
        }
        GiftBag giftBag = baseMapper.selectById(bo.getGiftBagId());
        if (giftBag.getStatus() == 2) {
            return R.fail("礼包已下架");
        }


        if (!DateUtil.isIn(new Date(), giftBag.getStartTime(), giftBag.getEndTime())) {
            return R.fail("礼包已过期");
        }
        //TODO---兑换码兑换的逻辑，暂未知

        GiftBagCdk giftBagCdk = giftBagCdkList.get(0);
        if (giftBagCdk.getStatus() == 1) {
            return R.fail("礼包已兑换");
        }
        giftBagCdk.setStatus(1);
        giftBagCdk.setExchangeTime(new Date());
        giftBagCdkMapper.updateById(giftBagCdk);

        Integer exchangeNum = giftBag.getExchangeNum();
        giftBag.setExchangeNum(exchangeNum + 1);
        baseMapper.updateById(giftBag);
        return R.ok();
    }

    @Override
    public R<Void> updateStatus(GiftBagBo bo) {
        if (bo.getId() == null) {
            return R.fail("礼包id为空");
        }
        if (bo.getStatus() == null) {
            return R.fail("礼包状态为空");
        }
        if (bo.getStatus() == 0) {
            return R.fail("礼包状态不能修改为待上架");
        }
        GiftBag giftBag = baseMapper.selectById(bo.getId());
        if (giftBag == null) {
            return R.fail("礼包不存在");
        }
        if (giftBag.getStatus().equals(bo.getStatus())) {
            return R.fail("不能修改为相同状态");
        }
        GiftBag add = BeanUtil.toBean(bo, GiftBag.class);
        baseMapper.updateById(add);
        return R.ok();
    }

    /**
     * list<map>排序
     *
     * @param list
     * @param orderd 排序规则desc倒序，空或asc正序
     * @param key    排序字段
     * @return list
     */
    private List<Map<String, Object>> listMapSort(List<Map<String, Object>> list, String orderd, String key) {
        List<Map> listmap = new ArrayList<>();
        Collections.sort(list, new Comparator<Map>() {
            public int compare(Map o1, Map o2) {
                if (orderd.equals("desc")) {
                    return (o2.get(key).toString()).compareTo(o1.get(key).toString());
                } else if (orderd.equals("asc") || "".equals(orderd)) {
                    return (o1.get(key).toString()).compareTo(o2.get(key).toString());
                } else {
                    return 0;
                }
            }
        });
        return list;
    }

}
