package com.jry.model;

public class TPConfig {

	private String ip;
	private String subnetmask;//子网掩码
	private String gateway;//网关
	private String equipmentmarking;
	private String creater;
	private String creattime;
	private String remarks;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getSubnetmask() {
		return subnetmask;
	}
	public void setSubnetmask(String subnetmask) {
		this.subnetmask = subnetmask;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	public String getEquipmentmarking() {
		return equipmentmarking;
	}
	public void setEquipmentmarking(String equipmentmarking) {
		this.equipmentmarking = equipmentmarking;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getCreattime() {
		return creattime;
	}
	public void setCreattime(String creattime) {
		this.creattime = creattime;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Override
	public String toString() {
		return "TPConfig [ip=" + ip + ", subnetmask=" + subnetmask
				+ ", gateway=" + gateway + "]";
	}
	
	
}
