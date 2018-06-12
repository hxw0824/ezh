package com.github.ezh.work.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

@TableName("c_temperature" )
public class CTemperature extends Model<CTemperature> {

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @TableField("user_id" )
    private String userId;

    @TableField("class_id" )
    private String classId;

    @TableField("office_id" )
    private String officeId;

    @TableField("temper_val" )
    private String temperVal;

    @TableField("create_date" )
    private Date createDate;

    @TableField("update_date" )
    private Date updateDate;

    @TableField("remarks" )
    private String remarks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemperVal() {
        return temperVal;
    }

    public void setTemperVal(String temperVal) {
        this.temperVal = temperVal;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
