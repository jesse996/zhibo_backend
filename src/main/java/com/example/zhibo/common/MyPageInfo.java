package com.example.zhibo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by jesse on 2021/4/8 下午12:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPageInfo<T> {
    Long total;
    int pages;
    int pageSize;
    int size;
    List<T> list;

    public MyPageInfo(List<T> t) {
        list = t;
    }
}
