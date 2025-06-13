package com.ctsw.recruit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ctsw.recruit.pojo.Firm;
import com.ctsw.recruit.pojo.States;
import com.github.yulichang.base.MPJBaseService;

public interface FirmService extends MPJBaseService<Firm> {
    Firm findByAccount(String account);
    void register(String account, String password);
}
