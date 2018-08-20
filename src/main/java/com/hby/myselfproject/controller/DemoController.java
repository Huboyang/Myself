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

    //请求参数  (get post)
    @GetMapping("test")
    //返回json数据
    @ResponseBody
    public String test(){
        return "test";
    }

    @GetMapping("")
    @ResponseBody
    public String demo(){
        return "Hello Word!";
    }

    @GetMapping("index")
    public String index(Model model){
        //返回到页面 templates  默认数resources的templates文件夹下面
        model.addAttribute("a","HelloWord");
        return "index";
    }

    @GetMapping("save")
    public String save(){
        Shop s = new Shop();
        s.setAge(25);
        s.setCupsize("B");
        System.out.println(s.toString());
        shopService.insert(s);
        return "success";
    }
}
