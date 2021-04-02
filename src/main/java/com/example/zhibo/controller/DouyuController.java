package com.example.zhibo.controller;

import com.example.zhibo.common.CommonResult;
import com.example.zhibo.dto.DouyuDto;
import com.example.zhibo.service.DouyuService;
import com.github.pagehelper.PageInfo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: jesse
 * @Date: 2021/4/2 10:24 上午
 */
@RestController
@CacheConfig(cacheNames = "douyu")
@RequestMapping("/douyu")
public class DouyuController {
    @Resource
    DouyuService douyuService;

    @GetMapping("")
    public CommonResult getAll() {
        return CommonResult.success(new PageInfo<>(douyuService.getAll()));
    }

    @Cacheable(key = "#id")
    @GetMapping("/url/{id}")
    public CommonResult getUrl(@PathVariable("id")String id){
        String playUrl = douyuService.getPlayUrl(id);
        return playUrl==null ? CommonResult.failed() : CommonResult.success(playUrl);
    }
}
