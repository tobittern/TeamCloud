package com.euler.sdk.service;

import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.domain.bo.GoodsBo;
import com.euler.sdk.domain.dto.GoodsDto;
import com.euler.sdk.api.domain.GoodsVo;

import java.util.Collection;
import java.util.List;

/**
 * 商品Service接口
 *
 * @author euler
 * @date 2022-03-21
 */
public interface IGoodsService {

    /**
     * 根据id查询商品
     *
     * @param id 商品主键
     * @return 商品
     */
    GoodsVo queryById(Integer id) ;

    /**
     * 查询商品
     *
     * @param id 商品主键
     * @return 商品
     */
    GoodsVo queryById(Integer id, Integer type, Long userId) ;

    /**
     * 查询商品列表
     *
     * @return 商品集合
     */
    TableDataInfo<GoodsVo> queryPageList(GoodsDto goodsDto);

    /**
     * 修改商品
     *
     * @return 结果
     */
    R insertByBo(GoodsBo bo);

    /**
     * 修改商品
     *
     * @return 结果
     */
    R updateByBo(GoodsBo bo);

    /**
     * 删除商品
     *
     * @param ids 需要删除的礼包活动管理主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid);

    /**
     * 商品操作上下线
     *
     * @return 结果
     */
    R operation(IdNameTypeDicDto idNameTypeDicDto, Long userId);

    /**
     * 查询已上架的年卡商品列表
     *
     * @return 商品集合
     */
    List<GoodsVo> queryCardList();

}
