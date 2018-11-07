package com.hby.myselfproject.controller;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hby.myselfproject.entity.Shop;
import com.hby.myselfproject.service.IShopService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Wrapper;
import java.util.List;

@RestController
public class DemoController {

    @Autowired
    IShopService shopService;

    @RequestMapping("test")
    public String test(){
        return "test";
    }

    @RequestMapping("")
    public String demo(){
        return "Hello Word!";
    }

    @RequestMapping("select")
    public String select(){
        Shop s = new Shop();
        EntityWrapper ew = new EntityWrapper();
        List<Shop> list = shopService.selectList(ew);
        return list.toString();
    }


    @Test
    public void dysjx(){
        for(int i=1;i<5;i++) {
            for (int k = 4; k >= i; k--) {
                System.out.print(" ");
            }
            for (int j = 1; j <= 2 * i - 1; j++) {
                System.out.print("*");
            }
            System.out.print("\n");
        }
    }
}
