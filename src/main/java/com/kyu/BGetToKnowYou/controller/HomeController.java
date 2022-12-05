package com.kyu.BGetToKnowYou.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(){
        log.info("Home Controller");
        return "<h1> this is home</h1>";
    }
}
