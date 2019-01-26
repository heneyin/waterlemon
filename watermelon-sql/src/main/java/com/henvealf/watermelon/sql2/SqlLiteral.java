package com.henvealf.watermelon.sql2;

import com.henvealf.watermelon.sql.IllegalArgTypeException;

/**
 * <p>
 *   sql 中用到的字面量, '123' 或者 123.123
 * <p>
 *
 * @author hongliang.yin/Henvealf on 2019/1/19
 */
public class SqlLiteral implements SqlField {

    private Object literal;
    private String alias;

    public SqlLiteral(Object literal) {
        this.literal = literal;
    }

    @Override
    public SqlField as(String colAlias) {
        this.alias = alias;
        return this;
    }

    @Override
    public String toString() {
        if (literal instanceof String) {
            return String.format("'%s'", literal);
        } else if (literal instanceof Number){
            return String.valueOf(literal);
        } else {
            throw new IllegalTypeException("Sql Literal type is not a number or string.");
        }
    }
}
