/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  下午2:56:15
 */
package com.irh.tcp.core.manager.spring;

import com.irh.tcp.core.annotation.Manager;
import com.irh.tcp.core.manager.IManager;
import com.irh.tcp.core.manager.ManagerType;
import com.irh.tcp.core.server.AbstractServer;
import com.irh.tcp.core.util.ProjectPathUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.File;

/**
 * SPRING理器.
 *
 * @author iritchie.ren
 */
@Manager(type = ManagerType.SPRING, desc = "SPRING管理器")
public final class SpringManager implements IManager {
    /**
     * 单例
     */
    private static final SpringManager INSTANCE = new SpringManager();

    /**
     * spring的上下文
     */
    private ApplicationContext springCtx;

    /**
     *
     */
    private SpringManager() {
    }

    @Override
    public void start() throws Exception {
        loadSpringXml();
    }

    @Override
    public void stop() throws Exception {
    }

    /**
     * @return
     */
    public static SpringManager getInstance() {
        return SpringManager.INSTANCE;
    }


    /**
     * 加载config文件
     */
    private void loadSpringXml() throws Exception {
        File springFile = new File(ProjectPathUtil
                .getConfigDirPath() + File.separator
                + System.getProperty(
                AbstractServer.SPRING_FILE_NAME));
        springCtx = new FileSystemXmlApplicationContext(springFile.getCanonicalPath());
    }
}
