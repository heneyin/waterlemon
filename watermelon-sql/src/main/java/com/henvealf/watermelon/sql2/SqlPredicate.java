package com.henvealf.watermelon.sql2;

import com.henvealf.watermelon.sql.IllegalArgTypeException;
import com.henvealf.watermelon.sql.SqlBuilder;
import com.henvealf.watermelon.sql.SqlField;
import com.henvealf.watermelon.sql.SqlLiteral;

/**
 * <p>
 *
 * <p>
 *
 * @author hongliang.yin/Henvealf on 2019/1/10
 */
public interface SqlPredicate {

    SqlPredicate eq(Object field);

    SqlPredicate lt(Object field);

    SqlPredicate le(Object field);

    SqlPredicate gt(Object field);

    SqlPredicate ge(Object field);

    SqlPredicate in(SqlBuilder sqlBuilder);

    SqlPredicate notIn(SqlBuilder sqlBuilder);

    SqlPredicate in(Object[] strs);

    SqlPredicate notIn(Object[] strs);

    SqlPredicate isNull();

    SqlPredicate isNotNull();

    default String predicate(String condition, Object colOb) {
        if (colOb instanceof SqlField || colOb instanceof String || colOb instanceof Number) {
            return String.format("%s %s %s", toString(), condition, colOb);
        } else if (colOb instanceof SqlBuilder) {
            return String.format("%s %s (%s)", toString(), condition, ((SqlBuilder) colOb).build());
        } else if (colOb instanceof Object[]) {
            Object[] objs = (Object[]) colOb;
            String[] newObjs = new String[objs.length];
            for (int i = 0; i < objs.length; i++) {
                newObjs[i] = new SqlLiteral(objs[i]).toString();
            }
            return String.format("%s %s (%s)", toString(), condition, String.join(", ",newObjs));
        } else {
            throw new IllegalArgTypeException("where predicate value should be String, Number or Sql, now is " + colOb.getClass().getCanonicalName());
        }
    }

}
