package com.hby.myselfproject.service.Impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hby.myselfproject.service.IShopService;
import com.hby.myselfproject.entity.Shop;
import com.hby.myselfproject.dao.ShopMapper;
import org.springframework.stereotype.Service;

@Service
public class IShopServiceImpl extends ServiceImpl<ShopMapper,Shop> implements IShopService{

}
