package com.github.ezh.work.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.work.model.entity.Card;
import com.github.ezh.work.model.entity.Office;
import com.github.ezh.work.model.entity.Work;

public interface WorkService extends IService<Work> {
    Office getOfficeByName(String name);

    Office getOfficeById(String id);

    Card getCard(String id);

    Work getByDeviceId(String deviceId);

    boolean bindCard(Card card);

    boolean save(Work work);
}
