package com.corp.utils;

import java.util.*;

public class CommonUtil {

    /**
     * 把所有的参数按照参数名做字典序排序
     * 以'&'符号连接key=val对，例如 key1=val1&key2=val2 记为 str
     * @param params
     * @return
     */
	public static String sortParams(Map<String, String> params) {
		Map<String, String> map = new TreeMap<String, String>(
				new Comparator<String>() {
					public int compare(String obj1, String obj2) {

						return obj1.compareTo(obj2);
					}
				});
		for (String key: params.keySet()) {
			map.put(key, params.get(key));
		}

		Set<String> keySet = map.keySet();
		Iterator<String> iter = keySet.iterator();
		String str = "";
		while (iter.hasNext()) {
			String key = iter.next();
			String value = map.get(key);
			str += key + "=" + value + "&";
		}
		if(str.length()>0){
			str = str.substring(0, str.length()-1);
		}
		return str;
	}
}
