package com.hby.myselfproject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("abc")
public class TestController{

    @GetMapping("abc")
    public String demo(){
        return "Hello Word!";
    }
}
