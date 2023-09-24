package com.euler.risk.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.risk.domain.bo.BlacklistBo;
import com.euler.risk.domain.dto.BlacklistDto;
import com.euler.risk.domain.entity.Blacklist;
import com.euler.risk.domain.vo.BlacklistVo;
import com.euler.risk.mapper.BlacklistMapper;
import com.euler.risk.service.IBlacklistService;
import com.euler.sdk.api.RemoteMemberService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 黑名单Service业务层处理
 *
 * @author euler
 * @date 2022-08-23
 */
@RequiredArgsConstructor
@Service
public class BlacklistServiceImpl extends ServiceImpl<BlacklistMapper, Blacklist> implements IBlacklistService {

    private final BlacklistMapper baseMapper;
    @DubboReference
    private RemoteMemberService remoteMemberService;

    /**
     * 查询黑名单列表
     *
     * @param dto 黑名单
     * @return 黑名单
     */
    @Override
    public TableDataInfo<BlacklistVo> queryPageList(BlacklistDto dto) {
        // 获取身份证的随机秘钥
        byte[] key = remoteMemberService.getIdCardAesKey();
        AES aes = SecureUtil.aes(key);

        if (StringUtils.equals("2", dto.getType()) && StringUtils.isNotBlank(dto.getTarget())) {
            // 身份证号检索, 需要加密
            String encrypt = aes.encryptBase64(dto.getTarget());
            dto.setTarget(encrypt);
        }

        // 查询设备信息，需要关联设备表
        QueryWrapper<Blacklist> lqw = buildQueryWrapper(dto);
        Page<BlacklistVo> result = baseMapper.selectBlackList(dto.build(), lqw);
        if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            result.getRecords().forEach(a -> {
                if (StringUtils.equals("2", a.getType())) {
                    // 身份证解密
                    String encrypt = aes.decryptStr(a.getTarget());
                    // 身份证号需要脱敏展示
                    a.setTarget(DesensitizedUtil.idCardNum(encrypt, 6, 3));
                }

                // 封停周期, 封号截止时间等于2194-03-05 00:00:00 默认为永久封禁
                String endTime = DateUtil.format(a.getEndTime(), "yyyy-MM-dd HH:mm:ss");
                if (StringUtils.equals("2194-03-05 00:00:00", endTime)) {
                    a.setBanTime("永久");
                } else {
                    a.setBanTime(DateUtil.format(a.getStartTime(), "yyyy/MM/dd") + "-" + DateUtil.format(a.getEndTime(), "yyyy/MM/dd"));
                }
            });
        }
        return TableDataInfo.build(result);
    }

    private QueryWrapper<Blacklist> buildQueryWrapper(BlacklistDto dto) {
        QueryWrapper<Blacklist> lqw = Wrappers.query();
        lqw.eq(StringUtils.isNotBlank(dto.getType()), "b.type", dto.getType());
        lqw.eq(StringUtils.isNotBlank(dto.getTarget()), "target", dto.getTarget());
        lqw.eq(StringUtils.isNotBlank(dto.getBanType()), "b.ban_type", dto.getBanType());
        lqw.le(StringUtils.isNotBlank(dto.getStartTime()), "b.start_time", DateUtils.getBeginOfDay(dto.getStartTime()));
        lqw.ge(StringUtils.isNotBlank(dto.getEndTime()), "b.end_time", DateUtils.getEndOfDay(dto.getEndTime()));
        lqw.eq("b.del_flag", "0");
        lqw.orderByDesc("b.id");
        return lqw;
    }

    /**
     * 新增黑名单
     *
     * @param bo 黑名单
     * @return 结果
     */
    @Override
    public R insertByBo(BlacklistBo bo) {
        // 删除之前的数据
        LambdaQueryWrapper<Blacklist> eq = Wrappers.<Blacklist>lambdaQuery().eq(Blacklist::getType, bo.getType())
            .eq(Blacklist::getTarget, bo.getTarget());
        List<BlacklistVo> blacklistVos = baseMapper.selectVoList(eq);
        List<Integer> ids = blacklistVos.stream().map(BlacklistVo::getId).collect(Collectors.toList());
        if (ids.size() > 0) {
            baseMapper.deleteBatchIds(ids);
        }
        // 执行新增
        Blacklist add = BeanUtil.toBean(bo, Blacklist.class);
        add.setStartTime(DateUtil.parse(DateUtils.getBeginOfDay(bo.getStartTime())));
        add.setEndTime(DateUtil.parse(DateUtils.getEndOfDay(bo.getEndTime())));
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            // 新增成功之后删除缓存值
            deleteRedisKey(bo.getType(), bo.getTarget());
            return R.ok();
        }
        return R.fail();
    }

    private void deleteRedisKey(String type, String target) {
        String deleteKey = "";
        switch (type) {
            case "1":
                deleteKey = Constants.RISK_KEY + "black:mobile:" + target;
                break;
            case "2":
                deleteKey = Constants.RISK_KEY + "black:id_card_no:" + target;
                break;
            case "3":
                deleteKey = Constants.RISK_KEY + "black:ip:" + target;
                break;
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case "10":
                deleteKey = Constants.RISK_KEY + "black:device:" + target;
                break;
        }
        if (!deleteKey.equals("")) {
            RedisUtils.deleteObject(deleteKey);
        }
    }


    /**
     * 批量删除黑名单
     *
     * @param ids 需要删除的黑名单主键
     * @return 结果
     */
    @Override
    public R deleteWithValidByIds(Collection<Integer> ids, Boolean isValid) {
        // 按照id进行删除
        LambdaQueryWrapper<Blacklist> in = Wrappers.<Blacklist>lambdaQuery().in(Blacklist::getId, ids);
        List<BlacklistVo> blacklistVos = baseMapper.selectVoList(in);
        if (blacklistVos.size() > 0) {
            blacklistVos.forEach(a -> {
                deleteRedisKey(a.getType(), a.getTarget());
            });
            baseMapper.deleteBatchIds(ids);
        }
        return R.ok();
    }

    /**
     * 根据指定参数获取最后一条的黑名单数据
     *
     * @return
     */
    @Override
    public BlacklistVo queryByParams(String param) {
        // 判断非空
        if (StringUtils.isBlank(param)) {
            return new BlacklistVo();
        }
        LambdaQueryWrapper<Blacklist> last = Wrappers.<Blacklist>lambdaQuery()
            .eq(Blacklist::getTarget, param)
            .orderByDesc(Blacklist::getId)
            .last("limit 1");
        BlacklistVo blacklistVo = baseMapper.selectVoOne(last);
        // 判断是否为空
        if (blacklistVo == null || blacklistVo.getId() == null) {
            return new BlacklistVo();
        }
        return blacklistVo;
    }

}
