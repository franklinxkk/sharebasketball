package com.bblanqiu.module.sms;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;



import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bblanqiu.common.util.StreamUtil;

@Component
public class MailSender {
    public static final Logger logger = LoggerFactory.getLogger(MailSender.class);

    private String senderName = "support@thingswhisper.net";
    private String senderPassword = "Nicaimima123!@#";
    private String smtpServer = "smtp.exmail.qq.com";
    private String mail = null;
    public String getMail(){
    	return mail;
    }
    private MailSender(){
        InputStream in = MailSender.class.getClassLoader().getResourceAsStream("mail_create.html");
        mail = StreamUtil.stream2String(in);
    }
    public void sendMail(String title,String content,List<String> mailList,List<String> cc) {
        for (String toMail : mailList) {
            try {
                sendHtmlMail(senderName, senderPassword, smtpServer, senderName, senderName, 
                		title, content, toMail, cc);
            } catch (Exception e) {
                logger.error("Send mail to {} with error {}.", toMail, e.getMessage());
            }
        }

    }

    private boolean sendHtmlMail(String user, String password,
                                 String smtpHost, String sendMail, String sendName, String title,
                                 String content, String toMail, List<String> ccMail) throws UnsupportedEncodingException, MessagingException {
        boolean isOk = true;
        Properties props = new Properties();

        SendMailAuthenticator au = new SendMailAuthenticator();
        au.check(user, password); // 认证 (用户名和密码)
        props.put("mail.smtp.host", smtpHost); // 设置smtp服务器
        props.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(props, au);// 创建session

        MimeMessage message = new MimeMessage(session); // 创建message对象

        Address address = new InternetAddress(sendMail, sendName); // 发件人地址
        // 设置收件人
        message.setFrom(address);// 设置发件人
        // 发送目的地址
        InternetAddress[] toAddress = InternetAddress.parse(toMail);
        message.setRecipients(Message.RecipientType.TO, toAddress);
        if (ccMail != null) {
            // 抄送目的地址
            for (String tmp : ccMail) {
                InternetAddress[] toscc = InternetAddress.parse(tmp);
                message.setRecipients(Message.RecipientType.CC, toscc);
            }
        }
        // 下面写邮件内容
        // 设置主题
        message.setSubject(MimeUtility.encodeText(title, "utf-8", "B"));
        message.setSentDate(new Date());// 设置日期
        message.setContent(content, "text/html;charset=utf-8");
        Transport.send(message);
        return isOk;
    }
    public static void main(String []args){
    	MailSender ms = new MailSender();
    	List<String> s = new ArrayList<>();
    	s.add("engourdi@126.com");
    	
    	String mail = ms.mail;
    	mail = mail.replaceFirst("mail_code", RandomStringUtils.random(5, "1234567890"));
    	mail = mail.replaceFirst("mail_to", s.get(0));
    	String email="liang-asd_s#thin-gswhisper.net";
    	System.out.println(email.matches("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$"));
//    	System.out.println(mail);
//    	ms.sendMail("花小白登錄", mail, s, null);
    }
}

