package com.example.zhibo.component;

import com.example.zhibo.dto.HuyaDto;
import com.example.zhibo.model.Huya;
import com.example.zhibo.service.HuyaService;
import com.example.zhibo.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Created by jesse on 2021/3/31 上午11:41
 */
//@Component
@Slf4j
@Async
public class HuyaTask {
    @Resource
    HuyaService huyaService;
    @Resource
    RedisService redisService;
    @Resource
    RedisTemplate<String, Huya> redisTemplate;

    static final String KEY = "huya";

    //每小时触发
//    @Scheduled(cron = "0 0 0/1 ? * ?")
//    @Scheduled(cron = "0 0/10 * ? * ?")
    @Scheduled(fixedRate = 1000 * 60 * 60)
    private void scanHuya() {
        int cpuCore = 8;
        long total = huyaService.getCount();
        int size = (int) total / cpuCore;

        for (int i = 0; i < cpuCore; i++) {
            final int finalI = i;
            new Thread(() -> {
                log.info("thread:{},page:{},size:{}", Thread.currentThread().getName(), finalI, size);
                List<Huya> huyaList = huyaService.getAll(finalI, size);
                for (Huya huya : huyaList) {
                    var id = huya.getHref().substring(21);
                    Optional<HuyaDto> optional = huyaService.getUrl(id);
                    SetOperations<String, Huya> opsForSet = redisTemplate.opsForSet();
                    if (optional.isPresent()) {
                        if (Boolean.FALSE.equals(opsForSet.isMember(KEY, huya))) {
                            opsForSet.add(KEY, huya);
                        }
                    } else {
                        if (Boolean.TRUE.equals(opsForSet.isMember(KEY, huya))) {
                            opsForSet.remove(KEY, huya);
                        }
                    }
                }
            }).start();
        }
    }
}
