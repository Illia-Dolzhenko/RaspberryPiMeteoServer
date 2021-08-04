package com.dolzhik.meteoServer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class DayEntry {
    private Double averageTemperature;
    private Double averagePressure;
    private Double averageHumidity;
    private Double averageCo2;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Kiev")
    private Timestamp from;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Kiev")
    private Timestamp to;

    public DayEntry(Double averageTemperature, Double averagePressure, Double averageHumidity, Double averageCo2, Timestamp from, Timestamp to) {
        this.averageTemperature = averageTemperature;
        this.averagePressure = averagePressure;
        this.averageHumidity = averageHumidity;
        this.averageCo2 = averageCo2;
        this.from = from;
        this.to = to;
    }

    public DayEntry(){

    }

    public Double getAverageTemperature() {
        return averageTemperature;
    }

    public void setAverageTemperature(Double averageTemperature) {
        this.averageTemperature = averageTemperature;
    }

    public Double getAveragePressure() {
        return averagePressure;
    }

    public void setAveragePressure(Double averagePressure) {
        this.averagePressure = averagePressure;
    }

    public Double getAverageHumidity() {
        return averageHumidity;
    }

    public void setAverageHumidity(Double averageHumidity) {
        this.averageHumidity = averageHumidity;
    }

    public Double getAverageCo2() {
        return averageCo2;
    }

    public void setAverageCo2(Double averageCo2) {
        this.averageCo2 = averageCo2;
    }

    public Timestamp getFrom() {
        return from;
    }

    public void setFrom(Timestamp from) {
        this.from = from;
    }

    public Timestamp getTo() {
        return to;
    }

    public void setTo(Timestamp to) {
        this.to = to;
    }
}
