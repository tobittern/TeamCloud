package com.euler.community.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.community.domain.entity.Report;
import com.euler.community.domain.vo.ReportVo;
import org.apache.ibatis.annotations.Param;

/**
 * 举报Mapper接口
 *
 * @author euler
 * @date 2022-06-09
 */
public interface ReportMapper extends BaseMapperPlus<ReportMapper, Report, ReportVo> {

    Page<ReportVo> selectFrontDamicList(@Param("page") Page<Report> page, @Param(Constants.WRAPPER) Wrapper<Report> queryWrapper);

    Page<ReportVo> selectFrontCommentsList(@Param("page") Page<Report> page, @Param(Constants.WRAPPER) Wrapper<Report> queryWrapper);
}
