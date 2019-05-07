package com.bblanqiu.module.mqtt.converter;

import java.nio.ByteBuffer;

import com.bblanqiu.common.mysql.model.DeviceAlarm;
import com.bblanqiu.common.util.DateUtils;
import com.bblanqiu.module.alarm.bean.AlarmType;
import com.bblanqiu.module.mqtt.bean.DeviceInitBean;
import com.bblanqiu.module.mqtt.bean.OpenLockBean;
import com.bblanqiu.module.mqtt.bean.OtherDataBean;

public class DataConverter {
	public static int SN_LEN = 8;
	public static byte[] packageData(){
	
		return null;
	}
	
	public static DeviceInitBean unWarpDeviceInitData(byte[] packages){
		ByteBuffer bb = ByteBuffer.wrap(packages, 0, packages.length);
		int header = bb.get();
		int type = bb.get();
		int len = bb.get();
		DeviceInitBean dib = new DeviceInitBean();
		dib.setDeviceSn(unwarpString(bb, SN_LEN));
		dib.setBallSn(unwarpString(bb, SN_LEN));
		dib.setImsi(unwarpString(bb, 15));
		dib.setSignal(byte2IntValue(bb.get()));
		dib.setTemp(byte2IntValue(bb.get()));
		dib.setWet(byte2IntValue(bb.get()));
		dib.setPower(byte2IntValue(bb.get()));
		dib.setLongitude(bb.getDouble());
		dib.setLatitude(bb.getDouble());
		dib.setStance(byte2IntValue(bb.get()));
		dib.setTime(bb.getLong());
		return dib;
	}
	public static DeviceInitBean unWarpDeviceData(byte[] packages){
		ByteBuffer bb = ByteBuffer.wrap(packages, 0, packages.length);
		int header = bb.get();
		int type = bb.get();
		int len = bb.get();
		DeviceInitBean dib = new DeviceInitBean();
		dib.setDeviceSn(unwarpString(bb, SN_LEN));
		dib.setSignal(byte2IntValue(bb.get()));
		dib.setTemp(byte2IntValue(bb.get()));
		dib.setWet(byte2IntValue(bb.get()));
		dib.setPower(byte2IntValue(bb.get()));
		dib.setLongitude(bb.getDouble());
		dib.setLatitude(bb.getDouble());
		dib.setStance(byte2IntValue(bb.get()));
		if(len == 46){
			dib.setLockState(unwarpIntString(bb, 9));
		}
		dib.setTime(bb.getLong());
		return dib;
	}
	public static byte[] warpDeviceData(String sn){
		ByteBuffer bb = ByteBuffer.allocate(49)/*.order(ByteOrder.LITTLE_ENDIAN)*/;
		bb.put((byte)0xbb);
		bb.put((byte)0x50);
		bb.put((byte)0x2e);
		char[] sns = sn.toCharArray();
		bb.put((byte)(sns[0]));
		bb.put((byte)(sns[1]));
		bb.put((byte)(sns[2]));
		bb.put((byte)(sns[3]));
		bb.put((byte)(sns[4]));
		bb.put((byte)(sns[5]));
		bb.put((byte)(sns[6]));
		bb.put((byte)(sns[7]));
		bb.put((byte)1);
		bb.put((byte)2);
		bb.put((byte)3);
		bb.put((byte)4);
		bb.putDouble(123d);
		bb.putDouble(123d);
		bb.put((byte)0);
		bb.put((byte)2);
		bb.put((byte)2);
		bb.put((byte)2);
		bb.put((byte)2);
		bb.put((byte)2);
		bb.put((byte)2);
		bb.put((byte)2);
		bb.put((byte)2);
		bb.put((byte)2);
		bb.putLong(DateUtils.getDate().getTime());
		return bb.array();
	}
	public static OtherDataBean unwarpOtherData(byte[] packages){
		ByteBuffer bb = ByteBuffer.wrap(packages, 0, packages.length);
		int header = bb.get();
		int type = bb.get();
		int len = bb.get();
		OtherDataBean odb = new OtherDataBean();
		odb.setSn(unwarpString(bb, SN_LEN));
		if(type == 4){
//			odb.setNum(byte2IntValue(bb.get()));
			odb.setPhone(unwarpString(bb, 11));
			odb.setPass(unwarpString(bb, 32));
		}else if(type == 3){
			if(len > 16){
				odb.setBallSn(unwarpString(bb, SN_LEN));
			}
		}
		odb.setTime(bb.getLong());
		return odb;
	}
	public static OpenLockBean unwarpLockBallData(byte[] packages){
		ByteBuffer bb = ByteBuffer.wrap(packages, 0, packages.length);
		int header = bb.get();
		int type = bb.get();
		int len = bb.get();
		OpenLockBean olb = new OpenLockBean();
		olb.setSn(unwarpString(bb, SN_LEN));
		olb.setBallSn(unwarpString(bb, SN_LEN));
		olb.setTime(DateUtils.getDate());
		return olb;
	}
	public static OpenLockBean unwarpLockBoxData(byte[] packages){
		ByteBuffer bb = ByteBuffer.wrap(packages, 0, packages.length);
		int header = bb.get();
		int type = bb.get();
		int len = bb.get();
		OpenLockBean olb = new OpenLockBean();
		olb.setSn(unwarpString(bb, SN_LEN));
		olb.setNum(byte2IntValue(bb.get()));
		olb.setTime(DateUtils.getDate());
		return olb;
	}
	public static DeviceAlarm unwarpAlarmData(byte[] packages){
		DeviceAlarm dab = new DeviceAlarm();
		ByteBuffer bb = ByteBuffer.wrap(packages, 0, packages.length);
		int header = bb.get();
		int type = bb.get();
		int len = bb.get();
		dab.setSn(unwarpString(bb, SN_LEN));
		dab.setAlarmTime(DateUtils.getDate());
		switch (type) {
		case 32://姿态告警
			dab.setType(AlarmType.STANCE);
			dab.setAlarm(String.valueOf(byte2IntValue(bb.get())));
			break;
		case 33://位置告警
			dab.setType(AlarmType.LOCATION);
			double longitude = bb.getDouble();
			double latitude = bb.getDouble();
			dab.setAlarm(longitude +"&"+latitude);
			break;
		case 34://低电量告警
			dab.setType(AlarmType.LOW_POWER);
			dab.setAlarm(String.valueOf(byte2IntValue(bb.get())));
			break;
		case 35://篮球箱状态异常告警
			dab.setType(AlarmType.BALL_EXCEPTION);
			dab.setAlarm(String.valueOf(byte2IntValue(bb.get())));
			break;
		case 36://储物箱状态异常告警
			dab.setType(AlarmType.BOX_EXCEPTION);
			dab.setAlarm(String.valueOf(byte2IntValue(bb.get())));
			break;
		case 37://温湿度异常告警
			dab.setType(AlarmType.TEMP_HU_EXCEPTION);
			int temp = byte2IntValue(bb.get());
			int humidity = byte2IntValue(bb.get());
			dab.setAlarm(temp+""+humidity);
			break;
		case 38://设备不匹配异常告警
			dab.setType(AlarmType.MATCH_EXCEPTION);
			dab.setAlarm(String.valueOf(byte2IntValue(bb.get())));
			break;
		default:
			break;
		}
		return dab;
		
		
	}
	public static String unwarpString(ByteBuffer bb, int len){
		StringBuffer sb = new StringBuffer();
		for(int j = 1; j <= len; j++){
			sb.append((char)bb.get());
		}
		return sb.toString();
	}
	public static String unwarpIntString(ByteBuffer bb, int len){
		StringBuffer sb = new StringBuffer();
		for(int j = 1; j <= len; j++){
			int i = byte2IntValue(bb.get());
			
			sb.append(i);
		}
		return sb.toString();
	}
	public static byte[] warpDeviceInitCmd(String sn){
		ByteBuffer bb = ByteBuffer.allocate(19)/*.order(ByteOrder.LITTLE_ENDIAN)*/;
		bb.put((byte)0xbb);
		bb.put((byte)0x50);
		bb.put((byte)0x10);
		char[] sns = sn.toCharArray();
		bb.put((byte)(sns[0]));
		bb.put((byte)(sns[1]));
		bb.put((byte)(sns[2]));
		bb.put((byte)(sns[3]));
		bb.put((byte)(sns[4]));
		bb.put((byte)(sns[5]));
		bb.put((byte)(sns[6]));
		bb.put((byte)(sns[7]));
		bb.putLong(DateUtils.getDate().getTime());
		return bb.array();
	}

	public static byte[] warpOpenBallBox(String sn){
		ByteBuffer bb = ByteBuffer.allocate(19)/*.order(ByteOrder.LITTLE_ENDIAN)*/;
		bb.put((byte)0xbb);
		bb.put((byte)0x51);
		bb.put((byte)0x10);
		char[] sns = sn.toCharArray();
		bb.put((byte)(sns[0]));
		bb.put((byte)(sns[1]));
		bb.put((byte)(sns[2]));
		bb.put((byte)(sns[3]));
		bb.put((byte)(sns[4]));
		bb.put((byte)(sns[5]));
		bb.put((byte)(sns[6]));
		bb.put((byte)(sns[7]));
		bb.putLong(DateUtils.getDate().getTime());
		return bb.array();
	}
	public static byte[] warpOpenStorageBox(String sn, int num){
		ByteBuffer bb = ByteBuffer.allocate(20)/*.order(ByteOrder.LITTLE_ENDIAN)*/;
		bb.put((byte)0xbb);
		bb.put((byte)0x52);
		bb.put((byte)0x11);
		char[] sns = sn.toCharArray();
		bb.put((byte)(sns[0]));
		bb.put((byte)(sns[1]));
		bb.put((byte)(sns[2]));
		bb.put((byte)(sns[3]));
		bb.put((byte)(sns[4]));
		bb.put((byte)(sns[5]));
		bb.put((byte)(sns[6]));
		bb.put((byte)(sns[7]));
		bb.put((byte)num);
		bb.putLong(DateUtils.getDate().getTime());
		return bb.array();
	}
	public static byte[] warpAck(String sn,int state){
		ByteBuffer bb = ByteBuffer.allocate(20)/*.order(ByteOrder.LITTLE_ENDIAN)*/;
		bb.put((byte)0xbb);
		bb.put((byte)0x70);
		bb.put((byte)0x11);
		char[] sns = sn.toCharArray();
		bb.put((byte)(sns[0]));
		bb.put((byte)(sns[1]));
		bb.put((byte)(sns[2]));
		bb.put((byte)(sns[3]));
		bb.put((byte)(sns[4]));
		bb.put((byte)(sns[5]));
		bb.put((byte)(sns[6]));
		bb.put((byte)(sns[7]));
		bb.put((byte)state);
		bb.putLong(DateUtils.getDate().getTime());
		return bb.array();
	}
	public static byte[] warpUseBallAck(String sn,String ballSn){
		ByteBuffer bb = ByteBuffer.allocate(27)/*.order(ByteOrder.LITTLE_ENDIAN)*/;
		bb.put((byte)0xbb);
		bb.put((byte)0x71);
		bb.put((byte)0x11);
		char[] sns = sn.toCharArray();
		bb.put((byte)(sns[0]));
		bb.put((byte)(sns[1]));
		bb.put((byte)(sns[2]));
		bb.put((byte)(sns[3]));
		bb.put((byte)(sns[4]));
		bb.put((byte)(sns[5]));
		bb.put((byte)(sns[6]));
		bb.put((byte)(sns[7]));
		char[] snss = ballSn.toCharArray();
		bb.put((byte)(snss[0]));
		bb.put((byte)(snss[1]));
		bb.put((byte)(snss[2]));
		bb.put((byte)(snss[3]));
		bb.put((byte)(snss[4]));
		bb.put((byte)(snss[5]));
		bb.put((byte)(snss[6]));
		bb.put((byte)(snss[7]));
		bb.putLong(DateUtils.getDate().getTime());
		return bb.array();
	}
	public static byte[] warpUseBoxAck(String sn,int num){
		ByteBuffer bb = ByteBuffer.allocate(20)/*.order(ByteOrder.LITTLE_ENDIAN)*/;
		bb.put((byte)0xbb);
		bb.put((byte)0x72);
		bb.put((byte)0x11);
		char[] sns = sn.toCharArray();
		bb.put((byte)(sns[0]));
		bb.put((byte)(sns[1]));
		bb.put((byte)(sns[2]));
		bb.put((byte)(sns[3]));
		bb.put((byte)(sns[4]));
		bb.put((byte)(sns[5]));
		bb.put((byte)(sns[6]));
		bb.put((byte)(sns[7]));
		bb.put((byte)num);
		bb.putLong(DateUtils.getDate().getTime());
		return bb.array();
	}
	public static int byte2IntValue(byte b){
		int i = 0;
		try {
			i = b & 0xff;
		} catch (Exception e) {
			return 0;
		}
		return i;
	}
	public static void main(String []args){
		byte[] b = warpDeviceData("00000001");
		System.out.println(unWarpDeviceData(b));
	}
}
