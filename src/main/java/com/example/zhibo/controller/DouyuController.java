package com.example.zhibo.controller;

import com.example.zhibo.common.CommonResult;
import com.example.zhibo.common.MyPageInfo;
import com.example.zhibo.service.DouyuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author: jesse
 * @Date: 2021/4/2 10:24 上午
 */
@RestController
@RequestMapping("/douyu")
public class DouyuController {
    @Resource
    DouyuService douyuService;

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
        var pageInfo = new MyPageInfo<>(data);
        var total = douyuService.getCount();
        pageInfo.setTotal(total);
        pageInfo.setPages((int) (total / size) + 1);
        pageInfo.setPageSize(size);
        pageInfo.setSize(data.size());
        return CommonResult.success(pageInfo);
    }

    @DeleteMapping("/{id}")
    public CommonResult deleteRid(@PathVariable("id") String rid) {
        if (douyuService.deleteRid(rid) == 1) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    @GetMapping("/search/{name}")
    public CommonResult searchByName(@PathVariable("name") String name) {
        return CommonResult.success(douyuService.findByName(name));
    }
}
