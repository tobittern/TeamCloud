package com.euler.system.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.system.api.domain.NoticeToUsers;
import com.euler.system.domain.dto.NoticeToUsersDto;
import com.euler.system.domain.vo.NoticeToUsersVo;

import java.util.List;

/**
 * 公告接收Mapper接口
 *
 * @author euler
 * @date 2022-04-08
 */
public interface NoticeToUsersMapper extends BaseMapperPlus<NoticeToUsersMapper, NoticeToUsers, NoticeToUsersVo> {

    List<NoticeToUsersVo> selectVoByNoticeId(Integer noticeId);

    NoticeToUsersVo selectVoByDto(NoticeToUsersDto dto);



}
