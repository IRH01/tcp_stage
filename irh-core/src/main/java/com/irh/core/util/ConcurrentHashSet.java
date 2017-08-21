/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  下午2:30:02
 */
package com.irh.core.util;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @param <E>
 *            元素
 */
public class ConcurrentHashSet<E> extends AbstractSet<E>
                implements Set<E> , java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8672117787651310382L;

    /**
     * 
     */
    private static final Object PRESENT = new Object();

    /**
     * 
     */
    private final ConcurrentHashMap<E, Object> map;

    /**
     * 
     */
    public ConcurrentHashSet() {
        map = new ConcurrentHashMap<E, Object>();
    }

    /**
     * @param initialCapacity
     */
    public ConcurrentHashSet(int initialCapacity) {
        map = new ConcurrentHashMap<E, Object>(initialCapacity);
    }

    /**
     */
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.util.AbstractCollection#size()
     */
    public int size() {
        return map.size();
    }

    /**
     * 
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * 
     */
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    /**
     * 
     */
    public boolean add(E e) {
        return map.put(e, PRESENT) == null;
    }

    /**
     * 
     */
    public boolean addIfAbsent(E e) {
        return map.putIfAbsent(e, PRESENT) == null;
    }

    /**
     * 
     */
    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }

    /**
     * 
     */
    public void clear() {
        map.clear();
    }
}