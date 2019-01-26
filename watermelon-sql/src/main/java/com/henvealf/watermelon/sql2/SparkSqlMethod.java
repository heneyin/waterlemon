package com.henvealf.watermelon.sql2;

/**
 * <p>
 *
 * <p>
 *
 * @author hongliang.yin/Henvealf on 2019/1/26
 */
public class SparkSqlMethod extends SqlMethod {

    @Override
    public SqlField arrayContains(Object arrayColName, Object value) {
        return methodWithArg("array_contains", value);
    }

    @Override
    public SqlField timestampStrToDateStr(String timestampColName) {
        return null;
    }
}
