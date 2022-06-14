package com.sellics.sherakhan.batch.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToIngestionModelTest {

    @Test
    void testMapToIngestionModelFromCSVModel() {
        CSVModel input = new CSVModel();
        input.setAsin("B07X2VD4DW");
        input.setRank(1);
        input.setTimestamp("1637024931");
        input.setKeyword("2012 f250 wheel hub");

        IngestionModel result = new ToIngestionModel().apply(input);

        assertEquals("B07X2VD4DW", result.getSerialNumber().getNumber());
        assertEquals(1, result.getProductRank().getPriority());
        assertEquals("1637024931", result.getEventTime().getEpoch());

        //TODO: Make more useful assert.
        assertEquals(4, result.getKeywords().size());
    }
}