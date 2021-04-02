package com.example.zhibo.controller;

import com.example.zhibo.common.CommonResult;
import com.example.zhibo.dto.DouyuDto;
import com.example.zhibo.service.DouyuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: jesse
 * @Date: 2021/4/2 10:24 上午
 */
@RestController
@RequestMapping("douyu")
public class DouyuController {
    @Resource
    DouyuService douyuService;

    @GetMapping("")
    public CommonResult getAll() {
        return CommonResult.success(douyuService.getAll());
    }
}
