package com.henvealf.watermelon.sql2;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *   包含各种各样的条件。
 * <p>
 *
 * @author hongliang.yin/Henvealf on 2019/1/24
 */
public class SqlConditions {

    public static SqlCondition andCondition() {
        return new SqlCondition("AND");
    }

    public static SqlCondition orCondition() {
        return new SqlCondition("OR");
    }

    public static SqlCondition condition(Object left, String symbol, String right) {
        return new SqlCondition(left, symbol, right);
    }

    public static SqlCondition eqCondition(Object left, Object right) {
        return new SqlCondition(left, "=", right);
    }

    public static SqlCondition gtCondition(Object left, Object right) {
        return new SqlCondition(left, ">", right);
    }

    public static SqlCondition ltCondition(Object left, Object right) {
        return new SqlCondition(left, "<", right);
    }

    public static SqlCondition geCondition(Object left, Object right) {
        return new SqlCondition(left, ">=", right);
    }

    public static SqlCondition leCondition(Object left, Object right) {
        return new SqlCondition(left, "<=", right);
    }

    public static SqlCondition neCondition(Object left, Object right) {
        return new SqlCondition(left, "!=", right);
    }

    public static SqlCondition inCondition(Object left, Object right) {
        return new SqlCondition(left, "IN", right);
    }

    public static SqlCondition notInCondition(Object left, Object right) {
        return new SqlCondition(left, "NOT IN", right);
    }

    public static SqlCondition betweenCondition(Object left, Object right) {
        return new SqlCondition(left, "BETWEEN", right);
    }

    public static SqlCondition methodCondition(Object method) {
        return new SqlCondition(method);
    }
}
