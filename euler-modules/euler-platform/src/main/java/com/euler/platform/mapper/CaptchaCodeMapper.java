package com.euler.platform.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.platform.api.domain.CaptchaCode;
import com.euler.platform.domain.bo.CaptchaCodeBo;
import com.euler.platform.domain.vo.CaptchaCodeVo;

/**
 * 邮箱Mapper接口
 *
 * @author open
 * @date 2022-02-28
 */
public interface CaptchaCodeMapper extends BaseMapperPlus<CaptchaCodeMapper, CaptchaCode, CaptchaCodeVo> {

    /**
     * 通过id查询
     *
     * @param id
     * @return 邮箱对象信息
     */
    CaptchaCode selectById(String id);

    /**
     * 通过邮箱查询
     *
     * @param email 邮箱
     * @return 邮箱对象信息
     */
    CaptchaCode selectByReceiver(String email);

    /**
     * 通过邮箱进行更新
     *
     * @param bo 邮箱bo
     * @return 邮箱对象信息
     */
    int updateByReceiver(CaptchaCodeBo bo);
}
