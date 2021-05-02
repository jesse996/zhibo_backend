package com.example.zhibo.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.zhibo.dto.DouyuDto;
import com.example.zhibo.service.DouyuService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Value("${my.nest.host}")
    private String NEST_HOST;

//    private final OkHttpClient client = new OkHttpClient();
    @Autowired
    OkHttpClient client;

    //一次获取所有，暂时不用
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
            Set<String> keys = stringRedisTemplate.keys(ROOM_LIST_HASH_KEY + "::" + rid + "::*");
            assert keys != null && !keys.isEmpty();
            String key = keys.toArray(new String[0])[0];
            douyuDto.setScore(next.getScore());
            douyuDto.setRid(rid);
            douyuDto.setTitle((String) stringRedisTemplate.opsForHash().get(key, "title"));
            douyuDto.setCoverImg((String) stringRedisTemplate.opsForHash().get(key, "coverImg"));
            String[] split = key.split("::");
            String name = split[split.length - 1];
            douyuDto.setName(name);
            res.add(douyuDto);
        }
        return res;
    }

    @Override
    public String getPlayUrl(String rid) {
        String res = null;
        Request request = new Request.Builder()
                .url("http://" + NEST_HOST + ":3000/douyu/" + rid)
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<DouyuDto> getAllByPage(Integer page, Integer size) {
        long start = (page - 1) * (long) size;
        long end = start + size - 1;
        Set<String> set = stringRedisTemplate.opsForZSet().range(ROOM_LIST_SET_KEY, start, end);
        if (set == null) return null;
        List<DouyuDto> res = new ArrayList<>();
        for (String rid : set) {
            Set<String> keys = stringRedisTemplate.keys(ROOM_LIST_HASH_KEY + "::" + rid + "::*");
            assert keys != null && !keys.isEmpty();
            String key = keys.toArray(new String[0])[0];
            List<Object> list = stringRedisTemplate.opsForHash().multiGet(key, Arrays.asList(new String[]{"title", "coverImg"}));
            DouyuDto douyuDto = new DouyuDto();
            douyuDto.setRid(rid);
            douyuDto.setTitle((String) list.get(0));
            douyuDto.setCoverImg((String) list.get(1));
            String[] split = key.split("::");
            String name = split[split.length - 1];
            douyuDto.setName(name);
            res.add(douyuDto);
        }
        return res;
    }

    @Override
    public Long getCount() {
        return stringRedisTemplate.opsForZSet().zCard(ROOM_LIST_SET_KEY);
    }

    @Override
    public Long deleteRid(String rid) {
        return stringRedisTemplate.opsForZSet().remove(ROOM_LIST_SET_KEY, rid);
    }

    @Override
    public List<DouyuDto> findByName(String name) {
        Set<String> keys = stringRedisTemplate.keys(ROOM_LIST_HASH_KEY + "*" + name + "*");
        List<DouyuDto> res = new ArrayList<>();
        for (String key : keys) {
            List<Object> list = stringRedisTemplate.opsForHash().multiGet(key, Arrays.asList(new String[]{"title", "coverImg"}));
            DouyuDto douyuDto = new DouyuDto();
            douyuDto.setTitle((String) list.get(0));
            douyuDto.setCoverImg((String) list.get(1));
            String[] split = key.split("::");
            String realName = split[split.length - 1];
            String rid = split[split.length - 2];
            douyuDto.setRid(rid);
            douyuDto.setName(realName);
            res.add(douyuDto);
        }
        return res;
    }
}
