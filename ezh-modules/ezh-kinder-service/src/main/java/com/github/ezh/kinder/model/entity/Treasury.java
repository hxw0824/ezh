package com.github.ezh.kinder.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;

import java.io.Serializable;

public class Treasury extends Model<Treasury> {

    private String id;
    private String resCode;
    private String periodId;
    private String periodName;
    private String item1Code;
    private String item1Name;
    private String item2Code;
    private String item2Name;
    private String item3Code;
    private String item3Name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public String getItem1Code() {
        return item1Code;
    }

    public void setItem1Code(String item1Code) {
        this.item1Code = item1Code;
    }

    public String getItem1Name() {
        return item1Name;
    }

    public void setItem1Name(String item1Name) {
        this.item1Name = item1Name;
    }

    public String getItem2Code() {
        return item2Code;
    }

    public void setItem2Code(String item2Code) {
        this.item2Code = item2Code;
    }

    public String getItem2Name() {
        return item2Name;
    }

    public void setItem2Name(String item2Name) {
        this.item2Name = item2Name;
    }

    public String getItem3Code() {
        return item3Code;
    }

    public void setItem3Code(String item3Code) {
        this.item3Code = item3Code;
    }

    public String getItem3Name() {
        return item3Name;
    }

    public void setItem3Name(String item3Name) {
        this.item3Name = item3Name;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
