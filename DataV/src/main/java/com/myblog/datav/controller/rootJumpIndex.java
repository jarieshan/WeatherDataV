package com.myblog.datav.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class rootJumpIndex {

    @RequestMapping("/")
    public String jump(){
        return "index";
    }

}
