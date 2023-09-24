package com.euler.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.core.enums.UserTypeEnum;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.system.api.domain.NoticeToUsers;
import com.euler.system.api.domain.NoticeTypeDto;
import com.euler.system.api.domain.SysNotice;
import com.euler.system.api.domain.UserNoticeVo;
import com.euler.system.domain.dto.FrontNoticePageDto;
import com.euler.system.domain.dto.NoticePageDto;
import com.euler.system.domain.dto.NoticeToUsersDto;
import com.euler.system.domain.vo.NoticeToUsersVo;
import com.euler.system.service.INoticeToUsersService;
import com.euler.system.service.ISysNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 公告 信息操作处理
 *
 * @author euler
 */
@Validated
@Api(value = "公告信息控制器", tags = {"公告信息管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/notice")
public class SysNoticeController extends BaseController {

    @Autowired
    private ISysNoticeService noticeService;
    @Autowired
    private INoticeToUsersService iNoticeToUsersService;

    /**
     * 获取通知公告列表
     */
    @ApiOperation("获取通知公告列表")
    @PostMapping("/list")
    public TableDataInfo<SysNotice> list(@RequestBody NoticePageDto noticePageDto) {

        //类型必填，为空的时候默认返回公告数据
        if (StringUtils.isEmpty(noticePageDto.getNoticeType()))
            noticePageDto.setNoticeType(Constants.NOTICE_TYPE_NOTIFY);
        if (LoginHelper.isFront() && Constants.NOTICE_TYPE_NOTIFY.equals(noticePageDto.getNoticeType())) {
            noticePageDto.setPublishUser(LoginHelper.getUserId().toString());
        }
        var noticleList = noticeService.selectPageNoticeList(noticePageDto);
        return noticleList;
    }

    /**
     * 获取通知公告列表
     */
    @ApiOperation("前台获取通知公告列表")
    @PostMapping("/frontList")
    public TableDataInfo<UserNoticeVo> frontList(@RequestBody FrontNoticePageDto noticePageDto) {

        //类型必填，为空的时候默认返回公告数据
        if (StringUtils.isEmpty(noticePageDto.getNoticeType()))
            noticePageDto.setNoticeType(Constants.NOTICE_TYPE_NOTIFY);

        if (Constants.NOTICE_TYPE_NOTIFY.equals(noticePageDto.getNoticeType()))
            noticePageDto.setUserId(LoginHelper.getUserId());
        var noticleList = noticeService.selectFrontPageNoticeList(noticePageDto);
        return noticleList;
    }


    /**
     * 根据通知公告编号获取详细信息
     */
    @ApiOperation("根据通知公告编号获取详细信息")
    @PostMapping(value = "/getInfo")
    public R<SysNotice> getInfo(@RequestBody IdDto<Integer> idDto) {

        // 获取通知公告详细信息
        var notice = noticeService.selectNoticeById(idDto.getId());


        if (notice != null && Constants.NOTICE_TYPE_PUBLIC.equals(notice.getNoticeType()))
            return R.ok(notice);

        if (UserTypeEnum.SYS_USER.equals(LoginHelper.getUserType()))
            return R.ok(notice);


        // 通知类型的详情，前端用户只可获取本账户的数据
        if (notice != null && Constants.NOTICE_TYPE_NOTIFY.equals(notice.getNoticeType())) {
            NoticeToUsersDto dto = new NoticeToUsersDto();
            dto.setNoticeId(notice.getNoticeId());
            dto.setToUserId(LoginHelper.getUserId());
            var noticeToUsers = iNoticeToUsersService.selectUnreadNotice(dto);

            if (Constants.PUSH_USER_PART.equals(notice.getPushUserType()) && noticeToUsers == null)
                return R.fail("只能查看自己的消息");

            if (noticeToUsers != null) {
                noticeToUsers.setReadStatus(Constants.READ);
                IdTypeDto<String, String> idTypeDto = new IdTypeDto<>();
                idTypeDto.setType("0");
                idTypeDto.setId(notice.getNoticeId().toString());
                noticeService.toRead(idTypeDto, LoginHelper.getUserId());
            } else {
                NoticeToUsers users = new NoticeToUsers();
                users.setNoticeId(notice.getNoticeId());
                users.setReadStatus(Constants.READ);
                users.setToUserId(LoginHelper.getUserId());
                iNoticeToUsersService.save(users);
            }
        }

        return R.ok(notice);
    }

    /**
     * 新增通知公告
     */
    @ApiOperation("新增通知公告")
    @SaCheckPermission("system:notice:add")
    @PostMapping("/add")
    public R add(@Validated @RequestBody SysNotice notice) {

        return noticeService.insertNotice(notice);
    }

    /**
     * 修改通知公告
     */
    @ApiOperation("修改通知公告")
    @SaCheckPermission("system:notice:edit")
    @PostMapping("/edit")
    public R edit(@Validated @RequestBody SysNotice notice) {

        return noticeService.updateNotice(notice);
    }

    /**
     * 删除通知公告
     */
    @ApiOperation("删除通知公告")
    @SaCheckPermission("system:notice:remove")
    @PostMapping("/remove")
    public R remove(@RequestBody NoticeTypeDto noticeDto) {

        return noticeService.deleteNoticeByIds(noticeDto);
    }

    /**
     * 置顶公告
     * id,逗号分隔
     * type,1:置顶，0：取消置顶
     */
    @ApiOperation("置顶公告")
    @SaCheckPermission("system:notice:totop")
    @PostMapping("/toTop")
    public R<Void> toTop(@RequestBody IdTypeDto idNameTypeDicDTO) {
        return toAjax(noticeService.toTopNoticeByIds(idNameTypeDicDTO));
    }

    /**
     * 设置通知已读
     * id,逗号分隔,全部已读时为空
     * type,0:批量已读，1：全部已读
     */
    @ApiOperation("设置通知已读")
    @PostMapping("/toRead")
    public R<Void> toRead(@RequestBody IdTypeDto idTypeDto) {

        return toAjax(noticeService.toRead(idTypeDto, LoginHelper.getUserId()));
    }

    /**
     * 批量推送
     * id,逗号分隔
     */
    @ApiOperation("批量推送")
    @PostMapping("/toPush")
    public R toPush(@RequestBody NoticeTypeDto noticeDto) {
        return noticeService.toPush(noticeDto, LoginHelper.getUserId());
    }

    /**
     * 下载Excel文件
     */
    @ApiOperation("下载Excel文件")
    @PostMapping("/downloadFile")
    public void downloadFile(HttpServletResponse response, @RequestBody NoticeToUsersDto dto) throws IOException {
        List<NoticeToUsersVo> list = iNoticeToUsersService.queryList(dto);
        ExcelUtil.exportExcel(list, "用户名单", NoticeToUsersVo.class, response);
    }

    @ApiOperation("获取用户未读消息数")
    @PostMapping("/getUnReadNoticeTotalCount")
    public R<Integer> getUnReadNoticeTotalCount(@RequestBody FrontNoticePageDto noticePageDto) {
        Long userId = LoginHelper.getUserId();
        noticePageDto.setUserId(userId);
        Integer totalCount = noticeService.getUnReadNoticeTotalCount(noticePageDto);
        return R.ok(totalCount);
    }


}
