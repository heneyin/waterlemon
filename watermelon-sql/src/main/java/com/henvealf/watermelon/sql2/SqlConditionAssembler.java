package com.henvealf.watermelon.sql2;

import com.google.common.base.Preconditions;

/**
 * <p>
 *
 * <p>
 *
 * @author hongliang.yin/Henvealf on 2019/1/24
 */
public class SqlConditionAssembler {

    private SqlCondition root ;

    public SqlConditionAssembler() {}

    public SqlConditionAssembler setRoot(SqlCondition root) {
        this.root = root;
        return this;
    }

    public SqlConditionAssembler and() {
        linkNew(SqlConditions.andCondition());
        return this;
    }

    public SqlConditionAssembler or() {
        linkNew(SqlConditions.orCondition());
        return this;
    }

    public SqlConditionAssembler eq(Object left, Object right) {
        linkNew(SqlConditions.eqCondition(left, right));
        return this;
    }

    public SqlConditionAssembler gt(Object left, Object right) {
        linkNew(SqlConditions.gtCondition(left, right));
        return this;
    }

    public SqlConditionAssembler lt(Object left, Object right) {
        linkNew(SqlConditions.ltCondition(left, right));
        return this;
    }

    public SqlConditionAssembler ge(Object left, Object right) {
        linkNew(SqlConditions.geCondition(left, right));
        return this;
    }

    public SqlConditionAssembler le(Object left, Object right) {
        linkNew(SqlConditions.leCondition(left, right));
        return this;
    }

    public SqlConditionAssembler ne(Object left, Object right) {
        linkNew(SqlConditions.neCondition(left, right));
        return this;
    }

    public SqlConditionAssembler in(Object left, Object right) {
        linkNew(SqlConditions.inCondition(left, right));
        return this;
    }

    public SqlConditionAssembler notIn(Object left, Object right) {
        linkNew(SqlConditions.inCondition(left, right));
        return this;
    }

    public SqlConditionAssembler between(Object left, Object right) {
        linkNew(SqlConditions.betweenCondition(left, right));
        return this;
    }

    public SqlConditionAssembler method(Object left) {
        linkNew(SqlConditions.methodCondition(left));
        return this;
    }

    public SqlConditionAssembler condition(SqlCondition next) {
        linkNew(next);
        return this;
    }

    public void linkNew(SqlCondition sqlCondition) {
        Preconditions.checkNotNull(sqlCondition);
        if (root == null) {
            root = sqlCondition;
            return;
        }
        if (root.isLeaf()) {
            root = root.joinParent(sqlCondition);
        } else if (sqlCondition.isLeaf()) {
            root.addChild(sqlCondition);
        } else if (!sqlCondition.isLeaf()){
            if (sqlCondition.symbolEqual(root)) {
                // 符号相同，并且有子节点，直接将子节点加到root节点上。
                if (sqlCondition.childrenSize() > 0) {
                    root.addChildren(sqlCondition.getChildren());
                }
            } else {
                // 符号不相同，有子节点，直接添加到root上。
                // 没有子节点，添加为root
                if (sqlCondition.childrenSize() > 0) {
                    root.addChild(sqlCondition);
                } else {
                    root = root.joinParent(sqlCondition);
                }
            }
        }

    }

    public SqlCondition getRoot() {
        return root;
    }

    public String assemble() {
        return root.toSqlString();
    }
}
