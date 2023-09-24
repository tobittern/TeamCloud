package com.euler.platform.mapper;

import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.platform.domain.OpenUserCertification;
import com.euler.platform.domain.vo.OpenUserCertificationVo;

import java.util.List;

/**
 * 用户的资质认证记录Mapper接口
 *
 * @author open
 * @date 2022-02-23
 */
public interface OpenUserCertificationMapper extends BaseMapperPlus<OpenUserCertificationMapper, OpenUserCertification, OpenUserCertificationVo> {

    /**
     * 统计
     */
    List<KeyValueDto> selectAuditStatusCount();

}
