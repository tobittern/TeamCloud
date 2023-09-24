package com.euler.common.oss.exception;

/**
 * OSS异常类
 *
 * @author euler
 */
public class OssException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OssException(String msg) {
        super(msg);
    }

}
