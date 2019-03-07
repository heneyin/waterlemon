package com.henvealf.watermelon.sql2.method;

import com.henvealf.watermelon.sql2.SqlField;
import com.henvealf.watermelon.sql2.method.SqlMethod;

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
        return methodWithArg("array_contains", arrayColName, value);
    }

    @Override
    public SqlField timestampStrToDateStr(String timestampColName) {
        return null;
    }
}
