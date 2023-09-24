package com.euler.community.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.config.CommonCommunityConfig;
import com.euler.community.constant.GitBagConstant;
import com.euler.community.domain.bo.HotGameBo;
import com.euler.community.domain.dto.HotGameDto;
import com.euler.community.domain.entity.HotGame;
import com.euler.community.domain.vo.HotGameVo;
import com.euler.community.mapper.HotGameMapper;
import com.euler.community.service.IHotGameService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Service业务层处理
 *
 * @author euler
 * @date 2022-06-17
 */
@RequiredArgsConstructor
@Service
public class HotGameServiceImpl extends ServiceImpl<HotGameMapper, HotGame> implements IHotGameService {

    private final HotGameMapper baseMapper;

    @Resource
    private CommonCommunityConfig commonCommunityConfig;

    /**
     * 查询
     *
     * @param id 主键
     * @return
     */
    @Override
    public HotGameVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询列表
     *
     * @param hotGameDto
     * @return
     */
    @Override
    public TableDataInfo<HotGameVo> queryPageList(HotGameDto hotGameDto) {
        LambdaQueryWrapper<HotGame> lqw = buildQueryWrapper(hotGameDto);
        Page<HotGameVo> result = baseMapper.selectVoPage(hotGameDto.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询列表
     *
     * @param hotGameDto
     * @return
     */
    @Override
    public List<HotGameVo> queryList(HotGameDto hotGameDto) {
        LambdaQueryWrapper<HotGame> lqw = buildQueryWrapper(hotGameDto);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HotGame> buildQueryWrapper(HotGameDto hotGameDto) {
        LambdaQueryWrapper<HotGame> lqw = Wrappers.lambdaQuery();
        lqw.eq(hotGameDto.getMemberId() != null, HotGame::getMemberId, hotGameDto.getMemberId());
        lqw.eq(hotGameDto.getGameId() != null, HotGame::getGameId, hotGameDto.getGameId());
        lqw.like(StringUtils.isNotBlank(hotGameDto.getGameName()), HotGame::getGameName, hotGameDto.getGameName());
        lqw.eq(StringUtils.isNotBlank(hotGameDto.getGamePic()), HotGame::getGamePic, hotGameDto.getGamePic());
        lqw.eq(hotGameDto.getNum() != null, HotGame::getNum, hotGameDto.getNum());
        return lqw;
    }

    /**
     * 新增
     *
     * @param bo
     * @return 结果
     */
    @Override
    public Boolean insertByBo(HotGameBo bo) {
        if (bo.getMemberId() == null) {
            return true;
        }
        HotGame add = BeanUtil.toBean(bo, HotGame.class);
        LambdaQueryWrapper<HotGame> lqw = Wrappers.lambdaQuery();
        lqw.eq(true, HotGame::getMemberId, bo.getMemberId());
        lqw.eq(true, HotGame::getGameId, bo.getGameId());
        List<HotGame> hotGames = baseMapper.selectList(lqw);
        if (hotGames.isEmpty()) {
            boolean flag = baseMapper.insert(add) > 0;
            if (flag) {
                bo.setId(add.getId());
            }
            return flag;
        } else {
            HotGame hotGame = hotGames.get(0);
            hotGame.setNum(hotGame.getNum() + 1);
            int i = baseMapper.updateById(hotGame);
            return i > 0;
        }
    }

    /**
     * 修改
     *
     * @param bo
     * @return 结果
     */
    @Override
    public Boolean updateByBo(HotGameBo bo) {
        HotGame update = BeanUtil.toBean(bo, HotGame.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除
     *
     * @param ids 需要删除的主键
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
    public R getHotGames(HotGameBo hotGameBo) {
        List<HotGame> hotGames = baseMapper.getHotGames(hotGameBo.getOperationPlatform());
        if (hotGames == null || hotGames.isEmpty()) {
            return R.ok(Collections.EMPTY_LIST);
        }
        //对返回图片的路径做优化
        String yunDomain = commonCommunityConfig.getYunDomain();
        for (HotGame hotGame : hotGames) {
            String gamePic = hotGame.getGamePic();
            if (StringUtils.isNotEmpty(gamePic)) {
                hotGame.setGamePic(gamePic.startsWith(GitBagConstant.HTTP) ? gamePic : yunDomain + gamePic);
            }
        }
        return R.ok(hotGames);
    }
}
