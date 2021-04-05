package com.example.zhibo.controller;

import com.example.zhibo.common.CommonResult;
import com.example.zhibo.dto.HuyaDto;
import com.example.zhibo.service.HuyaService;
import com.example.zhibo.service.impl.HuyaServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by jesse on 2021/3/27 下午10:34
 */
@RestController
@CacheConfig(cacheNames = "huya")
@RequestMapping("/huya")
public class HuyaController {
    @Resource
    HuyaService huyaService;

    @GetMapping("")
    @Cacheable(key = "#page+#size")
    public CommonResult getAll(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        var data = new PageInfo<>(huyaService.getAll(page,size));
//        var data = new PageInfo<>(huyaService.getAllByRedis());
        return CommonResult.success(data);
    }

    @Cacheable(key = "#id")
    @GetMapping("/url/{id}")
    public CommonResult getUrl(@PathVariable("id") String id) {
        Optional<HuyaDto> optional = huyaService.getUrl(id);
        if (optional.isEmpty()) {
            return CommonResult.failed("未开播或直播间不存在");
        } else {
            return CommonResult.success(optional.get());
        }
    }
}
