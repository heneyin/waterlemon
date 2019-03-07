package com.henvealf.watermelon.sql2;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * a sql builder.
 * 关于类型：
 * SqlCol 代表表中的列，且 String 类型默认表示列。
 * SqlLiteral 代表 Sql 中的字面量。
 * <p>
 *
 * @author hongliang.yin/Henvealf on 2019/1/7
 */
public class Sql implements SqlInterface {

    // Sql 也可以代表一个表，如果不为空，则表示只有一个表名。
    private String thisTableName;
    // Sql 语句代表的表的别名
    private String alias;

    private List<SqlField> selectFields = null;

    private List<SqlInterface> fromTables = null;
    private SqlConditionAssembler whereCondition;

    private SqlConditionAssembler onCondition;
    private List<SqlField> groupByFields;
    private List<SqlField> orderByFields;
    private String order;

    private SqlConditionAssembler havingCondition;
    private long limit = -1;

    private List<JoinSql> joinSqls;

    private CONDITION_LOCATION nowCondiLoca = CONDITION_LOCATION.WHERE;

    private enum CONDITION_LOCATION{
        WHERE, ON, HAVING
    }

    public Sql(String tableName) {
        this.thisTableName = tableName;
    }

    public Sql() { }

    public SqlInterface as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public SqlInterface select(Object... cols) {
        if (selectFields == null) selectFields = new ArrayList<>();
        SqlUtils.copyObjectsToFieldList(selectFields, cols);
        return this;
    }

    @Override
    public SqlInterface from(Object... tables) {
        this.fromTables = SqlTypeCheck.checkTable(tables);
        return this;
    }

    @Override
    public SqlInterface join(Object tableName, String alias, String joinType) {
        if (SqlUtils.nullOrEmptyList(joinSqls))
            joinSqls = new ArrayList<>();
        JoinSql joinSql = new JoinSql(SqlTypeCheck.checkTable(tableName), alias, joinType, this);
        joinSqls.add(joinSql);
        return joinSql;
    }

    @Override
    public SqlInterface on(SqlCondition condition) {
        nowCondiLoca = CONDITION_LOCATION.ON;
        onCondition = new SqlConditionAssembler();
        onCondition.setRoot(condition);
        return this;
    }

    @Override
    public SqlInterface where(SqlCondition sqlCondition) {
        nowCondiLoca = CONDITION_LOCATION.WHERE;
        whereCondition = new SqlConditionAssembler();
        whereCondition.setRoot(sqlCondition);
        return this;
    }

    @Override
    public SqlInterface and(SqlCondition condition) {
        addAndCondition(condition);
        return this;
    }

    @Override
    public SqlInterface and(SqlField condition) {
        addAndCondition(SqlConditions.methodCondition(condition));
        return this;
    }

    @Override
    public SqlInterface or(SqlCondition condition) {
        addOrCondition(condition);
        return this;
    }


    @Override
    public SqlInterface or(SqlField condition) {
        addOrCondition(SqlConditions.methodCondition(condition));
        return this;
    }


    private void addAndCondition(SqlCondition condition) {
        switch (nowCondiLoca) {
            case WHERE:
                whereCondition.and().condition(condition);
                break;
            case HAVING:
                havingCondition.and().condition(condition);
                break;
            case ON:
                onCondition.and().condition(condition);
                break;
        }
    }

    private void addOrCondition(SqlCondition condition) {
        switch (nowCondiLoca) {
            case WHERE:
                whereCondition.or().condition(condition);
                break;
            case HAVING:
                havingCondition.or().condition(condition);
                break;
            case ON:
                onCondition.or().condition(condition);
                break;
        }
    }

    @Override
    public SqlInterface groupBy(Object... cols) {
        if (groupByFields == null) groupByFields = new ArrayList<>();
        SqlUtils.copyObjectsToFieldList(groupByFields, cols);
        return this;
    }

    @Override
    public SqlInterface orderBy(Object... cols) {
        if (orderByFields == null) orderByFields = new ArrayList<>();
        SqlUtils.copyObjectsToFieldList(orderByFields, cols);
        return this;
    }

    @Override
    public SqlInterface desc() {
        order = "DESC";
        return this;
    }

    @Override
    public SqlInterface asc() {
        order = "ASC";
        return this;
    }

    @Override
    public SqlInterface limit(long limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public String toString() {
        // 一个表。
        if (StringUtils.isNotBlank(this.thisTableName)) {
            return thisTableName + SqlUtils.asTail(alias);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ").append(SqlUtils.joinStr(selectFields))
                .append(" FROM ").append(SqlUtils.joinStr(fromTables, SqlInterface::toStringAsSubSql));

        if (onCondition != null) {
            sb.append(" ON ").append(onCondition.assemble());
        }

        if (SqlUtils.notNullAndEmptyList(joinSqls)) {
            sb.append(SqlUtils.joinStr(" ", joinSqls, JoinSql::getJoinSql));
        }

        if (whereCondition != null) {
            sb.append(" WHERE ").append(whereCondition.assemble());
        }

        if (SqlUtils.notNullAndEmptyList(groupByFields)) {
            sb.append(" GROUP BY ").append(SqlUtils.joinStr(groupByFields));
        }

        if (havingCondition != null) {
            sb.append(" HAVING ").append(havingCondition.assemble());
        }

        if (SqlUtils.notNullAndEmptyList(orderByFields)) {
            sb.append(" ORDER BY ").append(SqlUtils.joinStr(groupByFields));
        }

        if (limit > -1) {
            sb.append(" LIMIT ").append(limit);
        }

        // 拼装 SQL；
        return sb.toString();
    }

    /**
     * 获取作为子SQL时的字符串。
     * @return
     */
    public String toStringAsSubSql() {
        if (StringUtils.isBlank(this.thisTableName)) {
            return " (" + toString() + ") " + SqlUtils.asTail(alias);
        }
        return toString() + SqlUtils.asTail(alias);
    }

}
