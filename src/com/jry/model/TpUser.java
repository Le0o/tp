package com.jry.model;

public class TpUser {
	private String logName;
	private String password;
	private String userName;
	private String sex;
	private Integer no;
	private String creater;
	private String createtime;
	private String updatetime;
	private String remarks;
	public String getLogName() {
		return logName;
	}
	public void setLogName(String logName) {
		this.logName = logName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Integer getNo() {
		return no;
	}
	public void setNo(Integer no) {
		this.no = no;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Override
	public String toString() {
		return "TpUser [creater=" + creater + ", createtime=" + createtime
				+ ", logName=" + logName + ", no=" + no + ", password="
				+ password + ", remarks=" + remarks + ", sex=" + sex
				+ ", updatetime=" + updatetime + ", userName=" + userName + "]";
	}
}
