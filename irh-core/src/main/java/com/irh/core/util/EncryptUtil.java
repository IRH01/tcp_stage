package com.irh.core.util;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * 加密解密工具类
 *
 * @author zk
 * @date 2016年2月23日 下午3:13:03
 */
public final class EncryptUtil {

    /**
     *
     */
    private EncryptUtil() {

    }

    /**
     * @param algorithm
     * @param origin
     * @return
     */
    private static String hash(String algorithm, String origin) {
        return hash(algorithm, origin.getBytes());
    }

    /**
     * @param algorithm
     * @param data
     * @return
     */
    private static String hash(String algorithm, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(data);
            return StringUtil.byteArrayToHexString(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param origin
     * @return
     */
    public static String hashByMD5(String origin) {
        return hash("MD5", origin);
    }

    /**
     * @param origin
     * @return
     */
    public static String hashByMD5(byte[] data) {
        return hash("MD5", data);
    }


    /**
     * @param origin
     * @return
     */
    public static String hashBySHA1(String origin) {
        return hash("SHA-1", origin);
    }

    /**
     * @param origin
     * @param charset
     * @return
     */
    public static String encodeByBase64(String origin, String charset) {
        return Base64.encodeBase64String(origin.getBytes(Charset.forName(charset)));
    }

    /**
     * @param encrypt
     * @param charset
     * @return
     */
    public static String decodeFromBase64(String encrypt, String charset) {
        return new String(Base64.decodeBase64(encrypt), Charset.forName(charset));
    }

    /**
     * @param origin
     * @return
     */
    public static String encodeByBase64(String origin) {
        return encodeByBase64(origin, "UTF-8");
    }

    /**
     * @param encrypt
     * @return
     */
    public static String decodeFromBase64(String encrypt) {
        return decodeFromBase64(encrypt, "UTF-8");
    }
}
