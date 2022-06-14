package com.sellics.sherakhan.batch.model;

import java.util.function.Function;

public class ToIngestionModel implements Function<CSVModel, IngestionModel> {
    @Override
    public IngestionModel apply(CSVModel input) {
        return IngestionModel.create(
                input.getTimestamp(),
                input.getAsin(),
                input.getKeyword(),
                input.getRank()
        );
    }
}
