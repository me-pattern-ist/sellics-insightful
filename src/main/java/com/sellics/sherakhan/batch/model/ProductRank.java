package com.sellics.sherakhan.batch.model;

import java.io.Serializable;
import java.util.Objects;

public class ProductRank implements Serializable {

    private final Integer priority;

    private ProductRank(Integer priority) {
        this.priority = priority;
    }

    public static ProductRank create(Integer rank) {
        return new ProductRank(rank);
    }

    public Integer getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductRank that = (ProductRank) o;
        return priority.equals(that.priority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(priority);
    }

    @Override
    public String toString() {
        return "ProductRank{" +
                "rank=" + priority +
                '}';
    }
}
