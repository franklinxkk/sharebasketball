package com.bblanqiu.common.bean;

public class ServerInfoBean {
    private String name;
    private String date;

    public ServerInfoBean(String name, String date) {
        super();
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
