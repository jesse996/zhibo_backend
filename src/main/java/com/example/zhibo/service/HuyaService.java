package com.example.zhibo.service;

import com.example.zhibo.dto.HuyaDto;
import com.example.zhibo.dto.HuyaUrl;

import java.util.List;

public interface HuyaService {
    HuyaUrl getPlayUrl(String rid);

    List<HuyaDto> getAllByPage(Integer page, Integer size);

    List<HuyaDto> findByName(String name);
}
