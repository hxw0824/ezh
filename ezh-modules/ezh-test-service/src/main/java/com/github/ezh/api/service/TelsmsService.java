package com.github.ezh.api.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.api.model.entity.Telsms;

import java.util.List;

public interface TelsmsService extends IService<Telsms> {
    List<Telsms> getByPhone(String phone);
}
