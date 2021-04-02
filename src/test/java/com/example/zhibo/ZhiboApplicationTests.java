package com.example.zhibo;

import com.example.zhibo.service.DouyuService;
import com.example.zhibo.service.HuyaService;
import com.example.zhibo.service.impl.HuyaServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ZhiboApplicationTests {
    @Autowired
    HuyaService huyaService;
    @Resource
    DouyuService douyuService;

    @Test
    void contextLoads() {
    }

    @Test
    void testHuya() {
        huyaService.getUrl("kpl");
    }

    @Test
    void douyuGetAll(){
        System.out.println(douyuService.getAll());
    }
}
