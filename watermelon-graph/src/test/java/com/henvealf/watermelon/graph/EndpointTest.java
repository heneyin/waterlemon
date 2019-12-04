package com.henvealf.watermelon.graph;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author hongliang.yin/Henvealf
 * @date 2019-11-12
 */
public class EndpointTest {

    @Test
    public void equalsTest() {
        Endpoint<String> endpoint1 = Endpoint.create("001", "002");
        Endpoint<String> endpoint2 = Endpoint.create("001", "003");
        Assert.assertNotEquals(endpoint1, endpoint2);

        Endpoint<String> endpoint3 = Endpoint.create("001", "002");
        Assert.assertEquals(endpoint1, endpoint3);

        Endpoint<String> endpoint4 = Endpoint.create("002", "001");
        Assert.assertEquals(endpoint1, endpoint4);
    }

}