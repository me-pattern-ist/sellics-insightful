package com.sellics.sherakhan.batch;

import com.sellics.sherakhan.batch.model.CSVModel;
import com.sellics.sherakhan.batch.model.IngestionModel;
import org.springframework.batch.item.ItemProcessor;

public class IngestionModelProcessor implements ItemProcessor<CSVModel, IngestionModel> {

    @Override
    public IngestionModel process(CSVModel input) {
        return IngestionModel.create(input.getTimestamp(),
                input.getAsin(),
                input.getKeyword(), input.getRank());
    }
}
