package com.henvealf.watermelon.sqlrepository;

import com.google.common.base.Preconditions;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 与 MyBatis 有关的工具。
 * @author hongliang.yin/Henvealf
 * @date 2019-07-15
 */
public class MyBatisUtil {

    public static String getMyBatisFinalSql(Configuration configuration, String mapperName, Object param ) {
        return getMyBatisFinalSql(configuration, mapperName, param, false);
    }
    /**
     * 拼装得到 mybatis 最终拼装的 SQL。
     * Node: 这里的 SQL 与 JDBC 拼装的结果可能会有差异。如果遇到一个比较奇怪的情况，可以查看相应 jdbc PreparedStatement 的实现。
     * @return
     */
    public static String getMyBatisFinalSql(Configuration configuration, String mapperName, Object param, boolean isOneLine) {

        MappedStatement statement = configuration.getMappedStatement(mapperName);
        BoundSql boundSql = statement.getBoundSql(param);

        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql();
        if (isOneLine) {
            sql = sql.replaceAll("[\\s]+", " ");
        }
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }
        return sql;
    }

    private static String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(obj) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }

    /**
     * 使用 mapper 名前缀与后缀获取完整的 mapper 名。
     */
    public static String getWholeMapperName(String mapperPrefix, String suffixName) {
        Preconditions.checkNotNull(mapperPrefix, "mapperPrefix is null");
        Preconditions.checkNotNull(suffixName, "suffixName is null");
        return mapperPrefix + suffixName;
    }

    public static boolean isCommandTypeMapper(Configuration configuration, SqlCommandType commandType, String mapperName) {
        return getCommandType(configuration, mapperName) == commandType;
    }


    public static boolean isSelectMapper(Configuration configuration,  String mapperName) {
        return getCommandType(configuration, mapperName) == SqlCommandType.SELECT;
    }

    public static boolean isInsertMapper(Configuration configuration,  String mapperName) {
        return getCommandType(configuration, mapperName) == SqlCommandType.INSERT;
    }

    public static SqlCommandType getCommandType(Configuration configuration, String mapperName) {

        try {
            MappedStatement mappedStatement = configuration.getMappedStatement(mapperName);
            return mappedStatement.getSqlCommandType();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(String.format("Mybatis mapper[%s] not found", mapperName), e);
        }
    }
}
