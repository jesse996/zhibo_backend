package com.example.zhibo.service.impl;

import cn.hutool.json.JSONUtil;
import com.example.zhibo.dao.DouyuDao;
import com.example.zhibo.dto.DouyuDto;
import com.example.zhibo.service.DouyuService;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: jesse
 * @Date: 2021/4/2 10:36 上午
 */
@Service
public class DouyuServiceImpl implements DouyuService {
    @Resource
    RedisTemplate<String, DouyuDao> redisTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    private static final String ROOM_LIST_KEY = "douyu::rooms";

    @Override
    public List<DouyuDto> getAll() {
        ZSetOperations<String, String> ops = stringRedisTemplate.opsForZSet();
        var scan = ops.scan(ROOM_LIST_KEY, ScanOptions.scanOptions().build());
        List<DouyuDto> res = new ArrayList<>();

        while (scan.hasNext()) {
            ZSetOperations.TypedTuple<String> next = scan.next();
            DouyuDto douyu = new DouyuDto();
            DouyuDao dao = JSONUtil.parseObj(next.getValue()).toBean(DouyuDao.class);
            douyu.setCoverImg(dao.getImgUrl());
            douyu.setName(dao.getUsername());
            //href去掉第一个'/'字符
            douyu.setRid(dao.getHref().substring(1));
            douyu.setTitle(dao.getTitle());
            douyu.setScore(next.getScore());
            res.add(douyu);
        }
        return res;
    }

    @Override
    public String getPayUrl() {
        return null;
    }
}
