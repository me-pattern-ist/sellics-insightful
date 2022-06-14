package com.sellics.sherakhan.batch.model;

import java.io.Serializable;
import java.util.Objects;

public class SerialNumber implements Serializable {

    private final String number;

    private SerialNumber(String number) {
        this.number = number;
    }

    public static SerialNumber create(String number) {
        return new SerialNumber(number);
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SerialNumber that = (SerialNumber) o;
        return number.equals(that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return "SerialNumber{" +
                "number='" + number + '\'' +
                '}';
    }
}
