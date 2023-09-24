package com.euler.statistics.api;

import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.system.api.domain.SysUserOnline;

import java.util.List;

public interface RemoteRechargeStatService {

    /**
     * 搜集填充数据到同一个基础表
     */
    void fillDataToSigle(FillDataDto fillDataDto);

    /**
     * 同步基础用户数据到指定表中
     */
    void fillBasicStatistics();

    /**
     * 获取在线用户
     */
    void fillOnlineUser(List<SysUserOnline> userOnlineList);

}
