/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  May 17, 2014  12:31:03 PM
 */
package com.irh.tcp.core.annotation;

import com.irh.tcp.core.manager.ManagerType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Manager 注解
 *
 * @author iritchie.ren
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Manager {
    /**
     * 模块类型
     *
     * @return
     */
    ManagerType type();

    /**
     * 如果是代理模块，这个表示真实模块的配置信息在game_server.xml里的xpath路径。
     */
    String desc() default "";

    /**
     * 依赖的组件，注意如果依赖出现循环依赖，那么应该 让 循环的依赖依赖另外一个独立的Manager。比如 A->B-C-A 那么出现了循环依赖，
     * 应该让其中两个依赖D,D包含他们共同需要的东西,比如A->D B->D C->D。或者用令牌方式。
     *
     * @return
     */
    ManagerType[] dependences() default ManagerType.NONE;

}
