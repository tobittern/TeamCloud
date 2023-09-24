package com.euler.risk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.risk.domain.dto.IpSearchDto;
import com.euler.risk.domain.entity.TfIpSummary;
import com.euler.risk.domain.vo.TfIpSummaryVo;

/**
 * ip汇总Service接口
 *
 * @author euler
 * @date 2022-08-24
 */
public interface ITfIpSummaryService extends IService<TfIpSummary> {

    /**
     * 查询ip行为异常预警列表
     *
     * @param dto ip检索条件
     * @return ip行为异常预警列表
     */
    TableDataInfo<TfIpSummaryVo> queryPageList(IpSearchDto dto);

    /**
     * 定时统计IP数据
     *
     * @param fillDataDto
     */
    void fillData(FillDataDto fillDataDto);

}
