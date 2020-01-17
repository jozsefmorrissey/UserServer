package com.userSrvc.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.userSrvc.client.constant.URI;
import com.userSrvc.server.aop.AopAuthImpl;

@CrossOrigin
@Controller
public class ConstCtrl {
    @RequestMapping(path=URI.CONST_ENDPOINTS, produces="application/json")
    @ResponseBody
    public String uri() {
        return new URI().toString();
    }

    @RequestMapping(path=URI.CONST_AUTH, produces="application/json")
    @ResponseBody
    public String auth() {
        return new AopAuthImpl().toString();
    }
}
