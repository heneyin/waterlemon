package com.henvealf.watermelon.graph;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author hongliang.yin/Henvealf
 * @date 2019-11-12
 */
public class VertexTest {

    @Test
    public void equalsTest() {
        Vertex<String> vertex1 = new Vertex<>("hello", "helloValue");
        Vertex<String> vertex2 = new Vertex<>("world", "worldValue");
        Assert.assertNotEquals(vertex1, vertex2);

        Vertex<String> vertex3 = new Vertex<>("world", "worldaa");
        Assert.assertEquals(vertex2, vertex3);
    }

}