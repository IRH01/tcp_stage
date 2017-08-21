/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-24  下午3:02:17
 */
package com.irh.tcp.core.manager.socket.mina;

import com.irh.tcp.core.manager.socket.Message;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * Mina编码器-明文.
 *
 * @author iritchie.ren
 */
final class MinaPlaintextStrictMessageEncoder extends ProtocolEncoderAdapter {

    /**
     * 编码
     */
    @Override
    public void encode(final IoSession session, final Object message, final ProtocolEncoderOutput out) throws Exception {
        // 若存在不同线程给同一玩家发送数据的情况，因此加密过程需要同步处理
        Message msg = (Message) message;
        byte[] plainText = msg.toByteBuffer().array();
        int length = plainText.length;
        IoBuffer cipherBuffer = IoBuffer.allocate(length);
        cipherBuffer.put(plainText);
        //写入
        out.write(cipherBuffer.flip());
        out.flush();
    }

}
