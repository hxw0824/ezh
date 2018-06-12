package com.github.ezh.admin.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

@TableName("sys_hxw_test" )
public class SysHxwTest extends Model<SysHxwTest> {

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @TableField("name" )
    private String name;

    @TableField("age" )
    private String age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
