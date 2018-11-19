package com.hby.myselfproject.service.Impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hby.myselfproject.dao.SapDictMapper;
import com.hby.myselfproject.entity.SapDict;
import com.hby.myselfproject.service.SapWerksService;
import org.springframework.stereotype.Service;

@Service
public class SapWerksImpl extends ServiceImpl<SapDictMapper, SapDict> implements SapWerksService {
}
