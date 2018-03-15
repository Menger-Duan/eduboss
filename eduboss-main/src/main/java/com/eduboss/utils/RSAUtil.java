package com.eduboss.utils;

import org.apache.xmlbeans.impl.util.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
/**
 * Author:JsonLu
 * DateTime:2016/6/23 19:50
 * Email:jsonlu@qq.com
 * Desc:RSA签名验签工具类
 **/
public class RSAUtil {
    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    //私钥 PKCS8格式
    private static final String SUMPAY_PAYMENT_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIkgZuqah8QvZYBmI7ARl7+9DBJP\n" +
            "PD6145FcB58HNnj7lMz5jswxzdSX9sYLSDaNRjFavLuw7/gSxEnXGJHuYHUQ/aAiqdH5OiCq8RrI\n" +
            "zwPDNakMBUSnMAswTJdw8xdNFTAcwlHEBKBeABBF8c21pu0eGTWMuVVOPX+pC0+4HC79AgMBAAEC\n" +
            "gYEAgZVpLFKT8kWH+eqL1xsI+FzCR5sxJ2Hjh74cRaAfTf0RBRLgav61sC1bOpP2zfZ7WrCQoh32\n" +
            "58t0SwNFaMKHyzbdkJRvnqHqP6Rqcv09OzwlwMXAg9s91zEhzplpfF16G+YKbOuAfgttRHbaIdlb\n" +
            "2mR7rs26aWbkaiFjQk09wlECQQDAtPPBrQVe76mWhTOqHAIsaHc7uK938GB5vuRdKjy/4hxMFHSB\n" +
            "XqEPes4d14ld6izk08RBEff7zCKFMff0a1yfAkEAtiovvD1A7nJW4GVkCZ39PB+hcENIqj11Le1A\n" +
            "jDQJumTbO/oXb7tFPzqsPXAG/HYNKAA7q0htq0YfXE7Jd+4y4wJAKA0z4FwKkrmzAJK8sy1RElO/\n" +
            "/EydQ4tVMdiH9AkUSTlAcluJ/11gkiwO7MrgRQGnxQNydae7Z7z8q7w/8a/itwJAPBpJK6tuG/4/\n" +
            "qcPqML4eylKJWlfoxrqGSgPocxnMR+J07yIBz4yTOVxrbc3jkDKG/ras+UNpY8Q8HqGkGVzPDwJB\n" +
            "AJ9Y/zoCZPqbE0Lm1BiIDFOUkFztgi4CaWkb3Wv6ZOA3o1YzmAZIPkWBIG/AXUDy7nyhdoL5qVpM\n" +
            "ZgOogzS7fRo=";
    private static final String SUMPAY_PAYMENT_SERVER_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJIGbqmofEL2WAZiOwEZe/vQwSTzw+teORXAef\n" +
            "BzZ4+5TM+Y7MMc3Ul/bGC0g2jUYxWry7sO/4EsRJ1xiR7mB1EP2gIqnR+TogqvEayM8DwzWpDAVE\n" +
            "pzALMEyXcPMXTRUwHMJRxASgXgAQRfHNtabtHhk1jLlVTj1/qQtPuBwu/QIDAQAB";

    /**
     * RSA签名
     *
     * @param content       待签名数据
     * @param input_charset 编码格式
     * @return 签名值
     */
    public static String sign(String content, String input_charset) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(SUMPAY_PAYMENT_PRIVATE_KEY.getBytes()));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes(input_charset));
            byte[] signed = signature.sign();
            return new String(Base64.encode(signed));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * RSA验签名检查
     *
     * @param content       待签名数据
     * @param sign          签名值
     * @param input_charset 编码格式
     * @return 布尔值
     */
    public static boolean verify(String content, String sign, String input_charset) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(SUMPAY_PAYMENT_SERVER_PUBLIC_KEY.getBytes());
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(input_charset));
            boolean bverify = signature.verify(Base64.decode(sign.getBytes()));
            return bverify;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String [] args){
       System.out.print( sign("yaoyuqi","utf-8"));
       System.out.print(verify("yaoyuqi","NfpeplYrRG/W5BrxJXh/V2q6JjJzRx5N9r3ey6EOj6EiIxAOiNK+od6lmy8HK13bjjc1qLGCK4lmiLf6QRg3QzxR8UQJA7ikN3aUhC0O2nBWR6MMGVwhTSZUHJxBzTB5pkEE3MNBf1Kh9UzCMooWzFVIUWg8rIln01LS7B4nb8A=","utf-8"));
    }
}
