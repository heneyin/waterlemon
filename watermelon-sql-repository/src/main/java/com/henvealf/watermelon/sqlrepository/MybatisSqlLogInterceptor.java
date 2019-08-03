package com.henvealf.watermelon.sqlrepository;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author hongliang.yin/Henvealf
 * @date 2019-07-03
 */

@Intercepts({
        @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class }) })
public class MybatisSqlLogInterceptor implements Interceptor {

    private static Logger logger = LoggerFactory.getLogger(MybatisSqlLogInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }

        Configuration configuration = mappedStatement.getConfiguration();

        //获取sql语句
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        logger.info("The raw sql: \n{}", boundSql.getSql());
        String sql = MyBatisUtil.getMyBatisFinalSql(configuration, mappedStatement.getId(), parameter);
        logger.info("Will exec sql: \n{}", sql);

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) { }

}
