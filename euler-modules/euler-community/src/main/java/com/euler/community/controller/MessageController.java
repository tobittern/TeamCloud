package com.euler.community.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.exception.NotLoginException;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.core.enums.UserTypeEnum;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.domain.dto.MessageDto;
import com.euler.community.domain.dto.MessageToUsersDto;
import com.euler.community.domain.dto.MessageTypeDto;
import com.euler.community.domain.entity.MessageToUsers;
import com.euler.community.domain.vo.MessageToUsersVo;
import com.euler.community.service.IMessageToUsersService;
import com.euler.system.api.RemoteNoticeService;
import com.euler.system.api.RemoteNoticeToUsersService;
import com.euler.system.api.domain.NoticeToUsers;
import com.euler.system.api.domain.NoticeTypeDto;
import com.euler.system.api.domain.SysNotice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.euler.community.domain.vo.MessageVo;
import com.euler.community.domain.bo.MessageBo;
import com.euler.community.service.IMessageService;
import com.euler.common.mybatis.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 消息Controller
 * 前端访问路由地址为:/community/message
 *
 * @author euler
 * @date 2022-06-06
 */
@Validated
@Api(value = "消息控制器", tags = {"消息管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController extends BaseController {

    private final IMessageService iMessageService;

    private final IMessageToUsersService iMessageToUsersService;

    @DubboReference
    private RemoteNoticeService remoteNoticeService;

    @DubboReference
    private RemoteNoticeToUsersService remoteToUsersService;

    /**
     * 查询消息列表
     */
    @ApiOperation("查询消息列表")
    @PostMapping("/list")
    public TableDataInfo<MessageVo> list(@RequestBody MessageDto dto) {
        TableDataInfo<MessageVo> messageList = new TableDataInfo<>();
        // 判断是app，还是sdk和开放平台
        if (StringUtils.equals(Constants.PLATFORM_TYPE_APP, dto.getPlatformType())) {
            if (dto.getType() == null || StringUtils.equals("0", dto.getType())) {
//                dto.setPushUserType(Constants.PUSH_USER_ALL);
                dto.setType(Constants.MESSAGE_TYPE_4);
            }
            messageList = iMessageService.queryPageList(dto);
            for (int i = 0; i < messageList.getRows().size(); i++) {
                MessageVo vo = messageList.getRows().get(i);
                vo.setNoticeType("1");
            }
        } else {
            // sdk和开放平台
            String noticeType = dto.getNoticeType();
            Long userId = 0L;
            //类型必填，为空的时候默认返回公告数据
            if (StringUtils.isEmpty(dto.getNoticeType()))
                noticeType = Constants.NOTICE_TYPE_NOTIFY;

            if (Constants.NOTICE_TYPE_NOTIFY.equals(dto.getNoticeType()))
                userId = LoginHelper.getUserId();

            TableDataInfo<SysNotice> list = remoteNoticeService.selectPageNoticeList(dto.getPlatformType(), noticeType, userId, dto.getPageSize(), dto.getPageNum());
            List<MessageVo> voList = new ArrayList<>();
            long total = 0L;
            boolean isRecordEmpty = true;
            if (list != null && list.getRows() != null && list.getRows().size() > 0) {
                isRecordEmpty = false;
                total = list.getTotal();
                list.getRows().forEach(a -> {
                    MessageVo vo = BeanUtil.toBean(a, MessageVo.class);
                    vo.setId(Convert.toLong(a.getNoticeId()));
                    vo.setTitle(a.getNoticeTitle());
                    vo.setContent(a.getNoticeContent());
                    vo.setNoticeType(a.getNoticeType());
                    voList.add(vo);
                });
            }

            if (isRecordEmpty) {
                messageList.setRows(new ArrayList<>());
            } else {
                messageList.setRows(voList);
            }
            messageList.setTotal(total);
            messageList.setCode(200);
            messageList.setMsg("查询列表数据成功");
        }
        return messageList;
    }

    /**
     * 前台获取消息列表
     */
    @ApiOperation("前台获取消息列表")
    @PostMapping("/frontList")
    public TableDataInfo<MessageVo> frontList(@RequestBody MessageDto dto) {
        TableDataInfo<MessageVo> messageList = new TableDataInfo<>();
        // 判断是app，还是sdk和开放平台
        if (StringUtils.equals(Constants.PLATFORM_TYPE_APP, dto.getPlatformType())) {
            if (ObjectUtil.isNull(dto.getType()) || StringUtils.isBlank(dto.getType())) {
                // 消息类型：4:系统消息
                dto.setType(Constants.MESSAGE_TYPE_4);
            }
            // 系统消息，不需要登录
            if (!StringUtils.equals(Constants.MESSAGE_TYPE_4, dto.getType())) {
                Long userId = LoginHelper.getUserId();
                dto.setUserId(userId);
            }
            messageList = iMessageService.queryFontPageList(dto);
            for (int i = 0; i < messageList.getRows().size(); i++) {
                MessageVo vo = messageList.getRows().get(i);
                vo.setNoticeType("1");
            }
        }
        return messageList;
    }

    /**
     * 获取消息的详细信息
     */
    @ApiOperation("获取消息的详细信息")
    @PostMapping("/getInfo")
    public R<MessageVo> getInfo(@RequestBody IdNameTypeDicDto<Long> idDto) {
        // 参数id校验
        if (ObjectUtil.isNull(idDto.getId())) {
            return R.fail("参数id不能为空");
        }
        // 推送平台校验
        Integer[] typeList = new Integer[]{1, 2, 3};
        Optional<Integer> operationPlatformAny = Arrays.stream(typeList).filter(a -> a.equals(idDto.getType())).findAny();
        if (!operationPlatformAny.isPresent()) {
            return R.fail("推送平台参数错误");
        }
        // 判断是app，还是sdk和开放平台
        if (idDto.getType() == 3) {
            // 获取消息的详细信息
            var messageVo = iMessageService.queryById(idDto.getId());

            // 消息的详情，前端用户只可获取本账户的数据
            if (ObjectUtil.isNotNull(messageVo)) {
                messageVo.setNoticeType("1");
                MessageToUsersDto dto = new MessageToUsersDto();
                dto.setMessageId(messageVo.getId());
                dto.setToUserId(LoginHelper.getUserId());
                var messageToUsers = iMessageToUsersService.selectUnreadMessage(dto);

                if (Constants.PUSH_USER_PART.equals(messageVo.getPushUserType())
                    && ObjectUtil.isNull(messageToUsers))
                    return R.fail("只能查看自己的消息");

                if (messageToUsers != null) {
                    messageToUsers.setReadStatus(Constants.READ);
                    IdTypeDto<String, String> idTypeDto = new IdTypeDto<>();
                    idTypeDto.setType("0");
                    idTypeDto.setId(messageVo.getId().toString());
                    iMessageService.toRead(idTypeDto, LoginHelper.getUserId());
                } else {
                    MessageToUsers users = new MessageToUsers();
                    users.setMessageId(messageVo.getId());
                    users.setReadStatus(Constants.READ);
                    users.setToUserId(LoginHelper.getUserId());
                    iMessageToUsersService.save(users);
                }
            }
            return R.ok(messageVo);
        } else {
            // 获取通知公告详细信息
            var notice = remoteNoticeService.selectNoticeById(Convert.toInt(idDto.getId()));
            if (ObjectUtil.isNotNull(notice)) {
                var messageVo = BeanUtil.toBean(notice, MessageVo.class);
                messageVo.setId(Convert.toLong(notice.getNoticeId()));
                messageVo.setTitle(notice.getNoticeTitle());
                messageVo.setContent(notice.getNoticeContent());
                messageVo.setNoticeType(notice.getNoticeType());

                if (notice != null && Constants.NOTICE_TYPE_PUBLIC.equals(notice.getNoticeType()))
                    return R.ok(messageVo);

                if (UserTypeEnum.SYS_USER.equals(LoginHelper.getUserType()))
                    return R.ok(messageVo);

                // 通知类型的详情，前端用户只可获取本账户的数据
                if (notice != null && Constants.NOTICE_TYPE_NOTIFY.equals(notice.getNoticeType())) {
                    var noticeToUsers = remoteToUsersService.selectUnreadNotice(notice.getNoticeId(), LoginHelper.getUserId(), "0");

                    if (Constants.PUSH_USER_PART.equals(notice.getPushUserType()) && noticeToUsers == null)
                        return R.fail("只能查看自己的消息");

                    if (noticeToUsers != null) {
                        noticeToUsers.setReadStatus(Constants.READ);
                        IdTypeDto<String, String> idTypeDto = new IdTypeDto<>();
                        idTypeDto.setType("0");
                        idTypeDto.setId(notice.getNoticeId().toString());
                        remoteNoticeService.toRead(idTypeDto, LoginHelper.getUserId());
                    } else {
                        NoticeToUsers users = new NoticeToUsers();
                        users.setNoticeId(notice.getNoticeId());
                        users.setReadStatus(Constants.READ);
                        users.setToUserId(LoginHelper.getUserId());
                        remoteToUsersService.save(users);
                    }
                }
                return R.ok(messageVo);
            }
            return R.ok();
        }
    }

    /**
     * 新增消息
     */
    @ApiOperation("新增消息")
    @SaCheckPermission("community:message:add")
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody MessageBo bo) {
        // 判断是app，还是sdk和开放平台
        if (StringUtils.equals(Constants.PLATFORM_TYPE_APP, bo.getPlatformType())) {
            // app默认是消息，没有公告
            bo.setNoticeType("1");
            return iMessageService.insertByBo(bo);
        } else {
            // sdk和开放平台
            SysNotice notice = BeanUtil.toBean(bo, SysNotice.class);
            notice.setNoticeId(Convert.toInt(bo.getId()));
            notice.setNoticeTitle(bo.getTitle());
            notice.setNoticeContent(bo.getContent());
            return remoteNoticeService.insertNotice(notice);
        }
    }

    /**
     * 修改消息
     */
    @ApiOperation("修改消息")
    @SaCheckPermission("community:message:edit")
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody MessageBo bo) {
        // 判断是app，还是sdk和开放平台
        if (StringUtils.equals(Constants.PLATFORM_TYPE_APP, bo.getPlatformType())) {
            // app默认是消息，没有公告
            bo.setNoticeType("1");
            return iMessageService.updateByBo(bo);
        } else {
            // sdk和开放平台
            SysNotice notice = BeanUtil.toBean(bo, SysNotice.class);
            notice.setNoticeId(Convert.toInt(bo.getId()));
            notice.setNoticeTitle(bo.getTitle());
            notice.setNoticeContent(bo.getContent());
            return remoteNoticeService.updateNotice(notice);
        }
    }

    /**
     * 删除消息
     */
    @ApiOperation("删除消息")
    @SaCheckPermission("community:message:remove")
    @PostMapping("/remove")
    public R remove(@Validated(EditGroup.class) @RequestBody MessageTypeDto dto) {
        // 判断是app，还是sdk和开放平台
        if (StringUtils.equals(Constants.PLATFORM_TYPE_APP, dto.getPlatformType())) {
            return iMessageService.deleteMessageById(dto);
        } else {
            // sdk和开放平台
            NoticeTypeDto notice = BeanUtil.toBean(dto, NoticeTypeDto.class);
            return remoteNoticeService.deleteNoticeByIds(notice);
        }
    }

    /**
     * 置顶消息
     * id,逗号分隔
     * type,1:置顶，0：取消置顶
     * name,"1":sdk "2":开放平台 "3":app
     */
    @ApiOperation("置顶消息")
    @SaCheckPermission("community:message:toTop")
    @PostMapping("/toTop")
    public R<Void> toTop(@RequestBody IdNameTypeDicDto idNameTypeDicDto) {
        // 参数id和类型的校验
        if (ObjectUtil.isNull(idNameTypeDicDto.getId()) || ObjectUtil.isNull(idNameTypeDicDto.getType())) {
            return R.fail("参数id和类型不能为空");
        }
        // 推送平台校验
        String[] typeList = new String[]{"1", "2", "3"};
        Optional<String> operationPlatformAny = Arrays.stream(typeList).filter(a -> a.equals(idNameTypeDicDto.getName())).findAny();
        if (!operationPlatformAny.isPresent()) {
            return R.fail("推送平台参数错误");
        }
        IdTypeDto dto = new IdTypeDto();
        dto.setId(idNameTypeDicDto.getId());
        dto.setType(idNameTypeDicDto.getType());

        // 判断是app，还是sdk和开放平台
        if (StringUtils.equals(Constants.PLATFORM_TYPE_APP, idNameTypeDicDto.getName())) {
            return toAjax(iMessageService.toTopMessageByIds(dto));
        } else {
            return toAjax(remoteNoticeService.toTopNoticeByIds(dto));
        }
    }

    /**
     * 设置消息已读
     * id,逗号分隔,全部已读时为空
     * type,0:批量已读，1：全部已读
     * name,"1":sdk "2":开放平台 "3":app
     */
    @ApiOperation("设置消息已读")
    @PostMapping("/toRead")
    public R<Void> toRead(@RequestBody IdNameTypeDicDto dto) {
        // 参数id和类型的校验
        if (ObjectUtil.isNull(dto.getId()) || ObjectUtil.isNull(dto.getType())) {
            return R.fail("参数id和类型不能为空");
        }
        // 推送平台校验
        String[] typeList = new String[]{"1", "2", "3"};
        Optional<String> operationPlatformAny = Arrays.stream(typeList).filter(a -> a.equals(dto.getName())).findAny();
        if (!operationPlatformAny.isPresent()) {
            return R.fail("推送平台参数错误");
        }
        IdTypeDto<String, String> idTypeDto = new IdTypeDto<>();
        idTypeDto.setType(Convert.toStr(dto.getType()));
        idTypeDto.setId(Convert.toStr(dto.getId()));

        // 判断是app，还是sdk和开放平台
        if (StringUtils.equals(Constants.PLATFORM_TYPE_APP, dto.getName())) {
            return toAjax(iMessageService.toRead(idTypeDto, LoginHelper.getUserId()));
        } else {
            return toAjax(remoteNoticeService.toRead(idTypeDto, LoginHelper.getUserId()));
        }
    }

    /**
     * 批量推送
     * id,逗号分隔
     */
    @ApiOperation("批量推送")
    @PostMapping("/toPush")
    public R toPush(@Validated(EditGroup.class) @RequestBody MessageTypeDto dto) {
        // 判断是app，还是sdk和开放平台
        if (StringUtils.equals(Constants.PLATFORM_TYPE_APP, dto.getPlatformType())) {
            return iMessageService.toPush(dto, LoginHelper.getUserId());
        } else {
            NoticeTypeDto noticeTypeDto = BeanUtil.toBean(dto, NoticeTypeDto.class);
            return remoteNoticeService.toPush(noticeTypeDto, LoginHelper.getUserId());
        }
    }

    /**
     * 下载Excel文件
     */
    @ApiOperation("下载Excel文件")
    @PostMapping("/downloadFile")
    public void downloadFile(HttpServletResponse response, @Validated(EditGroup.class) @RequestBody MessageToUsersDto dto) {
        List<MessageToUsersVo> list = new ArrayList<>();
        // 判断是app，还是sdk和开放平台
        if (StringUtils.equals(Constants.PLATFORM_TYPE_APP, dto.getPlatformType())) {
            list = iMessageToUsersService.queryList(dto);
        } else {
            List<NoticeToUsers> noticeList = remoteNoticeService.queryList(Convert.toInt(dto.getMessageId()), dto.getToUserId(), dto.getReadStatus());
            for (int i = 0; i < noticeList.size(); i++) {
                MessageToUsersVo vo = BeanUtil.toBean(noticeList.get(i), MessageToUsersVo.class);
                vo.setMessageId(Convert.toLong(noticeList.get(i).getNoticeId()));
                list.add(vo);
            }
        }
        ExcelUtil.exportExcel(list, "用户名单", MessageToUsersVo.class, response);
    }

    /**
     * 获取用户未读消息数
     */
    @ApiOperation("获取用户未读消息数")
    @PostMapping("/getUnReadCount")
    public R<Map> getUnReadCount(@RequestBody MessageDto dto) {
        // 推送平台校验
        String[] typeList = new String[]{"1", "2", "3"};
        Optional<String> operationPlatformAny = Arrays.stream(typeList).filter(a -> a.equals(dto.getPlatformType())).findAny();
        if (!operationPlatformAny.isPresent()) {
            return R.fail("推送平台参数错误");
        }
        Map map = new HashMap();
        // 点赞数量
        Integer praiseCount = 0;
        // 评论数量
        Integer commentCount = 0;
        // 粉丝数量
        Integer fansCount = 0;
        // 系统消息数量
        Integer systemCount = 0;
        // sdk和开放平台的未读消息数
        Integer totalCount = 0;
        // 判断是app，还是sdk和开放平台
        if (StringUtils.equals(Constants.PLATFORM_TYPE_APP, dto.getPlatformType())) {
            try {
                if (!StringUtils.equals(Constants.MESSAGE_TYPE_4, dto.getType()) && !StringUtils.equals("0", dto.getType())) {
                    dto.setUserId(LoginHelper.getUserId());
                }
                // 消息类型 '1':点赞 '2':评论 '3':新粉丝 '4':系统消息
                praiseCount = iMessageService.getUnReadCount(dto, Constants.MESSAGE_TYPE_1);
                commentCount = iMessageService.getUnReadCount(dto, Constants.MESSAGE_TYPE_2);
                fansCount = iMessageService.getUnReadCount(dto, Constants.MESSAGE_TYPE_3);
                systemCount = iMessageService.getUnReadCount(dto, Constants.MESSAGE_TYPE_4);
            } catch (NotLoginException e) {
                // 未登录时，粉丝/点赞/评论等未读消息数均为0
                if (!StringUtils.equals(Constants.MESSAGE_TYPE_4, dto.getType()) && !StringUtils.equals("0", dto.getType())) {
                    praiseCount = 0;
                    commentCount = 0;
                    fansCount = 0;
                    systemCount = 0;
                }
            }
        } else {
            dto.setUserId(LoginHelper.getUserId());
            totalCount = remoteNoticeService.getUnReadNoticeTotalCount(dto.getPlatformType());
        }

        map.put("app点赞数", praiseCount);
        map.put("app评论数", commentCount);
        map.put("app粉丝数", fansCount);
        map.put("app系统消息数", systemCount);
        map.put("sdk/开放平台: 未读消息数", totalCount);

        return R.ok(map);
    }

    /**
     * 获取用户未读消息数
     */
    @ApiOperation("获取用户未读消息数")
    @PostMapping("/getUnReadMessageCount")
    public R<Integer> getUnReadMessageCount(@RequestBody MessageDto dto) {
        // 推送平台校验
        String[] typeList = new String[]{"1", "2", "3"};
        Optional<String> operationPlatformAny = Arrays.stream(typeList).filter(a -> a.equals(dto.getPlatformType())).findAny();
        if (!operationPlatformAny.isPresent()) {
            return R.fail("推送平台参数错误");
        }
        // sdk和开放平台的未读消息数
        Integer totalCount = 0;
        // 判断是app，还是sdk和开放平台
        if (StringUtils.equals(Constants.PLATFORM_TYPE_APP, dto.getPlatformType())) {
            try {
                if (!StringUtils.equals(Constants.MESSAGE_TYPE_4, dto.getType()) && !StringUtils.equals("0", dto.getType())) {
                    dto.setUserId(LoginHelper.getUserId());
                }
                totalCount = iMessageService.getUnReadMessageCount(dto);
            } catch (NotLoginException e) {
                // 未登录时，粉丝/点赞/评论等未读消息数均为0
                if (!StringUtils.equals(Constants.MESSAGE_TYPE_4, dto.getType()) && !StringUtils.equals("0", dto.getType())) {
                    totalCount = 0;
                }
            }
        } else {
            dto.setUserId(LoginHelper.getUserId());
            totalCount = remoteNoticeService.getUnReadNoticeTotalCount(dto.getPlatformType());
        }


        return R.ok(totalCount);
    }
}
