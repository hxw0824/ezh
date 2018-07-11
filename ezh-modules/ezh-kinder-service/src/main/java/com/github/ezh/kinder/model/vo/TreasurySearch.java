package com.github.ezh.kinder.model.vo;

import java.io.Serializable;

public class TreasurySearch implements Serializable {

    private String name;
    private String column1Code;
    private String column2Code;
    private String num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
