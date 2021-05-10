package com.example.zhibo.service;

import com.example.zhibo.dto.CommonDto;
import java.util.List;

/**
 * @Author: jesse
 * @Date: 2021/4/2 10:34 上午
 */
public interface DouyuService {
    List<CommonDto> getAll();

    String getPlayUrl(String rid);

    List<CommonDto> getAllByPage(Integer page, Integer size);

    Long getCount();

    Long deleteRid(String rid);

    List<CommonDto> findByName(String name);
}
