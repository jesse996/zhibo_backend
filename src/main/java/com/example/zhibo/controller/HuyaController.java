package com.example.zhibo.controller;

import com.example.zhibo.common.CommonResult;
import com.example.zhibo.service.HuyaService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/huya")
public class HuyaController {
    @Resource
    HuyaService huyaService;

    @GetMapping("")
    public CommonResult getAll(@RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "size", defaultValue = "20") Integer size) {
        //这个size无效，固定为120，因为虎牙官方的接口bug
        return CommonResult.success(huyaService.getAllByPage(page, size));
    }

    @GetMapping("/url/{id}")
    public CommonResult getUrl(@PathVariable("id") String id) {
        String playUrl = huyaService.getPlayUrl(id);
        return playUrl == null ? CommonResult.failed() : CommonResult.success(playUrl);
    }
}
