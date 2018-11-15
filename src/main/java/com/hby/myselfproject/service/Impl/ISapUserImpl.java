package com.hby.myselfproject.service.Impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hby.myselfproject.dao.SapUserMapper;
import com.hby.myselfproject.entity.SapUser;
import com.hby.myselfproject.service.ISapUserService;
import org.springframework.stereotype.Service;

@Service
public class ISapUserImpl extends ServiceImpl<SapUserMapper, SapUser>  implements ISapUserService {
}
