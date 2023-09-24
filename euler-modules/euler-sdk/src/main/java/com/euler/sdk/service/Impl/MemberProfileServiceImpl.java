package com.euler.sdk.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import com.baomidou.lock.executor.RedissonLockExecutor;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.constant.UserConstants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.core.domain.dto.LoginUser;
import com.euler.common.core.utils.BeanCopyUtils;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.platform.api.RemoteCaptchaCodeService;
import com.euler.sdk.api.domain.Member;
import com.euler.sdk.api.domain.MemberDetailVo;
import com.euler.sdk.api.domain.MemberProfile;
import com.euler.sdk.api.domain.MemberProfileBasics;
import com.euler.sdk.api.domain.dto.SearchBanDto;
import com.euler.sdk.config.WebConfig;
import com.euler.sdk.domain.dto.MemberBlacklistDto;
import com.euler.sdk.domain.dto.MemberPageDto;
import com.euler.sdk.domain.dto.MobileDto;
import com.euler.sdk.domain.dto.SearchMemberlDto;
import com.euler.sdk.domain.entity.MemberChangePhoneRecord;
import com.euler.sdk.domain.vo.MemberBlacklistVo;
import com.euler.sdk.domain.vo.MemberProfileVo;
import com.euler.sdk.domain.vo.MemberVo;
import com.euler.sdk.mapper.MemberChangePhoneRecordMapper;
import com.euler.sdk.mapper.MemberMapper;
import com.euler.sdk.mapper.MemberProfileMapper;
import com.euler.sdk.service.IMemberBlacklistService;
import com.euler.sdk.service.IMemberProfileService;
import com.euler.sdk.service.IMemberService;
import com.euler.system.api.RemoteNoticeService;
import com.euler.system.api.RemoteNoticeToUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 会员详细信息Service业务层处理
 *
 * @author euler
 * @date 2022-03-21
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MemberProfileServiceImpl extends ServiceImpl<MemberProfileMapper, MemberProfile> implements IMemberProfileService {

    private final MemberProfileMapper baseMapper;
    private final MemberMapper memberMapper;

    @DubboReference
    private final RemoteNoticeService remoteNoticeService;
    @DubboReference
    private final RemoteNoticeToUsersService remoteNoticeToUsersService;
    @DubboReference
    private RemoteCaptchaCodeService remoteCaptchaCodeService;
    @Autowired
    private LockTemplate lockTemplate;
    @Autowired
    private MemberChangePhoneRecordMapper memberChangePhoneRecordMapper;
    @Autowired
    private WebConfig webConfig;
    @Autowired
    private IMemberBlacklistService iMemberBlacklistService;
    @Autowired
    private IMemberService memberService;


    /**
     * 会员详情列表
     *
     * @param memberPageDto
     * @return
     */
    @Override
    public TableDataInfo<MemberDetailVo> getMemberDetailPageList(MemberPageDto memberPageDto) {
        QueryWrapper<MemberDetailVo> wrapper = getMemberDetailVoQueryWrapper(memberPageDto);
        IPage<MemberDetailVo> page = baseMapper.getMemberDetailPageList(memberPageDto.build(), wrapper);
        // 获取这些用户的用户id
        List<Long> memberIds = page.getRecords().stream().map(MemberDetailVo::getMemberId).collect(Collectors.toList());
        // 查询一下当前用户的封禁情况
        MemberBlacklistDto memberBlacklistDto = new MemberBlacklistDto();
        memberBlacklistDto.setMemberIds(memberIds);
        memberBlacklistDto.setPlatform(1);
        List<MemberBlacklistVo> memberBlacklistVos = iMemberBlacklistService.queryList(memberBlacklistDto);
        page.getRecords().forEach(a -> {
            // 设置初始状态
            a.setBanStatus("账号正常");
            if (memberBlacklistVos.size() > 0) {
                // 先判断是否存在不针对与游戏的封禁
                Optional<MemberBlacklistVo> noGameId = memberBlacklistVos.stream().filter(
                    b -> b.getGameId().equals(0)
                        && b.getMemberId().equals(a.getMemberId())
                        && ((b.getEndTime() != null && b.getEndTime().getTime() > new Date().getTime()) || b.getIsPermanent().equals(1)))
                    .findFirst();
                // 进行判断
                if (noGameId.isPresent()) {
                    a.setBanStatus("全平台封禁");
                } else {
                    // 判断是否是指定游戏封禁了
                    long count = memberBlacklistVos.stream().filter(
                        b -> b.getMemberId().equals(a.getMemberId())
                            && ((b.getEndTime() != null && b.getEndTime().getTime() > new Date().getTime()) || b.getIsPermanent().equals(1)))
                        .count();
                    if (count > 0) {
                        a.setBanStatus("封禁了" + count + "款游戏");
                    }
                }
            }
        });

        // 补充一下用户的头像信息
        if (page.getRecords().size() > 0) {
            page.getRecords().forEach(a -> {
                a.setAvatar(memberService.getAvatar(a.getSex(), a.getAvatar()));
            });
        }
        return TableDataInfo.build(page);
    }

    private QueryWrapper<MemberDetailVo> getMemberDetailVoQueryWrapper(MemberPageDto memberPageDto) {
        QueryWrapper<MemberDetailVo> wrapper = Wrappers.query();
        wrapper.eq("m.del_flag", UserConstants.USER_NORMAL)
            .eq("mp.del_flag", UserConstants.USER_NORMAL)
            .eq(memberPageDto.getMemberId() != null, "m.id", memberPageDto.getMemberId())
            .in(memberPageDto.getMemberIds() != null, "m.id", memberPageDto.getMemberIds())
            .eq(StringUtils.isNotBlank(memberPageDto.getUniqueId()), "m.unique_id", memberPageDto.getUniqueId())
            .eq(StringUtils.isNotBlank(memberPageDto.getNickName()), "mp.nick_name", memberPageDto.getNickName())
            .eq(StringUtils.isNotBlank(memberPageDto.getMobile()), "m.mobile", memberPageDto.getMobile())
            .eq(StringUtils.isNotBlank(memberPageDto.getIdCardNo()), "mp.id_card_no", memberPageDto.getIdCardNo())
            .eq(StringUtils.isNotBlank(memberPageDto.getGameName()), "mp.game_name", memberPageDto.getGameName())
            .eq(StringUtils.isNotBlank(memberPageDto.getPackageCode()), "mp.package_code", memberPageDto.getPackageCode())
            .eq(Convert.toInt(memberPageDto.getMemberRightsId(), 0) > 0, "mr.lever", memberPageDto.getMemberRightsId())
            .eq(StringUtils.isNotBlank(memberPageDto.getRealName()), "mp.real_name", memberPageDto.getRealName())
            .eq(memberPageDto.getVerifyStatus() != null, "mp.verify_status", memberPageDto.getVerifyStatus())
            .eq(StringUtils.isNotBlank(memberPageDto.getSex()), "mp.sex", memberPageDto.getSex())
            .eq((memberPageDto.getChannelId() != null && memberPageDto.getChannelId() != 0), "mp.channel_id", memberPageDto.getChannelId())
            .eq((memberPageDto.getIsOfficial() != null && memberPageDto.getIsOfficial() != 0), "mp.is_official", memberPageDto.getIsOfficial())
            .le(StringUtils.isNotBlank(memberPageDto.getLastLoginTime()), "mp.login_date", DateUtils.getEndOfDay(memberPageDto.getLastLoginTime()))
            //普通会员
            .isNull(memberPageDto.getMemberRightsId() != null && Convert.toInt(memberPageDto.getMemberRightsId(), 0) == 0, "mr.lever");


        return wrapper;
    }

    /**
     * 获取会员详情
     *
     * @param memberId
     * @return
     */
    @Override
    public MemberDetailVo getMemberDetailByMemberId(long memberId) {
        MemberPageDto pageDto = new MemberPageDto();
        pageDto.setMemberId(memberId);
        QueryWrapper<MemberDetailVo> wrapper = getMemberDetailVoQueryWrapper(pageDto);
        MemberDetailVo vo = baseMapper.getMemberDetailByMemberId(wrapper);
        return vo;
    }


    /**
     * 根据memberId获取memberProfile
     *
     * @param memberId
     * @return
     */
    @Override
    public MemberProfile getByMemberId(long memberId) {
        MemberProfile memberProfile = getOne(Wrappers.<MemberProfile>lambdaQuery().eq(MemberProfile::getMemberId, memberId).last("limit 1"));
        return memberProfile;
    }

    /**
     * 根据memberId获取memberProfile
     *
     * @param memberIds
     * @return
     */
    @Override
    public List<MemberProfile> getByMemberIds(List<Long> memberIds) {
        LambdaQueryWrapper<MemberProfile> in = Wrappers.<MemberProfile>lambdaQuery().in(MemberProfile::getMemberId, memberIds);
        List<MemberProfile> memberProfiles = baseMapper.selectList(in);
        return memberProfiles;
    }

    /**
     * 根据memberId获取根据memberId获取memberProfileVo
     *
     * @param memberId
     * @return
     */
    @Override
    public MemberProfileVo getVoByMemberId(long memberId) {
        return baseMapper.selectVoOne(Wrappers.<MemberProfile>lambdaQuery().eq(MemberProfile::getMemberId, memberId).last("limit 1"));
    }


    /**
     * 根据会员id修改会员信息
     *
     * @param memberProfile
     * @return 结果
     */
    @Override
    public Boolean updateMemberDetail(MemberProfile memberProfile) {
        if (memberProfile.getMemberId() == null || memberProfile.getMemberId() <= 0)
            return false;
        var wrapper = getUpdateMemberProfileWrapper(memberProfile);
        return wrapper.update();
    }


    private LambdaUpdateChainWrapper<MemberProfile> getUpdateMemberProfileWrapper(MemberProfile memberProfile) {
        var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper);
        updateChainWrapper.eq(MemberProfile::getMemberId, memberProfile.getMemberId())
            .set(StringUtils.isNotBlank(memberProfile.getNickName()), MemberProfile::getNickName, memberProfile.getNickName())
            .set(StringUtils.isNotBlank(memberProfile.getMobile()), MemberProfile::getMobile, memberProfile.getMobile())
            .set(StringUtils.isNotBlank(memberProfile.getRealName()), MemberProfile::getRealName, memberProfile.getRealName())
            .set(StringUtils.isNotBlank(memberProfile.getVerifyStatus()), MemberProfile::getVerifyStatus, memberProfile.getVerifyStatus())
            .set(StringUtils.isNotBlank(memberProfile.getSex()), MemberProfile::getSex, memberProfile.getSex())
            .set(StringUtils.isNotBlank(memberProfile.getAvatar()), MemberProfile::getAvatar, memberProfile.getAvatar())
            .set(StringUtils.isNotBlank(memberProfile.getIdCardNo()), MemberProfile::getIdCardNo, memberProfile.getIdCardNo())
            .set(memberProfile.getSignDate() != null, MemberProfile::getSignDate, memberProfile.getSignDate())
            .set(memberProfile.getLoginDate() != null, MemberProfile::getLoginDate, memberProfile.getLoginDate())
            .set(StringUtils.isNotBlank(memberProfile.getLoginIp()), MemberProfile::getLoginIp, memberProfile.getLoginIp())
            .set(StringUtils.isNotBlank(memberProfile.getRegisterIp()), MemberProfile::getRegisterIp, memberProfile.getRegisterIp())
            .set(memberProfile.getSex() != null, MemberProfile::getSex, memberProfile.getSex())
            .set(StringUtils.isNotBlank(memberProfile.getDescription()), MemberProfile::getDescription, memberProfile.getDescription())

            .set(memberProfile.getChannelId() != null, MemberProfile::getChannelId, memberProfile.getChannelId())
            .set(StringUtils.isNotBlank(memberProfile.getChannelName()), MemberProfile::getChannelName, memberProfile.getChannelName())
            .set(StringUtils.isNotBlank(memberProfile.getPackageCode()), MemberProfile::getPackageCode, memberProfile.getPackageCode())
            .set(memberProfile.getGameId() != null, MemberProfile::getGameId, memberProfile.getGameId())
            .set(StringUtils.isNotBlank(memberProfile.getGameName()), MemberProfile::getGameName, memberProfile.getGameName())
            .set(memberProfile.getIsOfficial() != null, MemberProfile::getIsOfficial, memberProfile.getIsOfficial())

            .set(StringUtils.isNotBlank(memberProfile.getProvince()), MemberProfile::getProvince, memberProfile.getProvince())
            .set(StringUtils.isNotBlank(memberProfile.getCity()), MemberProfile::getCity, memberProfile.getCity())
            .set(StringUtils.isNotBlank(memberProfile.getArea()), MemberProfile::getArea, memberProfile.getArea())
            .set(memberProfile.getProvinceId() != null, MemberProfile::getProvinceId, memberProfile.getProvinceId())
            .set(memberProfile.getCityId() != null, MemberProfile::getCityId, memberProfile.getCityId())
            .set(memberProfile.getAreaId() != null, MemberProfile::getAreaId, memberProfile.getAreaId())

            .set(memberProfile.getSessionNum() != null, MemberProfile::getSessionNum, memberProfile.getSessionNum())
            .set(MemberProfile::getUpdateTime, new Date())
            .set(MemberProfile::getUpdateBy, memberProfile.getMemberId())
            .set(StringUtils.isNotBlank(memberProfile.getLoginDeviceId()), MemberProfile::getLoginDeviceId, memberProfile.getLoginDeviceId());


        return updateChainWrapper;

    }

    /**
     * 修改手机号
     *
     * @return
     * @Param dto
     */
    @Override
    public R updateMobileByMemberId(MobileDto dto) {
        if (!dto.getIsChange()) {    // 用户信息
            MemberVo memberVo = memberMapper.selectVoById(dto.getMemberId());
            // 判断当前账户是否已绑定过手机号，如果已经绑定提示: 您的账户已绑定手机号；
            if (null != memberVo && StringUtils.isNotBlank(memberVo.getMobile())) {
                return R.fail("您的账户已绑定手机号");
            }
        }
        String key =StringUtils.format("{}LOCK:USER_CHANGE_PHONE:{}",Constants.BASE_KEY , dto.getMobile());
        final LockInfo lockInfo = lockTemplate.lock(key, 30000L, 5000L, RedissonLockExecutor.class);
        try {
            if (null == lockInfo)
                return R.fail("业务处理中,请稍后再试");

            //校验验证码
            remoteCaptchaCodeService.checkCode(dto.getMobile(), dto.getCode());
            //更新用户手机号
            updateMemberMobile(dto);
            if (dto.getIsChange()) {
                // 记录一下当前用户更换的记录
                MemberVo memberVo = memberMapper.selectVoById(dto.getMemberId());
                MemberChangePhoneRecord memberChangePhoneRecord = new MemberChangePhoneRecord();
                memberChangePhoneRecord.setMemberId(dto.getMemberId());
                memberChangePhoneRecord.setOriginalMobile(memberVo.getMobile());
                memberChangePhoneRecord.setNewMobile(dto.getMobile());
                memberChangePhoneRecordMapper.insert(memberChangePhoneRecord);
            }
            //重新设置用户信息
            LoginUser loginUser = LoginHelper.getLoginUser();
            loginUser.setMobile(dto.getMobile());
            LoginHelper.setLoginUser(loginUser);


        } catch (Exception exception) {
            log.error("绑定手机号报错", exception);
        } finally {
            //释放锁
            lockTemplate.releaseLock(lockInfo);
        }


        return R.ok("绑定成功！");
    }


    private void updateMemberMobile(MobileDto dto) {

        // 更新用户详细信息表里的手机号
        var updateProfileWrapper = new LambdaUpdateChainWrapper<>(baseMapper);
        updateProfileWrapper.eq(MemberProfile::getMemberId, dto.getMemberId())
            .set(StringUtils.isNotBlank(dto.getMobile()), MemberProfile::getMobile, dto.getMobile()).set(MemberProfile::getUpdateTime, new Date()).set(MemberProfile::getUpdateBy, dto.getMemberId());
        updateProfileWrapper.update();

        // 更新用户信息表里的手机号
        var updateMemberWrapper = new LambdaUpdateChainWrapper<>(memberMapper);
        updateMemberWrapper.eq(Member::getId, dto.getMemberId())
            .set(StringUtils.isNotBlank(dto.getMobile()), Member::getMobile, dto.getMobile()).set(Member::getUpdateTime, new Date()).set(Member::getUpdateBy, dto.getMemberId());
        updateMemberWrapper.update();
    }


    /**
     * 按照条件查询用户
     *
     * @param dto
     * @return
     */
    @Override
    public R searchMember(SearchMemberlDto dto) {
        LambdaQueryWrapper<MemberProfile> memberProfileLambdaQueryWrapper = Wrappers.<MemberProfile>lambdaQuery()
            .eq(dto.getMobile() != null, MemberProfile::getMobile, dto.getMobile())
            .eq(dto.getMemberId() != null, MemberProfile::getMemberId, dto.getMemberId())
            .likeRight(dto.getNickName() != null, MemberProfile::getNickName, dto.getNickName());
        List<MemberProfileVo> memberProfileVos = baseMapper.selectVoList(memberProfileLambdaQueryWrapper);
        if (memberProfileVos != null) {
            return R.ok(memberProfileVos);
        }
        return R.ok();
    }

    @Override
    public List<MemberDetailVo> selectMemberByParam(Map<String, Object> orderMap) {
        String startTime = orderMap.get("startTime") == null ? null : orderMap.get("startTime").toString();
        String endTime = orderMap.get("endTime") == null ? null : orderMap.get("endTime").toString();
        LambdaQueryWrapper<MemberProfile> lqw = Wrappers.lambdaQuery();
        lqw.ge(StringUtils.isNotBlank(startTime), MemberProfile::getCreateTime, startTime);
        lqw.le(StringUtils.isNotBlank(endTime), MemberProfile::getCreateTime, endTime);
        List<MemberProfileVo> memberProfileVos = baseMapper.selectVoList(lqw);
        List<MemberDetailVo> rList = new ArrayList<>();
        for (MemberProfileVo v : memberProfileVos) {
            MemberDetailVo memberDetailVo = BeanUtil.toBean(v, MemberDetailVo.class);
            rList.add(memberDetailVo);
        }
        return rList;
    }


    /**
     * 按照指定条件查询出用户的信息用于封号使用
     *
     * @return
     */
    @Override
    public TableDataInfo<MemberProfileBasics> getMemberIdsByParams(SearchBanDto dto) {
        TableDataInfo<MemberProfileBasics> memberProfileBasicsTableDataInfo = new TableDataInfo<>();
        MemberPageDto memberPageDto = new MemberPageDto();
        // 设置分页
        memberPageDto.setPageSize(dto.getPageSize());
        memberPageDto.setPageNum(dto.getPageNum());
        // 设置分页的默认值
        memberProfileBasicsTableDataInfo.setCode(200);
        memberProfileBasicsTableDataInfo.setRows(new ArrayList<>());
        memberProfileBasicsTableDataInfo.setMsg("查询成功");
        if (StringUtils.isBlank(dto.getSearchKey())) {
            return memberProfileBasicsTableDataInfo;
        }
        try {
            TableDataInfo<MemberDetailVo> memberDetailPageList = new TableDataInfo<>();
            // 进行数据查询
            switch (dto.getSearchType()) {
                case 1:
                    memberPageDto.setMobile(dto.getSearchKey());
                    memberDetailPageList = getMemberDetailPageList(memberPageDto);
                    break;
                case 2:
                    memberPageDto.setMemberId(Convert.toLong(dto.getSearchKey()));
                    memberDetailPageList = getMemberDetailPageList(memberPageDto);
                    break;
                case 4:
                    // 随机生成密钥
                    byte[] key = webConfig.getCommonAesKey().getBytes(StandardCharsets.UTF_8);
                    AES aes = SecureUtil.aes(key);
                    // 加密
                    String checkIdCardNo = aes.encryptBase64(dto.getSearchKey());
                    memberPageDto.setIdCardNo(checkIdCardNo);
                    memberDetailPageList = getMemberDetailPageList(memberPageDto);
                    break;
                case 6:
                    String[] strArr = dto.getSearchKey().split(",");
                    Long[] ids = Convert.toLongArray(strArr);
                    List<Long> memberIds = Arrays.asList(ids);
                    memberPageDto.setMemberIds(memberIds);
                    memberDetailPageList = getMemberDetailPageList(memberPageDto);
                    break;
            }
            if (memberDetailPageList != null && memberDetailPageList.getRows().size() > 0) {
                // 循环进行赋值替换
                memberDetailPageList.getRows().forEach(row -> {
                    MemberProfileBasics copy = BeanCopyUtils.copy(row, MemberProfileBasics.class);
                    copy.setDeviceId(row.getLoginDeviceId());
                    copy.setCreateTime(row.getLoginDate());
                    memberProfileBasicsTableDataInfo.getRows().add(copy);
                });
                memberProfileBasicsTableDataInfo.setTotal(memberDetailPageList.getTotal());
                memberProfileBasicsTableDataInfo.setCode(memberDetailPageList.getCode());
            }
        } catch (Exception e) {
        }
        return memberProfileBasicsTableDataInfo;
    }

    /**
     * 一批身份证号进行加密处理
     *
     * @param idCardNos
     * @return
     */
    @Override
    public List<KeyValueDto<String>> getUserIdCardNoEncryption(List<String> idCardNos, Integer type) {
        // 随机生成密钥
        byte[] key = webConfig.getCommonAesKey().getBytes(StandardCharsets.UTF_8);
        AES aes = SecureUtil.aes(key);
        List<KeyValueDto<String>> returnList = new ArrayList<>();
        if (type.equals(1)) {
            // 加密
            idCardNos.forEach(a -> {
                KeyValueDto<String> objectKeyValueDto = new KeyValueDto<>();
                objectKeyValueDto.setKey(a);
                String checkIdCardNo = aes.encryptBase64(a);
                objectKeyValueDto.setValue(checkIdCardNo);
                returnList.add(objectKeyValueDto);
            });
        } else {
            // 解密
            idCardNos.forEach(b -> {
                KeyValueDto<String> objectKeyValueDto = new KeyValueDto<>();
                objectKeyValueDto.setKey(b);
                String checkIdCardNo = aes.decryptStr(b);
                objectKeyValueDto.setValue(checkIdCardNo);
                returnList.add(objectKeyValueDto);
            });
        }
        return returnList;
    }

    /**
     * 获取身份证的随机秘钥
     */
    public byte[] getIdCardAesKey() {
        // 随机生成密钥
        return webConfig.getCommonAesKey().getBytes(StandardCharsets.UTF_8);
    }

}
