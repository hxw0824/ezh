package com.github.ezh.work.model.domain;

public class CWorkDomain extends BaseDomain{
	private long selectTime;
    private String classId;
    private String selUserId;
    private String macid;
    private String signId;
	private String picurl;
	private String picurl1;
    private long signTime;
    private Double signTemp;
    private String signMode;
    private Double lon;
    private Double lat;
    private Integer year;
    private Integer month;

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getSignMode() {
        return signMode;
    }

    public void setSignMode(String signMode) {
        this.signMode = signMode;
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

    public String getSelUserId() {
        return selUserId;
    }

    public void setSelUserId(String selUserId) {
        this.selUserId = selUserId;
    }

    public String getMacid() {
        return macid;
    }

    public void setMacid(String macid) {
        this.macid = macid;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getPicurl1() {
        return picurl1;
    }

    public void setPicurl1(String picurl1) {
        this.picurl1 = picurl1;
    }

    public long getSignTime() {
        return signTime;
    }

    public void setSignTime(long signTime) {
        this.signTime = signTime;
    }

    public Double getSignTemp() {
        return signTemp;
    }

    public void setSignTemp(Double signTemp) {
        this.signTemp = signTemp;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
}
