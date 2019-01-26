package com.henvealf.watermelon.sql2;


import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 *
 * <p>
 *
 * @author hongliang.yin/Henvealf on 2019/1/19
 */
public class SqlCol implements SqlField{

    private String colName;
    private String alias;

    public SqlCol(String colName, String alias) {
        this.colName = colName;
        this.alias = alias;
    }

    public SqlCol(String colName) {
        this.colName = colName;
        this.alias = null;
    }

    @Override
    public SqlField as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public String toString() {
        return colName + SqlUtils.asTail(alias);
    }

}
