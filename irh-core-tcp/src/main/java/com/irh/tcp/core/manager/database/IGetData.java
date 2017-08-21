/**
 * Copyright(c) 2015 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2015年6月27日  上午10:47:17
 */
package com.irh.tcp.core.manager.database;

/**
 * @author near
 */
public interface IGetData {

    /**
     * @param id
     * @return
     */
    <T extends Object> T get(long id);
}
