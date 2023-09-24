package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.dto.CollectDto;
import com.euler.community.domain.entity.Collect;
import com.euler.community.domain.vo.CollectVo;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 动态收藏Service接口
 *
 * @author euler
 * @date 2022-06-06
 */
public interface ICollectService extends IService<Collect> {

    /**
     * 查询动态收藏列表
     *
     * @return 动态收藏集合
     */
    TableDataInfo<CollectVo> queryPageList(CollectDto dto);

    /**
     * 修改动态收藏
     *
     * @return 结果
     */
    R insertByBo(IdNameTypeDicDto<Long> dto);

    /**
     * 校验并批量删除动态收藏信息
     *
     * @param ids     需要删除的动态收藏主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 检测用户是否针对一些动态进行的收藏
     * @param userId
     * @param dynamicIds
     * @return
     */
    List<Long> checkUserIsCollect(Long userId, List<Long> dynamicIds);

    /**
     * 获取用户收藏数量
     */
    Integer count(Long userId);

    /**
     *  获取用户某天的收藏数量
     */
    Integer collectCountForDay(Long userId, Date date);

}
