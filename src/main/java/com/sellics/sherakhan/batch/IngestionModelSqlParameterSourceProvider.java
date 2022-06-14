package com.sellics.sherakhan.batch;

import com.sellics.sherakhan.batch.model.IngestionModel;
import com.sellics.sherakhan.batch.model.ProductKeyword;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.HashMap;
import java.util.stream.Collectors;

public class IngestionModelSqlParameterSourceProvider implements ItemSqlParameterSourceProvider<IngestionModel> {

    @Override
    public SqlParameterSource createSqlParameterSource(final IngestionModel item) {

        return new MapSqlParameterSource(new HashMap<String, Object>() {
            {
                put("event_date", item.getEventTime().getEpoch());
                put("serial_number", item.getSerialNumber().getNumber());
                String spaceSeparatedWords = item.getKeywords()
                        .stream()
                        .map(ProductKeyword::getKeyword)
                        .collect(Collectors.joining(" "));
                put("keywords", spaceSeparatedWords);
                put("rank", item.getProductRank().getPriority());
            }
        });
    }
}
