package com.henvealf.watermelon.sql2;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * The util of check sql ele type.
 * <p>
 *
 * @author hongliang.yin/Henvealf on 2019/1/19
 */
public class SqlTypeCheck {

    //--------------------------------------------------------
    public static void checkField(Object o) {
        Preconditions.checkNotNull(o, "SQL Field can't be null");
        if (!(o instanceof String || o instanceof SqlField))
            throw new IllegalTypeException("SQL Field type must be String or SqlField, now is " + o.getClass().getCanonicalName());
    }

    public static void checkField(Object[] os) {
        for (Object o: os) {
            checkField(o);
        }
    }

    //--------------------------------------------------------
    public static SqlInterface checkTable(Object o) {
        Preconditions.checkNotNull(o, "SQL FROM table can't be null");
        if (o instanceof String) {
            return new Sql((String)o);
        } else if (o instanceof Sql) {
            return (Sql) o;
        }
        throw new IllegalTypeException("SQL FROM table type must be String or Sql, now is " + o.getClass().getCanonicalName());
    }

    public static List<SqlInterface> checkTable(Object[] os) {
        Preconditions.checkNotNull(os, "SQL FROM tables can not be null");
        Preconditions.checkArgument( os.length > 0 && os.length <= 2 , "From table such only be two.");
        List<SqlInterface> sqlList = new ArrayList<>();
        for (Object o: os) {
            sqlList.add(checkTable(o));
        }
        return sqlList;
    }

}
