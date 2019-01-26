package com.henvealf.watermelon.sql2;

import org.checkerframework.checker.units.qual.A;

/**
 * <p>
 *
 * <p>
 *
 * @author hongliang.yin/Henvealf on 2019/1/10
 */
public class SqlShortcuts {

    public static SqlField col(String col) {
        return new SqlCol(col);
    }

    public static SqlField col(String tableAlias, String col) {
        return new SqlCol(tableAlias + "." + col);
    }

    public static String ltr(Object ltr) {
        return new SqlLiteral(ltr).toString();
    }

    public static SqlField ltrObj(Object ltr) {
        return new SqlLiteral(ltr);
    }

    public static SqlInterface sql(){
        return new Sql();
    }

    public static SqlInterface sql(String content){
        return new Sql(content);
    }

    public static SqlInterface select() {
        return new Sql();
    }

    public static SqlInterface select(Object... field) {
        return new Sql().select(field);
    }
}
