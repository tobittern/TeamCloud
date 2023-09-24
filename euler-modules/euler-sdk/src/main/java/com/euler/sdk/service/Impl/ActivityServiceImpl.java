package com.euler.sdk.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.core.enums.UserTypeEnum;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.sdk.domain.bo.ActivityBo;
import com.euler.sdk.domain.bo.GameUseRecordBo;
import com.euler.sdk.domain.dto.ActivityDto;
import com.euler.sdk.domain.entity.Activity;
import com.euler.sdk.domain.vo.ActivityVo;
import com.euler.sdk.enums.GameUseRecordTypeEnum;
import com.euler.sdk.mapper.ActivityMapper;
import com.euler.sdk.service.IActivityService;
import com.euler.sdk.service.IGameUseRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 活动Service业务层处理
 *
 * @author euler
 * @date 2022-03-29
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ActivityServiceImpl implements IActivityService {

    private final ActivityMapper baseMapper;
    @Autowired
    private IGameUseRecordService iGameUseRecordService;

    /**
     * 查询活动
     *
     * @param id 活动主键
     * @return 活动
     */
    @Override
    public ActivityVo queryById(Integer id) {
        ActivityVo activityVo = baseMapper.selectVoById(id);
        List<Integer> ids = new ArrayList<>();
        ids.add(id);
        List<OpenGameDubboVo> openGameDubboVos = iGameUseRecordService.selectGameNameByIds(ids, GameUseRecordTypeEnum.ACTIVITY_CREATION.getCode());
        if (openGameDubboVos != null && openGameDubboVos.size() > 0) {
            // 判断哪一个包含了这款游戏
            List<KeyValueDto> newList = openGameDubboVos.stream().filter(e -> e.getRelationList().contains(id)).map(e -> new KeyValueDto(e.getId(), e.getGameName())).collect(Collectors.toList());
            // 添加一个过滤 将获取的游戏列表按照id进行一下归类
            activityVo.setGameOriented(newList);
        }
        return activityVo;
    }

    /**
     * 查询活动列表
     *
     * @param
     * @return 活动
     */
    @Override
    public TableDataInfo<ActivityVo> queryPageList(ActivityDto dto) {
        // 判断用户类型 如果是sdk用户 只展示出上线的活动
        UserTypeEnum userType = LoginHelper.getUserType();
        if (userType.getUserTypeNum().equals("1")) {
            dto.setIsOnline(1);
        }
        LambdaQueryWrapper<Activity> lqw = buildQueryWrapper(dto);
        Page<ActivityVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        List<Integer> ids = result.getRecords().stream().map(ActivityVo::getId).collect(Collectors.toList());
        List<OpenGameDubboVo> openGameDubboVos = iGameUseRecordService.selectGameNameByIds(ids, GameUseRecordTypeEnum.ACTIVITY_CREATION.getCode());
        // 循环获取数据之后将游戏名累加到元数据中
        result.getRecords().forEach(row -> {
            if (openGameDubboVos != null && openGameDubboVos.size() > 0) {
                // 判断哪一个包含了这款游戏
                List<KeyValueDto> newList = openGameDubboVos.stream().filter(e -> e.getRelationList().contains(row.getId())).map(e -> new KeyValueDto(e.getId(), e.getGameName())).collect(Collectors.toList());
                // 添加一个过滤 将获取的游戏列表按照id进行一下归类
                row.setGameOriented(newList);
            }
        });
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<Activity> buildQueryWrapper(ActivityDto dto) {
        LambdaQueryWrapper<Activity> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getUserId() != null, Activity::getUserId, dto.getUserId());
        lqw.likeRight(StringUtils.isNotBlank(dto.getName()), Activity::getName, dto.getName());
        lqw.eq(dto.getIsOnline() != null, Activity::getIsOnline, dto.getIsOnline());
        lqw.orderByDesc(Activity::getId);
        return lqw;
    }

    /**
     * 新增活动
     *
     * @param bo 活动
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R insertByBo(ActivityBo bo) {
        Activity add = BeanUtil.toBean(bo, Activity.class);
        String s = validEntityBeforeSave(add);
        if (!s.equals("success")) {
            return R.fail(s);
        }
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            // 针对绑定的游戏进行一下处理
            GameUseRecordBo gameUseRecordBo = new GameUseRecordBo();
            gameUseRecordBo.setPartyAId(add.getId());
            gameUseRecordBo.setPartyBId(bo.getGameOriented());
            gameUseRecordBo.setType(GameUseRecordTypeEnum.ACTIVITY_CREATION.getCode());
            iGameUseRecordService.insertGameUseRecord(gameUseRecordBo);
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 修改活动
     *
     * @param bo 活动
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateByBo(ActivityBo bo) {
        Activity update = BeanUtil.toBean(bo, Activity.class);
        String s = validEntityBeforeSave(update);
        if (!s.equals("success")) {
            return R.fail(s);
        }
        int i = baseMapper.updateById(update);
        if (i > 0) {
            // 更新游戏关联信息数据
            GameUseRecordBo gameUseRecordBo = new GameUseRecordBo();
            gameUseRecordBo.setPartyAId(update.getId());
            gameUseRecordBo.setPartyBId(bo.getGameOriented());
            gameUseRecordBo.setType(GameUseRecordTypeEnum.ACTIVITY_CREATION.getCode());
            iGameUseRecordService.updateRGameUseRecord(gameUseRecordBo);
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private String validEntityBeforeSave(Activity entity) {
        //TODO 做一些数据校验,如唯一约束
        return "success";
    }

    /**
     * 批量删除活动
     *
     * @param ids 需要删除的活动主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        int i = baseMapper.deleteBatchIds(ids);
        if (i > 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 操作上下架
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
        // 执行更新操作
        var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper)
            .eq(Activity::getId, idNameTypeDicDto.getId())
            .set(Activity::getIsOnline, idNameTypeDicDto.getType());
        boolean update = updateChainWrapper.update();
        if (update) {
            return R.ok("修改成功");
        }
        return R.fail("数据修改失败");
    }

}
