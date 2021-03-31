package com.example.zhibo;

import com.example.zhibo.service.impl.HuyaServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ZhiboApplicationTests {
    @Autowired
    HuyaServiceImpl huyaService;

    @Test
    void contextLoads() {
    }

    @Test
    void testHuya() {
        huyaService.getUrl("kpl");
    }
}
