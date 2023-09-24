package com.euler.risk.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.core.utils.BeanCopyUtils;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.risk.api.domain.TdDeviceInfoVo;
import com.euler.risk.domain.bo.BanlistBo;
import com.euler.risk.domain.bo.BlacklistBo;
import com.euler.risk.domain.dto.BanlistDto;
import com.euler.risk.domain.dto.TdDeviceInfoPageDto;
import com.euler.risk.domain.dto.TdIpMemberPageDto;
import com.euler.risk.domain.entity.Banlist;
import com.euler.risk.domain.vo.BanlistVo;
import com.euler.risk.domain.vo.UserDeviceIdInfoVo;
import com.euler.risk.enums.DeviceInfoEnum;
import com.euler.risk.enums.SearchBanEnum;
import com.euler.risk.mapper.BanlistMapper;
import com.euler.risk.service.IBanlistService;
import com.euler.risk.service.IBlacklistService;
import com.euler.risk.service.ITdDeviceInfoService;
import com.euler.risk.service.ITdIpMemberService;
import com.euler.sdk.api.RemoteMemberService;
import com.euler.sdk.api.domain.MemberProfileBasics;
import com.euler.sdk.api.domain.dto.SearchBanDto;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 封号列Service业务层处理
 *
 * @author euler
 * @date 2022-08-23
 */
@RequiredArgsConstructor
@Service
public class BanlistServiceImpl extends ServiceImpl<BanlistMapper, Banlist> implements IBanlistService {

    private final BanlistMapper baseMapper;
    @DubboReference
    private RemoteMemberService remoteMemberService;
    @Autowired
    private ITdIpMemberService iTdIpMemberService;
    @Autowired
    private ITdDeviceInfoService iTdDeviceInfoService;
    @Autowired
    private IBlacklistService iBlacklistService;


    /**
     * 查询封号列
     *
     * @return 封号列
     */
    @Override
    public TableDataInfo<MemberProfileBasics> search(SearchBanDto dto) {
        // 补充用户的账号和手机号等信息  1手机号 2用户id 3 ip 4 身份证 5设备 6按照一批用户ID 7同一设备编码
        TableDataInfo<MemberProfileBasics> memberDeviceInfoList = new TableDataInfo<>();
        List<TdDeviceInfoVo> deviceInfoListByDeviceIds = new ArrayList<>();
        List<String> deviceIds = new ArrayList<>();
        SearchBanEnum searchBanEnum = SearchBanEnum.find(dto.getSearchType());
        switch (searchBanEnum) {
            case PHONE:
            case USERID:
            case IDCARD:
                memberDeviceInfoList = remoteMemberService.getMemberIdsByParams(dto);
                // 用户数据查询之后我们需要获取一下设备ID对应的设备信息
                deviceIds = memberDeviceInfoList.getRows().stream().map(MemberProfileBasics::getDeviceId).collect(Collectors.toList());
                if (deviceIds.size() > 0) {
                    deviceInfoListByDeviceIds = iTdDeviceInfoService.getDeviceInfoListByDeviceIds(deviceIds);
                    // 数据获取完毕之后将获取到的设备信息存放到返回数据中
                    for (MemberProfileBasics row : memberDeviceInfoList.getRows()) {
                        Optional<TdDeviceInfoVo> first = deviceInfoListByDeviceIds.stream().filter(b -> b.getId().equals(row.getDeviceId())).findFirst();
                        first.ifPresent(tdDeviceInfoVo -> row.setMobileType(tdDeviceInfoVo.getMobileType()));
                    }
                }
                break;
            case IP:
                TdIpMemberPageDto tdIpMemberPageDto = new TdIpMemberPageDto();
                tdIpMemberPageDto.setIp(dto.getSearchKey());
                TableDataInfo<UserDeviceIdInfoVo> getUserDeviceByIp = iTdIpMemberService.getUserDeviceInfoByParams(tdIpMemberPageDto);
                // 数据获取完毕之后进行替换
                if (getUserDeviceByIp.getRows().size() > 0) {
                    List<MemberProfileBasics> copyDeviceForIp = BeanCopyUtils.copyList(getUserDeviceByIp.getRows(), MemberProfileBasics.class);
                    // 用户数据查询之后我们需要获取一下设备ID对应的设备信息
                    deviceIds = copyDeviceForIp.stream().map(MemberProfileBasics::getDeviceId).collect(Collectors.toList());
                    if (deviceIds.size() > 0) {
                        deviceInfoListByDeviceIds = iTdDeviceInfoService.getDeviceInfoListByDeviceIds(deviceIds);
                        // 数据获取完毕之后将获取到的设备信息存放到返回数据中
                        for (MemberProfileBasics row : copyDeviceForIp) {
                            Optional<TdDeviceInfoVo> first = deviceInfoListByDeviceIds.stream().filter(b -> b.getId().equals(row.getDeviceId())).findFirst();
                            first.ifPresent(tdDeviceInfoVo -> row.setMobileType(tdDeviceInfoVo.getMobileType()));
                        }
                    }
                    memberDeviceInfoList.setRows(copyDeviceForIp);
                    memberDeviceInfoList.setCode(getUserDeviceByIp.getCode());
                    memberDeviceInfoList.setTotal(getUserDeviceByIp.getTotal());
                    memberDeviceInfoList.setMsg(getUserDeviceByIp.getMsg());
                }
                break;
            case DEVICE:
            case UNIFIED_DEVICE:
                TdDeviceInfoPageDto tdDeviceInfoPageDto = getSearchDeviceCondition(dto);
                TableDataInfo<UserDeviceIdInfoVo> getUserDeviceByDevice = iTdDeviceInfoService.getUserDeviceInfoByParams(tdDeviceInfoPageDto);
                if (getUserDeviceByDevice.getRows().size() > 0) {
                    List<MemberProfileBasics> copyDeviceForDevice = BeanCopyUtils.copyList(getUserDeviceByDevice.getRows(), MemberProfileBasics.class);
                    // 用户数据查询之后我们需要获取一下设备ID对应的设备信息
                    deviceIds = copyDeviceForDevice.stream().map(MemberProfileBasics::getDeviceId).collect(Collectors.toList());
                    if (deviceIds.size() > 0) {
                        deviceInfoListByDeviceIds = iTdDeviceInfoService.getDeviceInfoListByDeviceIds(deviceIds);
                        // 数据获取完毕之后将获取到的设备信息存放到返回数据中
                        for (MemberProfileBasics row : copyDeviceForDevice) {
                            Optional<TdDeviceInfoVo> first = deviceInfoListByDeviceIds.stream().filter(b -> b.getId().equals(row.getDeviceId())).findFirst();
                            first.ifPresent(tdDeviceInfoVo -> row.setMobileType(tdDeviceInfoVo.getMobileType()));
                        }
                    }
                    memberDeviceInfoList.setRows(copyDeviceForDevice);
                    memberDeviceInfoList.setCode(getUserDeviceByDevice.getCode());
                    memberDeviceInfoList.setTotal(getUserDeviceByDevice.getTotal());
                    memberDeviceInfoList.setMsg(getUserDeviceByDevice.getMsg());
                }
                break;
            default:
                break;
        }
        return memberDeviceInfoList;
    }

    /**
     * 根据选择的设备编码设置搜索字段
     *
     * @return
     */
    private TdDeviceInfoPageDto getSearchDeviceCondition(SearchBanDto dto) {
        TdDeviceInfoPageDto deviceInfoPageDto = new TdDeviceInfoPageDto();
        // 统一设备编码直接设置
        if (dto.getSearchType().equals(SearchBanEnum.UNIFIED_DEVICE.getSearchCode())) {
            deviceInfoPageDto.setId(dto.getSearchKey());
            return deviceInfoPageDto;
        }
        DeviceInfoEnum deviceInfoEnum = DeviceInfoEnum.find(dto.getEquipmentNum());
        switch (deviceInfoEnum) {
            case mac:
                deviceInfoPageDto.setDeviceMac(dto.getSearchKey());
                break;
            case oaid:
                deviceInfoPageDto.setDeviceOaid(dto.getSearchKey());
                break;
            case imei:
                deviceInfoPageDto.setDeviceImei(dto.getSearchKey());
                break;
            case android:
                deviceInfoPageDto.setDeviceAndroid(dto.getSearchKey());
                break;
            case uuid:
                deviceInfoPageDto.setDeviceUuid(dto.getSearchKey());
                break;
            case idfa:
                deviceInfoPageDto.setDeviceIdfa(dto.getSearchKey());
                break;
            case pushId:
                deviceInfoPageDto.setDevicePushId(dto.getSearchKey());
                break;
        }
        return deviceInfoPageDto;
    }


    /**
     * 查询封号列
     *
     * @param id 封号列主键
     * @return 封号列
     */
    @Override
    public BanlistVo queryById(Integer id) {
        BanlistVo banlistVo = baseMapper.selectVoById(id);
        if (banlistVo != null) {
            String sTime = DateUtil.format(banlistVo.getStartTime(), "yyyy/MM/dd");
            String eTime = DateUtil.format(banlistVo.getEndTime(), "yyyy/MM/dd");
            if (eTime.equals("2194/03/05")) {
                banlistVo.setBanTime("永久");
            } else {
                banlistVo.setBanTime(sTime + "-" + eTime);
            }
        }
        return banlistVo;
    }


    /**
     * 查询封号列
     *
     * @param id 封号列主键
     * @return 封号列
     */
    @Override
    public BanlistVo queryByMemberId(Long id) {
        LambdaQueryWrapper<Banlist> last = Wrappers.<Banlist>lambdaQuery().eq(Banlist::getMemberId, id)
            .orderByDesc(Banlist::getId)
            .last("limit 1");
        BanlistVo banlistVo = baseMapper.selectVoOne(last);
        if (banlistVo == null || banlistVo.getId() == null) {
            return new BanlistVo();
        }
        return banlistVo;
    }

    /**
     * 查询封号列列表
     *
     * @return 封号列
     */
    @Override
    public TableDataInfo<BanlistVo> queryPageList(BanlistDto dto) {
        LambdaQueryWrapper<Banlist> lqw = buildQueryWrapper(dto);
        Page<BanlistVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        if (result.getRecords() != null && result.getRecords().size() > 0) {
            result.getRecords().forEach(a -> {
                String sTime = DateUtil.format(a.getStartTime(), "yyyy/MM/dd");
                String eTime = DateUtil.format(a.getEndTime(), "yyyy/MM/dd");
                if (eTime.equals("2194/03/05")) {
                    a.setBanTime("永久");
                } else {
                    a.setBanTime(sTime + "-" + eTime);
                }
            });
        }
        return TableDataInfo.build(result);
    }


    private LambdaQueryWrapper<Banlist> buildQueryWrapper(BanlistDto dto) {
        LambdaQueryWrapper<Banlist> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getPlatform() != null, Banlist::getPlatform, dto.getPlatform());
        lqw.eq(dto.getMemberId() != null, Banlist::getMemberId, dto.getMemberId());
        lqw.eq(StringUtils.isNotBlank(dto.getAccount()), Banlist::getAccount, dto.getAccount());
        lqw.eq(StringUtils.isNotBlank(dto.getNickName()), Banlist::getNickName, dto.getNickName());
        lqw.eq(dto.getGameId() != null, Banlist::getGameId, dto.getGameId());
        lqw.eq(StringUtils.isNotBlank(dto.getMobile()), Banlist::getMobile, dto.getMobile());
        lqw.le(StringUtils.isNotBlank(dto.getStartTime()), Banlist::getStartTime, DateUtils.getBeginOfDay(dto.getStartTime()));
        lqw.ge(StringUtils.isNotBlank(dto.getEndTime()), Banlist::getEndTime, DateUtils.getEndOfDay(dto.getEndTime()));
        lqw.eq(StringUtils.isNotBlank(dto.getBanType()), Banlist::getBanType, dto.getBanType());
        lqw.eq(dto.getSearchType() != null, Banlist::getSearchType, dto.getSearchType());
        lqw.orderByDesc(Banlist::getId);
        return lqw;
    }

    /**
     * 新增封号列
     *
     * @param bo 封号列
     * @return 结果
     */
    @Override
    @Transactional
    public R insertByBo(BanlistBo bo) {
        // 需要从远程查询出当前用户的昵称信息
        SearchBanDto searchBanDto = new SearchBanDto();
        searchBanDto.setSearchType(SearchBanEnum.MUCHUSER.getSearchCode());
        searchBanDto.setSearchKey(bo.getMemberIds());
        TableDataInfo<MemberProfileBasics> memberIdsByParams = remoteMemberService.getMemberIdsByParams(searchBanDto);
        if (memberIdsByParams.getRows() == null || memberIdsByParams.getRows().size() == 0) {
            return R.fail("用户数据不存在");
        }
        List<MemberProfileBasics> memberByIds = memberIdsByParams.getRows();
        // 首先进行的是删除操作 删除之后在进行添加操作
        List<Long> memberIds = memberByIds.stream().map(MemberProfileBasics::getMemberId).collect(Collectors.toList());
        deleteWithValidByIds(memberIds, false);
        // 循环进行数据增加了
        memberByIds.forEach(a -> {
            Banlist add = BeanUtil.toBean(bo, Banlist.class);
            add.setMemberId(a.getMemberId());
            add.setNickName(a.getNickName());
            add.setAccount(a.getAccount());
            add.setMobile(a.getMobile());
            add.setStartTime(DateUtil.parse(DateUtils.getBeginOfDay(bo.getStartTime())));
            add.setEndTime(DateUtil.parse(DateUtils.getEndOfDay(bo.getEndTime())));
            boolean flag = baseMapper.insert(add) > 0;
            if (flag) {
                deleteRedisKey(a.getMemberId());
            }
        });
        // 判断是需要添加到黑名单中
        if (bo.getIsJoin().equals(1)) {
            BlacklistBo blacklistBo = BeanUtil.toBean(bo, BlacklistBo.class);
            // 根据搜索的类型 判断需要讲那些数据添加到黑名单里面
            if (bo.getSearchType().equals(SearchBanEnum.DEVICE.getSearchCode())) {
                if (bo.getEquipmentNum() == null) {
                    return R.ok("封设备并没有传输设备编码");
                }
                // 当前是按照设备添加到黑名单中
                blacklistBo.setType(Convert.toStr((bo.getEquipmentNum() + 3)));
                DeviceInfoEnum deviceInfoEnum = DeviceInfoEnum.find(bo.getEquipmentNum());
                if (deviceInfoEnum != null) {
                    KeyValueDto<String> objectKeyValueDto = new KeyValueDto<>();
                    objectKeyValueDto.setKey(deviceInfoEnum.getDeviceCode());
                    objectKeyValueDto.setValue(bo.getSearchKey());
                    // 设置
                    TdDeviceInfoVo deviceIdByDevice = iTdDeviceInfoService.getDeviceIdByDevice(objectKeyValueDto);
                    if (deviceIdByDevice != null && deviceIdByDevice.getId() != null) {
                        blacklistBo.setTarget(deviceIdByDevice.getId());
                    }
                }
            } else if (bo.getSearchType().equals(SearchBanEnum.IDCARD.getSearchCode())) {
                // 身份证
                blacklistBo.setType("2");
                List<String> idCardNoList = new ArrayList<>();
                idCardNoList.add(bo.getSearchKey());
                List<KeyValueDto<String>> userIdCardNoEncryption = remoteMemberService.getUserIdCardNoEncryption(idCardNoList, 1);
                Optional<KeyValueDto<String>> first = userIdCardNoEncryption.stream().filter(a -> a.getKey().equals(bo.getSearchKey())).findFirst();
                first.ifPresent(keyValueDto -> blacklistBo.setTarget(keyValueDto.getValue()));
            } else {
                blacklistBo.setType(bo.getSearchType().toString());
                blacklistBo.setTarget(bo.getSearchKey());
            }
            // 数据入库
            iBlacklistService.insertByBo(blacklistBo);
        }
        return R.ok();
    }

    /**
     * 修改封号列
     *
     * @param bo 封号列
     * @return 结果
     */
    @Override
    public R updateByBo(BanlistBo bo) {
        Banlist update = BeanUtil.toBean(bo, Banlist.class);
        // 时间处理
        update.setStartTime(DateUtil.parse(DateUtils.getBeginOfDay(bo.getStartTime())));
        update.setEndTime(DateUtil.parseTime(DateUtils.getEndOfDay(bo.getEndTime())));
        int i = baseMapper.updateById(update);
        if (i > 0) {
            deleteRedisKey(bo.getMemberId());
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 删除指定的redis key
     */
    @Override
    public void deleteRedisKey(Long userId) {
        // token 获取完毕之后我们需要生成redis的值
        String userBanKey = Constants.RISK_KEY + "ban:" + userId;
        RedisUtils.deleteObject(userBanKey);
    }

    /**
     * 批量删除封号列
     *
     * @param ids 需要删除的封号列主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(List<Long> ids, Boolean isValid) {
        if (ids.size() <= 0) {
            return false;
        }
        LambdaQueryWrapper<Banlist> in = Wrappers.<Banlist>lambdaQuery().in(Banlist::getMemberId, ids);
        List<BanlistVo> banlistVos = baseMapper.selectVoList(in);
        // 执行删除操作
        List<Integer> deleteIds = banlistVos.stream().map(BanlistVo::getId).collect(Collectors.toList());
        if (deleteIds.size() > 0) {
            int i = baseMapper.deleteBatchIds(deleteIds);
            if (i > 0) {
                List<Long> memberIds = banlistVos.stream().map(BanlistVo::getMemberId).distinct().collect(Collectors.toList());
                memberIds.forEach(a -> {
                    deleteRedisKey(a);
                });
            }
        }
        return true;
    }

    public List<Long> checkUserBanStatus(List<Long> memberIds) {
        LambdaQueryWrapper<Banlist> lqw = Wrappers.<Banlist>lambdaQuery().in(Banlist::getMemberId, memberIds)
            .lt(Banlist::getStartTime, new Date())
            .gt(Banlist::getEndTime, new Date());
        List<BanlistVo> banlistVos = baseMapper.selectVoList(lqw);
        return banlistVos.stream().map(BanlistVo::getMemberId).distinct().collect(Collectors.toList());
    }
}
