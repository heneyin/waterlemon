package com.henvealf.watermelon.sqlrepository;

import com.google.common.base.Preconditions;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author hongliang.yin/Henvealf
 * @date 2019-07-26
 */
public class MybatisSqlSessionFactory {

    public static SqlSessionFactory create(String config) {
        Preconditions.checkNotNull(config, "Mybatis config file is null");
        try(InputStream inputStream = new FileInputStream(config)) {
            return new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error when build SqlSessionFactory use config %s", config), e);
        }
    }

}
