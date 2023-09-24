package com.euler.sdk.api;

import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.core.exception.user.UserException;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.api.domain.*;
import com.euler.sdk.api.domain.dto.NewLoginResultDto;
import com.euler.sdk.api.domain.dto.SearchBanDto;
import com.euler.sdk.api.enums.RechargeTypeEnum;

import java.util.List;
import java.util.Map;

public interface RemoteMemberService {

    LoginMemberVo getMemberById(Long memberId);

    LoginMemberVo getMemberByIdNoException(Long memberId);

    MemberProfile getMemberByUserId(Long userId) throws UserException;

    List<MemberProfile> getMemberByUserIds(List<Long> userId) throws UserException;

    LoginMemberVo loginByUserName(String userName, String password) throws UserException;

    LoginMemberVo loginByUserNameNoEx(String userName);

    LoginMemberVo getMemberByMobile(String mobile);

    /**
     * 修改密码
     *
     * @param userId
     * @param hashpw
     * @return
     */
    boolean resetUserPwd(Long userId, String hashpw) throws UserException;

    /**
     * 校验手机号是否唯一
     *
     * @param member 用户信息
     * @return
     */
    String checkUnique(Member member);

    Long registerMember(Member member);

    /**
     * 检测用户是否可以玩
     *
     * @param idCardNo
     * @return
     */
    Boolean checkUserPlay(String idCardNo);

    /**
     * 获取会员钱包详情
     *
     * @param memberId
     * @return
     */
    WalletVo getMyWallet(Long memberId);

    /**
     * 钱包金额变动
     *
     * @param memberId         会员id
     * @param walletType       钱包类型，1：正常钱包，2：虚拟钱包
     * @param numValue         变动金额，负数为减
     * @param rechargeTypeEnum 变动类型，1：积分，2：余额，3：平台币，4：成长值
     * @param isAdd            增减，1：增加，2：减少
     * @param modifyDesc       修改备注
     * @return
     */
    boolean modifyWallet(Long memberId, Integer gameId, Integer walletType, Number numValue, RechargeTypeEnum rechargeTypeEnum, Integer isAdd, String modifyDesc);

    /**
     * 修改会员信息
     *
     * @param memberProfile
     * @return
     */
    Boolean updateMemberDetail(MemberProfile memberProfile);

    boolean addGrowth(Long userId, Integer gameId, Number number, String payChannel);

    /**
     * 检查用户是否被封禁
     *
     * @param userId
     * @param platform
     * @return
     */
    MemberBanVo checkUserBlacklist(Long userId, String username, Integer platform, Integer gameId);

    List<LoginMemberVo> getMutiMemberByMobile(String account);

    /**
     * 获取待执行注销会员列表
     *
     * @return
     */
    List<CancellationLog> getOpCancellationMemberList();


    Boolean cancellationMember(Integer id, Long memberId);

    /**
     * 获取用户列表
     *
     * @param account
     * @return
     */
    List<NewLoginResultDto> getMemberByAccount(String account, Integer gameId);

    /**
     * 积分计算
     *
     * @param bo
     * @return
     */
    R calculateScore(ScoreSystemBo bo);

    List<MemberDetailVo> selectMemberByParam(Map<String, Object> orderMap);


    /**
     * 下线用户 ，game=-1时，下线当前用户id下所有用户id
     *
     * @param memberId
     * @param gameId
     */
    Integer downLineUser(Long memberId, Integer gameId, Integer platform);

    /**
     * 获取用户头像
     *
     * @param sex
     * @param avatar
     * @return
     */
    String getAvatar(String sex, String avatar);

    /**
     * 通过指定数据获取用户的id列表
     *
     * @return
     */
    TableDataInfo<MemberProfileBasics> getMemberIdsByParams(SearchBanDto dto);

    /**
     * 一批身份证号进行加密处理
     *
     * @return
     */
    List<KeyValueDto<String>> getUserIdCardNoEncryption(List<String> idCardNos, Integer type);

    /**
     * 获取身份证的随机秘钥
     */
    byte[] getIdCardAesKey();

}
