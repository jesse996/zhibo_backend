package com.example.zhibo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by jesse on 2021/3/29 下午4:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HuyaDto implements Serializable {
    private static final long serialVersionUID = -6552764940425391330L;

    public enum Type {
        REPLAY, LIVE;
    }
    private Type type;
    //播放链接
    private String data;
}


