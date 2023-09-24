package com.euler.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.community.domain.entity.Region;
import com.euler.community.domain.vo.RegionVo;
import com.euler.community.mapper.RegionMapper;
import com.euler.community.service.IRegionService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 地区Service业务层处理
 *
 * @author euler
 * @date 2022-06-21
 */
@RequiredArgsConstructor
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements IRegionService {

    private final RegionMapper baseMapper;

    /**
     * 查询地区
     *
     * @param id 地区主键
     * @return 地区
     */
    @Override
    public RegionVo queryById(Integer id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 获取指定级别和父级的地区列表
     *
     * @param level
     * @param parentId
     * @return
     */
    @Override
    public List<RegionVo> getAppointRegion(Integer level, Integer parentId) {
        parentId = level <= 0 ? -1 : parentId;

        String levelKey = StringUtils.format("{}region:{}:{}", Constants.BASE_KEY, level, parentId);
        List<RegionVo>  cacheList = RedisUtils.getCacheList(levelKey);
        if (cacheList == null||cacheList.isEmpty()) {
            List<Integer> levels = new ArrayList<>();
            if (level == 0) {
                levels.add(1);
                levels.add(2);
            } else if (level > 0) {
                levels.add(level);
            }


            List<RegionVo> regionVoList = baseMapper.selectVoList(new LambdaQueryWrapper<Region>()
                .in(!levels.isEmpty(), Region::getLevel, levels).eq(parentId > 0, Region::getParentId, parentId));
            List<RegionVo> newList = convertList(regionVoList, level);
            RedisUtils.setCacheList(levelKey, newList, Duration.ofHours(168L));
            return newList;

        }
        return cacheList;
    }

    private List<RegionVo> convertList(List<RegionVo> appointRegion, Integer type) {
        if (type>0)
            return  appointRegion;
        List<RegionVo> newAppointRegion = new ArrayList<>();
        if (appointRegion != null && !appointRegion.isEmpty()) {
            var firstList = appointRegion.stream().filter(a -> a.getLevel().equals("1")).collect(Collectors.toList());

            if (firstList != null && !firstList.isEmpty()) {
                firstList.forEach(b -> {
                    var secondList = appointRegion.stream().filter(a -> a.getParentId().equals(b.getId())).collect(Collectors.toList());
                    if (type == -1) {
                        if (secondList != null && !secondList.isEmpty()) {
                            secondList.forEach(
                                c -> {
                                    var thirdList = appointRegion.stream().filter(a -> a.getParentId().equals(c.getId())).collect(Collectors.toList());
                                    c.setChildren(thirdList);
                                }
                            );


                        }

                    }
                    b.setChildren(secondList);


                });
            }

            newAppointRegion.addAll(firstList);
        }
        return newAppointRegion;

    }


}
