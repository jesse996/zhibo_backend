package com.example.zhibo.service;

import com.example.zhibo.common.CommonResult;
import com.example.zhibo.dto.DouyuDto;

import java.util.List;

/**
 * @Author: jesse
 * @Date: 2021/4/2 10:34 上午
 */
public interface DouyuService {
    public List<DouyuDto> getAll();
    public String getPlayUrl(String rid);
}
