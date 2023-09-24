package com.euler.sdk.service.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.MemberConstants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.sdk.api.domain.GoodsVo;
import com.euler.sdk.domain.dto.MemberRightsDto;
import com.euler.sdk.domain.entity.Goods;
import com.euler.sdk.domain.entity.MemberRights;
import com.euler.sdk.domain.entity.MemberRightsReceiveRecord;
import com.euler.sdk.domain.vo.MemberRightsVo;
import com.euler.sdk.mapper.GoodsMapper;
import com.euler.sdk.mapper.MemberRightsMapper;
import com.euler.sdk.mapper.MemberRightsReceiveRecordMapper;
import com.euler.sdk.service.IMemberRightsReceiveRecordService;
import com.euler.sdk.service.IMemberRightsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 会员权益Service业务层处理
 *
 * @author euler
 * @date 2022-03-21
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MemberRightsServiceImpl extends ServiceImpl<MemberRightsMapper, MemberRights> implements IMemberRightsService {

    private final MemberRightsMapper baseMapper;
    @Autowired
    private IMemberRightsReceiveRecordService iMemberRightsReceiveRecordService;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private MemberRightsReceiveRecordMapper memberRightsReceiveRecordMapper;

    /**
     * 查询会员权益
     *
     * @return 会员权益
     */
    @Override
    public MemberRightsVo queryById(IdDto<Long> idDto) {
        MemberRightsVo memberRightsVo = baseMapper.selectVoById(idDto.getId());
        if (memberRightsVo != null) {
            // 查询当前用户是否已领领取过年费会员的每月给与东西
            Integer year = DateUtil.year(new Date());
            Integer month = DateUtil.month(new Date());
            Boolean aBoolean = iMemberRightsReceiveRecordService.selectUserIsReceive(idDto.getId(), year, month, 0);
            if (aBoolean) {
                memberRightsVo.setRightsIsReceive(1);
            }
            // 查询一下当前用户本月可以立刻领取多少平台币
            LambdaQueryWrapper<Goods> eq = Wrappers.<Goods>lambdaQuery().select(Goods::getPlatformCurrency, Goods::getEveryMonthPlatformCurrency)
                .eq(Goods::getId, memberRightsVo.getGoodsId());
            GoodsVo goodsVo = goodsMapper.selectVoOne(eq);
            if (goodsVo != null) {
                // 判断一下当前会员立即领取的记录是否存在 理论上vip有效期一年 存在跨年操作 所以我们需要获取今年和前年  只要存在type为2的 就代表着我们领取过了
                // 获取今年和上年数据
                List<Integer> years = new ArrayList<>();
                years.add(year);
                years.add(year - 1);
                LambdaQueryWrapper<MemberRightsReceiveRecord> searchRecordNumLqw = Wrappers.<MemberRightsReceiveRecord>lambdaQuery()
                    .eq(MemberRightsReceiveRecord::getMemberId, idDto.getId())
                    .eq(MemberRightsReceiveRecord::getType, 2)
                    .in(MemberRightsReceiveRecord::getCurrentYear, years);
                Long aLong = memberRightsReceiveRecordMapper.selectCount(searchRecordNumLqw);
                if (aLong > 0) {
                    memberRightsVo.setType(1);
                    memberRightsVo.setEveryMonthPlatformCurrency(goodsVo.getEveryMonthPlatformCurrency());
                } else {
                    memberRightsVo.setType(2);
                    memberRightsVo.setEveryMonthPlatformCurrency(goodsVo.getPlatformCurrency());
                }
            }
            return memberRightsVo;
        }
        return new MemberRightsVo();
    }

    /**
     * 查询会员权益列表
     *
     * @param dto 会员权益
     * @return 会员权益
     */
    @Override
    public TableDataInfo<MemberRightsVo> queryPageList(MemberRightsDto dto) {
        LambdaQueryWrapper<MemberRights> lqw = buildQueryWrapper(dto);
        Page<MemberRightsVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<MemberRights> buildQueryWrapper(MemberRightsDto dto) {
        LambdaQueryWrapper<MemberRights> lqw = Wrappers.lambdaQuery();
        lqw.likeRight(StringUtils.isNotBlank(dto.getName()), MemberRights::getName, dto.getName());
        lqw.eq(StringUtils.isNotBlank(dto.getLever()), MemberRights::getLever, dto.getLever());
        lqw.eq(StringUtils.isNotBlank(dto.getStatus()), MemberRights::getStatus, dto.getStatus());
        lqw.eq(StringUtils.isNotBlank(dto.getIsUpgrade()), MemberRights::getIsUpgrade, dto.getIsUpgrade());
        lqw.eq(dto.getValidateStartTime() != null, MemberRights::getValidateStartTime, dto.getValidateStartTime());
        lqw.eq(dto.getValidateEndTime() != null, MemberRights::getValidateEndTime, dto.getValidateEndTime());
        return lqw;
    }

    /**
     * 开通会员
     *
     * @param bo 会员权益
     * @return 结果
     */
    @Override
    public R openMember(MemberRights bo) {
        // 获取原来的会员信息
        MemberRights vo = this.getById(bo.getId());

        Date validateEndTime = DateUtil.offsetMonth(DateUtil.date(), 12);
        if (vo == null) {
            vo = bo;
            // 是否升级: 未升级
            vo.setIsUpgrade(MemberConstants.IS_UPGRADE_NO);
            // 默认未上传
            vo.setUploadStatus(MemberConstants.IS_UPLOAD_NO);
            // 会员有效期开始时间：重新计时
            vo.setValidateStartTime(DateUtil.date());

        } else {
            // 有效期内，只能购买当前或者更高一级的会员
            if (MemberConstants.MEMBER_STATUS_NORMAL.equals(vo.getStatus())) {
                if (Convert.toInt(bo.getLever()) < (Convert.toInt(vo.getLever()))) {
                    return R.fail("只能购买当前或者更高一级的会员");
                }
                vo.setIsUpgrade(MemberConstants.IS_UPGRADE_YES);

                if (bo.getLever().equals(vo.getLever())) {
                    validateEndTime = DateUtil.offsetMonth(vo.getValidateEndTime(), 12);
                }

            } else {
                vo.setIsUpgrade(MemberConstants.IS_UPGRADE_NO);
                // 会员有效期开始时间：重新计时
                vo.setValidateStartTime(DateUtil.date());
            }
        }


        vo.setName(bo.getName());
        vo.setGoodsId(bo.getGoodsId());
        vo.setLever(bo.getLever());
        vo.setGoodsPrice(bo.getGoodsPrice());
        vo.setPayPrice(bo.getPayPrice());


        // 会员状态：有效
        vo.setStatus(MemberConstants.MEMBER_STATUS_NORMAL);

        // 会员有效期结束时间
        vo.setValidateEndTime(validateEndTime);
        // 会员时长
        vo.setMemberDuration("1年");

        boolean flag = this.saveOrUpdate(vo);
        if (!flag) {
            return R.fail("升级失败");
        }
        return R.ok("success");

    }


    /**
     * 批量删除会员权益
     *
     * @param ids 需要删除的会员权益主键
     * @return 结果
     */
    @Override
    public R deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        int i = baseMapper.deleteBatchIds(ids);
        if (i > 0) {
            return R.ok();
        }
        return R.fail("删除失败");
    }

    /**
     * 上传年费会员协议
     *
     * @param multipartFile 文件
     * @return
     */
    public R upload(MultipartFile multipartFile) {
        // 文件
        File file = toFile(multipartFile);
        // 文件名
        String fileName = multipartFile.getOriginalFilename();
        // 文件后缀名
        String suffix = FileNameUtil.getSuffix(file);
        // TODO h5地址未定
        String url = "https://www.baidu.com/";

        // 设置表单数据
        HashMap<String, Object> paramMap = new HashMap<String, Object>(2);
        paramMap.put("url", url);
        paramMap.put("fileName", fileName);

        // 上传文件
        HttpUtil.post(url, paramMap, 20000);

        MemberRightsDto dto = new MemberRightsDto();

        // 获取会员权益信息
        MemberRights update = this.getById(LoginHelper.getUserId());
        if (ObjectUtil.isNotNull(update)) {
            update.setUploadUrl(url);
            update.setUploadFileName(fileName);
            update.setUploadFileSuffix(suffix);
            update.setUploadStatus(MemberConstants.IS_UPLOAD_YES);

            // 更新会员权益表里的文件信息
            int i = baseMapper.updateById(update);
            if (i > 0) {
                return R.ok(paramMap);
            }
        } else {
            return R.fail("该用户不是年卡会员用户，请先开通会员");
        }

        return R.fail("上传年费会员协议失败");
    }

    /**
     * MultipartFile转File
     *
     * @param multipartFile
     * @return
     */
    private File toFile(MultipartFile multipartFile) {
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀
        String fileSuffix = StringUtils.substring(fileName, fileName.lastIndexOf("."), fileName.length());
        File file = null;

        try {
            // 用uuid作为文件的临时文件名，防止重复
            file = File.createTempFile(IdUtil.randomUUID(), fileSuffix);
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 根据会员id，修改会员权益
     */
    @Override
    public Boolean updateMemberRightsDetail(MemberRights entity) {
        var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper)
            .eq(MemberRights::getId, entity.getId())
            .set(MemberRights::getGoodsId, entity.getGoodsId())
            .set(MemberRights::getName, entity.getName())
            .set(MemberRights::getLever, entity.getLever())
            .set(MemberRights::getStatus, entity.getStatus())
            .set(MemberRights::getIsUpgrade, entity.getIsUpgrade())
            .set(MemberRights::getValidateStartTime, entity.getValidateStartTime())
            .set(MemberRights::getValidateEndTime, entity.getValidateEndTime())
            .set(MemberRights::getGoodsPrice, entity.getGoodsPrice())
            .set(MemberRights::getPayPrice, entity.getPayPrice());

        return updateChainWrapper.update();
    }

}
