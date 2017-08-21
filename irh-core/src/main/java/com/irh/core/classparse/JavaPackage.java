/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  下午2:30:02
 */
package com.irh.core.classparse;

/**
 * 
 * @author iritchie.ren
 */
public class JavaPackage {

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private int volatility;

    /**
     * 
     * @param name
     */
    public JavaPackage(String name) {
        this(name, 1);
    }

    /**
     * 
     * @param name
     * @param volatility
     */
    public JavaPackage(String name, int volatility) {
        this.name = name;
        setVolatility(volatility);
    }

    /**
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * @return The package's volatility (0-1).
     */
    public int getVolatility() {
        return volatility;
    }

    /**
     * @param v
     *            Volatility (0-1).
     */
    public void setVolatility(int v) {
        volatility = v;
    }

    /**
     * 
     */
    public boolean equals(Object other) {
        if (other instanceof JavaPackage) {
            JavaPackage otherPackage = (JavaPackage) other;
            return otherPackage.getName().equals(getName());
        }
        return false;
    }

    /**
     * 
     */
    public int hashCode() {
        return getName().hashCode();
    }
}
