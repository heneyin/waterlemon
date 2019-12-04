package com.henvealf.watermelon.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 无向图，无向是通过 Endpoint 的两个 id 不区分前后来实现的。
 *
 * <p>现在不需要有向图。
 *
 * @param <VT> 顶点 value 的类型。
 * @param <ET> 边 value 的类型。
 *
 * @author hongliang.yin/Henvealf
 * @date 2019-11-06
 */
public class Graph<VT, ET> {

    private String name = "unknown-ue-graph";
    private Map<String, Vertex<VT>> vertices;
    private Map<Endpoint<String>, Edge<ET>> edges;

    public Graph() {
        this.vertices = Maps.newHashMap();
        this.edges = Maps.newHashMap();
    }

    public Graph(Map<String, Vertex<VT>> vertices, Map<Endpoint<String>, Edge<ET>> edges) {
        initGraph(vertices, edges);
    }

    public Graph(Set<Vertex<VT>> vertices, Set<Edge<ET>> edges) {
        Map<String, Vertex<VT>> vertexMap = vertices.stream().collect(Collectors.toMap(Vertex::getId, a -> a));
        Map<Endpoint<String>, Edge<ET>> edgeMap = edges.stream().collect(Collectors.toMap(edge -> {
            return Endpoint.create(edge.getLeftId(), edge.getRightId());
        }, a -> a));
        initGraph(vertexMap, edgeMap);
    }

    private void initGraph(Map<String, Vertex<VT>> vertices, Map<Endpoint<String>, Edge<ET>> edges) {
        Preconditions.checkNotNull(vertices, "Vertices can't be null");
        Preconditions.checkNotNull(edges, "Vertices can't be null");
        vertices.forEach((id, vertex) -> {
            Preconditions.checkArgument(id.equals(vertex.getId()),
                    "Vertices map key '%s' not equals vertex id", id);
        });
        edges.forEach((endpoint, edge) -> {
            Preconditions.checkArgument(endpoint.equals(edge.getEndpoint()),
                    "Edge map key '%s' not equals edge id", endpoint);
            Preconditions.checkArgument(vertices.containsKey(endpoint.getLeft()),
                    "Edge endpoint '%s' not exists in vertices", endpoint.getLeft());
            Preconditions.checkArgument(vertices.containsKey(endpoint.getRight()),
                    "Edge endpoint '%s' not exists in vertices", endpoint.getRight());
            Preconditions.checkArgument(!endpoint.left.equals(endpoint.right),
                    "Graph not support vertex self loop, '%s'", endpoint.right);
        });
        this.vertices = vertices;
        this.edges = edges;
    }

    public void add(Vertex<VT> left, Vertex<VT> right, ET edge) {
        Preconditions.checkArgument(!left.getId().equals(right.getId()),
                "Graph not support vertex self loop, '%s'", left.getId());
        // Preconditions.checkArgument(!vertices.containsKey(left.getId()), "Vertex %s already exists", left.getId());
        // Preconditions.checkArgument(!vertices.containsKey(right.getId()), "Vertex %s already exists", right.getId());
        Endpoint<String> endpoint = Endpoint.create(left.getId(), right.getId());
        Preconditions.checkArgument(!edges.containsKey(endpoint),
                "Edge '%s'--'%s' already exists", left.getId(), right.getId());
        vertices.putIfAbsent(left.getId(), left);
        vertices.putIfAbsent(right.getId(), right);
        edges.put(endpoint, Edge.of(left.getId(), right.getId(), edge));
    }

    public void addVertex(@NotNull Vertex<VT> vertex) {
        Preconditions.checkArgument(!vertices.containsKey(vertex.getId()),
                "Vertex %s already exists", vertex.getId());
        vertices.put(vertex.getId(), vertex);
    }

    public void addVertex(@NotNull String id, VT value) {
        addVertex(Vertex.of(id, value));
    }

    private void addEdge(Edge<ET> edge) {
        Preconditions.checkArgument(!edges.containsKey(edge.getEndpoint()),
                "Edge %s already exists", edge.getEndpoint());
        edges.put(edge.getEndpoint(), edge);
    }

    /**
     * 根据当前顶点的值计算更新顶点的值。
     *
     * <p>如果顶点的值为空，则使用 valueSupplier 得到一个值，如果不为空，则使用 computeFun 计算得到新的值。
     *
     * <p>如果被操作顶点不存在，则不做任何操作。TODO 该行为待议。
     *
     * @param id 顶点 ID
     * @param valueSupplier 值为空时，提供初始值的函数。不指定则不会使用。
     * @param computeFun 值存在时，计算新值的函数。
     */
    public void computeVertexValue(String id, Supplier<VT> valueSupplier, @NotNull Function<VT, VT> computeFun) {
        if (vertices.containsKey(id)) {
            Vertex<VT> existsVertex = vertices.get(id);
            VT value = existsVertex.getValue();
            if (value == null && valueSupplier != null) {
                value = valueSupplier.get();
            } else {
                value = computeFun.apply(value);
            }
            existsVertex.setValue(value);
        }
    }

    public void computeVertexValue(String id, @NotNull Function<VT, VT> computeFun) {
        computeVertexValue(id, null, computeFun);
    }

    /**
     * 根据当前边的值计算更新边的值。
     *
     * <p>如果边的值为空，则使用 valueSupplier 得到一个值，如果不为空，则使用 computeFun 计算得到新的值。
     *
     * <p>如果被操作的边不存在，则不做任何操作。TODO 该行为待议。
     *
     * @param twoVertexIds 边的id。
     * @param valueSupplier 值为空时，提供初始值的函数。不指定则不会使用。
     * @param computeFun 值存在时，计算新值的函数。
     */
    public void computeEdgeValue(Endpoint<String> twoVertexIds,
                                 Supplier<ET> valueSupplier,
                                 @NotNull Function<ET, ET> computeFun) {
        if (edges.containsKey(twoVertexIds)) {
            Edge<ET> existsEdge = edges.get(twoVertexIds);
            ET value = existsEdge.getValue();
            if (value == null &&  valueSupplier != null) {
                value = valueSupplier.get();
            } else {
                value = computeFun.apply(value);
            }
            existsEdge.setValue(value);
        }
    }

    public void computeEdgeValue(Endpoint<String> twoVertexIds,
                                 @NotNull Function<ET, ET> computeFun) {
        computeEdgeValue(twoVertexIds, null, computeFun);
    }

    public void computeEdgeValue(@NotNull String leftId, @NotNull String rightId,
                                 @NotNull Function<ET, ET> computeFun) {
        computeEdgeValue(Endpoint.create(leftId, rightId), null, computeFun);
    }

    public void computeEdgeValue(@NotNull String leftId, @NotNull String rightId,
                                 Supplier<ET> valueSupplier,
                                 @NotNull Function<ET, ET> computeFun) {
        computeEdgeValue(Endpoint.create(leftId, rightId), valueSupplier, computeFun);
    }


    /**
     * 顶点不存在则添加，否则计算更新值。
     * @param id 顶点 id
     * @param vertexSupplier 不存在时提供顶点值的函数。
     * @param computeFun 计算值的函数。
     */
    public void addVertexIfAbsentOrComputeValue(@NotNull String id,
                                                 @NotNull Supplier<VT> vertexSupplier,
                                                 @NotNull Function<VT, VT> computeFun) {
        if (! existsVertex(id)) {
            addVertex(Vertex.of(id, vertexSupplier.get()));
        } else {
            computeVertexValue(id, computeFun);
        }
    }

    /**
     * 边不存在则添加，否则计算更新值。
     * @param leftId 顶点1
     * @param rightId 顶点2
     * @param edgeSupplier 不存在时提供边的值的函数。
     * @param computeFun 计算值的函数。
     */
    public void addEdgeIfAbsentOrComputeValue(@NotNull String leftId, @NotNull String rightId,
                                              @NotNull Supplier<ET> edgeSupplier,
                                              @NotNull Function<ET, ET> computeFun) {
        if (! existsEdge(leftId, rightId)) {
            addEdge(Edge.of(leftId, rightId, edgeSupplier.get()));
        } else {
            computeEdgeValue(leftId, rightId, computeFun);
        }
    }

    /**
     * 过滤顶点，生成一个新的 图。
     * @param predicate 过滤谓词函数。
     * @return
     */
    public Graph<VT, ET> filterVertex(Predicate<Vertex<VT>> predicate) {
        Map<String, Vertex<VT>> filtedVertex =
                vertices.values().stream().filter(predicate).collect(Collectors.toMap(Vertex::getId, a -> a));
        return copyGraphFromVertices(filtedVertex);
    }

    /**
     * 过滤边，生成一个新的图。
     * @param predicate 过滤谓词函数。
     * @return
     */
    public Graph<VT, ET> filterEdge(Predicate<Edge<ET>> predicate) {
        Map<Endpoint<String>, Edge<ET>> filtedEdge =
                this.edges.values().stream().filter(predicate).collect(Collectors.toMap(Edge::getEndpoint, a -> a));
        Map<String, Vertex<VT>> filtedVertex = Maps.newHashMap();
        filtedEdge.keySet().forEach(endpoint -> {
            filtedVertex.put(endpoint.getLeft(), vertices.get(endpoint.getLeft()));
            filtedVertex.put(endpoint.getRight(), vertices.get(endpoint.getRight()));
        });
        return new Graph<>(filtedVertex, filtedEdge);
    }

    /**
     * 过滤图三元组。生层一个新的图。
     *
     * @param predicate 过滤谓词函数。
     * @return
     */
    public Graph<VT, ET> filterTriple(Predicate<Triple<Vertex<VT>, Vertex<VT>, Edge<ET>>> predicate) {
        List<Triple<Vertex<VT>, Vertex<VT>, Edge<ET>>> triples =
                Lists.newArrayList();
        edges.forEach((endpoints, value) -> {
            Vertex<VT> oneVertex = vertices.get(endpoints.getLeft());
            Vertex<VT> twoVertex = vertices.get(endpoints.getRight());
            triples.add(Triple.of(oneVertex, twoVertex, value));
        });
        // 孤独顶点只占三元组的 left 一个。
        List<Vertex<VT>> lonelinessVertices = getLonelinessVertices();
        lonelinessVertices.forEach(v -> {
            triples.add(Triple.of(v, null, null));
        });
        Map<String, Vertex<VT>> vertexMap = Maps.newHashMap();
        Map<Endpoint<String>, Edge<ET>> edgeMap = Maps.newHashMap();
        triples.stream().filter(predicate).forEach(triple -> {
            if (triple.getLeft() != null) {
                vertexMap.put(triple.getLeft().getId(), triple.getLeft());
            }
            // 如此表明是只有孤独顶点的三元组
            if (triple.getMiddle() != null && triple.getRight() != null) {
                vertexMap.put(triple.getMiddle().getId(), triple.getMiddle());
                edgeMap.put(Endpoint.create(triple.getLeft().getId(), triple.getMiddle().getId()), triple.getRight());
            }
        });
        return new Graph<>(vertexMap, edgeMap);
    }

    public List<Vertex<VT>> getLonelinessVertices() {
        Set<String> haveEdgeVertexIds = edges.keySet().stream()
                .flatMap(endpoint -> Stream.of(endpoint.getLeft(), endpoint.getRight()))
                .collect(Collectors.toSet());

        List<Vertex<VT>> result = Lists.newArrayList();
        vertices.forEach((k, v) -> {
            if (!haveEdgeVertexIds.contains(k)) {
                result.add(v);
            }
        });
        return result;
    }

    /**
     * 获取当前图的所有的岛屿图。
     * @return 分离的岛屿们。
     */
    public List<Graph<VT, ET>> getIslands() {
        List<Graph<VT, ET>> result = Lists.newArrayList();
        // 现在剩下的 ID。
        Set<String> remainIds = Sets.newHashSet(vertices.keySet());

        for (String id : vertices.keySet()) {
            if (remainIds.contains(id)) {
                Set<String> connectedIds = getConnectedIds(id);
                remainIds.removeAll(connectedIds);
                Graph<VT, ET> island = copyGraphFromVertexIds(connectedIds);
                result.add(island);
            }
        }
        return result;
    }

    /**
     * 获取所有可连通的顶点 ID
     * @param vertexId 起始顶点 ID
     */
    public Set<String> getConnectedIds(String vertexId) {
        Queue<String> queue = Lists.newLinkedList();
        Set<String> reachedIds = Sets.newHashSet(vertexId);
        queue.offer(vertexId);
        while (true) {
            String id = queue.poll();
            Set<String> neighborIds = getNeighborIds(id, reachedIds);
            if (!neighborIds.isEmpty()) {
                reachedIds.addAll(neighborIds);
                queue.addAll(neighborIds);
            } else if (queue.isEmpty()) {
                break;
            }
        }
        return reachedIds;
    }

    /**
     * 获取顶点的邻居。
     * @param vertexId 顶点 id。
     * @return
     */
    public Set<String> getNeighborIds(String vertexId, Set<String> excludeIds) {
        return edges.keySet().stream()
                .map(endpont -> endpont.getNeighborIfContain(vertexId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(id -> {
                    if (excludeIds != null) {
                        return !excludeIds.contains(id);
                    }
                    return true;
                })
                .collect(Collectors.toSet());
    }

    public Set<String> getNeighborIds(String vertexId) {
        return getNeighborIds(vertexId, null);
    }

    public List<Vertex<VT>> getVerticesById(@NotNull List<String> vertexIds) {
        return vertexIds.stream().map(id -> vertices.getOrDefault(id, null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Optional<Vertex<VT>> getVertexById(@NotNull String vertexId) {
        return Optional.of(vertices.getOrDefault(vertexId, null));
    }

    public Optional<Edge<ET>> getEdgeByEndpoint(@NotNull Endpoint endpoint) {
        return Optional.of(edges.getOrDefault(endpoint, null));
    }

    public Optional<Edge<ET>> getEdgeById(@NotNull String leftId, @NotNull String rightId) {
        return getEdgeByEndpoint(Endpoint.create(leftId, rightId));
    }

    private Graph<VT, ET> copyGraphFromVertices(@NotNull Map<String, Vertex<VT>> neededVertices) {
        Map<Endpoint<String>, Edge<ET>> filtedEdge = Maps.newHashMap();
        this.edges.forEach((endpoint, value) -> {
            if (neededVertices.containsKey(endpoint.getLeft()) && neededVertices.containsKey(endpoint.getRight())) {
                filtedEdge.put(endpoint, value);
            }
        });
        return new Graph<>(neededVertices, filtedEdge);
    }

    public Graph<VT, ET> copyGraphFromVertexIds(@NotNull Set<String> ids) {
        Map<String, Vertex<VT>> vertices = ids.stream().map(id -> this.vertices.getOrDefault(id, null))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Vertex::getId, a -> a));
        return copyGraphFromVertices(vertices);
    }

    public boolean isEmpty() {
        return this.vertices.isEmpty();
    }

    public boolean existsVertex(@NotNull Vertex<VT> vertex) {
        String id = vertex.getId();
        return vertices.containsKey(id);
    }

    public boolean existsVertex(@NotNull String id) {
        return vertices.containsKey(id);
    }

    public boolean existsEdge(@NotNull Edge<ET> edge) {
        Endpoint endpoint = edge.getEndpoint();
        return edges.containsKey(endpoint);
    }

    public boolean existsEdge(@NotNull String leftId, @NotNull String rightId) {
        Endpoint endpoint = Endpoint.create(leftId, rightId);
        return edges.containsKey(endpoint);
    }

    public Set<Vertex<VT>> getVertices() {
        return Sets.newHashSet(vertices.values());
    }

    public Set<Edge<ET>> getEdges() {
        return Sets.newHashSet(edges.values());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
