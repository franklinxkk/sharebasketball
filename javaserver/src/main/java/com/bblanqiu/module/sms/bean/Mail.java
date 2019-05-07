package com.bblanqiu.module.sms.bean;

import java.io.Serializable;
import java.util.List;

public class Mail implements Serializable {
    private String user;
    private String password;
    private String smtpAuth;
    private String smtpHost;
    private String sendMail;
    private String sendName;
    private String title;
    private String content;
    private String deviceNeworkHostname;
    private List<String> toMailList;
    public List<String> getCcMailList() {
        return ccMailList;
    }

    public void setCcMailList(List<String> ccMailList) {
        this.ccMailList = ccMailList;
    }

    private List<String> ccMailList;

    public List<String> getToMailList() {
        return toMailList;
    }

    public void setToMailList(List<String> toMailList) {
        this.toMailList = toMailList;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSmtpAuth() {
        return smtpAuth;
    }

    public void setSmtpAuth(String smtpAuth) {
        this.smtpAuth = smtpAuth;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getSendMail() {
        return sendMail;
    }

    public void setSendMail(String sendMail) {
        this.sendMail = sendMail;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDeviceNeworkHostname() {
        return deviceNeworkHostname;
    }

    public void setDeviceNeworkHostname(String deviceNeworkHostname) {
        this.deviceNeworkHostname = deviceNeworkHostname;
    }

}

