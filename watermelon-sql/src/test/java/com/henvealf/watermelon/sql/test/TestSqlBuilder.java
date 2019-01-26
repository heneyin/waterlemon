package com.henvealf.watermelon.sql.test;

import com.henvealf.watermelon.sql2.*;
import org.junit.Test;

import static com.henvealf.watermelon.sql2.SqlShortcuts.col;
import static com.henvealf.watermelon.sql2.SqlShortcuts.select;
import static com.henvealf.watermelon.sql2.SqlShortcuts.sql;

/**
 * <p>
 *
 * <p>
 *
 * @author hongliang.yin/Henvealf on 2019/1/8
 */
public class TestSqlBuilder {

//    @Test
//    public void testSelect() {
//        String[] array = new String[] {
//                "123","456"
//        };
//
//        Integer[] intArray = new Integer[] {
//                1,2,3,4,5
//        };
//
//        SqlMethod m =  new SparkSqlMethod();
//
//        Sql qwe = sql()
//                .select("name", "age",
//                        ltr(1111),
//                        col("1111").as("num"),
//                        col("array()").as("a"),
//                        m.max("age"))
//                .from("test_table").as("t")
//                .where( col("xinbie").eq( ltr("123") ) )
//                .whereTrue()
//                .and( qa(col("id").eq(001)) .and() .col("name").eq("qwe")
//                        //.and() .col("name").eq("hello"))// .and() .qa(col("phone").eq(123131))
//                    );
//        System.out.println(qwe.build());
//    }

    @Test
    public void sql2() {
        SqlInterface where = new Sql().select("a", "b", "c")
                .from("t1", "t2")
                .on(col("t1","name").eq(col("t2","name")))
                .where(new SqlCol("haha").eq("123"))
                .and(col("wodetian").eq("nide"))
                .groupBy("t1","t2")
                .orderBy("t1","t2")
                .limit(10);
        System.out.println(where.toString());
    }

    @Test
    public void testJoin() {
        SqlInterface where = new Sql()
                .select("a", "b", "c")
                .from("t1")
                .leftJoin("table2", "t2")
                .on(col("t1","name").eq(col("t2","name")))
                .leftJoin("t3")
                .on("t3.a","t4.a")
                .where(new SqlCol("haha").eq("123"))
                .and(col("wodetian").eq("nide"))
                .groupBy("t1","t2")
                .orderBy("t1","t2")
                .limit(10);
        System.out.println(where.toString());
    }

    @Test
    public void testJoin1() {
        // 测试 join 过后只到 where
        SqlInterface sql = sql()
                .select("a", "b", "c")
                .from("t1")
                .leftJoin("table2", "t2")
                .on(col("t1","name").eq(col("t2","name")))
                .leftJoin("t3")
                .on("t3.a","t4.a")
                .where(new SqlCol("haha").eq("123"));
        System.out.println(sql);
    }

    @Test
    public void testJoinTable() {
        SqlInterface innerSql =
                select("*")
                .from("user")
                .where(col("id").eq("123"));

        SqlInterface ourSql = sql().select("*").from(innerSql.as("inner")).limit(10);

        System.out.println(ourSql);
    }

    @Test
    public void testInSql() {
        SqlInterface ourSql =
                select("*")
                .from("user")
                .where(col("user_name").notIn(select("*").from("user").where(col("id").eq("123"))))
                .limit(10);
        System.out.println(ourSql.toString());
    }

    @Test
    public void testMethod() {
        SqlMethod method = new SparkSqlMethod();
        select(method.max(col("age")), "group_id")
                .from("groups")
                .where(col("group_id").gt(123)).and(col("123").method(method.arrayContains("ips", "qweqweq")))
                .groupBy("group_id")
                .limit(10).print();
    }

    @Test
    public void testCondition() {

        SqlCondition root = new SqlCondition("OR");
        SqlCondition and1 = new SqlCondition("and");
        SqlCondition and2 = new SqlCondition("and");

        root.addChild(and1);
        root.addChild(and2);
        root.addChild(new SqlCondition("haha", "=", "hehe"));


        and1.addChild(new SqlCondition("name", "=", "123"));
        and1.addChild(new SqlCondition("age", "=", "hene"));
        and1.addChild(new SqlCondition("type", "=", "1"));

        and2.addChild(new SqlCondition("link", "=", "www"));


        System.out.println(root.toSqlString());
    }

    @Test
    public void testConditionParentheses() {
        SqlCondition root = new SqlCondition("AND");
        SqlCondition and1 = new SqlCondition("AND");
        SqlCondition c1 = new SqlCondition("haha", "=", "hehe");

        root.addChild(and1);
        root.addChild(c1);

        SqlCondition a1 = new SqlCondition("AND");
        and1.addChild(a1);
        and1.addChild(new SqlCondition("name", "=", "123"));

        a1.addChild(new SqlCondition("pp", "=", "sda"));
        a1.addChild(new SqlCondition("pp", "=", "sda"));
        System.out.println(root.toSqlString());
    }

    @Test
    public void testCondition2() {
        SqlCondition sqlCondition = new SqlCondition("a", "=", "b");
        SqlConditionAssembler assumbler = new SqlConditionAssembler();
        assumbler.setRoot(sqlCondition);
        assumbler.linkNew(new SqlCondition("and"));
        assumbler.linkNew(new SqlCondition("c","=","d"));
        assumbler.linkNew(new SqlCondition("OR"));
        assumbler.linkNew(new SqlCondition("d", "=", "e"));
        assumbler.linkNew(new SqlCondition("OR"));
        assumbler.linkNew(new SqlCondition("C", "=", "C"));
        System.out.println(assumbler.assumble());
    }

    @Test
    public void testCondition3() {
        SqlCondition sqlCondition = SqlConditions.eqCondition("a", "b");
        SqlConditionAssembler assumbler = new SqlConditionAssembler();
        assumbler.setRoot(sqlCondition);
        assumbler.linkNew(SqlConditions.andCondition());
        assumbler.linkNew(SqlConditions.eqCondition("c", "d"));
        assumbler.linkNew(SqlConditions.andCondition());
        assumbler.linkNew(SqlConditions.eqCondition("d", "f"));

        System.out.println(assumbler.assumble());

    }

    @Test
    public void testSqlConditionAssembler() {
        SqlCondition sqlCondition = SqlConditions.eqCondition("a", "b");

        SqlCondition nextRoot = new SqlConditionAssembler().eq("_Qwe", "qwdqd")
                .or().eq("rtd", "_213").getRoot();

        System.out.println(nextRoot.isLeaf());
        System.out.println(nextRoot);
        SqlConditionAssembler assumbler = new SqlConditionAssembler();
        assumbler.setRoot(sqlCondition)
                .and().eq("c", "d")
                .or().condition(nextRoot)
                .and().condition(new SqlConditionAssembler().eq("_Qwe", "qwdqd")
                .or().eq("rtd", "_213").getRoot());

        System.out.println(assumbler.assumble());
    }


    @Test
    public void testInstansOf() {
        Object a = "123";

        System.out.println(a instanceof String);

        Object b = 123;

        System.out.println(b instanceof Number);
    }

}
