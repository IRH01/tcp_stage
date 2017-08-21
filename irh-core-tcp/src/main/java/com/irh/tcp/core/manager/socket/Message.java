/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-24  下午3:39:44
 */
package com.irh.tcp.core.manager.socket;

import java.nio.ByteBuffer;

/**
 * Socket消息.
 * head(2个字节)+长度(2个字节)+code(2个字节)+类型(1个字节)+body
 *
 * @author iritchie.ren
 */
public final class Message {

    /**
     * 包括：协议体的头部，协议长度 ,协议号,playerId
     */
    public static final short HEAD_SIZE = 7;

    /**
     * 包的标识 32766
     */
    public static final short HEADER = 0x7ffe;

    /**
     * 协议号
     */
    private short code;

    /**
     * 类型
     */
    private byte type = 0;

    /**
     * 包体数据
     */
    private byte[] bodyData = null;

    /**
     * 私有构造方法，只能build中使用
     */
    private Message() {
    }

    /**
     * 构造方法
     *
     * @param code 协议号
     */
    public static Message createMessage(final byte type, final short code) {
        Message message = new Message();
        message.type = type;
        message.code = code;
        return message;
    }

    /**
     * 由字节构建包体
     *
     * @param dataBytes 这个数组中，第一个short是整个包的长度，第二个short是协议编号
     * @return
     */
    public static Message parse(final byte[] dataBytes) {
        //如果长度不够，返回
        if (dataBytes.length < Message.HEAD_SIZE) {
            return null;
        }

        Message message = new Message();

        ByteBuffer byteBuffer = ByteBuffer.wrap(dataBytes);
        //跳过包体标识，跳过包长度
        byteBuffer.position(4);
        message.code = byteBuffer.getShort();
        message.type = byteBuffer.get();
        int bodyLen = dataBytes.length - Message.HEAD_SIZE;
        //有些协议只需要发送消息头就好
        if (dataBytes.length > Message.HEAD_SIZE) {
            message.bodyData = new byte[bodyLen];
            byteBuffer.get(message.bodyData, 0, bodyLen);
        }
        return message;
    }

    /**
     * 数据包转换为ByteBuffer，包括包头和包体。
     *
     * @return
     */
    public ByteBuffer toByteBuffer() {
        short len = Message.HEAD_SIZE;
        if (this.bodyData != null) {
            len += (short) this.bodyData.length;
        }
        if (len <= 0) {
            throw new IllegalArgumentException(String.format(
                    "发送协议号为[%d]的包,长度小于等于0[%d]，长度=包头长度[10]+body长度[%d]",
                    this.code, len, (short) this.bodyData.length));
        }
        ByteBuffer buff = ByteBuffer.allocate(len);
        // 插入包头
        buff.putShort(Message.HEADER);

        // 插入长度
        buff.putShort(len);
        // 协议码
        buff.putShort(code);
        // 类型
        buff.put(type);
        if (bodyData != null) {
            buff.put(bodyData);
        }
        buff.flip();
        return buff;
    }

    /**
     * 获取包体，包体允许为null。
     *
     * @return
     */
    public byte[] getBody() {
        return bodyData;
    }

    /**
     * 设置包体，包体允许为null。
     *
     * @param bytes
     */
    public void setBody(final byte[] bytes) {
        this.bodyData = bytes;
    }

    /**
     * @return the type
     */
    public byte getType() {
        return type;
    }

    /**
     * @return the code
     */
    public short getCode() {
        return code;
    }

    /**
     * @return
     */
    public int getBodyLength() {
        if (bodyData != null) {
            return bodyData.length;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Message [code=" + code + ", type=" + type + "]";
    }

}
