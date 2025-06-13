package com.ctsw.recruit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctsw.recruit.mapper.ResumeMapper;
import com.ctsw.recruit.pojo.Resume;
import com.ctsw.recruit.service.ResumeService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ResumeServiceImpl extends MPJBaseServiceImpl<ResumeMapper,Resume> implements ResumeService {
}
