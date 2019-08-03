package com.henvealf.watermelon.sqlrepository;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.SqlSession;

import java.util.Collection;

/**
 * @author hongliang.yin/Henvealf
 * @date 2019-07-29
 */
public class SqlOperator {

    public static void delete(SqlSession sqlSession, DeleteDescription description) {
        sqlSession.delete(description.getMapperName(), description.getParam());
    }

    public static void insert(SqlSession sqlSession, InsertDescription description) {
        int total = 0;
        String mapper = description.getMapperName();
        JSONObject inner = new JSONObject();
        JSONObject param = description.getParam();
        Object datas = description.getData();
        boolean mutilInsert  = description.isMutilInsert();
        if (param == null && (datas == null)) {
            return;
        }
        if (datas instanceof Collection && ((Collection) datas).isEmpty()) {
            return;
        }
        if (!mutilInsert) {
            inner.put("datas", datas);
            inner.put("param", param);
            int res = sqlSession.insert(mapper, inner);
            total += res;
        } else {
            if (datas instanceof Collection) {
                for (Object o : (Collection) datas) {
                    int res = sqlSession.insert(mapper, o);
                    total += res;
                }
            } else {
                int res = sqlSession.insert(mapper, datas);
                total += res;
            }
        }
    }

    public static void update(SqlSession sqlSession, UpdateDescription description) {
        int total = 0;
        String mapper = description.getMapperName();
        JSONObject inner = new JSONObject();
        Object datas = description.getData();
        boolean mutilInsert  = description.isMutilUpdate();
        if (datas == null) {
            return;
        }
        if (datas instanceof Collection && ((Collection) datas).isEmpty()) {
            return;
        }
        if (!mutilInsert) {
            // 分开执行
            inner.put("datas", datas);
            int res = sqlSession.update(mapper, inner);
            total += res;
        } else {
            if (datas instanceof Collection) {
                // 一块执行
                for (Object o : (Collection) datas) {
                    int res = sqlSession.update(mapper, o);
                    total += res;
                }
            } else {
                int res = sqlSession.update(mapper, datas);
                total += res;
            }
        }
    }

}
