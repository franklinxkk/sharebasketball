//package com.bblanqiu.module.socket;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.Socket;
//import java.nio.ByteBuffer;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class Job implements Runnable{
//	private Socket socket;
//	private String ipAddr;
//	private String sn;
//	private OutputStream os;
//	private static Logger logger = LoggerFactory.getLogger(Job.class);
//	private String sName;
//	
//	public Job(String sName,Socket socket){
//		try {
//			this.sName = sName;
//			this.socket = socket;
//			this.ipAddr = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
//			this.os = socket.getOutputStream();
//			socket.setKeepAlive(true);
//			socket.setSoTimeout(3600000);//if the heartbeat is 5 seconds,timeout should be 1 hour
//		} catch (Exception e) {
//			logger.error("init client socket error addr:{}, msg:{}", ipAddr, e.getMessage());
//		}
//		
//	}
//	
//	/**
//	 * 服务器与硬件通信的包头由两个连续的0xff,0xff构成,过滤非平台的报文
//	 * @param bb
//	 * @return
//	 */
//	public boolean warpHeader(ByteBuffer bb){
//		int count = 0;
//		while(count < 2 && bb.position() < bb.limit()){
//			int dData = bb.get();
//			int header = (byte)0xff;
//			if(dData == header){
//				count ++;
//				
//			}
//		}
//		return count == 2 ? true:false;
//	}
//	
//	/**
//	 * 关闭线程所在socket
//	 */
//	public void close(){
//		try {
//			this.socket.close();
//		} catch (IOException e) {
//			logger.info("socket closed at "+ ipAddr);
//		}
//	}
//	
//	@Override
//	public void run() {
//		try {
//			logger.info("a "+sName+" client connect at "+ ipAddr);
//	        InputStream in =socket.getInputStream();
//	        byte[] bs = new byte[2048];
//	        int recvMsgSize;
//	        while((recvMsgSize = in.read(bs)) != -1){
//	        	try {
//	        		printPackage(recvMsgSize, bs);
//	        		OutputStream os = socket.getOutputStream();
//	        		int type = 3;
//	        		ByteBuffer bb1 = ByteBuffer.allocate(3);
//	        		bb1.put((byte)0xbb);//0xbb服务器数据包包头
//	        		bb1.put((byte)type);
//	        		bb1.put((byte)0x01);
//	        		os.write(bb1.array(), 0, bb1.limit());
//	        		os.write("\n".getBytes());
//	        		os.flush();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//	        }
//	        socket.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * 打印数据包
//	 * @param recvMsgSize
//	 * @param bs
//	 */
//	private void printPackage(int recvMsgSize, byte[] bs){
//		StringBuffer sb = new StringBuffer();
//		for(int i =0; i< recvMsgSize; i++){
//			sb.append(byte2IntValue(bs[i])).append(" ");
//		}
//		logger.info(sb.toString());
//	}
//	public  int byte2IntValue(byte b){
//		int i = 0;
//		try {
//			i = b & 0xff;
//		} catch (Exception e) {
//			return 0;
//		}
//		return i;
//	}
//	public static void main(String []args){
//	}
//}
