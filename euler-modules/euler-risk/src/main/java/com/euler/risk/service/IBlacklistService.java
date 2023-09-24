package com.euler.risk.service;

import com.euler.common.core.domain.R;
import com.euler.risk.domain.dto.BlacklistDto;
import com.euler.risk.domain.entity.Blacklist;
import com.euler.risk.domain.vo.BlacklistVo;
import com.euler.risk.domain.bo.BlacklistBo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;

/**
 * 黑名单Service接口
 *
 * @author euler
 * @date 2022-08-23
 */
public interface IBlacklistService extends IService<Blacklist> {

    /**
     * 查询黑名单列表
     *
     * @param dto 黑名单
     * @return 黑名单集合
     */
    TableDataInfo<BlacklistVo> queryPageList(BlacklistDto dto);

    /**
     * 修改黑名单
     *
     * @param bo 黑名单
     * @return 结果
     */
    R insertByBo(BlacklistBo bo);

    /**
     * 校验并批量删除黑名单信息
     *
     * @param ids     需要删除的黑名单主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    R deleteWithValidByIds(Collection<Integer> ids, Boolean isValid);

    /**
     * 根据指定条件查询一条数据
     * @return
     */
    BlacklistVo queryByParams(String param);

}
