package com.bblanqiu.module.file;

import java.io.FileInputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bblanqiu.common.util.StreamUtil;
import com.bblanqiu.module.user.controller.Usercontroller;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

@Service
public class QiniuService {
	@Value("${qiniu.key}")
	private String qiniuAccessKey;
	@Value("${qiniu.secret}")
	private String qiniuSecretKey;
	@Value("${qiniu.bucket}")
	private String bucketName;
	@Value("${qiniu.url}")
	private String url;
	private static Logger logger = LoggerFactory.getLogger(QiniuService.class);
	public String uploadFile(String key, InputStream is)throws Exception{
		Configuration cfg = new Configuration(Zone.zone2());
		UploadManager uploadManager = new UploadManager(cfg);
		if(is != null){
			Auth auth = Auth.create(qiniuAccessKey, qiniuSecretKey);
			String upToken = auth.uploadToken(bucketName);
			try {/*InputStream is = new FileInputStream("C:\\Users\\Lenovo\\Desktop\\bblq.184.jpg");*/
			    Response response = uploadManager.put(is, key, upToken, null, null);
			    //解析上传成功的结果
			    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
			    return "http://ops718719.bkt.clouddn.com/"+putRet.key;
			} catch (QiniuException ex) {
			    Response r = ex.response;
			    logger.error(qiniuAccessKey+" "+bucketName+" "+r.toString());
			    try {
			    	logger.error(r.bodyString());
			    } catch (QiniuException ex2) {
			        //ignore
			    }
			}
		}
		return null;
	}
	
	public String getQiniuAccessKey() {
		return qiniuAccessKey;
	}

	public void setQiniuAccessKey(String qiniuAccessKey) {
		this.qiniuAccessKey = qiniuAccessKey;
	}

	public String getQiniuSecretKey() {
		return qiniuSecretKey;
	}

	public void setQiniuSecretKey(String qiniuSecretKey) {
		this.qiniuSecretKey = qiniuSecretKey;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public static void main(String []args)throws Exception{
		String accessKey = "A_FFD7yO69LVqJIyfpDFL_FHl8MykfUIdEdDs5C-";
		String secretKey = "zAvXEfOebTnLWSl_8OMG0JI7oH8kghncA0JiYTrD";
		String bucket = "bblq";
		QiniuService qs = new QiniuService();
		qs.setBucketName(bucket);
		qs.setQiniuAccessKey(accessKey);
		qs.setQiniuSecretKey(secretKey);
		InputStream is = new FileInputStream("C:\\Users\\Lenovo\\Desktop\\bblq.184.jpg");
		System.out.println(qs.uploadFile("111s", is));
	}
}
