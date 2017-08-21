/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-24  下午3:02:17
 */
package com.irh.tcp.core.manager.socket.netty;

import com.irh.tcp.core.manager.socket.Message;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.AttributeKey;

/**
 * Netty 编码,解码器工厂.
 *
 * @author iritchie.ren
 */
final class NettyStrictCodecFactory {
    /**
     * 解密密钥netty上下文属性
     */
    static final AttributeKey<int[]> DECRYPTION_KEY = AttributeKey.valueOf("DECRYPTION_KEY");
    /**
     * 加密密钥netty上下文属性
     */
    static final AttributeKey<int[]> ENCRYPTION_KEY = AttributeKey.valueOf("ENCRYPTION_KEY");

    /**
     *
     */
    private NettyStrictCodecFactory() {

    }

    /**
     * 获得编码器
     */
    static MessageToByteEncoder<Message> getEncoder(final Boolean isCiphertext) {
        MessageToByteEncoder<Message> encoder = null;
        if (isCiphertext) {
            encoder = new NettyCiphertextStrictMessageEncoder();
        } else {
            encoder = new NettyPlaintextStrictMessageEncoder();
        }
        return encoder;
    }

    /**
     * 获得解码器
     */
    static ByteToMessageDecoder getDecoder(final Boolean isCiphertext) {
        ByteToMessageDecoder decoder = null;
        if (isCiphertext) {
            decoder = new NettyCiphertextStrictMessageDecoder();
        } else {
            decoder = new NettyPlaintextStrictMessageDecoder();
        }
        return decoder;
    }
}
