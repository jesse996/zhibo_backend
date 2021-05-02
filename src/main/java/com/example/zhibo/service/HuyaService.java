package com.example.zhibo.service;

import com.example.zhibo.dto.HuyaDto;
import java.util.List;

public interface HuyaService {
    String getPlayUrl(String rid);

    List<HuyaDto> getAllByPage(Integer page, Integer size);

    List<HuyaDto> findByName(String name);
}
