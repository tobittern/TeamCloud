package com.euler.community.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.core.domain.dto.TableDataInfoCoreDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.config.CommonCommunityConfig;
import com.euler.community.constant.GitBagConstant;
import com.euler.community.domain.bo.HistorySearchBo;
import com.euler.community.domain.dto.HistorySearchDto;
import com.euler.community.domain.dto.IndexDto;
import com.euler.community.domain.entity.HistorySearch;
import com.euler.community.domain.vo.HistorySearchVo;
import com.euler.community.esMapper.DynamicElasticsearch;
import com.euler.community.mapper.HistorySearchMapper;
import com.euler.community.service.IHistorySearchService;
import com.euler.platform.api.RemoteGameManagerService;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.system.api.RemoteDictService;
import com.euler.system.api.domain.SysDictData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 搜索历史Service业务层处理
 *
 * @author euler
 * 2022-06-07
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HistorySearchServiceImpl extends ServiceImpl<HistorySearchMapper, HistorySearch> implements IHistorySearchService {

    private final HistorySearchMapper baseMapper;

    @Resource
    private DynamicElasticsearch dynamicElasticsearch;

    @DubboReference
    private RemoteGameManagerService remoteGameManagerService;

    @Resource
    private CommonCommunityConfig commonCommunityConfig;

    @DubboReference
    private RemoteDictService remoteDictService;

    /**
     * 查询搜索历史
     *
     * @param id 搜索历史主键
     * @return 搜索历史
     */
    @Override
    public HistorySearchVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询搜索历史列表
     *
     * @param historySearchDto 搜索历史
     * @return 搜索历史
     */
    @Override
    public TableDataInfo<HistorySearchVo> queryPageList(HistorySearchDto historySearchDto) {
//        if(historySearchDto.getMemberId()==null){
//            return TableDataInfo.build(new Page<HistorySearchVo>());
//        }
        LambdaQueryWrapper<HistorySearch> lqw = buildQueryWrapper(historySearchDto);
        Page<HistorySearchVo> result = baseMapper.selectVoPage(historySearchDto.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询搜索历史列表
     *
     * @param historySearchDto 搜索历史
     * @return 搜索历史
     */
    @Override
    public List<HistorySearchVo> queryList(HistorySearchDto historySearchDto) {
        LambdaQueryWrapper<HistorySearch> lqw = buildQueryWrapper(historySearchDto);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HistorySearch> buildQueryWrapper(HistorySearchDto historySearchDto) {
        LambdaQueryWrapper<HistorySearch> lqw = Wrappers.lambdaQuery();
        lqw.eq(historySearchDto.getMemberId() != null, HistorySearch::getMemberId, historySearchDto.getMemberId());
        lqw.eq(StringUtils.isNotBlank(historySearchDto.getKey()), HistorySearch::getKey, historySearchDto.getKey());
        lqw.eq(historySearchDto.getNum() != null, HistorySearch::getNum, historySearchDto.getNum());
        lqw.eq(historySearchDto.getPosition() != null, HistorySearch::getPosition, historySearchDto.getPosition());
        lqw.orderByDesc(HistorySearch::getCreateTime);
        return lqw;
    }

    /**
     * 新增搜索历史
     *
     * @param bo 搜索历史
     * @return 结果
     */
    @Override
    public Boolean insertByBo(HistorySearchBo bo) {
        if (!LoginHelper.isLogin() || StringUtils.isBlank(bo.getKey())) {
            return false;
        }
        HistorySearch add = BeanUtil.toBean(bo, HistorySearch.class);
        HistorySearchDto historySearchDto = BeanUtil.toBean(bo, HistorySearchDto.class);
        historySearchDto.setMemberId(LoginHelper.getUserId());
        List<HistorySearchVo> list = queryList(historySearchDto);
        if (!list.isEmpty()) {
            //对相同内容的搜索不做插入操作,对原有数据进行更新操作
            add.setId(list.get(0).getId());
            add.setNum(list.get(0).getNum() + 1);//对次数进行累加
            baseMapper.updateById(add);
            return true;
        }
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改搜索历史
     *
     * @param bo 搜索历史
     * @return 结果
     */
    @Override
    public Boolean updateByBo(HistorySearchBo bo) {
        HistorySearch update = BeanUtil.toBean(bo, HistorySearch.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 删除搜索历史
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
     * 清空搜索历史
     *
     * @return 结果
     */
    @Override
    public Integer clearByUserId(IdTypeDto<Integer, Integer> dto) {
        Long userId = LoginHelper.getUserIdOther();
        Integer type = 0;
        if (dto.getType() != null) {
            type = dto.getType();
        }
        if (userId != 0L) {
            return baseMapper.update(null,
                new LambdaUpdateWrapper<HistorySearch>()
                    .set(HistorySearch::getDelFlag, "2")
                    .eq(HistorySearch::getMemberId, userId)
                    .eq(HistorySearch::getPosition, type));
        }
        return 0;
    }

    @Override
    public R<Object> search(HistorySearchDto bo) {
        if (bo.getPosition() == null) {
            return R.fail("搜索位置不能为空");
        }
        if (StringUtils.isBlank(bo.getKey())) {
            return R.fail("搜索内容不能为空");
        }
        if (bo.getPageNum() == null) {
            return R.fail("当前页不能为空");
        }
        if (bo.getPageSize() == null) {
            return R.fail("每页显示数量不能为空");
        }
        Object object;
        if (bo.getPosition() == 0) {//查询动态，首页搜索内容匹配的是：话题，文字内容，攻略标题，攻略内容；
            try {
                IndexDto indexDto = new IndexDto();
                indexDto.setPageNum(bo.getPageNum());
                indexDto.setPageSize(bo.getPageSize());
                indexDto.setPosition(1);//对应首页查询
                indexDto.setKeyword(bo.getKey());
                object = dynamicElasticsearch.getDocumentByPage(indexDto);
            } catch (Exception e) {
                log.error("获取动态异常:{}", e.getMessage());
                return R.fail("获取数据异常");
            }
        } else if (bo.getPosition() == 1) {//发现页，发现搜索内容是：游戏名，游戏类型，游戏标签；
            if (StringUtils.isBlank(bo.getOperationPlatform())) {
                return R.fail("运行平台不能为空");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("delFlag", 0);
            map.put("operationPlatform", bo.getOperationPlatform());
            map.put("gameName", bo.getKey());
            map.put("gameTags", bo.getKey());
            map.put("pageNum", (bo.getPageNum() - 1) * bo.getPageSize());
            map.put("pageSize", bo.getPageSize());
            //获取所有的游戏类别
            List<String> cateGoryList = new ArrayList<>();
            List<SysDictData> platformGameType = remoteDictService.selectDictDataByType("platform_game_type");
            for (SysDictData sd : platformGameType) {
                if (sd.getDictLabel().contains(bo.getKey())) {
                    cateGoryList.add(sd.getDictValue());
                }
            }
            map.put("cateGoryList", cateGoryList);
            TableDataInfoCoreDto<OpenGameDubboVo> result = remoteGameManagerService.selectGameByParam(map);
            String yunDomain = commonCommunityConfig.getYunDomain();
            if (result != null) {
                List<OpenGameDubboVo> openGameDubboVoList = result.getRows();
                for (OpenGameDubboVo v : openGameDubboVoList) {
                    //图片url
                    String iconUrl = v.getIconUrl();
                    v.setIconUrl(StringUtils.isBlank(iconUrl) ? null : (iconUrl.startsWith(GitBagConstant.HTTP) ? iconUrl : yunDomain + iconUrl));
                    //设置游戏类名称
                    for (SysDictData s : platformGameType) {
                        if (s.getDictValue().equals(v.getGameCategory())) {
                            v.setGameCategoryName(s.getDictLabel());
                        }
                    }
                }
            }
            object = result;
        } else {
            return R.ok(null);
        }
        //用户为空的时候，只查不记录
        if (bo.getMemberId() == 0L) {
            return R.ok(object);
        }
        HistorySearchBo addBo = BeanUtil.toBean(bo, HistorySearchBo.class);
        insertByBo(addBo);//插入用户搜索数据
        return R.ok(object);
    }
}
