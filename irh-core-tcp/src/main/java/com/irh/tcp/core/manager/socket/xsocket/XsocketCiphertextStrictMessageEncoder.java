/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-24  下午3:02:17
 */
package com.irh.tcp.core.manager.socket.xsocket;

import com.irh.tcp.core.manager.socket.Message;
import com.irh.tcp.core.manager.socket.SocketUtil;
import org.xsocket.connection.INonBlockingConnection;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Xsocket加密器.线程安全单例类.
 *
 * @author iritchie.ren
 */
@SuppressWarnings("unchecked")
final class XsocketCiphertextStrictMessageEncoder extends XsocketStrictCodecFactory.XsocketAbstractEncoder {

    /**
     *
     */
    XsocketCiphertextStrictMessageEncoder() {

    }

    /**
     * 编码
     */
    @Override
    public byte[] doEncode(final INonBlockingConnection session, final Message message) throws Exception {
        // 若存在不同线程给同一玩家发送数据的情况，因此加密过程需要同步处理
        byte[] plainText = message.toByteBuffer().array();

        int length = plainText.length;
        ByteBuffer cipherBuffer = ByteBuffer.allocate(length);

        //获取key
        int[] encryptKey = getKey(session);

        //加密过程
        byte[] cipherText = SocketUtil.encode(plainText, encryptKey);


        cipherBuffer.put(cipherText);

        cipherBuffer.flip();

        return cipherBuffer.array();
    }

    /**
     * 获取当前加解密密钥
     *
     * @param session
     * @return
     */
    private int[] getKey(final INonBlockingConnection session) {
        Map<String, Object> attachments = (Map<String, Object>) session.getAttachment();
        int[] key = (int[]) attachments.get(XsocketStrictCodecFactory.ENCRYPTION_KEY);
        return key;
    }

}
