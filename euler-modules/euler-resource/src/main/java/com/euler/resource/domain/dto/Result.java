/*
 *
 *      Copyright (c) 2018-2025, emp All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: emp
 *
 */

package com.euler.resource.domain.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author emp
 */
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 返回标记：成功标记=true，失败标记=false
     */
    @Getter
    @Setter
    private Boolean success;

    /**
     * 返回信息
     */
    @Getter
    @Setter
    private String message;

    /**
     * 数据
     */
    @Getter
    @Setter
    private ResultDto<T> result;


    public static <T> Result<T> ok() {
        return restResult(null, true, null);
    }

    public static <T> Result<T> ok(T data) {
        return restResult(data, true, null);
    }

    public static <T> Result<T> ok(T data, String msg) {
        return restResult(data, true, msg);
    }

    public static <T> Result<T> failed() {
        return restResult(null, false, null);
    }

    public static <T> Result<T> failed(String msg) {
        return restResult(null, false, msg);
    }

    public static <T> Result<T> failed(T data, String msg) {
        return restResult(data, false, msg);
    }

    public static <T> Result<T> restResult(T data, Boolean success, String msg) {
        Result<T> apiResult = new Result<>();
        apiResult.setSuccess(success);
        if (data != null) {
            ResultDto<T> resultDto = new ResultDto<>();
            resultDto.setData(data);
            apiResult.setResult(resultDto);
        }
        apiResult.setMessage(msg);
        return apiResult;
    }

}
