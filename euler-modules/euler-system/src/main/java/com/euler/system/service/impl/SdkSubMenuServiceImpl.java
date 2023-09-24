package com.euler.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.system.domain.SdkSubMenu;
import com.euler.system.domain.bo.SdkSubMenuBo;
import com.euler.system.domain.dto.SdkSubMenuDto;
import com.euler.system.domain.vo.SdkSubMenuVo;
import com.euler.system.mapper.SdkSubMenuMapper;
import com.euler.system.service.ISdkSubMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * SDK子菜单Service业务层处理
 *
 * @author euler
 * @date 2023-03-20
 */
@RequiredArgsConstructor
@Service
public class SdkSubMenuServiceImpl extends ServiceImpl<SdkSubMenuMapper, SdkSubMenu> implements ISdkSubMenuService {

    @Autowired
    private SdkSubMenuMapper baseMapper;

    /**
     * 查询SDK子菜单
     *
     * @param dto SDK子菜单
     * @return SDK子菜单
     */
    @Override
    public SdkSubMenuVo queryByDto(SdkSubMenuDto dto) {
        SdkSubMenuVo vo = baseMapper.selectVoOne(Wrappers.<SdkSubMenu>lambdaQuery().eq(SdkSubMenu::getId, dto.getId()).eq(SdkSubMenu::getName, dto.getName()).last("limit 1"));
        if(vo != null) {
            vo.setBadgeF(Convert.toBool(vo.getBadge()));
        }
        return vo;
    }

    /**
     * 查询SDK子菜单列表
     *
     * @param dto SDK子菜单
     * @return SDK子菜单集合
     */
    @Override
    public TableDataInfo<SdkSubMenuVo> queryPageList(SdkSubMenuDto dto) {
        LambdaQueryWrapper<SdkSubMenu> lqw = buildQueryWrapper(dto);
        Page<SdkSubMenuVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        if(result != null && result.getRecords() != null && result.getRecords().size() > 0) {
            result.getRecords().stream().forEach(a -> {
                a.setBadgeF(Convert.toBool(a.getBadge()));
            });
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询SDK子菜单列表
     *
     * @param dto SDK子菜单
     * @return SDK子菜单集合
     */
    @Override
    public List<SdkSubMenuVo> queryList(SdkSubMenuDto dto) {
        LambdaQueryWrapper<SdkSubMenu> lqw = buildQueryWrapper(dto);
        List<SdkSubMenuVo> list = baseMapper.selectVoList(lqw);
        if(list != null  && list.size() > 0) {
            list.stream().forEach(a -> {
                a.setBadgeF(Convert.toBool(a.getBadge()));
            });
        }
        return list;
    }

    private LambdaQueryWrapper<SdkSubMenu> buildQueryWrapper(SdkSubMenuDto dto) {
        LambdaQueryWrapper<SdkSubMenu> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getMainMenuId() != null && dto.getMainMenuId() > 0, SdkSubMenu::getMainMenuId, dto.getMainMenuId());
        lqw.orderByAsc(SdkSubMenu::getSort);
        return lqw;
    }

    /**
     * 新增SDK子菜单
     *
     * @param bo SDK子菜单
     * @return 结果
     */
    @Override
    public R insertByBo(SdkSubMenuBo bo) {
        SdkSubMenu add = BeanUtil.toBean(bo, SdkSubMenu.class);
        String result = validEntityBeforeSave(add);
        if (!result.equals("success")) {
            return R.fail(result);
        }
        add.setBadge(Convert.toStr(bo.getBadgeF()));
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            return R.ok("新增成功");
        }
        return R.fail("新增失败");
    }

    /**
     * 修改SDK子菜单
     *
     * @param bo SDK子菜单
     * @return 结果
     */
    @Override
    public R updateByBo(SdkSubMenuBo bo) {
        // 验证是否传输过来主键
        if (bo.getId() == null || bo.getId() <= 0 || bo.getName() == null) {
            return R.fail("参数缺失");
        }
        SdkSubMenu update = BeanUtil.toBean(bo, SdkSubMenu.class);
        String result = validEntityBeforeSave(update);
        if (!result.equals("success")) {
            return R.fail(result);
        }
        update.setBadge(Convert.toStr(bo.getBadgeF()));

        LambdaUpdateWrapper<SdkSubMenu> luw = new LambdaUpdateWrapper<>();
        luw.set(StringUtils.isBlank(update.getIcon()), SdkSubMenu::getIcon, update.getIcon());
        luw.set(StringUtils.isBlank(update.getPath()), SdkSubMenu::getPath, update.getPath());
        luw.set(StringUtils.isBlank(update.getSort()), SdkSubMenu::getSort, update.getSort());
        luw.set(StringUtils.isBlank(update.getCode()), SdkSubMenu::getCode, update.getCode());
        luw.set(StringUtils.isBlank(update.getBadge()), SdkSubMenu::getBadge, update.getBadge());
        luw.eq(SdkSubMenu::getId, update.getId());
        luw.eq(SdkSubMenu::getName, update.getName());
        int i = baseMapper.update(update, luw);
        if(i > 0) {
            return R.ok("修改成功");
        }
        return R.fail("修改失败");
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private String validEntityBeforeSave(SdkSubMenu entity){
        if(ObjectUtil.isNull(entity.getMainMenuId()) || entity.getMainMenuId() <= 0){
            return "SDK菜单ID不能为空";
        }
        return "success";
    }

    /**
     * 删除SDK子菜单
     *
     * @param dto 需要删除的SDK子菜单
     * @return 结果
     */
    @Override
    public R deleteSubMenu(SdkSubMenuDto dto) {
        int i = baseMapper.delete(new LambdaQueryWrapper<SdkSubMenu>().eq(SdkSubMenu::getId, dto.getId()).eq(SdkSubMenu::getName, dto.getName()));
        if(i > 0) {
            return R.ok("删除成功");
        }
        return R.fail("删除失败");
    }

}
