package com.bblanqiu.module.mqtt.almq;

import java.util.List;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.ons.model.v20160503.OnsMqttQueryClientByClientIdRequest;
import com.aliyuncs.ons.model.v20160503.OnsMqttQueryClientByClientIdResponse;
import com.aliyuncs.ons.model.v20160503.OnsRegionListRequest;
import com.aliyuncs.ons.model.v20160503.OnsRegionListResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.bblanqiu.module.mqtt.converter.DataConverter;

public class MqttOpenTool {
	private static String regionId = AlmqConstant.regionId;
	private static String accessKey = AlmqConstant.acessKey;
	private static String secretKey = AlmqConstant.secretKey;
	private static String endPointName = AlmqConstant.regionId;
	private static String productName ="Ons";
	private static String domain ="ons.cn-shenzhen.aliyuncs.com";

	public static void main(String[] args) {
		getClientState("ClientBblq_franklin_rec");
	}
	public static String getOnsByName(String name){
		try {
			DefaultProfile.addEndpoint(endPointName,regionId,productName,domain);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		IClientProfile profile= DefaultProfile.getProfile(regionId,accessKey,secretKey);
		IAcsClient IAcsClient= new DefaultAcsClient(profile);
		OnsRegionListRequest request = new OnsRegionListRequest();
		request.setAcceptFormat(FormatType.JSON);
		request.setPreventCache(System.currentTimeMillis());
		try {
			OnsRegionListResponse response = IAcsClient.getAcsResponse(request);
			List<OnsRegionListResponse.RegionDo> regionDoList=response.getData();
			for (OnsRegionListResponse.RegionDo regionDo:regionDoList){
				if(regionDo.getRegionName().equals(name)){
					return regionDo.getOnsRegionId();
				}
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void getClientState(String clientId){

		/**
		 *根据自己所在的区域选择 Region 后,设置对应的接入点。
		 */
		try {
			DefaultProfile.addEndpoint(endPointName,regionId,productName,domain);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		IClientProfile profile= DefaultProfile.getProfile(regionId,accessKey,secretKey);
		IAcsClient iAcsClient= new DefaultAcsClient(profile);
		OnsMqttQueryClientByClientIdRequest request = new OnsMqttQueryClientByClientIdRequest();
		/**
		 *ONSRegionId 是指你需要 API 访问 MQ 哪个区域的资源。
		 *该值必须要根据 OnsRegionList 方法获取的列表来选择和配置，因为 OnsRegionId 是变动的，不能够写固定值。
		 */
		request.setOnsRegionId(getOnsByName("华南1"));
		request.setPreventCache(System.currentTimeMillis());
		request.setAcceptFormat(FormatType.JSON);
		request.setClientId("GID_BBLQ_DEVICE@@@" + clientId);
		try {
			OnsMqttQueryClientByClientIdResponse response = iAcsClient.getAcsResponse(request);
			OnsMqttQueryClientByClientIdResponse.MqttClientInfoDo clientInfoDo = response.getMqttClientInfoDo();
			System.out.println(clientInfoDo.getOnline() + "  " +
					clientInfoDo.getClientId() + "  " +
					clientInfoDo.getLastTouch() + "  " +
					clientInfoDo.getSocketChannel());
			for (OnsMqttQueryClientByClientIdResponse.MqttClientInfoDo.SubscriptionDo subscriptionDo : clientInfoDo.getSubScriptonData()) {
				System.out.println(subscriptionDo.getParentTopic() + "   " + subscriptionDo.getSubTopic() + "  " + subscriptionDo.getQos());
			}
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}
	public static String getPayload(byte[] data){
		StringBuffer sb = new StringBuffer();
        for(int i=0; i< data.length; i++){
        	sb.append(Integer.toBinaryString(DataConverter.byte2IntValue(data[i]))+" ");
        }
        return sb.toString();
	}
}
