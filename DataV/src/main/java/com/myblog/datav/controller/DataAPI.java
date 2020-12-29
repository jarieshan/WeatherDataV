package com.myblog.datav.controller;

import com.myblog.datav.selectData.selectData;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class DataAPI {

    @RequestMapping(value="/areaEchartsData")
    @ResponseBody
    public Object areaEchartsData(){
        return selectData.areaEchartsData();
    }

    @RequestMapping(value="/areaEchartsMap")
    @ResponseBody
    public Object areaEchartsMap(){
        return selectData.areaEchartsMap();
    }

    @RequestMapping(value = "/SJZTodayInfo")
    @ResponseBody
    public Object SJZTodayInfo(){
        return selectData.SJZTodayInfo();
    }

    @RequestMapping(value = "/avgAQITop10")
    @ResponseBody
    public Object avgAQITop10(){
        return selectData.avgAQITop10();
    }

    @RequestMapping(value = "/SJZTodayPollution")
    @ResponseBody
    public Object SJZTodayPollution(){
        return selectData.SJZTodayPollution();
    }

    @RequestMapping(value = "/temp")
    @ResponseBody
    public Object temp(){
        return selectData.temp();
    }

    @RequestMapping(value = "/sumRain")
    @ResponseBody
    public Object sumRain(){
        return selectData.sumRain();
    }

}