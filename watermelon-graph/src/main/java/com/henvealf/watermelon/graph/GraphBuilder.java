package com.henvealf.watermelon.graph;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * 方便创建图的 builder。
 * @author hongliang.yin/Henvealf
 * @date 2019-11-12
 */
public class GraphBuilder<VT, ET> {

    private String name;
    private Set<Vertex<VT>> vertices = Sets.newHashSet();
    private Set<Edge<ET>> edges = Sets.newHashSet();

    public static <VT, ET> GraphBuilder<VT, ET> getBuilder() {
        return new GraphBuilder<>();
    }

    public static <VT, ET> Graph<VT, ET> empty() {
        return new Graph<>();
    }

    public GraphBuilder<VT, ET> addVertex(String id, VT value) {
        Vertex<VT> one = new Vertex<>(id, value);
        vertices.add(one);
        return this;
    }

    public GraphBuilder<VT, ET> addEdge(String leftId, String rightId, ET value) {
        Edge<ET> one = new Edge<>(leftId, rightId, value);
        edges.add(one);
        return this;
    }

    public GraphBuilder<VT, ET> addVertices(Set<Vertex<VT>> vertices) {
        this.vertices.addAll(vertices);
        return this;
    }

    public GraphBuilder<VT, ET> addEdges(Set<Edge<ET>> edges) {
        this.edges.addAll(edges);
        return this;
    }

    public GraphBuilder<VT, ET> setName(String name) {
        this.name = name;
        return this;
    }

    public Graph<VT, ET> build() {
        Graph<VT, ET> graph = new Graph<>(this.vertices, this.edges);
        if (StringUtils.isNotBlank(this.name)) {
            graph.setName(this.name);
        }
        return graph;
    }
}
