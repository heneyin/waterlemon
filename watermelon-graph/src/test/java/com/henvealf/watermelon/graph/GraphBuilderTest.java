package com.henvealf.watermelon.graph;


import com.google.common.collect.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author hongliang.yin/Henvealf
 * @date 2019-11-12
 */
public class GraphBuilderTest {

    @Test
    public void normalTest() {
        GraphBuilder<String, String> builder = GraphBuilder.getBuilder();
        builder.addVertex("beijing", "daxing");
        builder.addVertex("shanghai", "hongqiao");
        builder.addVertex("shenzhen", "air");
        builder.addVertex("jinan", "airport");

        builder.addEdge("beijing", "shanghai", "aPerson");
        builder.addEdge("shanghai", "shenzhen", "aPerson");
        builder.addEdge("beijing", "jinan", "aPerson");
        builder.addEdge("beijing", "shenzhen", "aPerson");

        Graph<String, String> graph = builder.build();

        // vertex
        Set<Vertex<String>> vertices = graph.getVertices();
        assertEquals(4, vertices.size());
        assertTrue(vertices.contains(new Vertex<String>("beijing", null)));
        assertTrue(vertices.contains(new Vertex<String>("shanghai", null)));
        assertTrue(vertices.contains(new Vertex<String>("shenzhen", null)));
        assertTrue(vertices.contains(new Vertex<String>("jinan", null)));

        // edge
        Set<Edge<String>> edges = graph.getEdges();
        assertEquals(4, edges.size());
        assertTrue(edges.contains(new Edge<String>("beijing", "shanghai", null)));
        assertTrue(edges.contains(new Edge<String>("shanghai", "shenzhen", null)));
        assertTrue(edges.contains(new Edge<String>("beijing", "jinan", null)));
        assertTrue(edges.contains(new Edge<String>("beijing", "shenzhen", null)));
    }


    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void duplicateVertexTest() {
        GraphBuilder<String, String> graphBuilder = GraphBuilder.getBuilder();
        graphBuilder.addVertex("beijing", "a");
        graphBuilder.addVertex("beijing", "b");
        Graph<String, String> graph = graphBuilder.build();
        Set<Vertex<String>> vertices = graph.getVertices();
        assertEquals(1, vertices.size());
        assertEquals("a", Lists.newArrayList(vertices).get(0).getValue());
    }

    /**
     * 测试边缺少顶点的情况
     */
    @Test
    public void edgeLackVertexTest() {
        GraphBuilder<String, String> graphBuilder = GraphBuilder.getBuilder();
        graphBuilder.addEdge("hello", "world", null);
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Edge endpoint 'hello' not exists in vertices");
        graphBuilder.build();
    }

    @Test
    public void vertexSelfLoopTest() {
        GraphBuilder<String, String> graphBuilder = GraphBuilder.getBuilder();
        graphBuilder.addVertex("hello", null);
        graphBuilder.addEdge("hello", "hello", null);
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Graph not support vertex self loop, 'hello'");
        graphBuilder.build();
    }

}