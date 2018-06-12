package com.github.ezh.work.model.domain;

public class CTemperatureDomain extends BaseDomain{
	private long selectTime;
	private String classId;
	private String selUserId;
	private String temperVal;

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	private String remarks;

	public String getTemperVal() {
		return temperVal;
	}

	public void setTemperVal(String temperVal) {
		this.temperVal = temperVal;
	}

	public String getSelUserId() {
		return selUserId;
	}

	public void setSelUserId(String selUserId) {
		this.selUserId = selUserId;
	}

	public long getSelectTime() {
		return selectTime;
	}

	public void setSelectTime(long selectTime) {
		this.selectTime = selectTime;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}
}
