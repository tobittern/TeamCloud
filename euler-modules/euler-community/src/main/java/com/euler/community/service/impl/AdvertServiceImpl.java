package com.euler.community.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.domain.R;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.config.CommonCommunityConfig;
import com.euler.community.constant.AdvertConstant;
import com.euler.community.constant.GitBagConstant;
import com.euler.community.domain.bo.AdvertBo;
import com.euler.community.domain.dto.AdvertDto;
import com.euler.community.domain.dto.AdvertDynamicEsDto;
import com.euler.community.domain.entity.Advert;
import com.euler.community.domain.entity.AdvertViewRecord;
import com.euler.community.domain.entity.DynamicFrontEs;
import com.euler.community.domain.vo.AdvertVo;
import com.euler.community.mapper.AdvertMapper;
import com.euler.community.mapper.AdvertViewRecordMapper;
import com.euler.community.service.IAdvertService;
import com.euler.community.utils.CommonForCommunityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.util.automaton.RegExp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 广告Service业务层处理
 *
 * @author euler
 * @date 2022-06-06
 */
@RequiredArgsConstructor
@Service
public class AdvertServiceImpl extends ServiceImpl<AdvertMapper, Advert> implements IAdvertService {

    private final AdvertMapper baseMapper;

    private final AdvertViewRecordMapper advertViewRecordMapper;

    @Resource
    private CommonCommunityConfig commonCommunityConfig;

    /**
     * 查询广告
     *
     * @param id 广告主键
     * @return 广告
     */
    @Override
    public AdvertVo queryById(Long id) {
        AdvertVo advertVo = baseMapper.selectVoById(id);
        setUrlPrefix(advertVo);
        return advertVo;
    }

    /**
     * 设置附件前缀
     *
     * @param advertVo
     */
    public void setUrlPrefix(AdvertVo advertVo) {
        if (advertVo != null) {
            String yunDomain = commonCommunityConfig.getYunDomain();
            //对附件进行处理
            String fileContent = advertVo.getFileContent();
            JSONArray jSONArray = null;
            if (StringUtils.isNotBlank(fileContent)) {
                jSONArray = JSONArray.parseArray(fileContent);
                for (int i = 0; i < jSONArray.size(); i++) {
                    JSONObject jsonObject = jSONArray.getJSONObject(i);
                    String filePath = jsonObject.getString(GitBagConstant.FILE_PATH);
                    jsonObject.put(GitBagConstant.FILE_PATH, StringUtils.isBlank(filePath) ? null : (filePath.startsWith(GitBagConstant.HTTP) ? filePath : yunDomain + filePath));
                }
            }
            advertVo.setFileContent(StringUtils.isBlank(fileContent) ? null : (jSONArray == null ? null : jSONArray.toJSONString()));
            //头像url
            String headUrl = advertVo.getHeadUrl();
            advertVo.setHeadUrl(StringUtils.isBlank(headUrl) ? null : (headUrl.startsWith(GitBagConstant.HTTP) ? headUrl : yunDomain + headUrl));
            //封面url
            String coverUrl = advertVo.getCoverUrl();
            advertVo.setCoverUrl(StringUtils.isBlank(coverUrl) ? null : (coverUrl.startsWith(GitBagConstant.HTTP) ? coverUrl : yunDomain + coverUrl));
        }
    }

    /**
     * 查询广告列表
     *
     * @param advertDto 广告
     * @return 广告
     */
    @Override
    public TableDataInfo<AdvertVo> queryPageList(AdvertDto advertDto) {
        LambdaQueryWrapper<Advert> lqw = buildQueryWrapper(advertDto);
        Page<AdvertVo> result = baseMapper.selectVoPage(advertDto.build(), lqw);
        //设置附件的url
        if (result != null) {
            List<AdvertVo> records = result.getRecords();
            for (AdvertVo vo : records) {
                setUrlPrefix(vo);
            }
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询广告列表
     *
     * @param advertDto 广告
     * @return 广告
     */
    @Override
    public List<AdvertVo> queryList(AdvertDto advertDto) {
        LambdaQueryWrapper<Advert> lqw = buildQueryWrapper(advertDto);
        List<AdvertVo> advertVos = baseMapper.selectVoList(lqw);
        //设置附件的url
        for (AdvertVo vo : advertVos) {
            setUrlPrefix(vo);
        }
        return advertVos;
    }

    private LambdaQueryWrapper<Advert> buildQueryWrapper(AdvertDto advertDto) {
        LambdaQueryWrapper<Advert> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(advertDto.getContent()), Advert::getContent, advertDto.getContent());
        lqw.like(StringUtils.isNotBlank(advertDto.getTopics()), Advert::getTopics, advertDto.getTopics());
        lqw.eq(advertDto.getGameId() != null, Advert::getGameId, advertDto.getGameId());
        lqw.like(StringUtils.isNotBlank(advertDto.getGameName()), Advert::getGameName, advertDto.getGameName());
        lqw.le(advertDto.getStartTime() != null, Advert::getStartTime, advertDto.getStartTime());
        lqw.ge(advertDto.getEndTime() != null, Advert::getEndTime, advertDto.getEndTime());
        //按创建时间倒序排列
        lqw.orderByDesc(Advert::getCreateTime);
        return lqw;
    }

    /**
     * 新增广告
     *
     * @param bo 广告
     * @return 结果
     */
    @Override
    public R<Void> insertByBo(AdvertBo bo) {
        Advert add = BeanUtil.toBean(bo, Advert.class);
        //参数校验
        String s = validateParam(bo);
        if (!s.equals("success")) {
            return R.fail(s);
        }
        // 入库
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return R.ok();
    }

    /**
     * 检验参数
     *
     * @param bo 广告
     */
    public String validateParam(AdvertBo bo) {
        //视频类,必须有封面
        //校验话题个数
        String topics = bo.getTopics();
        if (StringUtils.isBlank(topics)) {
            return "话题不能为空";
        } else {
            RegExp rg = new RegExp(",|，");
            String[] split = topics.split(rg.toString());
            if (split.length > 5) {
                return "话题个数不能超过5个";
            }
        }
        if (bo.getType() == 0 && StringUtils.isBlank(bo.getCoverUrl())) {
            return "请上传封面图片";
        }
        String[] picArr = AdvertConstant.ADVERT_PIC_TYPE.split(",");
        List<String> picSuffixList = Stream.of(picArr).collect(Collectors.toList());
        if (StringUtils.isNotBlank(bo.getCoverUrl())) {
            String picSuffix = bo.getCoverUrl().substring(bo.getCoverUrl().lastIndexOf("."));
            if (!picSuffixList.contains(picSuffix)) {
                return "封面图片上传格式错误";
            }
        }
        String fileContent = bo.getFileContent();
        if (StringUtils.isBlank(fileContent)) {
            return bo.getType() == 0 ? "上传视频不能为空" : "上传图片不能为空";
        }
        //校验附件
        try {
            JSONArray fileContentArray = JSONArray.parseArray(fileContent);
            if (fileContentArray.size() == 0) {
                return bo.getType() == 0 ? "请上传视频" : "请上传图片";
            }
            //校验附件，图片只能是jpeg/png/jpg，视频只能是mp4
            for (int i = 0; i < fileContentArray.size(); i++) {
                JSONObject jsonObject = fileContentArray.getJSONObject(i);
                String filePath = jsonObject.getString(GitBagConstant.FILE_PATH);
                if (StringUtils.isBlank(filePath)) {
                    return bo.getType() == 0 ? "请上传视频" : "请上传图片";
                }
                if (bo.getType() == 0) {//视频
                    String videoSuffix = filePath.substring(filePath.lastIndexOf("."));
                    String[] videoArr = AdvertConstant.ADVERT_VIDEO_TYPE.split(",");
                    List<String> videoSuffixList = Stream.of(videoArr).collect(Collectors.toList());
                    if (!videoSuffixList.contains(videoSuffix)) {
                        return "视频上传格式错误";
                    }
                } else if (bo.getType() == 1) {//图片
                    String picSuffix = filePath.substring(filePath.lastIndexOf("."));
                    if (!picSuffixList.contains(picSuffix)) {
                        return "图片上传格式错误";
                    }
                }
            }
        } catch (Exception e) {
            return "附件解析异常";
        }
        return "success";
    }


    /**
     * 修改广告
     *
     * @param bo 广告
     * @return 结果
     */
    @Override
    public R<Void> updateByBo(AdvertBo bo) {
        Advert update = BeanUtil.toBean(bo, Advert.class);
        //参数校验
        String s = validateParam(bo);
        if (!s.equals("success")) {
            return R.fail(s);
        }
        return baseMapper.updateById(update) > 0 ? R.ok() : R.fail("更新异常");
    }


    /**
     * 批量删除广告
     *
     * @param ids 需要删除的广告主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Boolean logicRemove(AdvertBo bo) {
        if (bo.getId() == null) {
            throw new RuntimeException("id不能为空");
        }
        return baseMapper.deleteById(bo.getId()) > 0;
    }

    @Override
    public List<AdvertDynamicEsDto> queryByPageNum(AdvertBo bo) {
        if (bo.getPage() == null) {
            throw new ServiceException("获取广告所在页不能为空");
        }
        LambdaQueryWrapper<Advert> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getPage() != null, Advert::getPage, bo.getPage());
        Date currentDate = new Date();
        lqw.le(true, Advert::getStartTime, currentDate);
        lqw.ge(true, Advert::getEndTime, currentDate);
        lqw.eq(true, Advert::getStatus, 1);//查询启用状态下的广告
        List<AdvertVo> advertVoList = baseMapper.selectVoList(lqw);
        //下面进行返回数据的封装
        List<AdvertDynamicEsDto> rList = new ArrayList<>();
        for (AdvertVo advertVo : advertVoList) {
            if (bo.getMemberId() != null && bo.getMemberId() != 0) {
                LambdaQueryWrapper<AdvertViewRecord> advertViewRecordLqw = Wrappers.lambdaQuery();
                advertViewRecordLqw.eq(true, AdvertViewRecord::getMemberId, bo.getMemberId());
                advertViewRecordLqw.eq(true, AdvertViewRecord::getAdvertId, advertVo.getId());
                List<AdvertViewRecord> advertViewRecords = advertViewRecordMapper.selectList(advertViewRecordLqw);
                if (!advertViewRecords.isEmpty()) {
                    AdvertViewRecord advertViewRecord = advertViewRecords.get(0);
                    //过滤掉当前app登录人 看过的次数>=广告曝光的次数  忽略掉该广告
                    if (advertViewRecord.getViewNum() >= advertVo.getExposureTimes()) {
                        continue;
                    }
                }
            }
            //对图片路径进行处理
            setUrlPrefix(advertVo);
            AdvertDynamicEsDto advertDynamicEsDto = new AdvertDynamicEsDto();
            DynamicFrontEs dynamicFrontEs = new DynamicFrontEs();
            advertDynamicEsDto.setRow(advertVo.getRow());//所在行
            dynamicFrontEs.setId(advertVo.getId());//id
            dynamicFrontEs.setIsAdv(1);//设置是广告
            dynamicFrontEs.setType(advertVo.getType() == 0 ? 1 : 2);//动态类型，1视频，2动态，3攻略
            dynamicFrontEs.setMemberId(advertVo.getMemberId());//用户id
            dynamicFrontEs.setNickname(advertVo.getNickName());//昵称
            dynamicFrontEs.setAvatar(advertVo.getHeadUrl());//头像
            dynamicFrontEs.setTopic(advertVo.getTopics());//动态话题
            dynamicFrontEs.setContent(advertVo.getContent());//文字内容
            dynamicFrontEs.setJumpUrl(advertVo.getJumpUrl());//跳转链接
            dynamicFrontEs.setExposureTimes(advertVo.getExposureTimes());//每个用户曝光次数
            dynamicFrontEs.setForwardNum(advertVo.getForwardNum());//转发量
            dynamicFrontEs.setPraiseNum(advertVo.getPraiseNum());//点赞量
            dynamicFrontEs.setCommentNum(advertVo.getCommentNum());//评论量
            dynamicFrontEs.setCollectNum(advertVo.getCollectNum());//收藏量
            dynamicFrontEs.setCover(advertVo.getCoverUrl());//封面
            dynamicFrontEs.setCoverHeight(advertVo.getCoverHeight());//封面高
            dynamicFrontEs.setCoverWidth(advertVo.getCoverWidth());//封面宽
            //下面封装封面类型
            String fileContent = advertVo.getFileContent();
            StringBuilder resouceUrl = new StringBuilder();
            if (StringUtils.isNotBlank(fileContent)) {
                JSONArray jArray = JSONArray.parseArray(fileContent);
                for (int i = 0; i < jArray.size(); i++) {
                    JSONObject jsonObject = jArray.getJSONObject(i);
                    String filePath = jsonObject.getString(GitBagConstant.FILE_PATH);
                    if (i == jArray.size() - 1) {
                        resouceUrl.append(filePath);
                    } else {
                        resouceUrl.append(filePath).append(",");
                    }
                    if (dynamicFrontEs.getCoverWidth() == null) {
                        String width = jsonObject.getString(GitBagConstant.FILE_WIDTH);
                        dynamicFrontEs.setCoverWidth(Convert.toInt(width));
                    }
                    if (dynamicFrontEs.getCoverHeight() == null) {
                        String height = jsonObject.getString(GitBagConstant.FILE_HEIGHT);
                        dynamicFrontEs.setCoverHeight(Convert.toInt(height));
                    }
                }
            }
            dynamicFrontEs.setResourceUrl(resouceUrl.toString());//设置资源
            dynamicFrontEs.setCoverType(CommonForCommunityUtils.getCoverType(dynamicFrontEs.getCoverWidth(), dynamicFrontEs.getCoverWidth(), resouceUrl.toString()));//设置封面类型
            advertDynamicEsDto.setDynamicEs(dynamicFrontEs);
            rList.add(advertDynamicEsDto);
        }
        return rList;
    }

    @Override
    public R<Void> updateStatus(AdvertBo bo) {
        if (bo.getId() == null) {
            return R.fail("广告id为空");
        }
        if (bo.getStatus() == null) {
            return R.fail("广告状态为空");
        }
        if (bo.getStatus() == 0) {
            return R.fail("广告状态不能修改为待启用");
        }
        Advert advert = baseMapper.selectById(bo.getId());
        if (advert == null) {
            return R.fail("广告不存在");
        }
        if (advert.getStatus().equals(bo.getStatus())) {
            return R.fail("不能修改为相同状态");
        }
        Advert copy = BeanUtil.toBean(bo, Advert.class);
        baseMapper.updateById(copy);
        return R.ok();
    }
}
