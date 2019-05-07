package com.bblanqiu.module.alarm.bean;

public class AlarmType {
	public static int STANCE = 1;
	public static int LOCATION = 2;
	public static int LOW_POWER = 3;
	public static int BALL_EXCEPTION = 4;
	public static int BOX_EXCEPTION = 5;
	public static int TEMP_HU_EXCEPTION = 6;
	public static int MATCH_EXCEPTION = 7;
	public static String getAlarm(int type){
		if(type == 1){
			return "姿态告警 ";
		}else if(type==2) {
			return "位置告警";
		}else if(type==3) {
			return "低电量告警";
		}else if(type==4) {
			return "球箱状态异常";
		}else if(type==5) {
			return "储物箱状态异常";
		}else if(type==6) {
			return "温湿度异常";
		}else if(type==7) {
			return "篮球与设备不匹配";
		}else{
			return "";
		}
	}
}
