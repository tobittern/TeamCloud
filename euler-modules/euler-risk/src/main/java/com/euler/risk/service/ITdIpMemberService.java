package com.euler.risk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.risk.domain.dto.TdIpMemberPageDto;
import com.euler.risk.domain.entity.TdIpMember;
import com.euler.risk.domain.vo.UserDeviceIdInfoVo;

/**
 * ip账号信息Service接口
 *
 * @author euler
 * @date 2022-08-24
 */
public interface ITdIpMemberService extends IService<TdIpMember> {


    /**
     * 查询ip账号信息列表
     *
     * @return 账号信息集合
     */
    TableDataInfo<UserDeviceIdInfoVo> getUserDeviceInfoByParams(TdIpMemberPageDto dto);

    TdIpMember getByUserAndIp(Long userId, String ip);

}
