package com.ctsw.recruit.service;

import com.ctsw.recruit.pojo.Firm;

public interface FirmService {
    Firm findByAccount(String account);
    void register(String account, String password);
}
