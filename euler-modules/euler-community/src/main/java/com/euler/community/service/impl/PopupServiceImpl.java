package com.euler.community.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.utils.ListUtil;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.community.config.CommonCommunityConfig;
import com.euler.community.constant.GitBagConstant;
import com.euler.community.domain.bo.PopupBo;
import com.euler.community.domain.dto.PopupDto;
import com.euler.community.domain.entity.Popup;
import com.euler.community.domain.vo.PopupVo;
import com.euler.community.mapper.PopupMapper;
import com.euler.community.service.IPopupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 弹窗管理Service业务层处理
 *
 * @author euler
 * @date 2022-06-02
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PopupServiceImpl extends ServiceImpl<PopupMapper, Popup> implements IPopupService {

    private final PopupMapper baseMapper;
    @Resource
    private CommonCommunityConfig commonCommunityConfig;

    /**
     * 查询弹窗管理
     *
     * @param id 弹窗管理主键
     * @return 弹窗管理
     */
    @Override
    public PopupVo queryById(Long id) {
        PopupVo vo = baseMapper.selectVoById(id);
        if (ObjectUtil.isNotNull(vo)) {
            setUrlPrefix(vo);
            vo.setShowTime(DateUtil.format(vo.getStartTime(), "yyyy/MM/dd") + "-" + DateUtil.format(vo.getEndTime(), "yyyy/MM/dd"));
        }
        return vo;
    }

    /**
     * 设置前缀
     *
     * @param popupVo
     */
    public void setUrlPrefix(PopupVo popupVo) {
        String yunDomain = commonCommunityConfig.getYunDomain();
        //弹窗图片 popup_icon
        String bannerIcon = popupVo.getPopupIcon();
        popupVo.setPopupIcon(StringUtils.isBlank(bannerIcon) ? null : (bannerIcon.startsWith(GitBagConstant.HTTP) ? bannerIcon : yunDomain + bannerIcon));
    }

    /**
     * 查询弹窗管理列表
     *
     * @param dto 弹窗管理
     * @return 弹窗管理
     */
    @Override
    public TableDataInfo<PopupVo> queryPageList(PopupDto dto) {
        LambdaQueryWrapper<Popup> lqw = buildQueryWrapper(dto);
        Page<PopupVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        //设置的url
        if (result != null && result.getRecords() != null && result.getRecords().size() > 0) {
            result.getRecords().forEach(vo -> {
                setUrlPrefix(vo);
                vo.setShowTime(DateUtil.format(vo.getStartTime(), "yyyy/MM/dd") + "-" + DateUtil.format(vo.getEndTime(), "yyyy/MM/dd"));
            });
        }
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<Popup> buildQueryWrapper(PopupDto dto) {
        LambdaQueryWrapper<Popup> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getId() != null, Popup::getId, dto.getId());
        lqw.eq(dto.getMemberId() != null, Popup::getMemberId, dto.getMemberId());
        lqw.likeRight(StringUtils.isNotBlank(dto.getTitle()), Popup::getTitle, dto.getTitle());
        lqw.eq(dto.getTimes() != null, Popup::getTimes, dto.getTimes());
        lqw.le(dto.getStartTime() != null, Popup::getStartTime, DateUtils.getBeginOfDay(dto.getStartTime()));
        lqw.ge(dto.getEndTime() != null, Popup::getEndTime, DateUtils.getBeginOfDay(dto.getEndTime()));
        lqw.eq(StringUtils.isNotBlank(dto.getJumpUrl()), Popup::getJumpUrl, dto.getJumpUrl());
        lqw.eq(dto.getLevel() != null, Popup::getLevel, dto.getLevel());
        lqw.eq(StringUtils.isNotBlank(dto.getStatus()), Popup::getStatus, dto.getStatus());
        lqw.like(StringUtils.isNotBlank(dto.getPosition()), Popup::getPosition, dto.getPosition());
        // 按照id进行排序
        lqw.orderByDesc(Popup::getId);
        return lqw;
    }

    /**
     * 查询弹窗管理列表
     *
     * @param position
     * @return 弹窗管理
     */
    @Override
    public List<PopupVo> queryList(PopupDto position) {
        String[] popupArr = position.getPosition().split(",");
        if (popupArr == null || popupArr.length == 0)
            throw new ServiceException("弹窗获取失败");


        List<PopupVo> popupVos = new ArrayList<>();
        for (String positionStr : popupArr) {
            String popupKey = Constants.BASE_KEY + "popup:" + positionStr;
            String cacheJson = RedisUtils.getCacheObject(popupKey);
            List<PopupVo> cacheList = new ArrayList<>();
            if (cacheJson == null) {
                cacheList = getPopupList(positionStr);
                RedisUtils.setCacheObject(popupKey, JsonHelper.toJson(cacheList), Duration.ofHours(168L));
            } else {
                cacheList = JsonHelper.toList(cacheJson, PopupVo.class);
            }

            if (cacheList != null && !cacheList.isEmpty())
                popupVos.addAll(cacheList);

        }

        List<PopupVo> newList=popupVos.stream().filter(ListUtil.distinctByKey(PopupVo::getId)).collect(Collectors.toList());
        return newList;

    }


    private List<PopupVo> getPopupList(String positionStr) {

        LambdaQueryWrapper<Popup> lqw =
            Wrappers.<Popup>lambdaQuery()
                .eq(Popup::getStatus, "1")
                .like(StringUtils.isNotBlank(positionStr), Popup::getPosition, positionStr)
                .le(Popup::getStartTime, new Date())
                .ge(Popup::getEndTime, new Date());
        List<PopupVo> popupVos = baseMapper.selectVoList(lqw);
        if (popupVos != null && popupVos.size() > 0) {
            // 设置附件的url
            for (PopupVo vo : popupVos) {
                setUrlPrefix(vo);
            }
        }
        return popupVos;

    }


    /**
     * 新增弹窗管理
     *
     * @param bo 弹窗管理
     * @return 结果
     */
    @Override
    public R insertByBo(PopupBo bo) {
        Popup add = BeanUtil.toBean(bo, Popup.class);
        validEntityBeforeSave(add);
        // 状态，默认是待启用
        add.setStatus("0");
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            // 清空缓存
            for (int i = 1; i < 5; i++) {
                String popupKey = Constants.BASE_KEY + "popup:" + i;
                RedisUtils.deleteObject(popupKey);
            }
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 修改弹窗管理
     *
     * @param bo 弹窗管理
     * @return 结果
     */
    @Override
    public R updateByBo(PopupBo bo) {
        Popup update = BeanUtil.toBean(bo, Popup.class);
        validEntityBeforeSave(update);
        int i = baseMapper.updateById(update);
        if (i > 0) {
            // 清空缓存
            for (int row = 1; row < 5; row++) {
                String popupKey = Constants.BASE_KEY + "popup:" + row;
                RedisUtils.deleteObject(popupKey);
            }
            return R.ok("修改成功");
        }
        return R.fail("修改失败");
    }

    /**
     * 删除弹窗管理
     *
     * @param ids
     * @return 结果
     */
    @Override
    public R deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (baseMapper.deleteBatchIds(ids) > 0) {
            // 清空缓存
            for (int row = 1; row < 5; row++) {
                String popupKey = Constants.BASE_KEY + "popup:" + row;
                RedisUtils.deleteObject(popupKey);
            }
            return R.ok("删除成功");
        }
        return R.fail("删除失败");
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(Popup entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 操作上下线
     *
     * @return 结果
     */
    @Override
    public R operation(IdNameTypeDicDto idNameTypeDicDto, Long userId) {
        // 数据存在 我们开始进行数据更新
        Integer[] typeList = new Integer[]{1, 2};
        Optional<Integer> operationPlatformAny = Arrays.stream(typeList).filter(a -> a.equals(idNameTypeDicDto.getType())).findAny();
        if (!operationPlatformAny.isPresent()) {
            return R.fail("参数错误");
        }
        PopupVo vo = queryById(Convert.toLong(idNameTypeDicDto.getId()));
        if (ObjectUtil.isNull(vo)) {
            return R.fail("数据不存在");
        }
        // 执行更新操作
        var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper)
            .eq(Popup::getId, idNameTypeDicDto.getId())
            .set(Popup::getStatus, idNameTypeDicDto.getType());
        boolean update = updateChainWrapper.update();
        if (update) {
            // 清空缓存
            for (int row = 1; row < 5; row++) {
                String popupKey = Constants.BASE_KEY + "popup:" + row;
                RedisUtils.deleteObject(popupKey);
            }
            return R.ok("修改成功");
        }
        return R.fail("数据修改失败");
    }

}
