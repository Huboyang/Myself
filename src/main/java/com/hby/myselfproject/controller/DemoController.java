package com.hby.myselfproject.controller;

import com.hby.myselfproject.entity.Shop;
import com.hby.myselfproject.exception.AesException;
import com.hby.myselfproject.service.IShopService;
import com.hby.myselfproject.utils.WXPublicUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

    @GetMapping("/wx/provingToken")
    public String provingToken(HttpServletRequest request) throws AesException {
        String msgSignature = request.getParameter("signature");
        String msgTimestamp = request.getParameter("timestamp");
        String msgNonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        if (WXPublicUtils.verifyUrl(msgSignature, msgTimestamp, msgNonce)) {
            return echostr;
        }
        return null;
    }
}
