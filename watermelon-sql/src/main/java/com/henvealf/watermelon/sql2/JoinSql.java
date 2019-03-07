package com.henvealf.watermelon.sql2;

import com.google.common.base.Preconditions;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * <p>
 *
 * <p>
 *
 * @author hongliang.yin/Henvealf on 2019/1/19
 */
public class JoinSql implements SqlInterface {

    protected static final String LEFT = " LEFT";
    protected static final String LEFT_OUTER = " LEFT OUTER";

    protected static final String RIGHT = " RIGHT";
    protected static final String RIGHT_OUTER = " RIGHT OUTER";

    protected static final String INNER = " INNER";
    protected static final String FULL = " FULL";

    private SqlInterface tableName;
    private String tableAlias;
    private String joinType;
    private SqlConditionAssembler onCondition;
    private SqlConditionAssembler whereCondition;
    private Sql sql;

    public JoinSql(SqlInterface tableName, String tableAlias, String joinType, Sql sql) {
        SqlTypeCheck.checkTable(tableName);
        Preconditions.checkNotNull(joinType);
        Preconditions.checkNotNull(sql);

        this.tableName = tableName;
        this.tableAlias = tableAlias;
        this.joinType = joinType;
        this.sql = sql;
    }

    public JoinSql(Sql tableName, String joinType, Sql sql) {
        this(tableName, null, joinType, sql);
    }

    @Override
    public SqlInterface select(Object... cols) {
        throw new NotImplementedException();
    }

    @Override
    public SqlInterface from(Object... tables) {
        throw new NotImplementedException();
    }

    @Override
    public SqlInterface join(Object tableName, String alias, String joinType) {
        return sql.join(tableName, alias, joinType);
    }

    public JoinSql on(SqlCondition condition) {
        if (onCondition == null) {
            onCondition =  new SqlConditionAssembler();
        }
        onCondition.linkNew(condition);
        return this;
    }

    public SqlInterface where(SqlCondition condition) {
        if (whereCondition == null) {
            whereCondition =  new SqlConditionAssembler();
        }
        whereCondition.linkNew(condition);
        return this;
    }

    public SqlInterface and(SqlCondition condition) {
        Preconditions.checkNotNull(whereCondition);
        whereCondition.or().condition(condition);
        return this;
    }

    @Override
    public SqlInterface and(SqlField condition) {
        return and(SqlConditions.methodCondition(condition));
    }

    public SqlInterface or(SqlCondition condition) {
        Preconditions.checkNotNull(whereCondition);
        whereCondition.or().condition(condition);
        return this;
    }

    public SqlInterface or(SqlField condition) {
        return or(SqlConditions.methodCondition(condition));
    }
    // ----------------- join 结束关键字
    public Sql groupBy(Object... cols) {
        sql.groupBy(cols);
        return sql;
    }

    public Sql orderBy(Object... cols) {
        sql.orderBy(cols);
        return sql;
    }

    @Override
    public SqlInterface desc() {
        return null;
    }

    @Override
    public SqlInterface asc() {
        return null;
    }

    public Sql limit(long limit) {
        sql.limit(limit);
        return sql;
    }

    @Override
    public SqlInterface as(String alias) {
        throw new NotImplementedException();
    }

    public String getJoinSql() {
        StringBuilder sb = new StringBuilder();
        sb.append(joinType).append(" JOIN ").append(tableName.toStringAsSubSql());
        if (onCondition != null) {
            sb.append(" ON ").append(onCondition.assemble());
        }
        if (whereCondition != null) {
            sb.append(" WHERE ").append(whereCondition.assemble());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return sql.toString();
    }

}
