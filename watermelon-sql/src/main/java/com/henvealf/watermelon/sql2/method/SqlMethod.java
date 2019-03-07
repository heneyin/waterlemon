package com.henvealf.watermelon.sql2.method;

import com.henvealf.watermelon.sql2.SqlField;
import com.henvealf.watermelon.sql2.SqlTypeCheck;
import com.henvealf.watermelon.sql2.SqlUtils;

import javax.swing.*;

/**
 * <p>
 *
 * <p>
 *
 * @author hongliang.yin/Henvealf on 2019/1/7
 */
public abstract class SqlMethod implements SqlField {

    String COUNT_NAME = "COUNT";
    String MAX_NAME = "MAX";
    String MIN_NAME = "MIN";
    String SUM_NAME = "SUM";

    private String content = null;
    private String alias = null;

    @Override
    public SqlField as(String alias) {
        this.alias = alias;
        return this;
    }

    public SqlField count(Object colName){
        return methodWithArg(COUNT_NAME, colName);
    }

    public SqlField min(Object colName) {
        return methodWithArg(MIN_NAME, colName);
    }

    public SqlField max(Object colName) {
        return methodWithArg(MAX_NAME, colName);
    }

    public SqlField sum(Object colName) {
        return methodWithArg(SUM_NAME, colName);
    }

    public SqlField rowNumberOver(Object[] partitonByCols, Object[] orderByCols){
        this.content = String.format("ROW_NUMBER() OVER(PARTITION BY %s ORDER BY %s)",
                SqlUtils.joinObject(partitonByCols),
                SqlUtils.joinObject(orderByCols));
        return this;
    }

    protected SqlField methodWithArg(String methodName, Object... colNames) {
        SqlTypeCheck.checkField(colNames);
        this.content = String.format("%s(%s)", methodName, String.join(", ", SqlUtils.valueOf(colNames)));
        return this;
    }

    public abstract SqlField arrayContains(Object arrayColName, Object value);

    public abstract SqlField timestampStrToDateStr(String timestampColName);

    @Override
    public String toString() {
        return content + SqlUtils.asTail(alias);
    }
}
