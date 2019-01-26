package com.henvealf.watermelon.sql2;

/**
 * <p>
 * the sql field
 * <p>
 *
 * @author hongliang.yin/Henvealf on 2019/1/8
 */
public interface SqlField{

    /**
     * the field alias
     * @param alias
     * @return
     */
    SqlField as(String alias);

    String toString();

    default SqlCondition eq(Object o){
        return SqlConditions.eqCondition(this, o);
    }

    default SqlCondition gt(Object o){
        return SqlConditions.gtCondition(this, o);
    }

    default SqlCondition lt(Object o){
        return SqlConditions.ltCondition(this, o);
    }

    default SqlCondition ge(Object o){
        return SqlConditions.geCondition(this, o);
    }

    default SqlCondition le(Object o){
        return SqlConditions.leCondition(this, o);
    }

    default SqlCondition ne(Object o){
        return SqlConditions.neCondition(this, o);
    }

    default SqlCondition between(Object o){
        return SqlConditions.betweenCondition(this, o);
    }

    default SqlCondition in(Object o){
        return SqlConditions.inCondition(this, o);
    }

    default SqlCondition notIn(Object o){
        return SqlConditions.notInCondition(this, o);
    }

    default SqlCondition method(Object o){
        return SqlConditions.methodCondition(o);
    }

}
