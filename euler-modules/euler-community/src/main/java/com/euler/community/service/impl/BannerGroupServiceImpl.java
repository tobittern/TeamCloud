package com.euler.community.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.BannerGroupBo;
import com.euler.community.domain.dto.BannerGroupDto;
import com.euler.community.domain.entity.BannerGroup;
import com.euler.community.domain.vo.BannerGroupListVo;
import com.euler.community.domain.vo.BannerGroupVo;
import com.euler.community.domain.vo.BannerVo;
import com.euler.community.mapper.BannerGroupMapper;
import com.euler.community.service.IBannerGroupService;
import com.euler.community.service.IBannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * banner组Service业务层处理
 *
 * @author euler
 * @date 2022-06-06
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BannerGroupServiceImpl extends ServiceImpl<BannerGroupMapper, BannerGroup> implements IBannerGroupService {

    private final BannerGroupMapper baseMapper;

    @Autowired
    private IBannerService iBannerService;

    /**
     * 查询banner组
     *
     * @param id banner组主键
     * @return banner组
     */
    @Override
    public BannerGroupVo queryById(Long id) {
        BannerGroupVo vo = baseMapper.selectVoById(id);
        if (vo == null) {
            return new BannerGroupVo();
        }
        if (ObjectUtil.isNotNull(vo)) {
            vo.setShowTime(DateUtil.format(vo.getStartTime(), "yyyy/MM/dd") + "-" + DateUtil.format(vo.getEndTime(), "yyyy/MM/dd"));
        }
        // 查询出当前banner分组中的banner信息
        String[] split = vo.getBannerContent().split(",");
        List<Long> ids = Arrays.asList(Convert.toLongArray(split));
        List<BannerVo> bannerVos = iBannerService.searchByIds(ids);
        vo.setShowBannerContent(bannerVos);
        return vo;
    }

    /**
     * @param ids banner组主键
     * @return
     */
    @Override
    public List<BannerGroupListVo> getGroupBannerList(List<Long> ids) {
        BannerGroupDto bannerGroupDto = new BannerGroupDto();
        bannerGroupDto.setBannerGroupIds(ids);
        bannerGroupDto.setPageNum(1);
        bannerGroupDto.setPageSize(ids.size());
        bannerGroupDto.setStatus("1");
        TableDataInfo<BannerGroupVo> bannerGroupVoTableDataInfo = queryPageList(bannerGroupDto);
        // 数据获取完毕之后进行指定数据拼接
        List<BannerGroupListVo> returnObject = new ArrayList<>();
        for (BannerGroupVo row : bannerGroupVoTableDataInfo.getRows()) {
            BannerGroupListVo bannerGroupListVo = new BannerGroupListVo();
            bannerGroupListVo.setId(row.getId());
            bannerGroupListVo.setGroupName(row.getGroupName());
            bannerGroupListVo.setBannerVoList(row.getShowBannerContent());
            returnObject.add(bannerGroupListVo);
        }
        return returnObject;
    }


    /**
     * 查询banner组列表
     *
     * @param dto banner组
     * @return banner组
     */
    @Override
    public TableDataInfo<BannerGroupVo> queryPageList(BannerGroupDto dto) {
        LambdaQueryWrapper<BannerGroup> lqw = buildQueryWrapper(dto);
        IPage<BannerGroupVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        if (result.getRecords() != null && result.getRecords().size() > 0) {
            result.getRecords().forEach(a -> {
                a.setShowTime(DateUtil.format(a.getStartTime(), "yyyy/MM/dd") + "-" + DateUtil.format(a.getEndTime(), "yyyy/MM/dd"));
                String[] split = a.getBannerContent().split(",");
                List<Long> ids = Arrays.asList(Convert.toLongArray(split));
                if (ids.size() > 0) {
                    List<BannerVo> bannerVos = iBannerService.searchByIds(ids);
                    a.setShowBannerContent(bannerVos);
                    a.setBannerNumber(bannerVos.size());
                }
            });
        }
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<BannerGroup> buildQueryWrapper(BannerGroupDto dto) {
        LambdaQueryWrapper<BannerGroup> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getId() != null, BannerGroup::getId, dto.getId());
        lqw.in((dto.getBannerGroupIds() != null && dto.getBannerGroupIds().size() > 0), BannerGroup::getId, dto.getBannerGroupIds());
        lqw.eq(StringUtils.isNotBlank(dto.getApplicationType()), BannerGroup::getApplicationType, dto.getApplicationType());
        lqw.like(StringUtils.isNotBlank(dto.getGroupName()), BannerGroup::getGroupName, dto.getGroupName());
        lqw.le(dto.getStartTime() != null, BannerGroup::getStartTime, DateUtils.getBeginOfDay(dto.getStartTime()));
        lqw.ge(dto.getEndTime() != null, BannerGroup::getEndTime, DateUtils.getBeginOfDay(dto.getEndTime()));
        lqw.eq(StringUtils.isNotBlank(dto.getStatus()), BannerGroup::getStatus, dto.getStatus());
        lqw.orderByDesc(BannerGroup::getCreateTime);
        return lqw;
    }

    /**
     * 新增banner组
     *
     * @param bo banner组
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R insertByBo(BannerGroupBo bo) {
        BannerGroup add = BeanUtil.toBean(bo, BannerGroup.class);
        R<String> stringR = validEntityBeforeSave(add);
        if (stringR.getCode() == R.FAIL) {
            return stringR;
        }
        // 状态，默认是待启用
        add.setStatus("0");
        add.setBannerNumber(add.getBannerContent().split(",").length);
        add.setBannerIcon(stringR.getData());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 修改banner组
     *
     * @param bo banner组
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateByBo(BannerGroupBo bo) {
        BannerGroup update = BeanUtil.toBean(bo, BannerGroup.class);
        R<String> stringR = validEntityBeforeSave(update);
        if (stringR.getCode() == R.FAIL) {
            return stringR;
        }
        update.setBannerNumber(update.getBannerContent().split(",").length);
        update.setBannerIcon(stringR.getData());
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
     * @return
     */
    private R<String> validEntityBeforeSave(BannerGroup entity) {
        // 判断应用场景类型是否正确
        String[] typeList = new String[]{"0", "1"};
        Optional<String> applicationTypePlatformAnyAny = Arrays.stream(typeList).filter(a -> a.equals(entity.getApplicationType())).findAny();
        if (!applicationTypePlatformAnyAny.isPresent()) {
            return R.fail("应用系统类型错误");
        }
        //判断个人中心banner组是否启用
        if (entity.getApplicationType().equals("1") && entity.getId() == null && entity.getStatus().equals("1")) {
            // 查询当前新增到个人中心中的banner组有多少个
            LambdaQueryWrapper<BannerGroup> count = Wrappers.<BannerGroup>lambdaQuery()
                .eq(BannerGroup::getApplicationType, "1")
                .eq(BannerGroup::getStatus, "1");
            Long aLong = baseMapper.selectCount(count);
            if (aLong > 0) {
                return R.fail("个人中心只能有一个已上架的banner组");
            }
        }
        String tempIconUrl = null;
        // 校验banner内容，格式为json格式
        if (StringUtils.isNotBlank(entity.getBannerContent())) {
            String[] contentArr = entity.getBannerContent().split(",");
            if (contentArr.length == 0) {
                return R.fail("至少添加1个banner");
            }
            if (contentArr.length > 5) {
                return R.fail("至多只能添加5个banner");
            }
            // 判断添加的banner中是否存在重复的
            List<String> collect = Arrays.stream(contentArr).distinct().collect(Collectors.toList());
            if (collect.size() != contentArr.length) {
                return R.fail("banner中不能存在重复的");
            }
            List<String> ids = Arrays.asList(contentArr);
            for (int i = 0; i < ids.size(); i++) {
                // banner id
                Long bannerId = Convert.toLong(ids.get(i));
                BannerVo bannerVos = iBannerService.queryById(bannerId);
                // 判断bannerId是否存在
                if (ObjectUtil.isNull(bannerVos)) {
                    return R.fail("添加的banner不存在");
                }
                if (i == 0) {
                    tempIconUrl = bannerVos.getBannerIcon();
                }
            }
        }
        return R.ok("", tempIconUrl);
    }

    /**
     * 删除bannerGroup配置
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

    /**
     * 操作上下架
     *
     * @return 结果
     */
    @Override
    public R operation(IdNameTypeDicDto<Long> idNameTypeDicDto, Long userId) {
        // 数据存在 我们开始进行数据更新
        Integer[] typeList = new Integer[]{1, 2};
        Optional<Integer> operationPlatformAny = Arrays.stream(typeList).filter(a -> a.equals(idNameTypeDicDto.getType())).findAny();
        if (!operationPlatformAny.isPresent()) {
            return R.fail("参数错误");
        }
        if (StringUtils.equals("1", Convert.toStr(idNameTypeDicDto.getType()))) {
            // APP配置-banner组配置：个人中心应用场景：
            // 1、未启用过个人中心banner组，点击启用直接启用成功
            // 2、有已启用的个人中心类型的banner组，点击启用，给出对应提示，提示意思大致是：已经有启用的个人中心banner组了
            LambdaQueryWrapper<BannerGroup> eq = Wrappers.<BannerGroup>lambdaQuery().eq(BannerGroup::getId, idNameTypeDicDto.getId());
            BannerGroup bannerGroup = baseMapper.selectOne(eq);
            if (bannerGroup.getApplicationType().equals("1")) {
                // 查询当前上线到个人中心中的banner组有多少个
                LambdaQueryWrapper<BannerGroup> count = Wrappers.<BannerGroup>lambdaQuery().eq(BannerGroup::getApplicationType, "1")
                    .eq(BannerGroup::getStatus, "1");
                Long aLong = baseMapper.selectCount(count);
                if (aLong > 0) {
                    return R.fail("个人中心只能有一个banner组启用");
                }
            }
        }
        // 执行更新操作
        var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper)
            .eq(BannerGroup::getId, idNameTypeDicDto.getId())
            .set(BannerGroup::getStatus, idNameTypeDicDto.getType());
        boolean update = updateChainWrapper.update();
        if (update) {
            return R.ok("修改成功");
        }
        return R.fail("数据修改失败");
    }
}


