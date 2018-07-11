package com.github.ezh.kinder.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

@TableName("c_relation" )
public class CRelation extends Model<CRelation> {

    public static final String OBJECT_TYPE_CLASS = "CLASS";
    public static final String OBJECT_TYPE_TASK = "TASK";
    public static final String OBJECT_TYPE_RESOURCE = "RESOURCE";

    @TableId(value = "id", type = IdType.UUID)
    private String id;
    private String handleId;
    private String userId;
    private String type;
    private String object;
    private Date createDate;
    private String delFlag;

    public CRelation(String handleId, String userId, String type ,String object) {
        this.handleId = handleId;
        this.userId = userId;
        this.type = type;
        this.object = object;
    }

    public CRelation() {
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHandleId() {
        return handleId;
    }

    public void setHandleId(String handleId) {
        this.handleId = handleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}