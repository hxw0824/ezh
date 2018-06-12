package com.github.ezh.work.model.dto;

import com.github.ezh.work.model.entity.CWork;
import com.github.ezh.work.model.entity.User;
import com.github.ezh.work.model.entity.Work;
import lombok.Data;

import java.io.Serializable;


@Data
public class WorkObjectDto implements Serializable {
    private CWork cWork;
    private User user;

    public WorkObjectDto(CWork cWork, User user) {
        this.cWork = cWork;
        this.user = user;
    }

    public CWork getcWork() {
        return cWork;
    }

    public void setcWork(CWork cWork) {
        this.cWork = cWork;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
