package com.bblanqiu.module.user.bean;

import java.util.Date;

public class UserScoreBean implements Comparable{
	
	private Integer id;
	private String name;
	private Integer userId;
	private Integer score;
	private Integer type;
	private Date createTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "UserScoreBean [id=" + id + ", name=" + name + ", userId="
				+ userId + ", score=" + score + ", type=" + type
				+ ", createTime=" + createTime + "]";
	}
	@Override
	public int compareTo(Object o) {
		int compareage=((UserScoreBean)o).getId();
        return compareage-this.getId();//倒叙
	}
}
