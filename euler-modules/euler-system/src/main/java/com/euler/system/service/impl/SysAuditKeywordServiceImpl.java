package com.euler.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.system.domain.SysAuditKeyword;
import com.euler.system.domain.bo.SysAuditKeywordBo;
import com.euler.system.domain.dto.SysAuditKeywordDto;
import com.euler.system.domain.vo.SysAuditKeywordVo;
import com.euler.system.mapper.SysAuditKeywordMapper;
import com.euler.system.service.ISysAuditKeywordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 审核关键词 - 敏感词Service业务层处理
 *
 * @author euler
 * @date 2022-03-21
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysAuditKeywordServiceImpl implements ISysAuditKeywordService {
    // 最小匹配规则
    public static int minMatchTYpe = 1;
    // 最大匹配规则
    public static int maxMatchType = 2;


    private final SysAuditKeywordMapper baseMapper;

    /**
     * 查询审核关键词 - 敏感词列表
     *
     * @return 审核关键词 - 敏感词
     */
    @Override
    public TableDataInfo<SysAuditKeywordVo> queryPageList(SysAuditKeywordDto sysAuditKeywordDto) {
        LambdaQueryWrapper<SysAuditKeyword> lqw = buildQueryWrapper(sysAuditKeywordDto);
        Page<SysAuditKeywordVo> result = baseMapper.selectVoPage(sysAuditKeywordDto.build(), lqw);
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<SysAuditKeyword> buildQueryWrapper(SysAuditKeywordDto dto) {
        LambdaQueryWrapper<SysAuditKeyword> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getUserId() != null, SysAuditKeyword::getUserId, dto.getUserId());
        lqw.eq(dto.getType() != null, SysAuditKeyword::getType, dto.getType());
        lqw.eq(dto.getIsItValid() != null, SysAuditKeyword::getIsItValid, dto.getIsItValid());
        lqw.likeRight(StringUtils.isNotBlank(dto.getKeywords()), SysAuditKeyword::getKeywords, dto.getKeywords());
        lqw.orderByDesc(SysAuditKeyword::getId);
        return lqw;
    }

    /**
     * 查询审核关键词 - 敏感词列表 无分页
     *
     * @return 审核关键词 - 敏感词  无分页
     */
    @Override
    public List<SysAuditKeywordVo> queryList(SysAuditKeywordDto sysAuditKeywordDto) {
        LambdaQueryWrapper<SysAuditKeyword> lqw = buildQueryWrapper(sysAuditKeywordDto);
        List<SysAuditKeywordVo> sysAuditKeywordVos = baseMapper.selectVoList(lqw);
        return sysAuditKeywordVos;
    }

    /**
     * 新增审核关键词 - 敏感词
     *
     * @param bo 审核关键词 - 敏感词
     * @return 结果
     */
    @Override
    public R insertByBo(SysAuditKeywordBo bo) {
        SysAuditKeyword add = BeanUtil.toBean(bo, SysAuditKeyword.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            return R.ok();
        }
        return R.fail("数据添加失败");
    }

    /**
     * 修改审核关键词 - 敏感词
     *
     * @param bo 审核关键词 - 敏感词
     * @return 结果
     */
    @Override
    public R updateByBo(SysAuditKeywordBo bo) {
        SysAuditKeyword update = BeanUtil.toBean(bo, SysAuditKeyword.class);
        validEntityBeforeSave(update);
        int i = baseMapper.updateById(update);
        if (i > 0) {
            return R.ok();
        }
        return R.fail("数据更新失败");
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(SysAuditKeyword entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除审核关键词 - 敏感词
     *
     * @param ids 需要删除的审核关键词 - 敏感词主键
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
    public R check(String checkString, Integer type) {
        Integer useType = 1;
        if (type != null && type > 0) {
            useType = type;
        }
        Set<String> sensitiveWordList = new HashSet<>();
        // 获取敏感词库 进行判断
        String keywordKey = Constants.AUDIT_SENSITIVE_WORD + "_" + useType;
        Map<String, Object> nowMap = RedisUtils.getCacheMap(keywordKey);
        if (null == nowMap || nowMap.size() == 0) {
            init(type);
            nowMap = RedisUtils.getCacheMap(keywordKey);
            if (null == nowMap || nowMap.size() == 0) {
                log.info("敏感词库为空");
                return R.ok();
            }
        }
        // 检测
        for (int i = 0; i < checkString.length(); i++) {
            int length = checkSensitiveWord(checkString, i, minMatchTYpe, nowMap);
            if (length > 0) {
                sensitiveWordList.add(checkString.substring(i, i + length));
                i = i + length - 1;
            }
        }
        // 敏感词信息返回
        if (CollectionUtil.isNotEmpty(sensitiveWordList)) {
            // 存在敏感词
            log.info("当前词语是敏感词 当前词是:{}, 检测的结果为：{}", checkString, JsonUtils.toJsonString(sensitiveWordList));
            return R.fail(JsonUtils.toJsonString(sensitiveWordList));
        }
        return R.ok();
    }

    /**
     * 初始化敏感词到缓存中
     */
    private void init(Integer type) {
        SysAuditKeywordDto sysAuditKeywordDto = new SysAuditKeywordDto();
        sysAuditKeywordDto.setType(type);
        sysAuditKeywordDto.setIsItValid(0);
        List<SysAuditKeywordVo> sysAuditKeywordVos = this.queryList(sysAuditKeywordDto);
        Set<String> collect = sysAuditKeywordVos.stream().map(auditKeyword1 -> auditKeyword1.getKeywords()).collect(Collectors.toSet());
        addSensitiveWordToHashMap(collect, type);
    }


    /**
     * 将敏感词添加到缓存中
     *
     * @param keyWordSet
     */
    private void addSensitiveWordToHashMap(Set<String> keyWordSet, Integer type) {
        HashMap sensitiveWordMap = new HashMap(keyWordSet.size());
        Map nowMap = null;
        Map<String, String> newWorMap = null;

        for (String key : keyWordSet) {
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);
                Object wordMap = nowMap.get(keyChar);

                if (wordMap != null) {
                    nowMap = (Map) wordMap;
                } else {
                    newWorMap = new HashMap<String, String>();
                    newWorMap.put("isEnd", "0");
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1");
                }
            }
        }
        RedisUtils.setCacheMap(Constants.AUDIT_SENSITIVE_WORD + "_" + type, sensitiveWordMap);
    }

    /**
     * 疆场当前词是否在敏感此种
     *
     * @param txt
     * @param beginIndex
     * @param matchType
     * @param nowMap
     * @return
     */
    private int checkSensitiveWord(String txt, int beginIndex, int matchType, Map<String, Object> nowMap) {
        boolean flag = false;
        int matchFlag = 0;
        char word = 0;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(String.valueOf(word));
            if (nowMap != null) {
                matchFlag++;
                if ("1".equals(nowMap.get("isEnd"))) {
                    flag = true;
                    if (minMatchTYpe == matchType) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
        if (matchFlag < 2 || !flag) {
            matchFlag = 0;
        }
        return matchFlag;
    }

}
