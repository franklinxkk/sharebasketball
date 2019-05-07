package com.bblanqiu.common.util;

public class CostUtil {
	public static float getCost(int x){
		float sum = 0f;
		if(x <= 30){
			sum = 0.5f;
		}else{
			sum = (x/30)*0.5f;
			if(x%30 > 0){
				sum +=0.5;
			}
		}
		return sum;
	}
}
