package com.henvealf.watermelon.sql2;

/**
 * <p>
 *  Sql 抽象接口。
 * <p>
 *
 * @author hongliang.yin/Henvealf
 * @since 2019/1/26
 */
public interface SqlInterface {

    String LEFT = " LEFT";
    String LEFT_OUTER = " LEFT OUTER";

    String RIGHT = " RIGHT";
    String RIGHT_OUTER = " RIGHT OUTER";

    String INNER = " INNER";
    String FULL = " FULL";

    SqlInterface select(Object... cols);

    SqlInterface from(Object... tables);

    SqlInterface join(Object tableName, String alias, String joinType);

    default SqlInterface leftJoin(Object tableName, String alias) {
        return join(tableName, alias, LEFT);
    }

    default SqlInterface rightJoin(Object tableName, String alias) {
        return join(tableName, alias, RIGHT);
    }

    default SqlInterface innerJoin(Object tableName, String alias) {
        return join(tableName, alias, INNER);
    }

    default SqlInterface leftJoin(Object tableName) {
        return join(tableName, null, LEFT);
    }

    default SqlInterface rightJoin(Object tableName) {
        return join(tableName, null, RIGHT);
    }

    default SqlInterface innerJoin(Object tableName) {
        return join(tableName, null, INNER);
    }

    SqlInterface on(SqlCondition condition);

    default SqlInterface on(Object left, Object right) {
        return on(SqlConditions.eqCondition(left, right));
    }

    SqlInterface where(SqlCondition sqlCondition);

    SqlInterface and(SqlCondition condition);

    SqlInterface and(SqlField condition);

    SqlInterface or(SqlCondition condition);

    SqlInterface or(SqlField condition);

    SqlInterface groupBy(Object... cols);

    SqlInterface orderBy(Object... cols);

    SqlInterface desc();

    SqlInterface asc();

    SqlInterface limit(long limit);

    SqlInterface as(String alias);

    public String toString();

    default String toSqlStirng(){
        return toString();
    }

    default String toStringAsSubSql() {
        return toString();
    }

    default void print() {
        System.out.println(toString());
    }
}
