package com.henvealf.watermelon.sqlrepository;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author hongliang.yin/Henvealf
 * @date 2019-07-26
 */
@Mapper
public interface RawSqlMapper {

    @Select("${sql}")
    public List<JSONObject> select(@Param("sql") String sql);

    @Insert("${sql}")
    public int insert(@Param("sql") String sql);
}
