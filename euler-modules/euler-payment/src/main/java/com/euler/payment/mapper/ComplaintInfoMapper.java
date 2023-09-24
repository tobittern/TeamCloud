package com.euler.payment.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.payment.domain.ComplaintInfo;
import com.euler.payment.domain.vo.ComplaintInfoVo;
import com.euler.sdk.api.domain.GameUserManagementVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 投诉详情Mapper接口
 *
 * @author euler
 * @date 2022-09-13
 */
@Mapper
public interface ComplaintInfoMapper extends BaseMapperPlus<ComplaintInfoMapper, ComplaintInfo, ComplaintInfoVo> {

}
