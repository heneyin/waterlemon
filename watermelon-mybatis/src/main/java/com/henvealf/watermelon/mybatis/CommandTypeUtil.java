package com.henvealf.watermelon.mybatis;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * @author hongliang.yin/Henvealf
 * @date 2020/3/26
 */
public class CommandTypeUtil {

    public static boolean isCommandTypeMapper(Configuration configuration,
                                              SqlCommandType commandType,
                                              String mapperName) {
        return getCommandType(configuration, mapperName) == commandType;
    }

    public static boolean isCommandTypeMapper(SqlSessionFactory sqlSessionFactory,
                                              SqlCommandType commandType,
                                              String mapperName) {
        return getCommandType(sqlSessionFactory, mapperName) == commandType;
    }

    public static SqlCommandType getCommandType(SqlSessionFactory sqlSessionFactory, String mapperName) {
        Configuration configuration = sqlSessionFactory.getConfiguration();
        return getCommandType(configuration, mapperName);
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
