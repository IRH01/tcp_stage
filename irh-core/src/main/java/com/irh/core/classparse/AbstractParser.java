/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  下午2:30:02
 */
package com.irh.core.classparse;

import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author iritchie.ren
 */
public abstract class AbstractParser {

    /**
     * 
     */
    public AbstractParser() {

    }

    /**
     * Registered parser listeners are informed that the resulting
     * <code>JavaClass</code> was parsed.
     */
    public abstract JavaClass parse(InputStream is) throws IOException;
}