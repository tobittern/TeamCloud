package com.euler.community.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.config.CommonCommunityConfig;
import com.euler.community.constant.GitBagConstant;
import com.euler.community.domain.bo.BannerBo;
import com.euler.community.domain.dto.BannerDto;
import com.euler.community.domain.entity.Banner;
import com.euler.community.domain.vo.BannerVo;
import com.euler.community.mapper.BannerMapper;
import com.euler.community.service.IBannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * banner列Service业务层处理
 *
 * @author euler
 * @date 2022-06-07
 */
@RequiredArgsConstructor
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper,Banner> implements IBannerService {

    private final BannerMapper baseMapper;
    @Resource
    private CommonCommunityConfig commonCommunityConfig;

    @Override
    public List<BannerVo> searchByIds(List<Long> ids) {
        LambdaQueryWrapper<Banner> in = Wrappers.<Banner>lambdaQuery().in(Banner::getId, ids);
        List<BannerVo> bannerVos = baseMapper.selectVoList(in);
        List<BannerVo> returnBanner = new ArrayList<>();
        for (Long id : ids) {
            Optional<BannerVo> first = bannerVos.stream().filter(a -> a.getId().equals(id)).findFirst();
            first.ifPresent(returnBanner::add);
        }
        return returnBanner;
    }

    /**
     * 查询banner列
     *
     * @param id banner列主键
     * @return banner列
     */
    @Override
    public BannerVo queryById(Long id) {
        BannerVo vo = baseMapper.selectVoById(id);
        return setUrlPrefix(vo);
    }

    /**
     * 设置前缀
     *
     * @param vo
     */
    public BannerVo setUrlPrefix(BannerVo vo) {
        if (vo == null) {
            return vo;
        }
        String yunDomain = commonCommunityConfig.getYunDomain();
        //banner图  banner_icon
        String bannerIcon = vo.getBannerIcon();
        vo.setBannerIcon(StringUtils.isBlank(bannerIcon) ? null : (bannerIcon.startsWith(GitBagConstant.HTTP) ? bannerIcon : yunDomain + bannerIcon));
        return vo;
    }

    /**
     * 查询banner列列表
     *
     * @param dto
     * @return banner列
     */
    @Override
    public TableDataInfo<BannerVo> queryPageList(BannerDto dto) {
        LambdaQueryWrapper<Banner> lqw = buildQueryWrapper(dto);
        Page<BannerVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        //设置的url
        if (result != null && result.getRecords() != null && result.getRecords().size() > 0) {
            result.getRecords().forEach(vo -> {
                setUrlPrefix(vo);
            });
        }
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<Banner> buildQueryWrapper(BannerDto dto) {
        LambdaQueryWrapper<Banner> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getId() != null, Banner::getId, dto.getId());
        lqw.eq(dto.getMemberId() != null, Banner::getMemberId, dto.getMemberId());
        lqw.like(StringUtils.isNotBlank(dto.getBannerName()), Banner::getBannerName, dto.getBannerName());
        lqw.ge(dto.getStartTime() != null, Banner::getCreateTime,  DateUtils.getBeginOfDay(dto.getStartTime()));
        lqw.le(dto.getEndTime() != null, Banner::getCreateTime,  DateUtils.getEndOfDay(dto.getEndTime()));
        // 按照id倒序排列
        lqw.orderByDesc(Banner::getId);
        return lqw;
    }

    /**
     * 新增banner列
     *
     * @param bo banner列
     * @return 结果
     */
    @Override
    public R insertByBo(BannerBo bo) {
        Banner add = BeanUtil.toBean(bo, Banner.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return R.ok();
    }

    /**
     * 修改banner列
     *
     * @param bo banner列
     * @return 结果
     */
    @Override
    public R updateByBo(BannerBo bo) {
        Banner update = BeanUtil.toBean(bo, Banner.class);
        validEntityBeforeSave(update);
        int i = baseMapper.updateById(update);
        if (i > 0) {
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(Banner entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 删除banner配置
     *
     * @param ids
     * @return 结果
     */
    @Override
    public R deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (baseMapper.deleteBatchIds(ids) > 0) {
            return R.ok("删除成功");
        }
        return R.fail("删除失败");
    }

}
