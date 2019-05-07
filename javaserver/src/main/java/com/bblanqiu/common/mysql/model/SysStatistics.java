package com.bblanqiu.common.mysql.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="bblq_statistics")
public class SysStatistics {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Integer id;
	@Column(name = "ball_trade_amount")
	private Float ballTradeAmount;
	@Column(name = "box_trade_amount")
	private Float boxTradeAmount;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Float getBallTradeAmount() {
		return ballTradeAmount;
	}
	public void setBallTradeAmount(Float ballTradeAmount) {
		this.ballTradeAmount = ballTradeAmount;
	}
	public Float getBoxTradeAmount() {
		return boxTradeAmount;
	}
	public void setBoxTradeAmount(Float boxTradeAmount) {
		this.boxTradeAmount = boxTradeAmount;
	}
	@Override
	public String toString() {
		return "SysStatistics [id=" + id + ", ballTradeAmount="
				+ ballTradeAmount + ", boxTradeAmount=" + boxTradeAmount + "]";
	}
	
}
