package com.danye.aihun.service;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 10:23
 */
@Service
public class WXSignService {

    /**
     * 校验签名
     *
     * @param signature 微信加密签名
     * @param token     开发模式接口配置信息中的 Token
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @return true | false
     */
    public boolean checkSignature(String signature, String token, String timestamp, String nonce) {
        // 对 token、timestamp 和 nonce 按字典顺序排序
        String[] paramArr = new String[]{token, timestamp, nonce};
        Arrays.sort(paramArr);
        // 将排序后的结果拼接成一个字符串
        String content = paramArr[0].concat(paramArr[1]).concat(paramArr[2]);
        String ciphertext = sha_1(content);
        // 将 sha1 加密后的字符串与 signature 进行对比
        return ciphertext != null && ciphertext.equals(signature.toUpperCase());
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray 字节数组
     * @return 字符串
     */
    protected String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param mByte 字节
     * @return 字符串
     */
    private String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        return new String(tempArr);
    }

    /**
     * jsApi获取签名
     * @param jsapi_ticket jsapi_ticket
     * @param nonceStr 随机字符串
     * @param timestamp 时间戳
     * @param url 当前网页的URL，不包含#及其后面部分
     * @return 字符串
     */
    public String getSignature(String jsapi_ticket, String nonceStr, String timestamp, String url) {
        // 这里参数的顺序要按照 key 值 ASCII 码升序排序
        String ascString = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;
        return sha_1(ascString);
    }

    /**
     * SHA-1签名
     */
    private String sha_1(String content) {
        String signature = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            // 对拼接后的字符串进行 sha1 加密
            byte[] digest = md.digest(content.getBytes());
            signature = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return signature;
    }
}
