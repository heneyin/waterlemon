package com.henvealf.watermelon.graph;

import com.sun.istack.internal.NotNull;

/**
 * @author hongliang.yin/Henvealf
 * @date 2019-11-11
 */
public class Edge<T> {

    private Endpoint<String> endpoint;
    private T value;

    public Edge(@NotNull String leftId, @NotNull String rightId, T value) {
        this.endpoint = Endpoint.create(leftId, rightId);
        this.value = value;
    }

    public static <T> Edge<T> of(String leftId, String rightId, T value) {
        return new Edge<>(leftId, rightId, value);
    }

    public String getLeftId() {
        return endpoint.getLeft();
    }


    public String getRightId() {
        return endpoint.getRight();
    }


    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Endpoint<String> getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint<String> endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public int hashCode() {
        return endpoint.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof Edge) {
            return ((Edge) obj).getEndpoint().equals(endpoint);
        }
        return false;
    }
}
