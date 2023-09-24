package com.euler.sdk.dubbo;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.euler.common.core.constant.UserConstants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.core.enums.UserStatus;
import com.euler.common.core.exception.user.UserException;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.api.RemoteMemberService;
import com.euler.sdk.api.RemoteMyGameService;
import com.euler.sdk.api.domain.*;
import com.euler.sdk.api.domain.dto.NewLoginResultDto;
import com.euler.sdk.api.domain.dto.NewLoginRoleDto;
import com.euler.sdk.api.domain.dto.SearchBanDto;
import com.euler.sdk.api.enums.RechargeTypeEnum;
import com.euler.sdk.config.WebConfig;
import com.euler.sdk.domain.dto.GetWalletDto;
import com.euler.sdk.service.*;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@DubboService
public class RemoteMemberServiceImpl implements RemoteMemberService {
    @Autowired
    private IMemberService memberService;
    @Autowired
    private IMemberProfileService memberProfileService;
    @Autowired
    private IMemberBlacklistService iMemberBlacklistService;
    @Autowired
    private IWalletService walletService;
    @Autowired
    private ICancellationLogService cancellationLogService;
    @Autowired
    private WebConfig webConfig;
    @Autowired
    private IScoreSystemService iScoreSystemService;
    @DubboReference
    private RemoteMyGameService remoteMyGameService;

    /**
     * 获取用户信息，不存在时抛出异常
     *
     * @param memberId
     * @return
     * @throws UserException
     */
    @Override
    public LoginMemberVo getMemberById(Long memberId) {
        LoginMemberVo member = memberService.getMemberById(memberId);
        if (ObjectUtil.isNull(member)) {
            throw new UserException("user.not.exists", memberId);
        }

        if (member != null) {
            if (UserStatus.DISABLE.getCode().equals(member.getStatus())) {
                throw new UserException("user.blocked", memberId);
            }
        }
        return member;
    }

    /**
     * 根据手机号获取用户信息
     */
    public LoginMemberVo getMemberByMobile(String mobile) {
        LoginMemberVo member = memberService.getMemberByMobile(mobile);
        return member;
    }

    /**
     * 获取用户信息，不存在时抛出异常
     *
     * @param memberId
     * @return
     * @throws UserException
     */
    @Override
    public LoginMemberVo getMemberByIdNoException(Long memberId) {
        LoginMemberVo member = memberService.getMemberById(memberId);
        return member;
    }

    @Override
    public MemberProfile getMemberByUserId(Long userId) throws UserException {
        MemberProfile byMemberId = memberProfileService.getByMemberId(userId);
        if (ObjectUtil.isNull(byMemberId)) {
            throw new UserException("user.not.exists", userId);
        }
        return byMemberId;
    }

    @Override
    public List<MemberProfile> getMemberByUserIds(List<Long> userIds) throws UserException {
        if (userIds.size() <= 0) {
            return new ArrayList<>();
        }
        List<MemberProfile> byMemberIds = memberProfileService.getByMemberIds(userIds);
        return byMemberIds;
    }

    /**
     * 登录，不存在时不抛出异常
     *
     * @param userName
     * @return
     * @throws UserException
     */
    @Override
    public LoginMemberVo loginByUserName(String userName, String password) throws UserException {
        LoginMemberVo member = memberService.login(userName, password);
        if (member != null) {
            if (UserStatus.DISABLE.getCode().equals(member.getStatus())) {
                throw new UserException("user.blocked", userName);
            }
        }
        return member;
    }

    /**
     * 登录，不存在时不抛出异常
     *
     * @param userName
     * @return
     * @throws UserException
     */
    @Override
    public LoginMemberVo loginByUserNameNoEx(String userName) {
        LoginMemberVo member = memberService.login(userName,null);
        return member;
    }

    /**
     * 修改密码
     *
     * @param userId
     * @param hashpw
     * @return
     */
    @Override
    public boolean resetUserPwd(Long userId, String hashpw) throws UserException {
        return memberService.update(
            new LambdaUpdateWrapper<Member>()
                .set(Member::getPassword, hashpw)
                .set(Member::getUpdateTime, new Date())
                .set(Member::getUpdateBy, userId)
                .eq(Member::getId, userId));
    }

    /**
     * 校验手机号是否唯一
     *
     * @param member 用户信息
     * @return
     */
    @Override
    public String checkUnique(Member member) {
        return memberService.checkUnique(member);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long registerMember(Member member) {
        Long memberId = memberService.register(member);
        return memberId;
    }

    /**
     * 检查用户是否可以玩
     *
     * @return
     */
    @Override
    public Boolean checkUserPlay(String idCardNo) {
        return memberService.checkUserPlay(idCardNo);
    }

    @Override
    public WalletVo getMyWallet(Long memberId) {
        return walletService.queryByMemberId(new GetWalletDto(memberId, 1, null));
    }

    /**
     * 钱包金额变动
     *
     * @param memberId         会员id
     * @param walletType       钱包类型 1：正常，2虚拟
     * @param numValue         变动金额，负数为减
     * @param rechargeTypeEnum 变动类型，1：积分，2：余额，3：平台币，4：成长值
     * @param isAdd            增减，1：增加，2：减少
     * @param modifyDesc       修改备注
     * @return
     */
    @Override
    public boolean modifyWallet(Long memberId, Integer gameId, Integer walletType, Number numValue, RechargeTypeEnum rechargeTypeEnum, Integer isAdd, String modifyDesc) {
        return walletService.modifyWallet(memberId, gameId, walletType, numValue, rechargeTypeEnum, isAdd, modifyDesc);
    }

    /**
     * 修改会员信息
     *
     * @param memberProfile
     * @return
     */
    @Override
    public Boolean updateMemberDetail(MemberProfile memberProfile) {
        return memberProfileService.updateMemberDetail(memberProfile);
    }

    @Override
    public boolean addGrowth(Long userId, Integer gameId, Number number, String payChannel) {
        Integer num = Convert.toInt(number, 0);
        if (num <= 0)
            return true;
        Integer growthValue = 0;
        if ("ali".equals(payChannel)
            || "wx".equals(payChannel)) {
            growthValue = num * 10;
        } else {
            // 苹果内购，成长值减半
            growthValue = num * 5;
        }
        return walletService.modifyWallet(userId, gameId, 1, growthValue, RechargeTypeEnum.growth_value, 1, "消费成长");
    }

    @Override
    public MemberBanVo checkUserBlacklist(Long userId, String username, Integer platform, Integer gameId) {
        return iMemberBlacklistService.checkUserIsBlacklist(userId, username, platform, gameId);
    }

    @Override
    public List<LoginMemberVo> getMutiMemberByMobile(String account) {
        return memberService.getMutiMemberByMobile(account);
    }

    /**
     * 获取待执行注销会员列表
     *
     * @return
     */
    @Override
    public List<CancellationLog> getOpCancellationMemberList() {
        return cancellationLogService.getOpList();
    }

    /**
     * 执行注销操作
     *
     * @param id
     * @param memberId
     * @return
     */
    @Override
    public Boolean cancellationMember(Integer id, Long memberId) {
        return memberService.cannellation(id, memberId);
    }

    /**
     * 获取用户列表
     *
     * @param account
     * @return
     */
    @Override
    public List<NewLoginResultDto> getMemberByAccount(String account, Integer gameId) {
        List<LoginMemberVo> list = memberService.getMutiMemberByMobile(account);

        if (list != null && !list.isEmpty()) {
            List<NewLoginResultDto> resultDtoList = new ArrayList<>();
            List<Long> memberIds = list.stream().map(a -> a.getId()).collect(Collectors.toList());

            // 查询角色信息
            List<GameUserManagement> gameUserInfoList = remoteMyGameService.getGameUserInfoList(memberIds, gameId);
            Date nowDate = new Date();
            for (var loginMemberVo : list) {
                NewLoginResultDto loginResultDto = new NewLoginResultDto();
                loginResultDto.setAccount(getAccount(loginMemberVo)).setId(loginMemberVo.getId())
                    .setMobile(loginMemberVo.getMobile())
                    .setAvatar(getAvatar(loginMemberVo.getSex(), loginMemberVo.getAvatar()))
                    .setSex(Convert.toStr(loginMemberVo.getSex(), UserConstants.SEX_UNKNOWN))
                    .setLastLoginDate(DateUtils.getDateString(loginMemberVo.getLoginDate(), nowDate));

                var gameUserInfoOp = gameUserInfoList.stream().filter(a -> a.getMemberId().equals(loginMemberVo.getId())).findFirst();

                if (gameUserInfoOp.isPresent()) {
                    var gameUserInfo = gameUserInfoOp.get();
                    NewLoginRoleDto loginRoleDto = new NewLoginRoleDto();
                    loginRoleDto.setRoleLevel(NumberUtil.isNumber(gameUserInfo.getRoleLevel()) ? gameUserInfo.getRoleLevel() + "级" : gameUserInfo.getRoleLevel())
                        .setRoleName(gameUserInfo.getRoleName())
                        .setServerName(gameUserInfo.getServerName());
                    loginResultDto.setLoginRole(loginRoleDto);
                }
                resultDtoList.add(loginResultDto);
            }
            return resultDtoList;
        }
        return null;
    }

    private String getAccount(LoginMemberVo loginMemberVo) {
        if (StringUtils.isNotBlank(loginMemberVo.getNickName()))
            return loginMemberVo.getNickName();
        if (StringUtils.isNotBlank(loginMemberVo.getMobile()))
            return loginMemberVo.getMobile();
        if (StringUtils.isNotBlank(loginMemberVo.getAccount()))
            return loginMemberVo.getAccount();

        return loginMemberVo.getId().toString();
    }

    /**
     * 积分计算
     *
     * @param bo
     * @return
     */
    public R calculateScore(ScoreSystemBo bo) {
        return iScoreSystemService.calculateScore(bo);
    }

    @Override
    public List<MemberDetailVo> selectMemberByParam(Map<String, Object> orderMap) {
        return memberProfileService.selectMemberByParam(orderMap);
    }

    @Override
    public Integer downLineUser(Long memberId, Integer gameId, Integer platform) {
        return memberService.downLineUser(memberId, gameId,  platform);
    }

    @Override
    public String getAvatar(String sex, String avatar) {
        return memberService.getAvatar(sex, avatar);
    }

    @Override
    public TableDataInfo<MemberProfileBasics> getMemberIdsByParams(SearchBanDto dto) {
        return memberProfileService.getMemberIdsByParams(dto);
    }

    @Override
    public List<KeyValueDto<String>> getUserIdCardNoEncryption(List<String> idCardNos, Integer type) {
        return memberProfileService.getUserIdCardNoEncryption(idCardNos, type);
    }

    @Override
    public byte[] getIdCardAesKey() {
        return memberProfileService.getIdCardAesKey();
    }

}
