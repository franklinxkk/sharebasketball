package com.bblanqiu.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {
	public static final InputStream byte2Input(byte[] buf) {  
		return new ByteArrayInputStream(buf);  
	}  

	public static final byte[] input2byte(InputStream inStream)  
			throws IOException {  
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();  
		byte[] buff = new byte[100];  
		int rc = 0;  
		while ((rc = inStream.read(buff, 0, 100)) > 0) {  
			swapStream.write(buff, 0, rc);  
		}  
		byte[] in2b = swapStream.toByteArray();  
		return in2b;  
	}  
	public static String stream2String(InputStream in) {
        if (in != null) {
            try {
                byte[] b = new byte[1024];
                int size;
                StringBuffer sb = new StringBuffer();
                while ((size = in.read(b)) > 0) {
                    sb.append(new String(b, 0, size, "UTF-8"));
                }
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
