package com.corp.jr.dubbo.provider;

import com.corp.jr.dubbo.service.GoodsService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * Created by Administrator on 2023/3/22.
 */
//@Service // 这里的service注解不能使用spring的注解，要使用dubbo中的注解
//@DubboService(interfaceClass = GoodsService.class,version = "1.0.0")
@DubboService(version = "1.0.0", group = "test")
public class GoodsServiceImpl implements GoodsService {
    @Override
    public String getData(String data) {
        System.out.println("getData 方法被访问了！data: " + data);
        return null;
    }

    @Override
    public String getGoods() {
        System.out.println("getGoods 方法被访问了！");
        return null;
    }
}
