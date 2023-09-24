package com.euler.common.core.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @author xZhen
 * @Date 2021/4/9 13:06
 * @Version 1.0
 */
public class AESUtil {

    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /***
     * @version 1.0 aes-128-gcm 加密
     * @params msg 为加密信息 password为32位的16进制key
     * @return 返回base64编码
     **/
    public static String Encrypt(String msg, String password, Integer type) {
        try {
            String instance = "";
            if (type.equals(1)) {
                instance = "AES/GCM/PKCS5Padding";
            } else {
                instance = "AES/ECB/PKCS5Padding";
            }
            //这里请务必用getBytes("UTF-8")，我之前用的一个大佬的getBytes()出现了问题，
            //getBytes()拿的是操作系统编码的字符数组，不一定是utf-8的，会导致后面加密出现问题
            byte[] sSrc = msg.getBytes("UTF-8");
            byte[] sKey = AESUtil.parseHexStr2Byte(password);
            SecretKeySpec skeySpec = new SecretKeySpec(sKey, "AES");
            Cipher cipher = Cipher.getInstance(instance);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            //这边是获取一个随机的iv 默认为12位的
            byte[] iv = cipher.getIV();
            //执行加密
            byte[] encryptData = cipher.doFinal(sSrc);
            //这边进行拼凑 为 iv + 加密后的内容
            byte[] message = new byte[12 + sSrc.length + 16];
            System.arraycopy(iv, 0, message, 0, 12);
            System.arraycopy(encryptData, 0, message, 12, encryptData.length);
            return Base64.getEncoder().encodeToString(message);
        } catch (Exception ex) {
            return null;
        }
    }

    /***
     * @version 1.0 aes-128-gcm 解密
     * @return msg 返回字符串
     */
    public static String Decrypt(String serect, String password, Integer type) {
        try {
            String instance = "";
            if (type.equals(1)) {
                instance = "AES/GCM/PKCS5Padding";
            } else {
                instance = "AES/ECB/PKCS5Padding";
            }
            byte[] sSrc = Base64.getDecoder().decode(serect);
            byte[] sKey = AESUtil.parseHexStr2Byte(password);

            GCMParameterSpec iv = new GCMParameterSpec(128, sSrc, 0, 12);
            Cipher cipher = Cipher.getInstance(instance);
            SecretKey key2 = new SecretKeySpec(sKey, "AES");

            cipher.init(Cipher.DECRYPT_MODE, key2, iv);

            //这边和nodejs不同的一点是 不需要移除后面的16位
            byte[] decryptData = cipher.doFinal(sSrc, 12, sSrc.length - 12);

            return new String(decryptData);
        } catch (Exception ex) {
            return null;
        }
    }
}
