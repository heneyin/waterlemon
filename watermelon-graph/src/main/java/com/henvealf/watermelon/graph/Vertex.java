package com.henvealf.watermelon.graph;

import com.sun.istack.internal.NotNull;

/**
 * @author hongliang.yin/Henvealf
 * @date 2019-11-11
 */
public class Vertex<T> {

    private String id;
    private int degree = 0;
    private T value;

    public Vertex(@NotNull String id, T value) {
        this.id = id;
        this.value = value;
    }

    public Vertex(@NotNull String id, int degree, T value) {
        this.id = id;
        this.value = value;
        this.degree = degree;
    }

    public static <T> Vertex<T> of(String id, T value) {
        return new Vertex<>(id, value);
    }

    public static <T> Vertex<T> of(String id, int degree, T value) {
        return new Vertex<>(id, degree, value);
    }

    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Vertex) {
            return ((Vertex) obj).getId().equals(id);
        }
        return false;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public void incrementDegree() {
        this.degree += 1;
    }
}
