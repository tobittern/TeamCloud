package com.euler.risk.service.impl;

import cn.hutool.core.date.DateUtil;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.LoginUser;
import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.common.core.domain.dto.SdkChannelPackageDto;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.HttpRequestHeaderUtils;
import com.euler.common.core.utils.ServletUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.risk.api.domain.TdDeviceInfoVo;
import com.euler.risk.domain.dto.HeartBeatDto;
import com.euler.risk.domain.vo.BanlistVo;
import com.euler.risk.domain.vo.BlacklistVo;
import com.euler.risk.service.IBanlistService;
import com.euler.risk.service.IBlacklistService;
import com.euler.risk.service.IHeartBeatService;
import com.euler.risk.service.ITdDeviceInfoService;
import com.euler.sdk.api.RemoteMemberService;
import com.euler.sdk.api.RemotePopupService;
import com.euler.sdk.api.domain.LoginMemberVo;
import com.euler.sdk.api.domain.MemberBanVo;
import com.euler.sdk.api.domain.SdkPopupVo;
import com.euler.sdk.api.domain.dto.SdkPopupDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Service业务层处理
 *
 * @author euler
 * @date 2022-08-24
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HeartBeatServiceImpl implements IHeartBeatService {

    @Autowired
    private IBanlistService iBanlistService;
    @Autowired
    private IBlacklistService iBlacklistService;
    @Autowired
    private ITdDeviceInfoService iTdDeviceInfoService;
    @DubboReference
    private RemoteMemberService remoteMemberService;
    @DubboReference
    private RemotePopupService remotePopupService;

    /**
     * 检测用户状态
     *
     * @return
     */
    @Override
    public R heartJumpCollection(HeartBeatDto dto) {
        // 首先判断用户是否登录
        if (LoginHelper.isLogin()) {
            //region 封号的开始
            // 用户登陆了 我们需要判断用户是否被封禁
            LoginUser loginUser = LoginHelper.getLoginUser();
            Long userId = loginUser.getUserId();
            String ip = ServletUtils.getClientIP();
            String memberName = loginUser.getUsername();
            String mobile = loginUser.getMobile();
            RequestHeaderDto requestHeaderDto = HttpRequestHeaderUtils.getFromHttpRequest();
            R checkUserStatusCheck = checkUserStatus(userId, memberName, mobile, ip, requestHeaderDto);
            if (checkUserStatusCheck.getCode() == 4003) {
                return checkUserStatusCheck;
            }
            //endregion 封号的结束

            //region 强退，奖励，活动弹框的开始
            try {
                SdkChannelPackageDto sdkChannelPackage = loginUser.getSdkChannelPackage();
                Integer gameId = sdkChannelPackage.getGameId();
                String packageCode = sdkChannelPackage.getPackageCode();
                SdkPopupDto popupDto = new SdkPopupDto();
                popupDto.setGameId(gameId);
                popupDto.setMemberId(userId);
                popupDto.setPackageCode(packageCode);
                popupDto.setIsFirst(dto.getFirstRequest());
                List<SdkPopupVo> popupVos = remotePopupService.selectPopupList(popupDto);
                if (popupVos.size() > 0) {
                    return R.ok(popupVos);
                }
            } catch (Exception e) {
            }
            //endregion 强退，奖励，活动弹框的开始
        }
        return R.ok();
    }

    /**
     * 检测用户是否被封禁
     *
     * @param userId
     * @param memberName
     * @param mobile
     * @param ip
     * @param dto
     * @return
     */
    @Override
    public R checkUserStatus(Long userId, String memberName, String mobile, String ip, RequestHeaderDto dto) {
        try {
            // 设置一个永久封禁的时间
            String longBanTime = "2194-03-05 00:00:00";
            Date longBanTimeDate = DateUtil.parse(longBanTime, "yyyy-MM-dd HH:mm:ss");
            // 首先判断用户是否登录
            // 定义初始变量值
            R<MemberBanVo> r = new R<>();
            r.setCode(4003);
            String msg = "你被封禁了";
            //region 封号的开始
            // token 获取完毕之后我们需要生成redis的值
            String userBanKey = Constants.RISK_KEY + "ban:" + userId;
            BanlistVo cacheBanObject = RedisUtils.getCacheObject(userBanKey);
            if (cacheBanObject == null) {
                // 按照用户ID查询到的封号信息
                cacheBanObject = iBanlistService.queryByMemberId(userId);
                RedisUtils.setCacheObject(userBanKey, cacheBanObject);
            }
            // 判断用户ID是否被封号
            if (cacheBanObject != null && cacheBanObject.getId() != null) {
                // 获取昵称
                memberName = StringUtils.isNotBlank(cacheBanObject.getNickName()) ? cacheBanObject.getNickName() : memberName;
                // 这个时候代表着存储的有数据 我们需要判断
                if (cacheBanObject.getEndTime().equals(longBanTimeDate)) {
                    // 永久封禁
                    msg = memberName + ",永久封号," + cacheBanObject.getReason();
                    r.setMsg(msg);
                    return r;
                }
                // 如果不是永久封号 那么我们判断一下用户是否在指定时间内被封号了
                if (cacheBanObject.getEndTime().getTime() > new Date().getTime()) {
                    msg = memberName + "," + DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", cacheBanObject.getEndTime()) + "," + cacheBanObject.getReason();
                    r.setMsg(msg);
                    return r;
                }
            }
            // 获取用户基础信息
            LoginMemberVo memberByIdNoException = remoteMemberService.getMemberByIdNoException(userId);
            // 判断用户手机号是否在黑名单中
            if (memberByIdNoException != null && StringUtils.isNotBlank(memberByIdNoException.getMobile())) {
                String userBlackMobileKey = Constants.RISK_KEY + "black:mobile:" + memberByIdNoException.getMobile();
                BlacklistVo cacheBlackMobileObject = RedisUtils.getCacheObject(userBlackMobileKey);
                if (cacheBlackMobileObject == null) {
                    // 按照手机号查询到的封号信息
                    cacheBlackMobileObject = iBlacklistService.queryByParams(memberByIdNoException.getMobile());
                    RedisUtils.setCacheObject(userBlackMobileKey, cacheBlackMobileObject);
                }
                if (cacheBlackMobileObject != null && cacheBlackMobileObject.getId() != null) {
                    if (cacheBlackMobileObject.getEndTime().equals(longBanTimeDate)) {
                        // 永久封禁
                        msg = memberName + ",永久封号," + cacheBlackMobileObject.getReason();
                        r.setMsg(msg);
                        return r;
                    }
                    // 如果不是永久封号 那么我们判断一下用户是否在指定时间内被封号了
                    if (cacheBlackMobileObject.getEndTime().getTime() > new Date().getTime()) {
                        msg = memberName + "," + DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", cacheBlackMobileObject.getEndTime()) + "," + cacheBlackMobileObject.getReason();
                        r.setMsg(msg);
                        return r;
                    }
                }
            }
            // 判断用户身份证号是否在黑名单中
            if (memberByIdNoException != null
                && StringUtils.isNotBlank(memberByIdNoException.getIdCardNo())) {
                String idCardNo = memberByIdNoException.getIdCardNo();
                String userBlackIdCardNoKey = Constants.RISK_KEY + "black:id_card_no:" + idCardNo;
                BlacklistVo cacheBlackIdCardNoObject = RedisUtils.getCacheObject(userBlackIdCardNoKey);
                if (cacheBlackIdCardNoObject == null) {
                    // 按照身份证查询到的封号信息
                    cacheBlackIdCardNoObject = iBlacklistService.queryByParams(idCardNo);
                    RedisUtils.setCacheObject(userBlackIdCardNoKey, cacheBlackIdCardNoObject);
                }
                if (cacheBlackIdCardNoObject != null && cacheBlackIdCardNoObject.getId() != null) {
                    if (cacheBlackIdCardNoObject.getEndTime().equals(longBanTimeDate)) {
                        // 永久封禁
                        msg = memberName + ",永久封号," + cacheBlackIdCardNoObject.getReason();
                        r.setMsg(msg);
                        return r;
                    }
                    // 如果不是永久封号 那么我们判断一下用户是否在指定时间内被封号了
                    if (cacheBlackIdCardNoObject.getEndTime().getTime() > new Date().getTime()) {
                        msg = memberName + "," + DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", cacheBlackIdCardNoObject.getEndTime()) + "," + cacheBlackIdCardNoObject.getReason();
                        r.setMsg(msg);
                        return r;
                    }
                }
            }
            // 判断用户ip是否在黑名单中
            String userBlackIpKey = Constants.RISK_KEY + "black:ip:" + ip;
            BlacklistVo cacheBlackIpObject = RedisUtils.getCacheObject(userBlackIpKey);
            if (cacheBlackIpObject == null) {
                // 按照ip查询到的封号信息
                cacheBlackIpObject = iBlacklistService.queryByParams(ip);
                RedisUtils.setCacheObject(userBlackIpKey, cacheBlackIpObject);
            }
            if (cacheBlackIpObject != null && cacheBlackIpObject.getId() != null) {
                if (cacheBlackIpObject.getEndTime().equals(longBanTimeDate)) {
                    // 永久封禁
                    msg = memberName + ",永久封号," + cacheBlackIpObject.getReason();
                    r.setMsg(msg);
                    return r;
                }
                // 如果不是永久封号 那么我们判断一下用户是否在指定时间内被封号了
                if (cacheBlackIpObject.getEndTime().getTime() > new Date().getTime()) {
                    msg = memberName + "," + DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", cacheBlackIpObject.getEndTime()) + "," + cacheBlackIpObject.getReason();
                    r.setMsg(msg);
                    return r;
                }
            }
            // 判断用户设备是否在黑名单中
            TdDeviceInfoVo deviceInfoByHeader = iTdDeviceInfoService.getDeviceInfoByHeader(dto);
            if (deviceInfoByHeader != null && deviceInfoByHeader.getId() != null) {
                String deviceId = deviceInfoByHeader.getId();
                String userBlackDeviceKey = Constants.RISK_KEY + "black:device:" + deviceId;
                BlacklistVo cacheBlackDeviceObject = RedisUtils.getCacheObject(userBlackDeviceKey);
                if (cacheBlackDeviceObject == null) {
                    // 按照设备查询到的封号信息
                    cacheBlackDeviceObject = iBlacklistService.queryByParams(deviceId);
                    RedisUtils.setCacheObject(userBlackDeviceKey, cacheBlackDeviceObject);
                }
                if (cacheBlackDeviceObject != null && cacheBlackDeviceObject.getId() != null) {
                    // 这个时候代表着存储的有数据 我们需要判断
                    if (cacheBlackDeviceObject.getEndTime().equals(longBanTimeDate)) {
                        // 永久封禁
                        msg = memberName + ",永久封号," + cacheBlackDeviceObject.getReason();
                        r.setMsg(msg);
                        return r;
                    }
                    // 如果不是永久封号 那么我们判断一下用户是否在指定时间内被封号了
                    if (cacheBlackDeviceObject.getEndTime().getTime() > new Date().getTime()) {
                        msg = memberName + "," + DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", cacheBlackDeviceObject.getEndTime()) + "," + cacheBlackDeviceObject.getReason();
                        r.setMsg(msg);
                        return r;
                    }
                }
            }
        } catch (Exception e) {
            log.info("风控出现错误:", e);
        }

        return R.ok();
    }

}
