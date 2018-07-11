package com.github.ezh.api.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.api.model.entity.Telsms;

import java.util.List;

public interface TelsmsService extends IService<Telsms> {

    Telsms getLastByPhone(String phone,String type);

    boolean delTelsms(String phone,String type);

}
