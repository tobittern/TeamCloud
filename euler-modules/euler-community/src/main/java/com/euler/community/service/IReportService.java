package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.community.domain.dto.ReportDto;
import com.euler.community.domain.entity.Report;
import com.euler.community.domain.vo.ReportVo;
import com.euler.community.domain.bo.ReportBo;
import com.euler.common.mybatis.core.page.TableDataInfo;

/**
 * 举报Service接口
 *
 * @author euler
 * @date 2022-06-09
 */
public interface IReportService extends IService<Report> {

    /**
     * 查询举报
     *
     * @param id 举报主键
     * @return 举报
     */
    ReportVo queryById(Long id);

    /**
     * 查询举报列表
     *
     * @param dto 举报
     * @return 举报集合
     */
    TableDataInfo<ReportVo> queryPageList(ReportDto dto);

    /**
     * 举报
     *
     * @param bo 举报
     * @return 结果
     */
    R insertByBo(ReportBo bo);

}
