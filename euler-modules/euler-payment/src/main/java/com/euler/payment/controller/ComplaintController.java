package com.euler.payment.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import com.alipay.api.FileItem;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.payment.bean.ComplainCallable;
import com.euler.common.payment.enums.PayTypeEnumd;
import com.euler.common.payment.factory.PayFactory;
import com.euler.payment.domain.AliComplaint;
import com.euler.payment.domain.AliComplaintFeedback;
import com.euler.payment.domain.ComplaintResponse;
import com.euler.payment.domain.dto.*;
import com.euler.payment.domain.vo.AliComplaintFeedbackVo;
import com.euler.payment.domain.vo.AliComplaintInfoVo;
import com.euler.payment.domain.vo.AliComplaintVo;
import com.euler.payment.domain.vo.ComplaintInfoVo;
import com.euler.payment.service.IAliComplaintFeedbackService;
import com.euler.payment.service.IComplaintResponseService;
import com.euler.payment.service.IComplaintService;
import com.github.binarywang.wxpay.bean.complaint.NegotiationHistoryResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 投诉Controller
 * 前端访问路由地址为:/payment/complaint
 *
 * @author euler
 * @date 2022-03-29
 */
@Validated
@Api(value = "投诉控制器", tags = {"投诉控制器"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/complaint")
public class ComplaintController extends BaseController {
    @Autowired
    private IComplaintService complaintService;
    @Autowired
    private IComplaintResponseService complaintResponseService;
    @Autowired
    private IAliComplaintFeedbackService complaintFeedbackService;


    //region 微信投诉

    @ApiOperation("投诉列表")
    @SaCheckPermission("payment:complaint:list")
    @PostMapping("/list")
    public TableDataInfo<ComplaintInfoVo> list(@RequestBody ComplaintInfoPageDto pageDto) {
        return complaintService.queryPageInfoList(pageDto);
    }


    @ApiOperation("投诉详情")
    @SaCheckPermission("payment:complaint:getInfo")
    @PostMapping("/getInfo")
    public R<ComplaintInfoVo> getInfo(@RequestBody IdDto<String> idDto) {
        var complaintInfoVo = complaintService.queryById(idDto.getId());
        return R.ok(complaintInfoVo);
    }

    @ApiOperation("协商列表")
    @SaCheckPermission("payment:complaint:historyList")
    @PostMapping("/historyList")
    public R<List<NegotiationHistoryResult.NegotiationHistory>> historyList(@RequestBody IdDto<String> idDto) {
        var wxCmplaintService = PayFactory.getComplaintServiceType(PayTypeEnumd.WxComplain.getValue());
        String json = wxCmplaintService.getNegotiationHistoryList(idDto.getId());
        List<NegotiationHistoryResult.NegotiationHistory> histories = JsonHelper.toList(json, NegotiationHistoryResult.NegotiationHistory.class);
        return R.ok(histories);
    }


    @SneakyThrows
    @ApiOperation("微信投诉上传图片")
    @SaCheckPermission("payment:complaint:uploadImg")
    @PostMapping("/uploadImg")
    public R uploadImg(@RequestPart("file") MultipartFile file) {
        if (file == null) {
            throw new ServiceException("上传文件不能为空");
        }
        var wxCmplaintService = PayFactory.getComplaintServiceType(PayTypeEnumd.WxComplain.getValue());

        String mediaId = wxCmplaintService.uploadImg(file.getInputStream(), file.getName());
        if (StringUtils.isNotBlank(mediaId))
            return R.ok("", mediaId);
        return R.fail("上传图片失败");
    }

    @ApiOperation("更新投诉回调地址")
    @SaCheckPermission("payment:complaint:updateNotifyUrl")
    @PostMapping("/updateNotifyUrl")
    public R updateNotifyUrl(@RequestBody KeyValueDto<String> keyValueDto) {
        var wxCmplaintService = PayFactory.getComplaintServiceType(PayTypeEnumd.WxComplain.getValue());

        var aBoolean = wxCmplaintService.updateNotifyUrl(keyValueDto.getValue());
        if (aBoolean)
            return R.ok();
        return R.fail("更新投诉回调地址失败");
    }

    @ApiOperation("投诉回调地址")
    @SaCheckPermission("payment:complaint:getNotifyUrl")
    @PostMapping("/getNotifyUrl")
    public R getNotifyUrl() {
        var wxCmplaintService = PayFactory.getComplaintServiceType(PayTypeEnumd.WxComplain.getValue());

        var aBoolean = wxCmplaintService.queryNotifyUrl();
        if (StringUtils.isNotBlank(aBoolean))
            return R.ok(aBoolean);
        return R.fail("获取投诉回调地址失败");
    }


    @ApiOperation("投诉回复")
    @SaCheckPermission("payment:complaint:response")
    @PostMapping("/response")
    public R response(@RequestBody ComplaintResponse responseVo) {
        if (StringUtils.isBlank(responseVo.getComplaintId())) {
            throw new ServiceException("投诉id不能为空");
        }
        if (StringUtils.isBlank(responseVo.getResponseContent())) {
            throw new ServiceException("回复内容不能为空");
        }

        boolean flag = complaintResponseService.save(responseVo);
        if (flag) {
            var wxCmplaintService = PayFactory.getComplaintServiceType(PayTypeEnumd.WxComplain.getValue());
            wxCmplaintService.response(responseVo.getComplaintId(), responseVo.getResponseContent(), responseVo.getResponseImages(), responseVo.getJumpUrl(), responseVo.getJumpUrlText());
            return R.ok();

        }
        return R.fail("回复失败");

    }


    /**
     * 微信投诉回调
     *
     * @param request
     * @param response
     */
    @ApiOperation("微信投诉回调")
    @PostMapping(value = "/wxNotify")
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) {
        var wxCmplaintService = PayFactory.getComplaintServiceType(PayTypeEnumd.WxComplain.getValue());
        //解析请求参数
        ComplainCallable<Boolean> callable = result -> {
            //处理业务
            return complaintService.insertComplaint(result);
        };
        wxCmplaintService.AsyncNotify(request, response, callable);
    }
    //endregion 微信投诉


    //region 支付投诉

    @ApiOperation("支付宝投诉详情")
    @SaCheckPermission("payment:complaint:getInfo")
    @PostMapping("/aliInfo")
    public AliComplaintInfoVo list(@RequestBody IdDto<String> idDto) {
        return complaintService.queryAliById(idDto.getId());
    }


    @ApiOperation("支付宝投诉列表")
    @SaCheckPermission("payment:complaint:list")
    @PostMapping("/aliList")
    public TableDataInfo<AliComplaintInfoVo> list(@RequestBody AliComplaintPageDto pageDto) {
        return complaintService.queryAliPageList(pageDto);
    }

    /**
     * 支付宝投诉回调
     *
     * @param request
     * @param response
     */
    @ApiOperation("支付宝投诉回调")
    @PostMapping(value = "/aliNotify")
    public void aliNotify(HttpServletRequest request, HttpServletResponse response) {
        var aliCmplaintService = PayFactory.getAliComplaintServiceType(PayTypeEnumd.AliComplain.getValue());
        //解析请求参数
        ComplainCallable<Boolean> callable = result -> {
            //处理业务
            return complaintService.insertAliComplaintStr(result);
        };
        aliCmplaintService.AsyncNotify(request, response, callable);
    }

    @ApiOperation("商家处理交易投诉")
    @SaCheckPermission("payment:complaint:response")
    @PostMapping("/feedback")
    public R feedback(@RequestBody AliComplaintFeedback feedback) {
        if (StringUtils.isBlank(feedback.getComplainEventId())) {
            throw new ServiceException("支付宝侧投诉单号不能为空");
        }
        if (StringUtils.isBlank(feedback.getFeedbackCode())) {
            throw new ServiceException("反馈内容不能为空");
        }
        if (feedback.getFeedbackContent().length() > 200) {
            throw new ServiceException("反馈内容不能超过200字");
        }
        if (StringUtils.isNotBlank(feedback.getOperator()) && feedback.getOperator().length() > 6) {
            throw new ServiceException("处理投诉人，字数不超过6个字");
        }
        // 数据添加
        boolean flag = complaintFeedbackService.save(feedback);
        if (flag) {
            var complaintService = PayFactory.getAliComplaintServiceType(PayTypeEnumd.AliComplain.getValue());
            return complaintService.feedback(feedback.getComplainEventId(), "", feedback.getFeedbackContent(), feedback.getFeedbackImages(), "");
        }
        return R.fail("商家处理交易投诉失败");

    }



    @ApiOperation("商家留言回复列表")
    @SaCheckPermission("payment:complaint:historyList")
    @PostMapping("/feedbackList")
    public TableDataInfo<AliComplaintFeedbackVo> feedbackList(@RequestBody AliComplaintFeedbackPageDto dto) {
        if (StringUtils.isBlank(dto.getComplainEventId())) {
            throw new ServiceException("支付宝侧投诉单号不能为空");
        }
        return complaintFeedbackService.queryPageList(dto);
    }

    @ApiOperation("支付宝投诉--商家留言回复")
    @SaCheckPermission("payment:complaint:response")
    @PostMapping("/reply")
    public R reply(@RequestBody AliComplaintFeedback feedback) {
        if (StringUtils.isBlank(feedback.getComplainEventId())) {
            throw new ServiceException("支付宝侧投诉单号不能为空");
        }
        if (StringUtils.isBlank(feedback.getFeedbackCode())) {
            throw new ServiceException("反馈内容不能为空");
        }
        if (feedback.getFeedbackContent().length() > 200) {
            throw new ServiceException("反馈内容不能超过200字");
        }
        if (StringUtils.isNotBlank(feedback.getOperator()) && feedback.getOperator().length() > 6) {
            throw new ServiceException("处理投诉人，字数不超过6个字");
        }
        // 数据添加
        boolean flag = complaintFeedbackService.save(feedback);
        if (flag) {
            var complaintService = PayFactory.getAliComplaintServiceType(PayTypeEnumd.AliComplain.getValue());
            return complaintService.reply(feedback.getComplainEventId(), feedback.getFeedbackContent(), feedback.getFeedbackImages());

        }
        return R.fail("商家留言回复失败");

    }


    @SneakyThrows
    @ApiOperation("支付宝投诉上传图片")
    @SaCheckPermission("payment:complaint:uploadImg")
    @PostMapping("/uploadAliImg")
    public R<String> uploadAliImg(@RequestPart("file") MultipartFile file) {
        if (file == null) {
            throw new ServiceException("上传文件不能为空");
        }
        var complaintService = PayFactory.getAliComplaintServiceType(PayTypeEnumd.AliComplain.getValue());
        String imgSuffix= FileUtil.getSuffix(file.getName());
        FileItem fileItem=new FileItem(file.getName(),file.getBytes(), file.getContentType());
        return complaintService.uploadImg(fileItem, file.getName());

    }


    //endregion

}
