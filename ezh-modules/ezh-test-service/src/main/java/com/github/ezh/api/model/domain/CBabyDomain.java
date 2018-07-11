package com.github.ezh.api.model.domain;

public class CBabyDomain extends BaseDomain{
    private final Integer PAGE_SIZE = 10;
    private String text;
    private String img;
    private String classId;
    private Integer offset;
    private Integer limit;
    private long createDate;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Integer getOffset() {
        return offset == null ? 1 : offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit == null ? PAGE_SIZE : limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
