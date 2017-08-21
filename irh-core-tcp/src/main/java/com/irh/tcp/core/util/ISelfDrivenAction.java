/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-6-6  下午6:37:47
 */
package com.irh.tcp.core.util;

/**
 * 标识接口，表示所有可以放在自驱动队列里面的Action。
 *
 * @author iritchie.ren
 */
public interface ISelfDrivenAction {
    /**
     *
     */
    void execute();

}
