package com.henvealf.watermelon.sql2.method;

/**
 * <p>
 *
 * <p>
 *
 * @author hongliang.yin/Henvealf on 2019/1/30
 */
public class SparkSqlMethodFactory implements SqlMethodFactory{

    @Override
    public SqlMethod p() {
        return new SparkSqlMethod();
    }
}
