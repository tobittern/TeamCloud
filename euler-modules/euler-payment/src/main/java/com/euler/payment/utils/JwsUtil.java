package com.euler.payment.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.euler.payment.utils.common.JWSDecodedHeader;
import com.euler.payment.utils.common.JWSRenewalInfoDecodedPayload;
import com.euler.payment.utils.common.JWSTransactionDecodedPayload;
import com.euler.payment.utils.common.ResponseBodyV2DecodedPayload;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.io.ByteArrayInputStream;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.util.Base64;

public class JwsUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static ResponseBodyV2DecodedPayload decodePayload(String signedPayload) throws JsonProcessingException, CertificateException {
        DecodedJWT jwt = JWT.decode(signedPayload);
        verify(jwt);
        String payload = new String(Base64.getDecoder().decode(jwt.getPayload()));
        return objectMapper.readValue(payload, ResponseBodyV2DecodedPayload.class);
    }

    private static void verify(DecodedJWT jwt) throws JsonProcessingException, CertificateException {
        String header = new String(Base64.getDecoder().decode(jwt.getHeader()));
        JWSDecodedHeader decodedHeader = objectMapper.readValue(header, JWSDecodedHeader.class);
        PublicKey publicKey = x5c0ToKey(decodedHeader.getX5c()[0]);
        Algorithm.ECDSA256((ECPublicKey) publicKey, null).verify(jwt);
    }

    private static PublicKey x5c0ToKey(String x5c0) throws CertificateException {
        byte[] x5c0Bytes = Base64.getDecoder().decode(x5c0);
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate =
            (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(x5c0Bytes));
        return certificate.getPublicKey();
    }

    public static JWSRenewalInfoDecodedPayload decodeRenewalInfo(String signedRenewalInfo) throws JsonProcessingException, CertificateException {
        DecodedJWT jwt = JWT.decode(signedRenewalInfo);
        verify(jwt);
        String payload = new String(Base64.getDecoder().decode(jwt.getPayload()));
        return objectMapper.readValue(payload, JWSRenewalInfoDecodedPayload.class);
    }

    public static JWSTransactionDecodedPayload decodeTransaction(String signedTransaction) throws JsonProcessingException, CertificateException {
        DecodedJWT jwt = JWT.decode(signedTransaction);
        verify(jwt);
        String payload = new String(Base64.getDecoder().decode(jwt.getPayload()));
        return objectMapper.readValue(payload, JWSTransactionDecodedPayload.class);
    }

}
