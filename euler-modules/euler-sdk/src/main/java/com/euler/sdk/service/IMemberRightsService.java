package com.euler.sdk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.domain.dto.MemberRightsDto;
import com.euler.sdk.domain.entity.MemberRights;
import com.euler.sdk.domain.vo.MemberRightsVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

/**
 * 会员权益Service接口
 *
 * @author euler
 * @date 2022-03-21
 */
public interface IMemberRightsService extends IService<MemberRights> {

    /**
     * 查询会员权益
     *
     * @param idDto 会员权益主键
     * @return 会员权益
     */
    MemberRightsVo queryById(IdDto<Long> idDto);

    /**
     * 查询会员权益列表
     *
     * @param dto 会员权益
     * @return 会员权益集合
     */
    TableDataInfo<MemberRightsVo> queryPageList(MemberRightsDto dto);

    /**
     * 开通会员
     *
     * @param bo 会员权益
     * @return 结果
     */
    R openMember(MemberRights bo);

    /**
     * 校验并批量删除会员权益信息
     *
     * @param ids 需要删除的会员权益主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    R deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 上传年费会员协议
     *
     * @param multipartFile 文件
     * @return
     */
    R upload(MultipartFile multipartFile);

    /**
     * 根据会员id，修改会员权益
     */
    Boolean updateMemberRightsDetail(MemberRights entity);

}
