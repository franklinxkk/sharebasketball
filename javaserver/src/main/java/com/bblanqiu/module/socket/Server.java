//package com.bblanqiu.module.socket;
//
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.security.KeyStore;
//import java.util.Properties;
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//
//import javax.net.ssl.KeyManager;
//import javax.net.ssl.KeyManagerFactory;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLServerSocket;
//import javax.net.ssl.SSLServerSocketFactory;
//import javax.net.ssl.SSLSocket;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//@Component
//public class Server {
//	private Logger logger = LoggerFactory.getLogger(Server.class);
//	private SSLServerSocketFactory sslServerSocketFactory;
//	private SSLServerSocket sslServerSocket;
//	private final Executor executor;
//
//	public Server() throws Exception{
//		Integer serverListenPort = 8843;
//		Integer serverThreadPoolSize = 20;
//		Integer serverRequestQueueSize =12;
//		executor = Executors.newFixedThreadPool(serverThreadPoolSize);
//		String certificateChain = "/cert/server.keystore";
//		char[] password = "franklin".toCharArray();
//		SSLContext context = null;  
//		KeyStore ks = KeyStore.getInstance("JKS");  
//		ks.load(Server.class.getResourceAsStream(certificateChain), password);  
//		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");  
//		kmf.init(ks, "franklin".toCharArray());  
//		KeyManager[] km = kmf.getKeyManagers();  
//		context = SSLContext.getInstance("SSL");  
//		context.init(km, null, null);  
//		sslServerSocketFactory = context.getServerSocketFactory();  
//		//只是创建一个TCP连接，SSL handshake还没开始
//		//客户端或服务端第一次试图获取socket输入流或输出流时，
//		//SSL handshake才会开始
//		sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(); 
//		String[] pwdsuits = sslServerSocket.getSupportedCipherSuites();
//		sslServerSocket.setEnabledCipherSuites(pwdsuits);
//		//默认是client mode，必须在握手开始之前调用
//		sslServerSocket.setUseClientMode(false);
//
//		sslServerSocket.setReuseAddress(true);
//		sslServerSocket.setReceiveBufferSize(128*1024);
//		sslServerSocket.setPerformancePreferences(3, 2, 1);
//		sslServerSocket.bind(new InetSocketAddress(serverListenPort),serverRequestQueueSize);
//
//		logger.info("server start at:"+serverListenPort);
//	}
//
//	public void stop(){
//		try {
//			sslServerSocket.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	public void service(){
//		Thread t = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				while(true){
//					SSLSocket socket = null;
//					try{
//						logger.debug("Wait for client request!");
//						socket = (SSLSocket)sslServerSocket.accept();
//						logger.debug("Get client request!");
//
//						Runnable job = new Job("ssl",socket);
//						executor.execute(job);	
//					}catch(Exception e){
//						logger.error("server run exception");
//						try {
//							socket.close();
//						} catch (IOException e1) {
//							e1.printStackTrace();
//						}
//					}
//				}
//			}
//		});
//		t.start();
//
//	}
//
//	public static void main(String[] args) {
//		Server server;
//		try{
//			server = new Server();
//			server.service();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
//}
//
