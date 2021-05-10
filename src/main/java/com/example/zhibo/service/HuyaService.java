package com.example.zhibo.service;

import com.example.zhibo.dto.ResponseCommonDto;
import com.example.zhibo.dto.ResponseCommonUrl;
import java.util.List;

public interface HuyaService {
    ResponseCommonUrl getPlayUrl(String rid);

    List<ResponseCommonDto> getAllByPage(Integer page, Integer size);

    List<ResponseCommonDto> findByName(String name);
}
