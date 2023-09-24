package com.euler.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.system.domain.SdkMainMenu;
import com.euler.system.domain.SdkSubMenu;
import com.euler.system.domain.bo.SdkMainMenuBo;
import com.euler.system.domain.dto.SdkMainMenuDto;
import com.euler.system.domain.vo.SdkMainMenuVo;
import com.euler.system.mapper.SdkMainMenuMapper;
import com.euler.system.mapper.SdkSubMenuMapper;
import com.euler.system.service.ISdkMainMenuService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Collection;
import java.util.Optional;

/**
 * SDK菜单Service业务层处理
 *
 * @author euler
 * @date 2023-03-20
 */
@RequiredArgsConstructor
@Service
public class SdkMainMenuServiceImpl extends ServiceImpl<SdkMainMenuMapper, SdkMainMenu> implements ISdkMainMenuService {

    @Autowired
    private SdkMainMenuMapper baseMapper;

    @Autowired
    private SdkSubMenuMapper subMenuMapper;

    /**
     * 查询SDK菜单
     *
     * @param id SDK菜单主键
     * @return SDK菜单
     */
    @Override
    public SdkMainMenuVo queryById(Integer id) {
        SdkMainMenuVo vo = baseMapper.selectVoById(id);
        if(vo != null) {
            vo.setBadgeF(Convert.toBool(vo.getBadge()));
        }
        return vo;
    }

    /**
     * 查询SDK菜单列表
     *
     * @param dto SDK菜单
     * @return SDK菜单
     */
    @Override
    public TableDataInfo<SdkMainMenuVo> queryPageList(SdkMainMenuDto dto) {
        LambdaQueryWrapper<SdkMainMenu> lqw = buildQueryWrapper(dto);
        Page<SdkMainMenuVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        if(result != null && result.getRecords() != null && result.getRecords().size() > 0) {
            result.getRecords().stream().forEach(a -> {
                a.setBadgeF(Convert.toBool(a.getBadge()));
            });
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询SDK菜单列表
     *
     * @param dto SDK菜单
     * @return SDK菜单
     */
    @Override
    public List<SdkMainMenuVo> queryList(SdkMainMenuDto dto) {
        LambdaQueryWrapper<SdkMainMenu> lqw = buildQueryWrapper(dto);
        List<SdkMainMenuVo> list = baseMapper.selectVoList(lqw);
        if(list != null && list.size() > 0) {
            list.stream().forEach(a -> {
                a.setBadgeF(Convert.toBool(a.getBadge()));
            });
        }
        return list;
    }

    private LambdaQueryWrapper<SdkMainMenu> buildQueryWrapper(SdkMainMenuDto pageDto) {
        LambdaQueryWrapper<SdkMainMenu> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(pageDto.getName()), SdkMainMenu::getName, pageDto.getName());
        lqw.eq(StringUtils.isNotBlank(pageDto.getDictValue()), SdkMainMenu::getDictValue, pageDto.getDictValue());
        lqw.eq(StringUtils.isNotBlank(pageDto.getIsUp()), SdkMainMenu::getIsUp, pageDto.getIsUp());
        lqw.orderByAsc(SdkMainMenu:: getSort);
        return lqw;
    }

    /**
     * 新增SDK菜单
     *
     * @param bo SDK菜单
     * @return 结果
     */
    @Override
    public R insertByBo(SdkMainMenuBo bo) {
        SdkMainMenu add = BeanUtil.toBean(bo, SdkMainMenu.class);
        add.setBadge(Convert.toStr(bo.getBadgeF()));
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return R.ok("新增成功！");
    }

    /**
     * 修改SDK菜单
     *
     * @param bo SDK菜单
     * @return 结果
     */
    @Override
    public R updateByBo(SdkMainMenuBo bo) {
        SdkMainMenu update = BeanUtil.toBean(bo, SdkMainMenu.class);
        update.setBadge(Convert.toStr(bo.getBadgeF()));
        int i = baseMapper.updateById(update);
        if( i> 0) {
            return R.ok("修改成功！");
        }
        return R.fail("修改失败！");
    }

    /**
     * 操作上下架
     *
     * @param idNameTypeDicDto 字典Dto
     * @param userId           用户id
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
            .eq(SdkMainMenu::getId, idNameTypeDicDto.getId())
            .set(SdkMainMenu::getIsUp, idNameTypeDicDto.getType());
        boolean update = updateChainWrapper.update();
        if (update) {
            return R.ok("修改成功");
        }
        return R.fail("修改失败");
    }

    /**
     * 批量删除SDK菜单
     *
     * @param ids 需要删除的SDK菜单主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public R deleteWithValidByIds(Collection<Integer> ids) {
        // 删除SDK菜单
        int i = baseMapper.deleteBatchIds(ids);
        if(i > 0) {
            // 删除SDK子菜单
            subMenuMapper.delete(new LambdaQueryWrapper<SdkSubMenu>().in(SdkSubMenu::getMainMenuId, ids));
            return R.ok("删除成功");
        }
        return R.fail("删除失败");
    }

}

