package com.euler.payment.utils.common;

import lombok.Data;

/**
 * 已解码的JSON Web签名标头，包含交易或续订信息。
 */
@Data
public class JWSDecodedHeader {

    /**
     * 对JSON Web签名（JWS）进行签名的算法
     */
    private String alg;

    /**
     * 保护JWS的密钥相对应的X.509证书链
     */
    private String[] x5c;

}
