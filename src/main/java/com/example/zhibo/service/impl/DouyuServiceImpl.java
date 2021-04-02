package com.example.zhibo.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.zhibo.dao.DouyuDao;
import com.example.zhibo.dto.DouyuDto;
import com.example.zhibo.service.DouyuService;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class DouyuServiceImpl implements DouyuService {
    @Resource
    StringRedisTemplate stringRedisTemplate;

    private static final String ROOM_LIST_SET_KEY = "douyu::rooms::set";
    private static final String ROOM_LIST_HASH_KEY = "douyu::rooms::hash";

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
        try {
            Process proc = Runtime.getRuntime().exec("node /Users/jesse/Documents/puppeteer_zhibo/src/douyu.js " + rid);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                stringBuilder.append(line);
            }
            JSONObject object = JSONUtil.parseObj(stringBuilder.toString());

            if (object.getInt("err") == 0) {
                res = object.getStr("data");
            } else {
                //房间未开播或不存在，从redis中删除
                stringRedisTemplate.opsForZSet().remove(ROOM_LIST_SET_KEY, rid);
            }
            in.close();
            proc.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<DouyuDto> getAllByPage(Integer page, Integer size) {
        long start = (page - 1) * (long) size;
        long end = start + size;
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
}
