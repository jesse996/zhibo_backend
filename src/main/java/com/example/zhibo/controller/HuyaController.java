package com.example.zhibo.controller;

import com.example.zhibo.common.CommonResult;
import com.example.zhibo.common.MyPageInfo;
import com.example.zhibo.dto.ResponseCommonUrl;
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
                               @RequestParam(value = "size", defaultValue = "120") Integer size) {
        //这个size无效，固定为120，因为虎牙官方的接口bug
        var data = huyaService.getAllByPage(page, size);
        var pageInfo = new MyPageInfo<>(data);
        pageInfo.setSize(data.size());
        return CommonResult.success(pageInfo);
    }

    @GetMapping("/url/{id}")
    public CommonResult getUrl(@PathVariable("id") String id) {
        ResponseCommonUrl responseCommonUrl = huyaService.getPlayUrl(id);
        return responseCommonUrl == null ? CommonResult.failed() : CommonResult.success(responseCommonUrl);
    }
}
