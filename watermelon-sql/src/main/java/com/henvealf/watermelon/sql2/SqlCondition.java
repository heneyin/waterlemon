package com.henvealf.watermelon.sql2;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * <p>
 *
 * @author hongliang.yin/Henvealf on 2019/1/10
 */
public class SqlCondition {

    // SqlField, String or SqlMethod
    private Object left;
    // eq, lt, gt, in, not in, and, or ..etc
    private String symbol;
    // SqlField String or SqlMethod
    private Object right;

    private boolean symbolDiffWithParent;

    private List<SqlCondition> children;

    public SqlCondition(Object left, String symbol, Object right) {
        Preconditions.checkNotNull(left);
        Preconditions.checkNotNull(right);
        Preconditions.checkNotNull(symbol);
        this.left = left;
        this.symbol = symbol;
        this.right = right;
    }

    /**
     * used to the method condition like array_contains(arr, ele)
     * @param left
     */
    public SqlCondition(Object left) {
        this(left, "", "");
    }

    private SqlCondition() {}

    public SqlCondition(String symbol) {
        Preconditions.checkNotNull(symbol);
        this.symbol = symbol;
        this.children = new ArrayList<>();
    }

    public void addChild(SqlCondition sqlCondition) {
        Preconditions.checkArgument(!isLeaf(), "The leaf node can't add child.");

        this.children.add(sqlCondition);
        sqlCondition.symbolDiffWithParent = !sqlCondition.symbol.equals(this.symbol);
    }
    public void addChildOnFirst(SqlCondition sqlCondition) {
        Preconditions.checkArgument(!isLeaf(), "The leaf node can't add child.");

        this.children.add(0, sqlCondition);
        sqlCondition.symbolDiffWithParent = !sqlCondition.symbol.equals(this.symbol);
    }

    public void addChildren(List<SqlCondition> sqlConditions){
        for (SqlCondition sqlCondition: sqlConditions) {
            addChild(sqlCondition);
        }
    }

    public SqlCondition joinParent(SqlCondition parent) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkArgument(!parent.isLeaf(), "The leaf node can't add child.");
        parent.addChildOnFirst(this);
        return parent;
    }

    public boolean isLeaf() {
        return left != null && right != null && symbol != null;
    }

    public int childrenSize() {
        return this.children.size();
    }

    public SqlCondition getChild(int index) {
        return this.children.get(index);
    }

    public List<SqlCondition> getChildren() {
        return children;
    }

    /**
     * weather need Parentheses
     * @return
     */
    public boolean isNeedParentheses() {
        if (isLeaf()) return false;
        return children.size() > 1 && this.symbolDiffWithParent;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean symbolEqual(SqlCondition sqlCondition){
        Preconditions.checkNotNull(sqlCondition);
        return this.symbol.equals(sqlCondition.symbol);
    }

    @Override
    public String toString() {
        if (isLeaf()) {
            if ("BETWEEN".equals(symbol)) {
                return "BETWEEN" + left.toString() + " AND " + right.toString();
            } else {
                String rightStr = null;
                // in  not in a sql
                if (right instanceof Sql) {
                    rightStr = " (" + right + ") ";
                } else {
                    rightStr = right.toString();
                }
                return String.valueOf(left) + " " + symbol + " " + rightStr;
            }
        } else {
            return " " + symbol + " ";
        }
    }

    public String toSqlString() {
        return getSqlFromConditionTree(this);
    }

    /**
     * generate the condition sql base the root node of the condition tree .
     * @param root
     * @return condition sql String
     */
    private static String getSqlFromConditionTree(SqlCondition root) {
        if (root.isLeaf()) {
            return root.toString();
        } else {
            List<String> childResult = new ArrayList<>();
            for (int i = 0; i < root.childrenSize(); i ++) {
                SqlCondition child = root.getChild(i);
                if (child.isNeedParentheses() && i != 0) {
                    childResult.add(" ( " + getSqlFromConditionTree(child) + " ) ");
                } else {
                    childResult.add(getSqlFromConditionTree(child));
                }
            }
            return SqlUtils.joinStr(" " + root.getSymbol() + " ", childResult);
        }
    }

}
