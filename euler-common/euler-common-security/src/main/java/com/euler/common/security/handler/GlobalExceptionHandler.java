package com.euler.common.security.handler;

import cn.dev33.satoken.exception.IdTokenInvalidException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.hutool.core.util.ObjectUtil;
import com.euler.common.core.constant.HttpStatus;
import com.euler.common.core.domain.R;
import com.euler.common.core.exception.DemoModeException;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.exception.SignBadException;
import com.euler.common.core.exception.user.UserException;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.utils.ServletUtils;
import com.euler.common.security.config.GlobalConfig;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 全局异常处理器
 *
 * @author euler
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private GlobalConfig globalConfig;

    /**
     * 权限码异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public R<Void> handleNotPermissionException(NotPermissionException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限码校验失败", requestURI);
        return R.fail(HttpStatus.FORBIDDEN, "没有访问权限，请联系客服授权");
    }

    /**
     * 角色权限异常
     */
    @ExceptionHandler(NotRoleException.class)
    public R<Void> handleNotRoleException(NotRoleException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',角色权限校验失败", requestURI);
        return R.fail(HttpStatus.FORBIDDEN, "没有访问权限，请联系客服授权");
    }


    /**
     * 签名校验异常
     */
    @ExceptionHandler(SignBadException.class)
    public R<Void> handleNotSignCheckedException(SignBadException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String paras = ServletUtils.getParas(request);
        String msg = e.getMessage();
        if (msg == null && e.getCause() != null)
            msg = e.getCause().getMessage();
        var headers = ServletUtils.getHeaders(request);

        log.error("请求地址：{}，paras：{}，headers:{},签名校验失败", requestURI, paras, JsonHelper.toJson(headers), e);
        return R.fail(HttpStatus.FORBIDDEN, "签名校验失败，" + msg);
    }

    /**
     * 认证失败
     */
    @ExceptionHandler(NotLoginException.class)
    public R<Void> handleNotLoginException(NotLoginException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String paras = ServletUtils.getParas(request);
        String msg = e.getMessage();
        if (msg == null && e.getCause() != null)
            msg = e.getCause().getMessage();
        var headers = ServletUtils.getHeaders(request);
        log.error("请求地址'{}'，paras：{}，headers：{},认证失败,无法访问系统资源,msg：{}", requestURI, paras, JsonHelper.toJson(headers),msg);
        return R.fail(HttpStatus.UNAUTHORIZED, "认证失败，无法访问系统资源,"+msg);
    }

    /**
     * 无效认证
     */
    @ExceptionHandler(IdTokenInvalidException.class)
    public R<Void> handleIdTokenInvalidException(IdTokenInvalidException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String paras = ServletUtils.getParas(request);

        var headers = ServletUtils.getHeaders(request);
        log.error("请求地址'{}'，paras：{}，headers：{},内网认证失败,无法访问系统资源", requestURI, paras, JsonHelper.toJson(headers));
        return R.fail(HttpStatus.UNAUTHORIZED, "认证失败，无法访问系统资源");
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<Void> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                       HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.info("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return R.fail(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public R<Void> handleServiceException(ServiceException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        String msg = e.getMessage();
        if (msg == null && e.getCause() != null)
            msg = e.getCause().getMessage();
        String paras = ServletUtils.getParas(request);
        var headers = ServletUtils.getHeaders(request);

        log.info("业务异常：url：{}，msg:{}，parsms：{}，headers:{}", requestURI, msg, paras, JsonHelper.toJson(headers));
        Integer code = e.getCode();
        return ObjectUtil.isNotNull(code) ? R.fail(code.intValue(), msg) : R.fail(msg);
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(UserException.class)
    public R<Void> handleUserException(UserException e, HttpServletRequest request) {
        String msg = e.getMessage();
        if (msg == null && e.getCause() != null)
            msg = e.getCause().getMessage();
        String requestURI = request.getRequestURI();
        String paras = ServletUtils.getParas(request);
        var headers = ServletUtils.getHeaders(request);

        log.info("用户异常：url：{}，msg：{}，params：{}", requestURI, msg, paras, JsonHelper.toJson(headers));
        return R.fail(msg);
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public R<Void> handleRuntimeException(RuntimeException e, HttpServletRequest request) {

        String msg = e.getMessage();
        if (msg == null && e.getCause() != null)
            msg = e.getCause().getMessage();

        String requestURI = request.getRequestURI();
        String paras = ServletUtils.getParas(request);
        var headers = ServletUtils.getHeaders(request);

        log.error("运行时异常：url：{}，msg：{}，params：{}", requestURI, msg, paras, JsonHelper.toJson(headers), e);

        return R.fail(globalConfig.getEnableExceptionMsg() ? msg : "发生错误，请联系客服");
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e, HttpServletRequest request) {


        String msg = e.getMessage();
        if (msg == null && e.getCause() != null)
            msg = e.getCause().getMessage();

        String requestURI = request.getRequestURI();
        String paras = ServletUtils.getParas(request);
        var headers = ServletUtils.getHeaders(request);

        log.error("系统异常：url：{}，msg：{}，params：{}，headers：{}", requestURI, msg, paras, JsonHelper.toJson(headers), e);
        return R.fail(globalConfig.getEnableExceptionMsg() ? msg : "发生错误，请联系客服");
    }


    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public R<Void> handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return R.fail(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return R.fail(message);
    }

    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public R<Void> handleDemoModeException(DemoModeException e) {
        return R.fail("演示模式，不允许操作");
    }


}
