package com.sellics.sherakhan.batch.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class IngestionModel implements Serializable {

    private final EventTime eventTime;
    private final SerialNumber serialNumber;
    private final List<ProductKeyword> keywords;
    private final ProductRank productRank;

    private IngestionModel(EventTime eventTime,
                           SerialNumber serialNumber,
                           List<ProductKeyword> keywords,
                           ProductRank productRank) {
        this.eventTime = eventTime;
        this.serialNumber = serialNumber;
        this.keywords = keywords;
        this.productRank = productRank;
    }

    public static IngestionModel create(String eventTime,
                                        String serialNumber,
                                        String keywords,
                                        Integer rank) {
        EventTime epochTime = EventTime.create(eventTime);
        List<ProductKeyword> words = Arrays.stream(keywords.split(" "))
                .map(ProductKeyword::create)
                .collect(Collectors.toList());
        SerialNumber productNumber = SerialNumber.create(serialNumber);
        ProductRank productRank = ProductRank.create(rank);

        return new IngestionModel(epochTime,
                productNumber,
                words,
                productRank);
    }

    public EventTime getEventTime() {
        return eventTime;
    }

    public SerialNumber getSerialNumber() {
        return serialNumber;
    }

    public List<ProductKeyword> getKeywords() {
        return keywords;
    }

    public ProductRank getProductRank() {
        return productRank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IngestionModel that = (IngestionModel) o;
        return eventTime.equals(that.eventTime) &&
                serialNumber.equals(that.serialNumber) &&
                keywords.equals(that.keywords) &&
                productRank.equals(that.productRank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventTime, serialNumber, keywords, productRank);
    }

    @Override
    public String toString() {
        return "IngestionModel{" +
                "eventTime=" + eventTime +
                ", serialNumber=" + serialNumber +
                ", keywords=" + keywords +
                ", rank=" + productRank +
                '}';
    }
}
