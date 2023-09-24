package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.community.domain.entity.Region;
import com.euler.community.domain.vo.RegionVo;

import java.util.List;

/**
 * 地区Service接口
 *
 * @author euler
 * @date 2022-06-21
 */
public interface IRegionService extends IService<Region> {

    /**
     * 查询地区
     *
     * @param id 地区主键
     * @return 地区
     */
    RegionVo queryById(Integer id);


    /**
     * 获取指定级别和父级的地区列表
     *
     * @param level
     * @param parentId
     * @return
     */
    List<RegionVo> getAppointRegion(Integer level, Integer parentId);


}
