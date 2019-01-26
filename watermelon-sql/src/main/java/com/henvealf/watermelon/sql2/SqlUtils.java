package com.henvealf.watermelon.sql2;

import com.google.common.base.Preconditions;
import com.henvealf.watermelon.sql.SqlLiteral;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * <p>
 *
 * <p>
 *
 * @author hongliang.yin/Henvealf on 2019/1/19
 */
public class SqlUtils {

    /**
     * Object[] to String[]
     * @param objs
     * @return
     */
    public static String[] valueOf(Object[] objs) {
        String[] stringArray = new String[objs.length];
        for (int i = 0; i < objs.length; i++) {
            stringArray[i] = objs[i].toString();
        }
        return stringArray;
    }

    public static String[] valueOf(List<? extends Object> colOb) {
        String[] stringArray = new String[colOb.size()];
        for (int i = 0; i < colOb.size(); i++) {
            stringArray[i] = colOb.get(i).toString();
        }
        return stringArray;
    }

    public static <T> String[] valueOf(List<T> colOb, Function<T, String> fun) {
        String[] stringArray = new String[colOb.size()];
        for (int i = 0; i < colOb.size(); i++) {
            stringArray[i] = fun.apply(colOb.get(i));
        }
        return stringArray;
    }

    //----------------------------------------------
    public static String joinStr(String t, String[] strs) {
        return String.join(t, SqlUtils.valueOf(strs));
    }

    public static String joinStr(String[] strs) {
        return String.join( ", ", SqlUtils.valueOf(strs));
    }

    public static String joinStr(String t, List<? extends Object> strs) {
        return String.join(t, SqlUtils.valueOf(strs) );
    }

    public static <T> String joinStr(String t, List<T> strs, Function<T, String> fun) {
        return String.join(t, SqlUtils.valueOf(strs, fun) );
    }

    public static <T> String joinStr(List<T> strs, Function<T, String> fun) {
        return String.join(", ", SqlUtils.valueOf(strs, fun) );
    }

    public static String joinStr(List<? extends Object> strs) {
        return joinStr(", ", strs);
    }

    public static String joinObject(String t, Object[] strs) {
        SqlTypeCheck.checkField(strs);
        return String.join( t, SqlUtils.valueOf(strs));
    }

    public static String joinObject(Object[] strs) {
        SqlTypeCheck.checkField(strs);
        return joinObject(", ", strs);
    }

    //----------------------------------------------
    public static String asTail(String alias) {
        if (!StringUtils.isBlank(alias)) {
            return String.format(" AS %s ", alias);
        }
        return "";
    }

    //-----------------------------------------
    public static void copyObjectsToFieldList(List<SqlField> fieldList, Object... cols) {
        Preconditions.checkNotNull(fieldList, "The destination List can't be null.");
        SqlTypeCheck.checkField(cols);
        for (Object o : cols) {
            if (o instanceof String) {
                fieldList.add(new SqlCol(String.valueOf(o)));
            } else {
                fieldList.add((SqlField) o);
            }
        }
    }


    public static boolean notNullAndEmptyList(List list) {
        return list != null && !list.isEmpty();
    }

    public static boolean nullOrEmptyList(List list) {
        return list == null || list.isEmpty();
    }

}
