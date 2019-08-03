package com.henvealf.watermelon.sqlrepository;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

/**
 *
 * 使用 MyBatis 操作数据库。
 * 除了执行原始 SQL, MyBatis 执行的 Mapper 与 SQL 中的参数与方法都由外部来指定。
 *
 * @author hongliang.yin/Henvealf
 * @date 2019-07-03
 */
public class MyBatisSqlRepository implements SqlRepository {

    private static Logger logger = LoggerFactory.getLogger(MyBatisSqlRepository.class);

    private SqlSessionFactory sqlSessionFactory;

    public MyBatisSqlRepository(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        addRawSqlMapper();

    }

    public MyBatisSqlRepository(String mybatisConfig) {
        this.sqlSessionFactory = MybatisSqlSessionFactory.create(mybatisConfig);
        addRawSqlMapper();
    }

    private void addRawSqlMapper() {
        try {
            this.sqlSessionFactory.getConfiguration().addMapper(RawSqlMapper.class);
        } catch (BindingException e) {
            // no op
        }
    }

    @Override
    public List<JSONObject> selectUseSql(String sql) {
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession()){
            RawSqlMapper mapper = sqlSession.getMapper(RawSqlMapper.class);
            return mapper.select(sql);
        }
    }

    @Override
    public int insertUseSql(String sql) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()){
            RawSqlMapper mapper = sqlSession.getMapper(RawSqlMapper.class);
            return mapper.insert(sql);
        }
    }

    @Override
    public List<JSONObject> select(String mapperName, JSONObject sqlParameter) {
        logger.info("The mapper[{}] parameter {}", mapperName, sqlParameter);
        try (SqlSession sqlSession = sqlSessionFactory.openSession()){
            return sqlSession.selectList(mapperName, sqlParameter);
        }
    }

    @Override
    public int insert(String mapperName, JSONObject sqlParameter, List<JSONObject> datas) {
        if (datas.isEmpty()) return 0;

        JSONObject par = new JSONObject();
        par.put("param", sqlParameter);
        par.put("datas", datas);

        try (SqlSession sqlSession = sqlSessionFactory.openSession()){
            int res = sqlSession.insert(mapperName, par);
            sqlSession.commit();
            return res;
        }
    }

    @Override
    public int insert(List<InsertDescription> descriptions) {
        if (descriptions == null || descriptions.isEmpty())
            return 0;
        int total = 0;
        try (SqlSession sqlSession = sqlSessionFactory.openSession()){
            try {
                for (InsertDescription description: descriptions) {
                    SqlOperator.insert(sqlSession, description);
                }
                sqlSession.commit();
            } catch (Exception e) {
                sqlSession.rollback();
                logger.error("Error happened when insert{}, rollback.", e);
                return 0;
            }
        }
        return total;
    }

    public void execute(Consumer<SqlSession> operate) {
        if (operate == null )
            return ;
        try (SqlSession sqlSession = sqlSessionFactory.openSession()){
            try {
                operate.accept(sqlSession);
                sqlSession.commit();
            } catch (Exception e) {
                sqlSession.rollback();
                logger.error("Error happened when insert{}, rollback.", e);
            }
        }
    }


}
