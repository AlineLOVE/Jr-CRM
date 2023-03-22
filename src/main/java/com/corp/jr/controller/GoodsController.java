//package com.corp.jr.controller;
//
//import com.alibaba.dubbo.config.annotation.Reference;
//import com.corp.jr.dubbo.service.GoodsService;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * Created by Administrator on 2023/3/22.
// */
//@RestController
//@RequestMapping("/goods")
//public class GoodsController {
//
//    @Reference //使用dubbo中的@Reference注解
//    private GoodsService goodsService;
//
//    @RequestMapping("/get")
//    public Object getGoods() {
//        return goodsService.getGoods();
//    }
//}
