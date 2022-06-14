package com.sellics.sherakhan.batch.model;

import java.io.Serializable;
import java.util.Objects;

public class EventTime implements Serializable {

    private final String epoch;

    private EventTime(String epoch) {
        this.epoch = epoch;
    }

    public static EventTime create(String epoch) {
        return new EventTime(epoch);
    }

    public String getEpoch() {
        return epoch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventTime eventTime = (EventTime) o;
        return epoch.equals(eventTime.epoch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(epoch);
    }

    @Override
    public String toString() {
        return "EventTime{" +
                "epoch='" + epoch + '\'' +
                '}';
    }
}
