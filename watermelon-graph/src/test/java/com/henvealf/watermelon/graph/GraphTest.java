package com.henvealf.watermelon.graph;

import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hongliang.yin/Henvealf
 * @date 2019-11-13
 */
public class GraphTest {

    @Test
    public void addTest() {
        Graph<String, String> graph = new Graph<>();
        graph.add(Vertex.of("hello", null),
                  Vertex.of("world", null),
                  null);
        Set<Vertex<String>> vertices = graph.getVertices();
        Assert.assertEquals(2, vertices.size());
        Assert.assertTrue(vertices.contains(Vertex.of("hello", null)));
        Assert.assertTrue(vertices.contains(Vertex.of("world", null)));

        Set<Edge<String>> edges = graph.getEdges();
        Assert.assertEquals(1, edges.size());
        Assert.assertTrue(edges.contains(Edge.of("hello", "world", null)));
    }

    @Rule
    public ExpectedException expectException = ExpectedException.none();

    @Test
    public void addTest_selfLoop() {
        Graph<String, String> graph = new Graph<>();
        expectException.expect(IllegalArgumentException.class);
        expectException.expectMessage("Graph not support vertex self loop, 'hello'");
        graph.add(Vertex.of("hello", null),
                Vertex.of("hello", null),
                null);
    }

    public void addTest_vertexExistsTest() {

    }

    @Test
    public void computeVertexValueTest() {
        GraphBuilder<Integer, String> graphBuilder = GraphBuilder.getBuilder();
        graphBuilder.addVertex("hello", null);
        Graph<Integer, String> graph = graphBuilder.build();

        graph.computeVertexValue("hello", () -> 0, i -> i + 1);
        Optional<Vertex<Integer>> hello = graph.getVertexById("hello");
        Assert.assertEquals(Optional.of(0), hello.map(Vertex::getValue));

        graph.computeVertexValue("hello", () -> 0, i -> i + 1);
        Assert.assertEquals(Optional.of(1), hello.map(Vertex::getValue));

        graph.computeVertexValue("hello", i -> i + 1);
        Assert.assertEquals(Optional.of(2), hello.map(Vertex::getValue));
    }

    @Test
    public void computeEdgeValueTest() {
        GraphBuilder<Integer, Integer> graphBuilder = GraphBuilder.getBuilder();
        graphBuilder.addVertex("hello", null);
        graphBuilder.addVertex("world", null);
        graphBuilder.addEdge("hello", "world", null);
        Graph<Integer, Integer> graph = graphBuilder.build();

        graph.computeEdgeValue(Endpoint.create("hello", "world"), () -> 0, i -> i + 1);
        Optional<Edge<Integer>> hello = graph.getEdgeById("hello", "world");
        Assert.assertEquals(Optional.of(0), hello.map(Edge::getValue));

        graph.computeEdgeValue(Endpoint.create("hello", "world"), () -> 0, i -> i + 1);
        Assert.assertEquals(Optional.of(1), hello.map(Edge::getValue));

        graph.computeEdgeValue(Endpoint.create("hello", "world"), i -> i + 1);
        Assert.assertEquals(Optional.of(2), hello.map(Edge::getValue));
    }

    @Test
    public void filterVertexTest() {
        Graph<Integer, Integer> graph = GraphTestUtil.createCityGraph();
        Graph<Integer, Integer> filtedGraph = graph.filterVertex(vertex -> vertex.getValue() > 10000);
        Set<Vertex<Integer>> vertices = filtedGraph.getVertices();
        Assert.assertEquals(2, vertices.size());
        Assert.assertTrue(vertices.contains(Vertex.of("shanghai", null)));
        Assert.assertTrue(vertices.contains(Vertex.of("shenzhen", null)));

        Set<Edge<Integer>> edges = filtedGraph.getEdges();
        Assert.assertEquals(1, edges.size());
        Assert.assertTrue(edges.contains(Edge.of("shanghai", "shenzhen", null)));
    }

    @Test
    public void filterEdgeTest() {
        Graph<Integer, Integer> graph = GraphTestUtil.createCityGraph();
        Graph<Integer, Integer> filtedGraph = graph.filterEdge(edge -> edge.getValue() > 1000);
        Set<Vertex<Integer>> vertices = filtedGraph.getVertices();

        Assert.assertEquals(2, vertices.size());
        Assert.assertTrue(vertices.contains(Vertex.of("shanghai", null)));
        Assert.assertTrue(vertices.contains(Vertex.of("shenzhen", null)));

        Set<Edge<Integer>> edges = filtedGraph.getEdges();
        Assert.assertEquals(1, edges.size());
        Assert.assertTrue(edges.contains(Edge.of("shanghai", "shenzhen", null)));
    }

    @Test
    public void filterTripleTest() {
        Graph<Integer, Integer> graph = GraphTestUtil.createCityGraph();
        Graph<Integer, Integer> filtedGraph = graph.filterTriple((triple) -> {
            Vertex<Integer> left = triple.getLeft();
            Vertex<Integer> right = triple.getMiddle();
            return left.getValue() > 10000 && right.getValue() > 10000;
        });
        Set<Vertex<Integer>> vertices = filtedGraph.getVertices();

        Assert.assertEquals(2, vertices.size());
        Assert.assertTrue(vertices.contains(Vertex.of("shanghai", null)));
        Assert.assertTrue(vertices.contains(Vertex.of("shenzhen", null)));

        Set<Edge<Integer>> edges = filtedGraph.getEdges();
        Assert.assertEquals(1, edges.size());
        Assert.assertTrue(edges.contains(Edge.of("shanghai", "shenzhen", null)));
    }

    @Test
    public void getConnectedIdsTest() {
        Graph<Integer, Integer> graph = GraphTestUtil.createCityGraph();
        Set<String> ids = graph.getConnectedIds("beijing");
        Assert.assertEquals(ids, Sets.newHashSet("shanghai", "beijing", "shenzhen"));
    }

    @Test
    public void getIslandsTest() {
        Graph<Integer, Integer> graph = GraphTestUtil.createCityGraph();
        List<Graph<Integer, Integer>> islands = graph.getIslands();
        Assert.assertEquals(1, islands.size());
        Graph<Integer, Integer> island = islands.get(0);
        Set<String> ids = island.getVertices().stream().map(Vertex::getId).collect(Collectors.toSet());
        Assert.assertEquals(ids, Sets.newHashSet("shanghai", "beijing", "shenzhen"));

        // add one
        graph.addVertex("congqing", null);

        List<Graph<Integer, Integer>> islands1 = graph.getIslands();
        Assert.assertEquals(2, islands1.size());
        islands1.forEach(g -> {
            Set<String> innerIds = g.getVertices().stream().map(Vertex::getId).collect(Collectors.toSet());
            if (innerIds.size() == 1) {
                Assert.assertEquals(innerIds, Sets.newHashSet("congqing"));
            } else if (innerIds.size() == 3) {
                Assert.assertEquals(innerIds, Sets.newHashSet("shanghai", "beijing", "shenzhen"));
            } else {
                Assert.fail("islands size error");
            }
        });

        graph.add(Vertex.of("congqing", null), Vertex.of("chengdu", null), null);
        List<Graph<Integer, Integer>> islands2 = graph.getIslands();
        Assert.assertEquals(2, islands2.size());
        islands2.forEach(g -> {
            Set<String> innerIds = g.getVertices().stream().map(Vertex::getId).collect(Collectors.toSet());
            if (innerIds.size() == 2) {
                Assert.assertEquals(innerIds, Sets.newHashSet("congqing", "chengdu"));
            } else if (innerIds.size() == 3) {
                Assert.assertEquals(innerIds, Sets.newHashSet("shanghai", "beijing", "shenzhen"));
            } else {
                Assert.fail("islands size error");
            }
        });
    }

    @Test
    public void addVertexIfAbsentOrComputeValueTest() {
        Graph<String, String> graph = GraphBuilder.empty();
        graph.addVertexIfAbsentOrComputeValue("beijing", () -> "welcome",
                (str) ->  str + 1);
        Set<Vertex<String>> vertices = graph.getVertices();
        Assert.assertEquals(1, vertices.size());
        Assert.assertEquals("beijing", vertices.iterator().next().getId());
        Assert.assertEquals("welcome", vertices.iterator().next().getValue());

        graph.addVertexIfAbsentOrComputeValue("beijing", () -> "welcome",
                (str) ->  str + 1);
        Assert.assertEquals(1, vertices.size());
        Assert.assertEquals("beijing", vertices.iterator().next().getId());
        Assert.assertEquals("welcome1", vertices.iterator().next().getValue());
    }

    @Test
    public void addEdgeIfAbsentOrComputeValueTest() {
        Graph<String, String> graph = GraphBuilder.empty();
        graph.addVertex("beijing", "welcome");
        graph.addVertex("shanghai", "love");

        graph.addEdgeIfAbsentOrComputeValue("beijing", "shanghai",
                () -> "connected",
                (str) ->  str + 1);
        Set<Edge<String>> edges = graph.getEdges();
        Assert.assertEquals(1, edges.size());
        Assert.assertEquals(Endpoint.create("beijing", "shanghai"),
                edges.iterator().next().getEndpoint());
        Assert.assertEquals("connected", edges.iterator().next().getValue());

        graph.addEdgeIfAbsentOrComputeValue("beijing", "shanghai",() -> "welcome",
                (str) ->  str + 1);
        Assert.assertEquals(1, edges.size());
        Assert.assertEquals(Endpoint.create("beijing", "shanghai"),
                edges.iterator().next().getEndpoint());
        Assert.assertEquals("connected1", edges.iterator().next().getValue());
    }

}