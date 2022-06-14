package com.sellics.sherakhan.batch.model;

import java.io.Serializable;
import java.util.Objects;

public class ProductKeyword implements Serializable {

    private final String keyword;

    private ProductKeyword(String keyword) {
        this.keyword = keyword;
    }

    public static ProductKeyword create(String keyword) {
        return new ProductKeyword(keyword);
    }

    public String getKeyword() {
        return keyword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductKeyword that = (ProductKeyword) o;
        return keyword.equals(that.keyword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyword);
    }

    @Override
    public String toString() {
        return "ProductKeyword{" +
                "keyword='" + keyword + '\'' +
                '}';
    }
}
