package com.example.zhibo.service;

import com.example.zhibo.dto.CommonDto;
import com.example.zhibo.dto.HuyaUrl;
import java.util.List;

public interface HuyaService {
    HuyaUrl getPlayUrl(String rid);

    List<CommonDto> getAllByPage(Integer page, Integer size);

    List<CommonDto> findByName(String name);
}
