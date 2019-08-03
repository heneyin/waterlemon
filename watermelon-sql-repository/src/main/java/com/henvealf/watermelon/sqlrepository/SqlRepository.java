package com.henvealf.watermelon.sqlrepository;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author hongliang.yin/Henvealf
 * @date 2019-07-03
 */
public interface SqlRepository {

    default List<JSONObject> select(String mapperName) {
        return select(mapperName, new JSONObject());
    }

    default List<JSONObject> selectTableDescribeUseSql(String sql) {
        return selectUseSql("DESCRIBE TABLE ( " + sql + ")");
    }

    List<JSONObject> selectUseSql(String sql);

    int insertUseSql(String sql);


    List<JSONObject> select(String mapperName, JSONObject sqlParameter);

    int insert(String mapperName, JSONObject sqlParameter, List<JSONObject> datas);

    /**
     * 将一份数据应用到多个 mapper 上。如果数据库支持事务，所有 mapper 在一个事务内。
     * @param mapperNames
     * @param sqlParameter
     * @param datas
     * @return
     */
    default int insert(List<String> mapperNames, JSONObject sqlParameter, List<JSONObject> datas){
        if (datas.isEmpty()) return 0;
        List<InsertDescription> desribes = mapperNames.stream().map(mapper -> {
            return InsertDescription.create(mapper, sqlParameter, datas);
        }).collect(Collectors.toList());
        return insert(desribes);
    }

    /**
     * 根据描述写入。如果数据库支持事务，所有的写入在一个事务内。
     * @param descriptions
     * @return
     */
    int insert(List<InsertDescription> descriptions);

    default int insert(String mapperName, JSONObject sqlParameter){
        return insert(Lists.newArrayList(InsertDescription.create(mapperName, sqlParameter)));
    }
    public void execute(Consumer<SqlSession> operate);
}
