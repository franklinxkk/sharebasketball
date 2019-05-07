package com.bblanqiu.module.mqtt.bean;

import java.util.Arrays;

public class CmdTransBean {
	private String sn;
	private byte[] cmd;
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public byte[] getCmd() {
		return cmd;
	}
	public void setCmd(byte[] cmd) {
		this.cmd = cmd;
	}
	@Override
	public String toString() {
		return "CmdTransBean [sn=" + sn + ", cmd=" + Arrays.toString(cmd) + "]";
	}
	
}
