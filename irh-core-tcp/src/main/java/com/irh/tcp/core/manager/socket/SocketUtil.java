/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-3-10  上午10:37:43
 */
package com.irh.tcp.core.manager.socket;

import java.util.Arrays;

/**
 * Socket工具类
 *
 * @author iritchie.ren
 */
public final class SocketUtil {
    /**
     * 默认密钥。
     */
    private static final int[] DEFAULT_KEY = new int[]{0xae, 0xbf, 0x56, 0x78, 0xab, 0xcd, 0xef, 0xf1};

    /**
     * 步长种子。
     */
    private static final int STEP_SEED = 1;

    /**
     *
     */
    private SocketUtil() {

    }

    /**
     * 加密的过程
     *
     * @param encryptKey
     * @return
     */
    public static byte[] encode(final byte[] plainText, final int[] encryptKey) {
        int lastCipherByte = 0;
        int length = plainText.length;
        // 加密首字节
        lastCipherByte = (byte) ((plainText[0] ^ encryptKey[0]) & 0xff);
        plainText[0] = (byte) lastCipherByte;
        //加密第二个字节
        encryptKey[1] = (((encryptKey[1] ^ lastCipherByte)) & 0xff);
        lastCipherByte = (((plainText[1] ^ encryptKey[1]) & 0xff) + lastCipherByte) & 0xff;
        plainText[1] = (byte) lastCipherByte;

        // 循环加密,包长度不加密
        int keyLen = encryptKey.length;
        for (int i = 4, j = 0; i < length; i += SocketUtil.STEP_SEED, j++) {
            if (j == keyLen) {
                j = 0;
            }
            encryptKey[j] = (((encryptKey[j] + lastCipherByte) ^ i) & 0xff);
            lastCipherByte = (((plainText[i] ^ encryptKey[j]) & 0xff) + lastCipherByte) & 0xff;
            plainText[i] = (byte) lastCipherByte;
        }
        return plainText;
    }

    /**
     * 解密整段数据
     *
     * @param data
     * @param decryptKey
     * @return
     * @throws Exception
     */
    public static byte[] decode(final byte[] data, final int[] decryptKey) {
        if (data.length == 0) {
            return data;
        }
        int length = data.length;
        int lastCipherByte;
        int plainText;
        int key;

        // 解密首字节
        lastCipherByte = data[0] & 0xff;
        data[0] ^= decryptKey[0];

        //解密第二个字节
        key = ((decryptKey[1] ^ lastCipherByte));
        plainText = (((data[1] & 0xff) - lastCipherByte) ^ key) & 0xff;
        // 更新变量值
        lastCipherByte = data[1] & 0xff;
        data[1] = (byte) plainText;
        decryptKey[1] = (key & 0xff);

        int keyLen = decryptKey.length;
        for (int i = 4, j = 0; i < length; i += SocketUtil.STEP_SEED, j++) {
            if (j == keyLen) {
                j = 0;
            }
            // 解密当前字节
            key = ((decryptKey[j] + lastCipherByte) ^ i);
            plainText = (((data[i] & 0xff) - lastCipherByte) ^ key) & 0xff;
            // 更新变量值
            lastCipherByte = data[i] & 0xff;
            data[i] = (byte) plainText;
            decryptKey[j] = (key & 0xff);
        }
        return data;
    }

    /**
     * 拷贝默认key
     *
     * @return
     */
    public static int[] copyDefaultKey() {
        return Arrays.copyOf(SocketUtil.DEFAULT_KEY, SocketUtil.DEFAULT_KEY.length);
    }
}
