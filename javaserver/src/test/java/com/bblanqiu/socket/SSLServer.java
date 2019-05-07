//package com.bblanqiu.socket;
//
//import java.net.*;
//
//import javax.net.ssl.*;
//
//import com.bblanqiu.module.socket.Job;
//
//import java.io.*;
//import java.security.*;
//
//public class SSLServer
//{
//	static int port=8266;  //系统将要监听的端口号
//	static SSLServerSocket server;
//
//	/*
//	 *构造函数
//	 */
//
//	public SSLServer()
//	{
//
//	}
//
//
//	/*
//	 *@param port 监听的端口号
//	 *@return 返回一个SSLServerSocket对象
//	 */
//
//	private static SSLServerSocket getServerSocket(int thePort)
//	{
//		SSLServerSocket s=null;
//		try
//		{
//			String key="G:\\work\\bblq\\emqca\\server.keystore";  //要使用的证书名
//
//			char keyStorePass[]="franklin".toCharArray();  //证书密码
//
//			char keyPassword[]="franklin".toCharArray();  //证书别称所使用的主要密码
//
//			KeyStore ks=KeyStore.getInstance("JKS");  //创建JKS密钥库
//
//			ks.load(new FileInputStream(key),keyStorePass);
//
//			//创建管理JKS密钥库的X.509密钥管理器
//			KeyManagerFactory kmf=KeyManagerFactory.getInstance("SunX509");
//
//			kmf.init(ks,keyPassword);
//
//			SSLContext sslContext=SSLContext.getInstance("SSLv3");
//
//			sslContext.init(kmf.getKeyManagers(),null,null);
//
//			//根据上面配置的SSL上下文来产生SSLServerSocketFactory,与通常的产生方法不同
//			SSLServerSocketFactory factory=sslContext.getServerSocketFactory();
//
//			s=(SSLServerSocket)factory.createServerSocket(thePort);
//
//		}catch(Exception e)
//		{
//			System.out.println(e);
//		}
//		return(s);
//	}
//
//
//	public static void main(String args[])
//	{
//		try
//		{
//			server=getServerSocket(port);
//			System.out.println("在”+port+”端口等待连接...");
//
//				//SSLSocket socket=(SSLSocket)server.accept();
//
//				//将得到的socket交给CreateThread对象处理,主线程继续监听
////				new CreateThread(socket);
//				while(true){  
//					Thread tt = new Thread(new Job("ssl",server.accept()));
//					tt.start();
//				} 
//		}catch(Exception e)
//		{e.printStackTrace();
//			System.out.println("main方法错误80:"+e);
//		}
//	}
//}
//
///*
// *内部类,获得主线程的socket连接,生成子线程来处理
// */
//
//class CreateThread extends Thread
//{
//	static BufferedReader in;
//	static PrintWriter out;
//	static Socket s;
//
//	/*
//	 *构造函数,获得socket连接,初始化in和out对象
//	 */
//
//	public CreateThread(Socket socket)
//	{
//		try
//		{
//			s=socket;
//			in=new BufferedReader(new InputStreamReader(s.getInputStream(),"gb2312"));
//
//			out=new PrintWriter(s.getOutputStream(),true);
//
//			start();  //开新线程执行run方法
//
//		}catch(Exception e)
//		{
//			System.out.println(e);
//		}
//
//	}
//
//	/*
//	 *线程方法,处理socket传递过来的数据
//	 */
//
//	public void run()
//	{
//		try
//		{
//			String msg=in.readLine();
//			System.out.println(msg);
//			s.close();
//		}catch(Exception e)
//		{
//			System.out.println(e);
//		}
//	}
//}
