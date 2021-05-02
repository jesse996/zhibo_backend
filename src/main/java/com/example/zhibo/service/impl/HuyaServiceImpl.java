package com.example.zhibo.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.zhibo.dto.HuyaDto;
import com.example.zhibo.service.HuyaService;
import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.JstlUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class HuyaServiceImpl implements HuyaService {
    @Autowired
    OkHttpClient client;

    @Override
    public String getPlayUrl(String rid) {
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/75.0.3770.100 Mobile Safari/537.36 ")
                .url(StrUtil.format("https://m.huya.com/{}", rid))
                .build();
        try {
            Response response = client.newCall(request).execute();
            assert response.body() != null;
            String body = response.body().string();
//            System.out.println(body);
            ArrayList<String> list = ReUtil.findAll("liveLineUrl = \"([\\s\\S]*?)\";", body, 1, new ArrayList<>());
            assert !list.isEmpty();
            String liveLineUrl = list.get(0);
            liveLineUrl = Base64.decodeStr(liveLineUrl);
            System.out.println(liveLineUrl);
            String res = "";
            if (!StrUtil.isBlank(liveLineUrl)) {
                if (StrUtil.contains(liveLineUrl, "replay")) {
                    res = "https:" + liveLineUrl;
                } else {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<HuyaDto> getAllByPage(Integer page, Integer size) {
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/75.0.3770.100 Mobile Safari/537.36 ")
                .url(StrUtil.format("https://m.huya.com/cache.php?m=Live&do=ajaxGetProfileLive&page={}&pageSize={}}", page, size))
                .build();
        List<HuyaDto> res = new ArrayList<>();
        try {
            Response response = client.newCall(request).execute();
            assert response.body() != null;
            String body = response.body().string();
            JSONObject jsonObject = JSONUtil.parseObj(body);
            JSONArray gameList = jsonObject.getJSONArray("gameList");
            for (Object o : gameList) {
                HuyaDto huyaDto = JSONUtil.parseObj(o).toBean(HuyaDto.class);
                res.add(huyaDto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<HuyaDto> findByName(String name) {
        return null;
    }
}
