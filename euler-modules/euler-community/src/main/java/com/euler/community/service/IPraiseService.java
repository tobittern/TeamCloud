package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.PraiseBo;
import com.euler.community.domain.dto.PraiseDto;
import com.euler.community.domain.entity.Praise;
import com.euler.community.domain.vo.NewPraiseVo;
import com.euler.community.domain.vo.PraiseVo;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 点赞Service接口
 *
 * @author euler
 * @date 2022-06-06
 */
public interface IPraiseService extends IService<Praise> {
    /**
     * 查询点赞列表
     *
     * @return 点赞集合
     */
    TableDataInfo<PraiseVo> queryPageList(PraiseDto dto);

    /**
     * 修改点赞
     *
     * @return 结果
     */
    R insertByBo(PraiseBo bo);

    /**
     * 校验并批量删除点赞信息
     *
     * @param ids     需要删除的点赞主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 检测用户是否针对一些动态进行的点赞
     *
     * @param userId
     * @param type
     * @param relationId
     * @return
     */
    List<Long> checkUserIsPraise(Long userId, Integer type, List<Long> relationId);

    /**
     * 查询新点赞消息列表
     *
     * @return 点赞集合
     */
    TableDataInfo<NewPraiseVo> queryNewPageList(PraiseDto dto);

    /**
     * 获取用户某天的点赞数量
     *
     * @param userId
     * @param date
     * @param type 类型 1动态 2评论
     * @return
     */
    Integer praiseCountForDay(Long userId, Date date, String type);

}
