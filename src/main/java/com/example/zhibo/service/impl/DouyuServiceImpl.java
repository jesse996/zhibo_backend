package com.example.zhibo.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.zhibo.dto.DouyuDto;
import com.example.zhibo.service.DouyuService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: jesse
 * @Date: 2021/4/2 10:36 上午
 */
@Service
//@PropertySource("classpath:application.yaml")
public class DouyuServiceImpl implements DouyuService {
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Value("${spring.redis.key.douyu.room-set}")
    private String ROOM_LIST_SET_KEY;

    @Value("${spring.redis.key.douyu.room-hash}")
    private String ROOM_LIST_HASH_KEY;

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public List<DouyuDto> getAll() {
        ZSetOperations<String, String> ops = stringRedisTemplate.opsForZSet();
        var scan = ops.scan(ROOM_LIST_SET_KEY, ScanOptions.scanOptions().count(200).build());
        List<DouyuDto> res = new ArrayList<>();

        while (scan.hasNext()) {
            ZSetOperations.TypedTuple<String> next = scan.next();
            DouyuDto douyuDto = new DouyuDto();
            String rid = next.getValue();
            assert rid != null;
            Object o = stringRedisTemplate.opsForHash().get(ROOM_LIST_HASH_KEY, rid);
            JSONObject jsonObject = JSONUtil.parseObj(o);
            douyuDto.setScore(next.getScore());
            douyuDto.setRid(rid);
            douyuDto.setTitle(jsonObject.getStr("title"));
            douyuDto.setName(jsonObject.getStr("username"));
            douyuDto.setCoverImg(jsonObject.getStr("coverImg"));
            res.add(douyuDto);
        }
        return res;
    }

    @Override
    public String getPlayUrl(String rid) {
        String res = null;
        Request request = new Request.Builder()
                .url("http://localhost:3000/douyu/" + rid)
                .build();
        try {
            Response response = client.newCall(request).execute();
            assert response.body() != null;
            String body = response.body().string();

            JSONObject jsonObject = JSONUtil.parseObj(body);
            int err = jsonObject.getInt("err");
            if (err == 0) {
                res = jsonObject.getStr("data");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<DouyuDto> getAllByPage(Integer page, Integer size) {
        long start = (page - 1) * (long) size;
        long end = start + size - 1;
        Set<String> set = stringRedisTemplate.opsForZSet().reverseRange(ROOM_LIST_SET_KEY, start, end);
        if (set == null) return null;
        List<DouyuDto> res = new ArrayList<>();
        for (String rid : set) {
            Object o = stringRedisTemplate.opsForHash().get(ROOM_LIST_HASH_KEY, rid);
            DouyuDto douyuDto = new DouyuDto();
            JSONObject jsonObject = JSONUtil.parseObj(o);
            douyuDto.setRid(rid);
            douyuDto.setTitle(jsonObject.getStr("title"));
            douyuDto.setName(jsonObject.getStr("username"));
            douyuDto.setCoverImg(jsonObject.getStr("coverImg"));
            res.add(douyuDto);
        }
        return res;
    }

    @Override
    public Long getCount() {
        return stringRedisTemplate.opsForZSet().zCard(ROOM_LIST_SET_KEY);
    }
}
