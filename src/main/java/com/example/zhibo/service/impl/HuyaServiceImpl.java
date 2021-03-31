package com.example.zhibo.service.impl;

import com.example.zhibo.dto.HuyaDto;
import com.example.zhibo.mapper.HuyaMapper;
import com.example.zhibo.model.Huya;
import com.example.zhibo.model.HuyaExample;
import com.example.zhibo.service.HuyaService;
import com.example.zhibo.service.RedisService;
import com.github.pagehelper.PageHelper;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by jesse on 2021/3/27 下午10:32
 */
@Service
public class HuyaServiceImpl implements HuyaService {
    @Resource
    HuyaMapper huyaMapper;
    @Resource
    RedisService redisService;
    @Resource
    RedisTemplate<String, Huya> redisTemplate;

    static final String KEY = "huya";

    @Override
    public List<Huya> getAll(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        return huyaMapper.selectByExampleWithBLOBs(new HuyaExample());
    }

    @Override
    public List<Huya> getAll() {
        return huyaMapper.selectByExampleWithBLOBs(new HuyaExample());
    }

    @Override
    public Optional<HuyaDto> getUrl(String id) {
        Request request = new Request.Builder()
                .url("https://m.huya.com/" + id)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Mobile Safari/537.36")
                .build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            assert response.body() != null;
            String body = response.body().string();
            String pattern = "liveLineUrl = \"([\\s\\S]*?)\";";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(body);
            if (m.find()) {
                var liveLineUrl = m.group(1);
                if (liveLineUrl == null || liveLineUrl.length() == 0) return Optional.empty();
                HuyaDto dto = new HuyaDto();
                dto.setData(liveLineUrl);
                if (liveLineUrl.contains("replay")) {
                    dto.setType(HuyaDto.Type.REPLAY);
                } else {
                    dto.setType(HuyaDto.Type.LIVE);
                }
                return Optional.of(dto);
            } else {
                //没找到
                SetOperations<String, Huya> opsForSet = redisTemplate.opsForSet();
                Huya huya = getByRoomId(id);
                opsForSet.remove(KEY, huya);
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Huya getByRoomId(String id) {
        HuyaExample example = new HuyaExample();
        example.setDistinct(true);
        example.createCriteria().andHrefEqualTo("https://www.huya.com/" + id);
        return huyaMapper.selectByExampleWithBLOBs(example).get(0);
    }

    @Override
    public Long getCount() {
        return huyaMapper.countByExample(new HuyaExample());
    }

    @Override
    public List<Huya> getAllByRedis() {
        SetOperations<String, Huya> opsForSet = redisTemplate.opsForSet();
        Cursor<Huya> cursor = opsForSet.scan(KEY, ScanOptions.scanOptions().count(10).build());
        List<Huya> res = new ArrayList<>();
        while (cursor.hasNext()) {
            Huya huya = cursor.next();
            res.add(huya);
        }
        return res;

//        return opsForSet.randomMembers(KEY, 20);
    }
}
