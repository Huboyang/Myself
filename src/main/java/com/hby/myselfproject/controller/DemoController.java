package com.hby.myselfproject.controller;

import com.hby.myselfproject.entity.Shop;
import com.hby.myselfproject.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController {

    @Autowired
    IShopService shopService;

    @GetMapping("test")
    @ResponseBody
    public String test(){
        return "test";
    }

    @GetMapping("")
    @ResponseBody
    public String demo(){
        return "Hello Word!";
    }

    @GetMapping("save")
    public String save(){
        Shop s = new Shop();
        s.setId(5);
        s.setAge(25);
        s.setCupsize("B");
        boolean b = shopService.insert(s);
        System.out.println(b);
        return "success";
    }
}
