package com.github.ezh.api.model.domain;

public class NoticeMessageDomain extends BaseDomain{
    private final Integer PAGE_SIZE = 10;
    private String type;
    private Integer offset;
    private Integer limit;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
