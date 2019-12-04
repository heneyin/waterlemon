package com.henvealf.watermelon.graph;


/**
 * @author hongliang.yin/Henvealf
 * @date 2019-11-13
 */
public class GraphTestUtil {

    /**
     * 顶点值为经济。边的值为交通费用。
     */
    public static Graph<Integer, Integer> createCityGraph() {
        GraphBuilder<Integer, Integer> graphBuilder = GraphBuilder.getBuilder();
        graphBuilder.addVertex("beijing", 10000);
        graphBuilder.addVertex("shanghai", 11000);
        graphBuilder.addVertex("shenzhen", 11010);

        graphBuilder.addEdge("beijing", "shanghai", 600);
        graphBuilder.addEdge("beijing", "shenzhen", 1000);
        graphBuilder.addEdge("shanghai", "shenzhen", 1200);
        return graphBuilder.build();
    }
}
