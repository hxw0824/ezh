package com.github.ezh.api.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

@TableName("c_course" )
public class CCourse extends Model<CCourse> {

    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    private String bookId;
    private String periodId;
    private String resCode;
    private String fileName;
    private String fileUrl;
    private String fileAuffix;
    private String iconUrl;
    private String thumbnailUrl;
    private String content;
    private String tagcode;
    private String tag;
    private String recommendSort;
    private String sort;
    private String clicksNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileAuffix() {
        return fileAuffix;
    }

    public void setFileAuffix(String fileAuffix) {
        this.fileAuffix = fileAuffix;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTagcode() {
        return tagcode;
    }

    public void setTagcode(String tagcode) {
        this.tagcode = tagcode;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getRecommendSort() {
        return recommendSort;
    }

    public void setRecommendSort(String recommendSort) {
        this.recommendSort = recommendSort;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getClicksNum() {
        return clicksNum;
    }

    public void setClicksNum(String clicksNum) {
        this.clicksNum = clicksNum;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
