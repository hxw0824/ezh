package com.github.ezh.kinder.model.domain;

public class TreasuryDomain extends BaseDomain{
    private String column1Code;
    private String column2Code;
    private String periodId;

    private String searchFiled;
    private String resourceId;

    private Integer offset;
    private Integer limit;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getSearchFiled() {
        return searchFiled;
    }

    public void setSearchFiled(String searchFiled) {
        this.searchFiled = searchFiled;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public String getColumn1Code() {
        return column1Code;
    }

    public void setColumn1Code(String column1Code) {
        this.column1Code = column1Code;
    }

    public String getColumn2Code() {
        return column2Code;
    }

    public void setColumn2Code(String column2Code) {
        this.column2Code = column2Code;
    }
}
