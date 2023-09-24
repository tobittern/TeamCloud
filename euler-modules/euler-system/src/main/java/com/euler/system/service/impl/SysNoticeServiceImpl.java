package com.euler.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HtmlUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.system.api.domain.NoticeToUsers;
import com.euler.system.api.domain.NoticeTypeDto;
import com.euler.system.api.domain.SysNotice;
import com.euler.system.api.domain.UserNoticeVo;
import com.euler.system.domain.dto.FrontNoticePageDto;
import com.euler.system.domain.dto.NoticePageDto;
import com.euler.system.mapper.NoticeToUsersMapper;
import com.euler.system.mapper.SysNoticeMapper;
import com.euler.system.service.ISysNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 公告 服务层实现
 *
 * @author euler
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysNoticeServiceImpl implements ISysNoticeService {

    private final SysNoticeMapper baseMapper;

    private final NoticeToUsersMapper noticeToUsersMapper;

    public List<SysNotice> selectNoticeList() {
        return baseMapper.selectList(new LambdaQueryWrapper<SysNotice>()
            .eq(SysNotice::getStatus, Constants.COMMON_STATUS_NO));
    }

    @Override
    public TableDataInfo<UserNoticeVo> selectFrontPageNoticeList(FrontNoticePageDto noticePageDto) {
        var lqw = buildWrapper(noticePageDto);
        Page<UserNoticeVo> result = baseMapper.selectFrontPageNoticeList(noticePageDto.build(), lqw, noticePageDto.getUserId(), noticePageDto.getNoticeType());

        if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            result.getRecords().forEach(notice -> {
                // 新增的时候，消息公告的内容，去除标签,并且截取字符串
                if (ObjectUtil.isNotNull(notice.getNoticeContent())) {
                    notice.setNoticeContent(StringUtils.substring(HtmlUtil.cleanHtmlTag(notice.getNoticeContent()), 0, 100));
                }
            });
        }

        return TableDataInfo.build(result);
    }

    private QueryWrapper<SysNotice> buildWrapper(FrontNoticePageDto noticePageDto) {
        Date now = new Date();

        var lqw = Wrappers.<SysNotice>query()
            .eq("n.`status`", "0") //消息状态正常
            .eq("n.`notice_type`", noticePageDto.getNoticeType())//消息类型，1：消息，2：公告
            .eq("n.push_status", "1")//推送状态，1：已推送
            .eq("n.platform_type", noticePageDto.getPlatformType());//平台：1：sdk，2：开放平台

        //未读，已读数据筛选
        if (Constants.UNREAD.equals(noticePageDto.getReadStatus()))
            lqw.isNull("nu.read_status");
        if (Constants.READ.equals(noticePageDto.getReadStatus()))
            lqw.eq("nu.read_status", Constants.READ);

        //消息类型，推送用户为全部或者推送用户为部分用户的数据
        if (Constants.NOTICE_TYPE_NOTIFY.equals(noticePageDto.getNoticeType())) {
            lqw.and(a -> a.eq("n.push_user_type", "0").or(b -> b.eq("n.push_user_type", "1").eq("nu.to_user_id", noticePageDto.getUserId())));
            lqw.orderByDesc("n.push_time");
        } else {
            //公告类型，持续时间为永久或者有效期内的数据
            lqw.and(a -> a.isNull("n.duration_start_time").or(b -> b.le("n.duration_start_time", now).ge("n.duration_end_time", now)));
            lqw.orderByDesc("n.to_top").orderByDesc("n.push_time");
        }

        return lqw;
    }


    @Override
    public TableDataInfo<SysNotice> selectPageNoticeList(NoticePageDto noticePageDto) {
        LambdaQueryWrapper<SysNotice> lqw = new LambdaQueryWrapper<SysNotice>()
            .eq(noticePageDto.getNoticeId() != null, SysNotice::getNoticeId, noticePageDto.getNoticeId())
            .eq(StringUtils.isNotBlank(noticePageDto.getNoticeType()), SysNotice::getNoticeType, noticePageDto.getNoticeType())
            .eq(StringUtils.isNotBlank(noticePageDto.getStatus()), SysNotice::getStatus, noticePageDto.getStatus())
            .likeRight(StringUtils.isNotBlank(noticePageDto.getNoticeTitle()), SysNotice::getNoticeTitle, noticePageDto.getNoticeTitle())
            .likeRight(StringUtils.isNotBlank(noticePageDto.getCreateBy()), SysNotice::getCreateBy, noticePageDto.getCreateBy())
            .eq(StringUtils.isNotBlank(noticePageDto.getPlatformType()), SysNotice::getPlatformType, noticePageDto.getPlatformType())
            .eq(StringUtils.isNotBlank(noticePageDto.getPushUserType()), SysNotice::getPushUserType, noticePageDto.getPushUserType())
            .eq(StringUtils.isNotBlank(noticePageDto.getAutoPush()), SysNotice::getAutoPush, noticePageDto.getAutoPush())
            .eq(StringUtils.isNotBlank(noticePageDto.getPushStatus()), SysNotice::getPushStatus, noticePageDto.getPushStatus());

        lqw.orderByDesc(StringUtils.isNotBlank(noticePageDto.getToTop()), SysNotice::getToTop);
        lqw.orderByDesc(SysNotice::getPushTime);
        Page<SysNotice> result = baseMapper.selectPage(noticePageDto.build(), lqw);

        if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            result.getRecords().forEach(notice -> {

                // 新增的时候，消息公告的内容，去除标签,并且截取字符串
                if (ObjectUtil.isNotNull(notice.getNoticeContent())) {
                    notice.setNoticeContent(StringUtils.substring(HtmlUtil.cleanHtmlTag(notice.getNoticeContent()), 0, 100));
                }
                // 如果是公告的话，显示公告持续时间
                if (StringUtils.equals(Constants.NOTICE_TYPE_PUBLIC, notice.getNoticeType())) {
                    String startTime = DateUtil.formatDate(notice.getDurationStartTime());
                    String endTime = DateUtil.formatDate(notice.getDurationEndTime());
                    // 公告持续时间
                    notice.setDurationTime(startTime + "至" + endTime);
                }

            });
        }

        return TableDataInfo.build(result);
    }

    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public SysNotice selectNoticeById(Integer noticeId) {
        return baseMapper.selectById(noticeId);
    }

    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public R insertNotice(SysNotice notice) {

        String check = validEntityBeforeSave(notice);
        if (!check.equals("success")) {
            return R.fail(check);
        }
        // 发布人
        notice.setPublishUser(LoginHelper.getUsername());
        //新增公告
        boolean flag = baseMapper.insert(notice) > 0;
        if (flag) {
            // 关联数据的添加
            Boolean relationFlag = updateRelationData(notice);
            if (relationFlag) {
                return R.ok("新增成功");
            }
        }

        return R.ok("新增成功");
    }

    /**
     * 修改公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public R updateNotice(SysNotice notice) {
        // 验证是否传输过来主键
        if (notice.getNoticeId() == null || notice.getNoticeId() <= 0) {
            return R.fail("参数缺失");
        }
        String check = validEntityBeforeSave(notice);
        if (!check.equals("success")) {
            return R.fail(check);
        }

        int i = baseMapper.updateById(notice);
        if (i > 0) {
            // 关联数据修正
            Boolean flag = updateRelationData(notice);
            if (flag) {
                return R.ok("更新成功");
            }
        }
        return R.fail("更新失败");
    }

    /**
     * 关联数据修正
     *
     * @param entity 消息
     */
    @SneakyThrows
    private Boolean updateRelationData(SysNotice entity) {
        // 关联数据修正
        // 公告的关联数据的添加
        String pushUser = "";
        // 判断是否是消息，并且，推送用户是部分用户
        if (StringUtils.equals(Constants.NOTICE_TYPE_NOTIFY, entity.getNoticeType())
            && StringUtils.equals(Constants.PUSH_USER_PART, entity.getPushUserType())) {

            List<NoticeToUsers> entityList = new ArrayList<>();
            // 推送用户列表
            // 如果上传的文件为空，也没有手动入力用户id, 就会报错
            if (ObjectUtil.isNull(entity.getPushUsers())) {
                throw new ServiceException("请输入部分用户ID，至少一个");
            } else {
                pushUser = entity.getPushUsers();
                List<String> userlist = Convert.convert(List.class, pushUser);

                for (int i = 0; i < userlist.size(); i++) {
                    NoticeToUsers noticeToUsers = new NoticeToUsers();

                    noticeToUsers.setNoticeId(entity.getNoticeId());
                    noticeToUsers.setToUserId(Convert.toLong(userlist.get(i)));
                    // 阅读状态，默认是未读
                    noticeToUsers.setReadStatus(Constants.UNREAD);
                    // 判断是否存在
                    LambdaQueryWrapper<NoticeToUsers> eq = Wrappers.<NoticeToUsers>lambdaQuery()
                        .eq(NoticeToUsers::getNoticeId, entity.getNoticeId())
                        .eq(NoticeToUsers::getToUserId, Convert.toLong(userlist.get(i)));

                    boolean flag = noticeToUsersMapper.exists(eq);
                    if (!flag) {
                        entityList.add(noticeToUsers);
                    }
                }
            }
            // 新增到关联表
            noticeToUsersMapper.insertOrUpdateBatch(entityList);
        } else {
            pushUser = "全部用户";
        }
        // 画面展示的推送用户信息更新
        entity.setPushUsers(pushUser);
        entity.setPushStatus(Constants.IS_PUSH_NO);

        return baseMapper.updateById(entity) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param notice 实体类数据
     */
    private String validEntityBeforeSave(SysNotice notice) {

        // 消息类型校验
        String[] typeList = new String[]{"1", "2"};
        Optional<String> operationPlatformAny = Arrays.stream(typeList).filter(a -> a.equals(notice.getNoticeType())).findAny();
        if (!operationPlatformAny.isPresent()) {
            return "消息类型参数错误";
        }
        if (ObjectUtil.isNull(notice.getPushTime())) {
            return "没有设置推送时间";
        }

        // 如果是公告的话，显示公告持续时间
        if (StringUtils.equals(Constants.NOTICE_TYPE_PUBLIC, notice.getNoticeType())) {
            if (ObjectUtil.isNull(notice.getDurationStartTime()) || ObjectUtil.isNull(notice.getDurationEndTime())) {
                return "没有设置公告持续时间";
            }
        }
        return "success";
    }

    /**
     * 批量删除公告信息
     *
     * @param noticeDto 需要删除的公告ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R deleteNoticeByIds(NoticeTypeDto noticeDto) {
        List<Long> ids = Convert.convert(List.class, noticeDto.getId());

        // 消息公告表
        int i = baseMapper.delete(new LambdaQueryWrapper<SysNotice>().in(SysNotice::getNoticeId, ids));

        // 判断是否是消息，并且，推送用户是部分用户
        if (StringUtils.equals(Constants.NOTICE_TYPE_NOTIFY, noticeDto.getNoticeType())
            && StringUtils.equals(Constants.PUSH_USER_PART, noticeDto.getPushUserType())) {
            // 删除公告关联表的信息
            int j = noticeToUsersMapper.delete(
                new LambdaQueryWrapper<NoticeToUsers>()
                    .in(NoticeToUsers::getNoticeId, ids));
        } else {
            // 全部用户
            int j = noticeToUsersMapper.delete(
                new LambdaQueryWrapper<NoticeToUsers>()
                    .in(NoticeToUsers::getNoticeId, ids)
                    .eq(NoticeToUsers::getToUserId, LoginHelper.getUserId()));
        }
        return R.ok("删除成功");
    }

    /**
     * 批量置顶公告信息
     *
     * @param idTypeDto
     * @return
     */
    @Override
    public boolean toTopNoticeByIds(IdTypeDto<String, String> idTypeDto) {
        List<Integer> ids = Convert.convert(List.class, idTypeDto.getId());

        //idTypeDto.getType(),1:置顶，0：取消置顶
        var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper).in(SysNotice::getNoticeId, ids)
            .set(SysNotice::getToTop, idTypeDto.getType().equals("1") ? Constants.READ : Constants.UNREAD);
        boolean flag = updateChainWrapper.update();
        return flag;
    }

    /**
     * 批量已读通知
     *
     * @param idTypeDto
     * @param userId
     * @return
     */
    @Override
    public boolean toRead(IdTypeDto<String, String> idTypeDto, Long userId) {
        // 获取id
        List<Integer> ids = Convert.toList(Integer.class, idTypeDto.getId());

        var selectWrapper = new LambdaQueryWrapper<NoticeToUsers>();
        selectWrapper.eq(NoticeToUsers::getToUserId, userId);

        // idTypeDto.getType，0：批量已读，1：全部已读
        if (Constants.UNREAD.equals(idTypeDto.getType())) {
            selectWrapper.in(NoticeToUsers::getNoticeId, ids);
        }
        // 查询那些消息我们没有入库
        List<NoticeToUsers> noticeToUsers = noticeToUsersMapper.selectList(selectWrapper);
        // 判断那些已经存在数据库中
        List<Integer> mysqlHave = new ArrayList<>();
        if (noticeToUsers != null && noticeToUsers.size() > 0) {
            mysqlHave = noticeToUsers.stream().map(NoticeToUsers::getNoticeId).collect(Collectors.toList());
        }
        List<NoticeToUsers> list = new ArrayList<NoticeToUsers>();
        // 循环判断当前消息那些不存在数据库中
        for (var id : ids) {
            // 判断那些在数据库中不存在的
            if (mysqlHave.size() > 0 && !mysqlHave.contains(Convert.toInt(id))) {
                NoticeToUsers rm = new NoticeToUsers();
                rm.setNoticeId(Convert.toInt(id));
                rm.setReadStatus("0");
                rm.setToUserId(userId);
                list.add(rm);
            }
        }
        // 判断是否存在需要入库的数据
        Integer rows = 1;
        if (list.size() > 0) {
            rows = noticeToUsersMapper.insertBatch(list) ? list.size() : 0;
        }
        // 开始进行更新数据
        var updateWrapper = new LambdaUpdateChainWrapper<>(noticeToUsersMapper);
        updateWrapper.eq(NoticeToUsers::getToUserId, userId);
        // idTypeDto.getType，0：批量已读，1：全部已读
        if (Constants.UNREAD.equals(idTypeDto.getType())) {
            updateWrapper.in(NoticeToUsers::getNoticeId, ids);
        }
        updateWrapper.set(NoticeToUsers::getReadStatus, Constants.READ);
        updateWrapper.update();
        return true;
    }

    /**
     * 批量推送
     *
     * @param noticeDto
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R toPush(NoticeTypeDto noticeDto, Long userId) {
        List<Integer> ids = Convert.convert(List.class, noticeDto.getId());
        // 消息公告表
        var baseWrapper = new LambdaUpdateChainWrapper<>(baseMapper);

        baseWrapper.eq(SysNotice::getPushStatus, Constants.IS_PUSH_NO);
        baseWrapper.in(SysNotice::getNoticeId, ids);
        baseWrapper.set(SysNotice::getPushStatus, Constants.IS_PUSH_YES);
        baseWrapper.set(SysNotice::getPushTime, DateUtil.date());

        baseWrapper.update();
        return R.ok("推送成功");
    }

    /**
     * 获取用户未读消息数
     *
     * @param
     * @return
     */
    @Override
    public Integer getUnReadNoticeTotalCount(FrontNoticePageDto noticePageDto) {

        noticePageDto.setReadStatus("0");
        noticePageDto.setNoticeType("1");
        var lqw = buildWrapper(noticePageDto);
        Integer unReadCount = baseMapper.getUnReadCount(lqw, noticePageDto.getUserId());
        return unReadCount;
    }


}
