package com.example.zhibo.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: jesse
 * @Date: 2021/4/2 10:58 上午
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DouyuDao implements Serializable{
//    private static final long serialVersionUID = 8163440132735590985L;
    String title;
    String imgUrl;
    String username;
    String rid;
}
