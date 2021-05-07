package com.example.zhibo.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.zhibo.dto.HuyaDto;
import com.example.zhibo.dto.HuyaUrl;
import com.example.zhibo.service.HuyaService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@Slf4j
public class HuyaServiceImpl implements HuyaService {
    @Autowired
    OkHttpClient client;

    private String live(String e) {
        String[] split = e.split("\\?");
        String i = split[0];
        String b = split[1];
        String[] r = i.split("/");
        var s = ReUtil.replaceAll(r[r.length - 1], "\\.(flv|m3u8)", "");
        var c = b.split("&", 4);
        c = Arrays.stream(c).filter(_i -> !_i.equals("")).toArray(String[]::new);
        var n = new HashMap<String, String>();
        for (var _i : c) {
            n.put(_i.split("=")[0], _i.split("=")[1]);
        }
        var fm = URLUtil.decode(n.get("fm"));
        var u = Base64.decodeStr(fm);
        var p = u.split("_")[0];
        Instant now = Instant.now();
        long second = now.getEpochSecond();
        long nano = now.getNano();
        long f = (second * 1000000000L + nano) / 100;
        var ll = n.get("wsTime");
        var t = "0";
        var h = String.join("_", (new String[]{p, t, s, String.valueOf(f), ll}));
        var m = MD5.create().digestHex(h);
        var y = c[c.length - 1];
        return StrUtil.format("{}?wsSecret={}&wsTime={}&u={}&seqid={}&{}", i, m, ll, t, f, y);
    }

    @Override
    public HuyaUrl getPlayUrl(String rid) {
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/75.0.3770.100 Mobile Safari/537.36 ")
                .url(StrUtil.format("https://m.huya.com/{}", rid))
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.body() == null) throw new RuntimeException("body为空");
            String body = response.body().string();
            ArrayList<String> list = ReUtil.findAll("liveLineUrl = \"([\\s\\S]*?)\";", body, 1, new ArrayList<>());
            if (list.isEmpty()) throw new RuntimeException(rid+" 未开播或房间不存在");
            String liveLineUrl = list.get(0);
            liveLineUrl = Base64.decodeStr(liveLineUrl);
            String url = "";
            HuyaUrl huyaUrl = new HuyaUrl();
            if (!StrUtil.isBlank(liveLineUrl)) {
                if (StrUtil.contains(liveLineUrl, "replay")) {
                    //重播
                    url = liveLineUrl;
                    huyaUrl.setLive(false);

                } else {
                    //直播
                    huyaUrl.setLive(true);
                    url = live(liveLineUrl);
                }
                huyaUrl.setUrl("https:" + url);
                return huyaUrl;
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
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
