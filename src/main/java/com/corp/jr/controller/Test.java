package com.corp.jr.controller;

import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mycomputer on 2022-01-19.
 */
public class Test {
    public static  void  main(String[] args){
        String sVersion = SpringVersion.getVersion();
        System.out.println("Spring 版本号：" + sVersion);
        String bVersion = SpringBootVersion.getVersion();
        System.out.println("SpringBoot 版本号：" + bVersion);
        List list = new ArrayList<>();
        list.add(0);
        list.add("1");
        list.add("2");
        list.add("a");
        System.out.println(list);

        for(Object id:list){
            System.out.println(id);

        }
    }
}
