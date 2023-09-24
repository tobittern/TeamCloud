package com.euler.statistics.dubbo;

import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.statistics.api.RemoteRechargeStatService;
import com.euler.statistics.service.IBasicStatisticsService;
import com.euler.statistics.service.IOnlineUserService;
import com.euler.statistics.service.IRechargeStatService;
import com.euler.statistics.service.ITfUserQuotaService;
import com.euler.system.api.domain.SysUserOnline;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@DubboService
public class RemoteRechargeStatServiceImpl implements RemoteRechargeStatService {

    @Autowired
    private IRechargeStatService rechargeStatService;
    @Autowired
    private IBasicStatisticsService iBasicStatisticsService;
    @Autowired
    private IOnlineUserService onlineUserService;

    /**
     * 搜集填充数据到同一个基础表
     */
    @Override
    public void fillDataToSigle(FillDataDto fillDataDto) {
        rechargeStatService.fillRechargeData(fillDataDto);
    }

    /**
     * 基础数据同步到一张表中
     */
    @Override
    public void fillBasicStatistics() {
        iBasicStatisticsService.getDataIntoMysql();
    }


    @Override
    public  void  fillOnlineUser(List<SysUserOnline> userOnlineList){
        onlineUserService.getCurrentOnlineUser(userOnlineList);
    }



}
