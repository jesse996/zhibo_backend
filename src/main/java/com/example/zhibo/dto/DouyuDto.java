package com.example.zhibo.dto;

import lombok.AllArgsConstructor;
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
public class DouyuDto implements Serializable {
    private static final long serialVersionUID = 3948607265589451817L;

    private String name;

    private String title;

    //room id
    private String rid;

    private Double score;

    private String coverImg;

    private String playUrl;
}
