package com.euler.common.core.exception;

/*
 * @Author:dqw
 * @Description:自定义异常,记录Code和Message
 * @Date:2022-3-9
 * spring 只回滚RuntimeException
 */

import lombok.Getter;
import lombok.Setter;

public class SignBadException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    @Getter
    @Setter
    private Integer code;

    /**
     * 错误提示
     */
    @Getter
    @Setter
    private String message;


    public SignBadException(String message) {
        this.message = message;
    }

    public SignBadException(Integer code) {
        this.code = code;
    }

    public SignBadException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }


}
