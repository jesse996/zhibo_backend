package com.example.zhibo.service;

import com.example.zhibo.dto.HuyaDto;
import com.example.zhibo.model.Huya;

import java.util.List;
import java.util.Optional;

/**
 * Created by jesse on 2021/3/30 下午12:53
 */
public interface HuyaService {
    List<Huya> getAll(Integer page,Integer size);

    List<Huya> getAll();

    Optional<HuyaDto> getUrl(String id);

    Long getCount();

    List<Huya> getAllByRedis();

    Huya getByRoomId(String id);
}
