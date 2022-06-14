package com.sellics.sherakhan.batch.model;

import java.io.Serializable;

/**
 * Represents data format of CSV.
 * TimeStamp is unix based epoch time
 * ASIN is Amazon Standard Identification Number for products.
 * Keyword are space separated words for products.
 * Rank is ordering parameter of products. Lower the rank, top on search results for products.
 */
public class CSVModel implements Serializable {

    private String timestamp;
    private String asin;
    private String keyword;
    private int rank;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
