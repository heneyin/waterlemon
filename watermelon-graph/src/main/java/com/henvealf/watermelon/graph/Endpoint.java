package com.henvealf.watermelon.graph;

import org.apache.commons.lang3.tuple.MutablePair;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 代表图中边的两个顶点 ID。
 *
 * <p>继承自 apache common lang3 中的 Pair。重写其中的 Endpoint 的 equals 方法，忽略两顶点的方向。
 *
 * <p>Pair 中 hash code 的值与两顶点中的方向无关，所有没有重写。
 *
 * <p>比较方法没有用到，也没有重写。
 *
 * @author hongliang.yin/Henvealf
 * @date 2019-11-12
 */
public class Endpoint<L> extends MutablePair<L, L> {

    public static <L> Endpoint<L> create(L left, L right) {
        return new Endpoint<>(left, right);
    }

    /**
     * Create a new pair instance.
     *
     * @param left  the left value, may be null
     * @param right the right value, may be null
     */
    public Endpoint(L left, L right) {
        super(left, right);
    }

    /**
     * 主要重写相等方法，以支持无向。
     *
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Map.Entry<?, ?>) {
            final Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
            return (Objects.equals(getKey(), other.getKey())
                    && Objects.equals(getValue(), other.getValue()))
                    || (Objects.equals(getKey(), other.getValue())
                    && Objects.equals(getValue(), other.getKey()));
        }
        return false;
    }

    public boolean contain(L id) {
        return left.equals(id) || right.equals(id);
    }

    public Optional<L> getNeighborIfContain(L id) {
        if (contain(id)) {
            if (getLeft().equals(id)) {
                return Optional.of(getRight());
            } else if (getRight().equals(id)) {
                return Optional.of(getLeft());
            }
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return left + " -- " + right;
    }
}
