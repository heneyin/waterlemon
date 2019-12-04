package com.henvealf.watermelon.graph;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author hongliang.yin/Henvealf
 * @date 2019-11-12
 */
public class EdgeTest {

    @Test
    public void equalsTest() {
        Edge<String> edg1 = new Edge<>("001", "002", "value1");
        Edge<String> edg2 = new Edge<>("001", "002", "value2");
        Assert.assertEquals(edg1, edg2);

        Edge<String> edg3 = new Edge<>("002", "001", "value3");
        Assert.assertEquals(edg1, edg3);

        Edge<String> edg4 = new Edge<>("003", "001", "value3");
        Assert.assertNotEquals(edg1, edg4);
    }

}