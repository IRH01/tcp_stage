/**
 * Copyright(c) 2015 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  Feb 2, 2015  5:14:59 PM
 */
package com.irh.tcp.core.manager.socket.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

/**
 * @author iritchie.ren
 */
public class MinaKeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {

    /* (non-Javadoc)
     * @see org.apache.mina.filter.keepalive.KeepAliveMessageFactory#isRequest(org.apache.mina.core.session.IoSession, java.lang.Object)
     */
    @Override
    public boolean isRequest(IoSession session, Object message) {
        return false;
    }

    /* (non-Javadoc)
     * @see org.apache.mina.filter.keepalive.KeepAliveMessageFactory#isResponse(org.apache.mina.core.session.IoSession, java.lang.Object)
     */
    @Override
    public boolean isResponse(IoSession session, Object message) {
        return false;
    }

    /* (non-Javadoc)
     * @see org.apache.mina.filter.keepalive.KeepAliveMessageFactory#getRequest(org.apache.mina.core.session.IoSession)
     */
    @Override
    public Object getRequest(IoSession session) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.mina.filter.keepalive.KeepAliveMessageFactory#getResponse(org.apache.mina.core.session.IoSession, java.lang.Object)
     */
    @Override
    public Object getResponse(IoSession session, Object request) {
        return null;
    }

}
