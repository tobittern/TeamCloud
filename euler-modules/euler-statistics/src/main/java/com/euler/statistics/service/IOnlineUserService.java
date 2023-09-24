package com.euler.statistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.statistics.domain.dto.OnlineUserPageDto;
import com.euler.statistics.domain.dto.SummaryQueryDto;
import com.euler.statistics.domain.dto.SummaryResultDto;
import com.euler.statistics.domain.entity.OnlineUser;
import com.euler.statistics.domain.vo.OnlineUserVo;
import com.euler.system.api.domain.SysUserOnline;
import lombok.var;

import java.util.List;

/**
 * Service接口
 *
 * @author euler
 * @date 2022-09-14
 */
public interface IOnlineUserService extends IService<OnlineUser> {

    /**
     * 查询
     *
     * @param id 主键
     * @return
     */
    OnlineUserVo queryById(Long id);

    /**
     * 查询列表
     *
     * @param pageDto
     * @return 集合
     */
    TableDataInfo<OnlineUserVo> queryPageList(OnlineUserPageDto pageDto);

    /**
     * 查询列表
     *
     * @param pageDto
     * @return 集合
     */
    List<OnlineUserVo> queryList(OnlineUserPageDto pageDto);

    /**
     * 获取在线用户
     */
    void getCurrentOnlineUser(List<SysUserOnline> userOnlineList) ;


    /**
     * 获取在线用户统计
     *
     * @param queryDto
     * @return
     */
    SummaryResultDto getIncOnlineUserNum(SummaryQueryDto queryDto);


    /**
     * 获取在线用户折线
     *
     * @param queryDto
     * @return
     */
    List<SummaryResultDto> getIncOnlineUserGroupNum(SummaryQueryDto queryDto);


}
