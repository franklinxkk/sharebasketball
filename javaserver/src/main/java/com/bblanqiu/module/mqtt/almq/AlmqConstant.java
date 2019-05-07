package com.bblanqiu.module.mqtt.almq;

public class AlmqConstant {
	final static String regionId = "cn-shenzhen";
	/**
	 * 设置当前用户私有的MQTT的接入点。例如此处示意使用XXX，实际使用请替换用户自己的接入点。接入点的获取方法是，在控制台申请MQTT实例，每个实例都会分配一个接入点域名。
	 */
	final static String broker ="tcp://mqtt-cn-mp902t8qh0b.mqtt.aliyuncs.com:1883";
	final static String sslbroker ="ssl://mqtt-cn-mp902t8qh0b.mqtt.aliyuncs.com:8883";
	/**
	 * 设置阿里云的AccessKey，用于鉴权
	 */
	final static String acessKey ="LTAIUF8D4di5eHtC";
	/**
	 * 设置阿里云的SecretKey，用于鉴权
	 */
	final static String secretKey ="tfZBmgwQSkXfSovtiaWA7DQomm3eUs";
	/**
	 * 发消息使用的一级Topic，需要先在MQ控制台里申请
	 */
	final static String topic ="bblq-mqtt-device";
	/**
	 * MQTT的ClientID，一般由两部分组成，GroupID@@@DeviceID
	 * 其中GroupID在MQ控制台里申请
	 * DeviceID由应用方设置，可能是设备编号等，需要唯一，否则服务端拒绝重复的ClientID连接
	 */
	final static String clientIdR ="GID_BBLQ_DEVICE@@@ClientBblq_franklin_rec";
	final static String clientIdS ="GID_BBLQ_DEVICE@@@ClientBblq_franklin_send";
}
