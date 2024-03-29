package com.corp.jr;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

//使用springBoot微服务框架,test
//@ComponentScan(basePackages = {"com.corp.jr.dao.generator",})
//@ImportResource(locations = {"classpath:spring/applicationContext.xml"})
@PropertySources(value = {@PropertySource("classpath:properties/application.properties"),
        @PropertySource("classpath:properties/env.properties"),
        @PropertySource("classpath:/properties/dubbo-provider.properties")})
@SpringBootApplication(exclude = {org.beetl.sql.starter.BeetlSqlStater.class})
@MapperScan(basePackages = "com.corp.jr.dao.generator")//mapper.xml对应的dao的位置
@EnableDubbo(scanBasePackages = "com.corp.jr.dubbo.provider")//提供服务接口的实现类
@EnableCaching
public class ApplicationApp  extends SpringBootServletInitializer  {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApplicationApp.class);
    }

    public static void main(String[] args) {

        SpringApplication.run(ApplicationApp.class, args);
    }



}
//
//@MapperScan("com.corp.jr.mapper.generator")
//@ComponentScan(basePackages = {"com.corp.jr.service","com.corp.jr.dao.generator","com.corp.jr.service.serviceImpl"})
//@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
//public class MainApplication  extends SpringBootServletInitializer  {
//
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(MainApplication.class);
//    }
//
//    public static void main(String[] args) {
//
//        SpringApplication.run(MainApplication.class, args);
//    }
//
//
//
//}
