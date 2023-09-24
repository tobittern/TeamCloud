package com.euler.risk.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.domain.R;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.risk.api.domain.LoginConfigVo;
import com.euler.risk.domain.dto.LoginConfigDto;
import com.euler.risk.domain.entity.LoginConfig;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;
import com.euler.risk.domain.bo.LoginConfigBo;
import com.euler.risk.mapper.LoginConfigMapper;
import com.euler.risk.service.ILoginConfigService;

import java.util.*;

/**
 * 登录配置Service业务层处理
 *
 * @author euler
 * @date 2022-08-23
 */
@RequiredArgsConstructor
@Service
public class LoginConfigServiceImpl extends ServiceImpl<LoginConfigMapper, LoginConfig> implements ILoginConfigService {

    private final LoginConfigMapper baseMapper;

    /**
     * 查询登录配置详细信息
     *
     * APP, 根据平台信息（1:Android 2:ios 3:h5），查询登录配置详情
     * SDK单游戏配置, 需要和全局的配置信息取交集；没有配置的话，默认取SDK全局配置
     *
     * @param dto 登录配置
     * @return 登录配置
     */
    @Override
    public LoginConfigVo queryInfo(LoginConfigDto dto) {
        LoginConfigVo vo = new LoginConfigVo();
        if (dto.getPlatformType() == null) {
            throw new ServiceException("平台标识的参数缺失");
        }
        // app全局登录配置
        if (dto.getPlatform() == null) {
            throw new ServiceException("平台参数缺失");
        }
        // SDK单游戏配置
        if (StringUtils.equals("1", dto.getPlatformType())) {
            // 查询SDK全局登录配置
            List<LoginConfigVo> sdkGlobalList = querySdkGlobalList(dto);
            // 全局的登录配置信息
            LoginConfigVo globalVo = sdkGlobalList.get(0);

            if (dto.getGameId() == null || dto.getGameId() == 0) {
                // 没有配置过该游戏的话，默认取SDK全局配置
                vo = BeanUtil.toBean(globalVo, LoginConfigVo.class);
            } else {

                // 查询单个游戏的登录配置，理论上该平台下该游戏至多只有一条数据。
                List<LoginConfigVo> sdkList = querySdkList(dto);
                if (sdkList == null || sdkList.size() == 0) {
                    // 没有配置过该游戏的话，默认取SDK全局配置
                    vo = BeanUtil.toBean(globalVo, LoginConfigVo.class);
                } else {
                    // SDK单游戏的登录配置信息
                    LoginConfigVo sdkVo = sdkList.get(0);

                    //            List<String> globalList = Arrays.asList(globalVo.getMobileLogin(), globalVo.getIdLogin(), globalVo.getCaptchaLogin(), globalVo.getPasswordLogin());
                    //            List<String> gameList = Arrays.asList(sdkVo.getMobileLogin(), sdkVo.getIdLogin(), sdkVo.getCaptchaLogin(), sdkVo.getPasswordLogin());
                    //            // SDK取交集
                    //            Object[] intrList = CollUtil.intersection(globalList, gameList).toArray();

                    // 取交集后的最新配置设置到实体里返回
                    sdkVo.setMobileLogin(StringUtils.equals(globalVo.getMobileLogin(), sdkVo.getMobileLogin()) ? sdkVo.getMobileLogin() : "1");
                    sdkVo.setIdLogin(StringUtils.equals(globalVo.getIdLogin(), sdkVo.getIdLogin()) ? sdkVo.getIdLogin() : "1");
                    sdkVo.setCaptchaLogin(StringUtils.equals(globalVo.getCaptchaLogin(), sdkVo.getCaptchaLogin()) ? sdkVo.getCaptchaLogin() : "1");
                    sdkVo.setPasswordLogin(StringUtils.equals(globalVo.getPasswordLogin(), sdkVo.getPasswordLogin()) ? sdkVo.getPasswordLogin() : "1");

                    vo = BeanUtil.toBean(sdkVo, LoginConfigVo.class);
                }
            }
        } else {
            List<LoginConfigVo> appGlobalList = queryAppGlobalList(dto);
            vo = appGlobalList.get(0);
        }

        return vo;
    }

    /**
     * 查询SDK全局的登录配置列表
     *
     * @param dto 登录配置
     * @return 登录配置
     */
    @Override
    public List<LoginConfigVo> querySdkGlobalList(LoginConfigDto dto) {
        // 平台标识(1:sdk)
        dto.setPlatformType("1");
        // 是否是全局配置（1:是）
        dto.setGlobalConfig("1");
        LambdaQueryWrapper<LoginConfig> lqw = buildQueryWrapper(dto);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询APP全局的登录配置列表
     *
     * @param dto 登录配置
     * @return 登录配置
     */
    @Override
    public List<LoginConfigVo> queryAppGlobalList(LoginConfigDto dto) {
        // 平台标识(4:九区玩家app)
        dto.setPlatformType("4");
        // 是否是全局配置（1:是）
        dto.setGlobalConfig("1");
        LambdaQueryWrapper<LoginConfig> lqw = buildQueryWrapper(dto);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询SDK单个游戏的登录配置列表
     *
     * @param dto 登录配置
     * @return 登录配置
     */
    @Override
    public List<LoginConfigVo> querySdkList(LoginConfigDto dto) {
        // 平台标识(1:sdk)
        dto.setPlatformType("1");
        // 是否是全局配置（0:不是）
        dto.setGlobalConfig("0");
        LambdaQueryWrapper<LoginConfig> lqw = buildQueryWrapper(dto);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询SDK单个游戏的登录配置列表
     *
     * @param dto 登录配置
     * @return 登录配置
     */
    @Override
    public TableDataInfo<LoginConfigVo> queryPageList(LoginConfigDto dto) {
        // 平台标识(1:sdk)
        dto.setPlatformType("1");
        // 是否是全局配置（0:不是）
        dto.setGlobalConfig("0");
        LambdaQueryWrapper<LoginConfig> lqw = buildQueryWrapper(dto);
        Page<LoginConfigVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<LoginConfig> buildQueryWrapper(LoginConfigDto dto) {
        LambdaQueryWrapper<LoginConfig> lqw = Wrappers.lambdaQuery();
        // 平台标识(1:sdk 4:九区玩家app)
        lqw.eq(LoginConfig::getPlatformType, dto.getPlatformType());
        // 是否是全局配置（0:不是 1:是）
        lqw.eq(LoginConfig::getGlobalConfig, dto.getGlobalConfig());

        // SDK单个游戏的登录配置，才拼下面的条件
        if (StringUtils.equals("0", dto.getGlobalConfig())) {
            lqw.eq(dto.getGameId() != null, LoginConfig::getGameId, dto.getGameId());
            lqw.likeRight(StringUtils.isNotBlank(dto.getGameName()), LoginConfig::getGameName, dto.getGameName());
            lqw.eq(StringUtils.isNotBlank(dto.getPlatform()), LoginConfig::getPlatform, dto.getPlatform());
        }

        // APP登录配置
        if (StringUtils.equals("4", dto.getPlatformType())) {
            lqw.eq(StringUtils.isNotBlank(dto.getPlatform()), LoginConfig::getPlatform, dto.getPlatform());
        }
        return lqw;
    }

    /**
     * 新增登录配置
     *
     * @param bo 登录配置
     * @return 结果
     */
    @Override
    public R insertByBo(LoginConfigBo bo) {
        //        // 查询sdk的全局配置数据
        //        LambdaQueryWrapper<LoginConfig> lqw = Wrappers.lambdaQuery();
        //        lqw.eq(LoginConfig::getPlatformType, "1");
        //        lqw.eq(LoginConfig::getGlobalConfig, "1");
        //        List<LoginConfigVo> list = baseMapper.selectVoList(lqw);
        //        if (list != null && list.size() > 1) {
        //            return R.fail("SDK全局配置数据有多条");
        //        } else if (list == null || list.size() == 0) {
        //            return R.fail("没有配置SDK全局数据");
        //        }
        //        /**
        //         * 判断全局配置里：一键登录/游客登录/验证码登录/账号密码登录是否关闭
        //         * 如果关闭的话，单游戏配置里该设置为不可用
        //         */
        //        LoginConfigVo vo = list.get(0);
        //        // 一键登录
        //        bo.setMobileLogin(StringUtils.equals("1", vo.getMobileLogin()) ? "2" : bo.getMobileLogin());
        //        // 游客登录
        //        bo.setIdLogin(StringUtils.equals("1", vo.getIdLogin()) ? "2" : bo.getIdLogin());
        //        // 验证码登录
        //        bo.setCaptchaLogin(StringUtils.equals("1", vo.getCaptchaLogin()) ? "2" : bo.getCaptchaLogin());
        //        // 账号密码登录
        //        bo.setPasswordLogin(StringUtils.equals("1", vo.getPasswordLogin()) ? "2" : bo.getPasswordLogin());
        LoginConfig add = BeanUtil.toBean(bo, LoginConfig.class);
        // 保存前的数据校验
        String result = validEntityBeforeSave(add, true);
        if (!result.equals("success")) {
            return R.fail(result);
        }
        int i = baseMapper.insert(add);
        if (i > 0) {
            bo.setId(add.getId());
        }
        return R.ok("新增成功");
    }

    /**
     * 修改登录配置
     *
     * @param bo 登录配置
     * @return 结果
     */
    @Override
    public R updateByBo(LoginConfigBo bo) {
        LoginConfig update = BeanUtil.toBean(bo, LoginConfig.class);
        // 保存前的数据校验
        String result = validEntityBeforeSave(update, false);
        if (!result.equals("success")) {
            return R.fail(result);
        }

        // 九区玩家app的登录配置修改
        if (StringUtils.equals("1", bo.getGlobalConfig()) && StringUtils.equals("4", bo.getPlatformType())) {

            // 九区玩家app的登录配置列表只有3条数据
            List<LoginConfigVo> appConfigList = bo.getAppConfigList();
            if (appConfigList == null || appConfigList.size() != 3) {
                return R.fail("九区玩家app的登录配置列表没有3条数据");
            }

            appConfigList.stream().forEach(app -> {
                if (StringUtils.equals("3", app.getPlatform())) {
                    // h5时，一键登录不可用
                    app.setMobileLogin("2");
                }

                // app的全局配置，没有游客登录
                var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper)
                    .eq(LoginConfig::getGlobalConfig, "1")
                    .eq(LoginConfig::getPlatformType, "4")
                    .eq(LoginConfig::getPlatform, app.getPlatform())
                    .set(LoginConfig::getMobileLogin, app.getMobileLogin())
                    .set(LoginConfig::getCaptchaLogin, app.getCaptchaLogin())
                    .set(LoginConfig::getPasswordLogin, app.getPasswordLogin())
                    .set(LoginConfig::getUpdateTime, new Date())
                    .set(LoginConfig::getUpdateBy, LoginHelper.getUserId());
                // 更新app配置信息
                updateChainWrapper.update();
            });
        } else {
            // sdk的全局登录配置修改
            if (StringUtils.equals("1", bo.getGlobalConfig())) {
                // 全局配置修改
                var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper)
                    .eq(LoginConfig::getGlobalConfig, "1")
                    .eq(LoginConfig::getPlatformType, "1")
                    .set(LoginConfig::getMobileLogin, update.getMobileLogin())
                    .set(LoginConfig::getIdLogin, update.getIdLogin())
                    .set(LoginConfig::getCaptchaLogin, update.getCaptchaLogin())
                    .set(LoginConfig::getPasswordLogin, update.getPasswordLogin())
                    .set(LoginConfig::getUpdateTime, new Date())
                    .set(LoginConfig::getUpdateBy, LoginHelper.getUserId());
                // 更新sdk全局配置信息
                updateChainWrapper.update();
            } else {
                // 单个游戏项目的配置
                baseMapper.updateById(update);
            }
        }

        return R.ok("修改成功");
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     * @return 校验结果
     */
    private String validEntityBeforeSave(LoginConfig entity, Boolean isAdd) {

        // 关于一键登录/游客登录/验证码登录/账号密码登录全部关闭的校验，目前没有

        if (isAdd) {
            // 平台标识（1:sdk）校验
            if (!StringUtils.equals("1", entity.getPlatformType())) {
                return "平台标识错误，请确认";
            }
            // 是否是全局配置（0:不是）:单个游戏配置
            if (!StringUtils.equals("0", entity.getGlobalConfig())) {
                return "是否是全局配置错误，请确认";
            }
            // 平台（1:Android 2:ios 3:h5）校验
            String[] typeList = new String[]{"1", "2", "3"};
            Optional<String> platformAny = Arrays.stream(typeList).filter(a -> a.equals(entity.getPlatform())).findAny();
            if (!platformAny.isPresent()) {
                return "平台（1:Android 2:ios 3:h5）参数错误";
            }
            // 校验某平台下该游戏是否配置过
            LambdaQueryWrapper<LoginConfig> lqw = Wrappers.lambdaQuery();
            lqw.eq(LoginConfig::getPlatformType, "1");
            lqw.eq(LoginConfig::getGlobalConfig, "0");
            lqw.eq(LoginConfig::getGameId, entity.getGameId());
            lqw.eq(LoginConfig::getPlatform, entity.getPlatform());
            lqw.eq(LoginConfig::getDelFlag, "0");
            List<LoginConfigVo> list = baseMapper.selectVoList(lqw);
            if (list != null && list.size() > 0) {
                return "该游戏配置过了";
            }
        } else {
            if (StringUtils.equals("1", entity.getPlatformType())) {
                // 校验SDK全局登录配置/单个游戏配置
                if (entity.getMobileLogin() == null || entity.getIdLogin() == null
                    || entity.getCaptchaLogin() == null || entity.getPasswordLogin() == null) {
                    return "SDK全局的登录配置还没有配置，请确认";
                }

                // SDK单个游戏配置
                if (StringUtils.equals("0", entity.getGlobalConfig())) {
                    // 验证是否传输过来主键
                    if (entity.getId() == null || entity.getId() <= 0) {
                        return "参数缺失";
                    }
                    if (entity.getGameId() == null || entity.getGameId() <= 0 || entity.getGameName() == null) {
                        return "游戏id或名称不能为空";
                    }
                    // 平台（1:Android 2:ios 3:h5）校验
                    String[] typeList = new String[]{"1", "2", "3"};
                    Optional<String> platformAny = Arrays.stream(typeList).filter(a -> a.equals(entity.getPlatform())).findAny();
                    if (!platformAny.isPresent()) {
                        return "平台（1:Android 2:ios 3:h5）参数错误";
                    }
                }
            }
        }

        return "success";
    }

    /**
     * 批量删除登录配置
     *
     * @param ids 需要删除的登录配置主键
     * @return 结果
     */
    @Override
    public R deleteWithValidByIds(Collection<Integer> ids, Boolean isValid) {
        baseMapper.deleteBatchIds(ids);
        return R.ok("删除成功");
    }
}
