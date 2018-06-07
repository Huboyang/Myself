package com.hby.myselfproject.Service.Impl;

import com.hby.myselfproject.Service.ShopService;
import com.hby.myselfproject.dao.ShopMapper;
import com.hby.myselfproject.entity.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    ShopMapper shopMapper;

    @Override
    public void insert(Shop s) {
        System.out.println(s.toString());
        shopMapper.save(s);
    }
}
