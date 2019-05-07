package com.bblanqiu.common.util;

public class PageUtil {
	public static int checkLimit(Integer limit, Integer max){
		if(max == null){
			return limit;
		}else{
			return limit > max ? max : limit;
		}
	}
	
	public static int checkLimit(Integer limit){
		Integer max = 10;
		return limit > max ? max : limit;
	}
	public static int checkCursorByPage(Integer cursor, Integer limit, Integer page){
		if(page != null){
			int temp = (page - 1) * limit;
			return temp >= 0 ? temp:0;
		}else{
			return cursor;
		}
	}
	public static long getTotalPage(long total, Integer limit){
		return limit == 0 ? 0 : ((total/limit) + (total%limit > 0 ? 1:0));
	}
}
