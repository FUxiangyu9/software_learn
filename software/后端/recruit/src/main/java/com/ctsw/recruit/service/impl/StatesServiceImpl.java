package com.ctsw.recruit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctsw.recruit.mapper.StatesMapper;
import com.ctsw.recruit.pojo.States;
import com.ctsw.recruit.service.StatesService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class StatesServiceImpl extends MPJBaseServiceImpl<StatesMapper, States> implements StatesService {
}
