package com.example.zhibo.controller;

import com.example.zhibo.common.CommonResult;
import com.example.zhibo.dto.DouyuDto;
import com.example.zhibo.service.DouyuService;
import com.github.pagehelper.PageInfo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;
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

//    @GetMapping("")
//    public CommonResult getAll() {
//        return CommonResult.success(new PageInfo<>(douyuService.getAll()));
//    }

    @Cacheable(key = "#id")
    @GetMapping("/url/{id}")
    public CommonResult getUrl(@PathVariable("id") String id) {
        String playUrl = douyuService.getPlayUrl(id);
        return playUrl == null ? CommonResult.failed() : CommonResult.success(playUrl);
    }

    @GetMapping("")
    public CommonResult getAll(@RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "size", defaultValue = "20") Integer size) {
        var data = douyuService.getAllByPage(page, size);
        if (data == null) return CommonResult.failed();
        var pageInfo = new PageInfo<>(data);
        var total = douyuService.getCount();
        pageInfo.setTotal(total);
        pageInfo.setPages((int) (total / size) + 1);
        pageInfo.setPageSize(size);
        pageInfo.setSize(data.size());
        return CommonResult.success(pageInfo);
    }
}
