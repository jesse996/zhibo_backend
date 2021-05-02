package com.example.zhibo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HuyaDto implements Serializable {

    private static final long serialVersionUID = -8071308067862041916L;

    String gameFullName;
    String gameHostName;
    String gid;
    String introduction;
    String liveSourceType;
    String nick;
    String privateHost;
    String profileRoom;
    String screenType;
    String screenshot;
    String totalCount;
    String uid;
}
