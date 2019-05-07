///**
// * 
// */
//package com.bblanqiu.module.socket;
//
//import java.io.IOException;
//import java.net.ServerSocket;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SyncTcpServer {
//	private static Logger logger = LoggerFactory.getLogger(SyncTcpServer.class);
//	private ServerSocket servSocket = null;
//
//	/*@PostConstruct*/
//	public void start(){ 
//		final int servPort = 8888; 
//		Thread t = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					servSocket = new ServerSocket(servPort);  
//					while(true){  
//						Thread tt = new Thread(new Job("normal",servSocket.accept()));
//						tt.start();
//					}
//				} catch (Exception e) {
//					try {
//						if(servSocket != null && !servSocket.isClosed()){
//							logger.info("server sockect closed:" + e.getMessage());
//							servSocket.close();
//						}
//					} catch (IOException ioe) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//		t.start();
//		logger.info("tcp server start at:" + servPort);
//	}
//
//	/*@PreDestroy*/
//	public void shutdown(){
//		try {
//			if(servSocket != null && !servSocket.isClosed()){
//				servSocket.close();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//}
//
