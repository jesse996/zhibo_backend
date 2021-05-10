package com.example.zhibo.service;

import com.example.zhibo.dto.ResponseCommonDto;
import java.util.List;

/**
 * @Author: jesse
 * @Date: 2021/4/2 10:34 上午
 */
public interface DouyuService {
    List<ResponseCommonDto> getAll();

    String getPlayUrl(String rid);

    List<ResponseCommonDto> getAllByPage(Integer page, Integer size);

    Long getCount();

    Long deleteRid(String rid);

    List<ResponseCommonDto> findByName(String name);
}
