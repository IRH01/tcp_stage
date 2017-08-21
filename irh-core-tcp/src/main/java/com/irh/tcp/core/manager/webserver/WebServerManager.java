/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  下午2:56:59
 */
package com.irh.tcp.core.manager.webserver;

import com.irh.tcp.core.annotation.Manager;
import com.irh.tcp.core.manager.IManager;
import com.irh.tcp.core.manager.ManagerType;
import org.eclipse.jetty.server.Server;

/**
 * @author iritchie.ren
 */
@Manager(type = ManagerType.WEB_SERVER, desc = "WEB服务器")
public final class WebServerManager implements IManager {
    /**
     *
     */
    private static final WebServerManager INSTANCE = new WebServerManager();

    /**
     * server
     */
    private Server server;

    /**
     *
     */
    private WebServerManager() {

    }

    /**
     * @return
     */
    public static WebServerManager getInstance() {
        return WebServerManager.INSTANCE;
    }

    /**
     *
     */
    @Override
    public void start() throws Exception {
//              server = new Server();
//        int webPort = BaseConfig.getInstance().getWebPort();
//        Connector connector = new SelectChannelConnector();
//        connector.setPort(webPort);
//        connector.setResponseBufferSize(64000);
//        server.setConnectors(new Connector[]{connector});
//
//        WebAppContext webAppContext = new WebAppContext();
//        webAppContext.setContextPath("/");
//        webAppContext.setDescriptor(ProjectPathUtil.getConfigDirPath()
//                + File.separator
//                + System.getProperty(AbstractServer.WEB_FILE_NAME));
//        webAppContext.setResourceBase("/");
//        webAppContext.setConfigurationDiscovered(true);
//        webAppContext.setParentLoaderPriority(true);
//        webAppContext.setClassLoader(
//                Thread.currentThread().getContextClassLoader());
//        server.setHandler(webAppContext);
//        //启动
//        server.start();
//
//        LogUtil.info("WebServer端口是【{}】", webPort);
    }

    /**
     * 停止
     */
    @Override
    public void stop() throws Exception {
        server.stop();
    }

}
