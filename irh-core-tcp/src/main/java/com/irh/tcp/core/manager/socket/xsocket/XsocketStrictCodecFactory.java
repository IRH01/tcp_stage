/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-24  下午3:02:17
 */
package com.irh.tcp.core.manager.socket.xsocket;

import com.irh.tcp.core.config.BaseConfig;
import com.irh.tcp.core.manager.socket.Message;
import org.xsocket.connection.INonBlockingConnection;

import java.io.IOException;

/**
 * Netty 编码,解码器工厂.
 *
 * @author iritchie.ren
 */
public final class XsocketStrictCodecFactory {
    /**
     * 在session中用来存储加密字符串的key
     */
    static final String DECRYPTION_KEY = "DECRYPTION_KEY";
    /**
     * 在session中用来解密的字符串
     */
    static final String ENCRYPTION_KEY = "ENCRYPTION_KEY";

    /**
     *
     */
    private XsocketStrictCodecFactory() {

    }

    /**
     * 获得编码器
     */
    static XsocketAbstractEncoder getEncoder(final Boolean isCiphertext) {
        XsocketAbstractEncoder encoder = null;
        if (BaseConfig.getInstance().getCiphertext()) {
            encoder = new XsocketCiphertextStrictMessageEncoder();
        } else {
            encoder = new XsocketplaintextStrictMessageEncoder();
        }
        return encoder;
    }

    /**
     * 获得解码器
     */
    static XsocketAbstractDecoder getDecoder(final Boolean isCiphertext) {
        XsocketAbstractDecoder decoder = null;
        if (BaseConfig.getInstance().getCiphertext()) {
            decoder = new XsocketCiphertextStrictMessageDecoder();
        } else {
            decoder = new XsocketPlaintextStrictMessageDecoder();
        }
        return decoder;
    }

    /**
     * 抽象解码器
     *
     * @author iritchie.ren
     */
    public abstract static class XsocketAbstractDecoder {
        /**
         * 解码
         */
        public abstract Message doDecode(INonBlockingConnection session)
                throws IOException;
    }

    /**
     * 抽象的编码器.
     *
     * @author iritchie.ren
     */
    public abstract static class XsocketAbstractEncoder {
        /**
         * 编码
         */
        public abstract byte[] doEncode(INonBlockingConnection session,
                                        Message message) throws Exception;
    }

}
