package com.irh.core.util;

import java.util.List;

/**
 * 二制字符串转long类型的索引工具类
 * @author zk
 * @date 2016年3月17日 上午10:57:19
 */
public final class ReferenceIndexUtil {

	/**
	 *
	 */
	private ReferenceIndexUtil(){

	}

	/**
	 * 二制字符串转long类型
	 * @param index  初始索引值，若没有，则传null
	 * @param location  二进制字符串，从右往左数的位置
	 * @return
	 */
	public static Long locationToIndex(Long index, int location) {
		if (location <= 0) {
			return index;
		}
		if (index == null) {
			index = 0L;
		}
		return index|(long) Math.pow(2, location-1);
	}

	/**
	 * 二制字符串转long类型
	 */
	public static Long locationToIndex(Long index, List<Integer> locationList) {
		if (locationList == null) {
			return index;
		}
		for (Integer location : locationList) {
			index = locationToIndex(index, location);
		}
		return index;
	}

}
