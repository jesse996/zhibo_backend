package com.example.zhibo.mapper;

import com.example.zhibo.model.Huya;
import com.example.zhibo.model.HuyaExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HuyaMapper {
    long countByExample(HuyaExample example);

    int deleteByExample(HuyaExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Huya record);

    int insertSelective(Huya record);

    List<Huya> selectByExampleWithBLOBs(HuyaExample example);

    List<Huya> selectByExample(HuyaExample example);

    Huya selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Huya record, @Param("example") HuyaExample example);

    int updateByExampleWithBLOBs(@Param("record") Huya record, @Param("example") HuyaExample example);

    int updateByExample(@Param("record") Huya record, @Param("example") HuyaExample example);

    int updateByPrimaryKeySelective(Huya record);

    int updateByPrimaryKeyWithBLOBs(Huya record);

    int updateByPrimaryKey(Huya record);
}