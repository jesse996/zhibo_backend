package com.example.zhibo.component;

import com.example.zhibo.dto.DouyuDto;
import com.example.zhibo.dto.HuyaDto;
import com.example.zhibo.model.Huya;
import com.example.zhibo.service.DouyuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Timer;

@Component
@Slf4j
public class DouyuTask {

    @Resource
    DouyuService douyuService;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Value("${spring.redis.key.douyu.room-set}")
    private String ROOM_LIST_SET_KEY;

    @Value("${spring.redis.key.douyu.room-hash}")
    private String ROOM_LIST_HASH_KEY;

    //每小时触发
//    @Scheduled(cron = "0 0 0/1 ? * ?")
//    @Scheduled(cron = "0 0/10 * ? * ?")
    @Scheduled(fixedRate = 1000 * 60 * 60)
    private void scanHuya() {
        try {
            Runtime.getRuntime().exec("node ./zhibo_spider/src/douyuSpider.js");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
