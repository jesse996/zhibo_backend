package com.example.zhibo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: jesse
 * @Date: 2021/4/2 10:25 上午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonDto {
    private String name;
    private String title;
    private String rid;
    private String coverImg;
}
