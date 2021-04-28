package com.example.zhibo.service;

import com.example.zhibo.dto.DouyuDto;

import java.util.List;

/**
 * @Author: jesse
 * @Date: 2021/4/2 10:34 上午
 */
public interface DouyuService {
    List<DouyuDto> getAll();

    String getPlayUrl(String rid);

    List<DouyuDto> getAllByPage(Integer page, Integer size);

    Long getCount();

    Long deleteRid(String rid);

    List<DouyuDto> findByName(String name);
}
