package com.dolzhik.meteoServer.controller;

import com.dolzhik.meteoServer.entity.DataEntry;
import com.dolzhik.meteoServer.entity.DayEntry;
import com.dolzhik.meteoServer.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class MeteoController {

    private final DataRepository dataRepository;

    @Autowired
    public MeteoController(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @GetMapping("/data")
    public Object getMeteoData() {
        return dataRepository.findTopByOrderByLogTimeDesc();
    }

    @GetMapping("/lastEntries")
    public Object getLastData() {
        return dataRepository.findAllByLogTimeAfter(new Timestamp(Instant.now().minus(1, ChronoUnit.HOURS).toEpochMilli()));
    }

    @GetMapping("/day")
    public Object getDay() {
        var dayRecords = dataRepository.findAllByLogTimeAfter(new Timestamp(Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli()));
        dayRecords.sort(Comparator.comparing(DataEntry::getLogTime));
        var first = dayRecords.get(0).getLogTime().toLocalDateTime();
        var last = dayRecords.get(dayRecords.size() - 1).getLogTime().toLocalDateTime();
        first = first.withMinute(0).withSecond(0).withNano(0);
        last = last.withMinute(0).withSecond(0).withNano(0);

        List<DayEntry> averagePerHour = new ArrayList<>();
        for (int i = 23; i > 0; i--) {
            var timeSlice = last.minusHours(i);
            var recordSlice = dayRecords.stream().filter(record -> {
                var recordTime = record.getLogTime().toLocalDateTime();
                return recordTime.isAfter(timeSlice) && recordTime.isBefore(timeSlice.plusHours(1));
            }).collect(Collectors.toList());

            DayEntry average = new DayEntry(0.0, 0.0, 0.0, 0.0, Timestamp.valueOf(timeSlice), Timestamp.valueOf(timeSlice.plusHours(1)));

            recordSlice.forEach(record -> {
                average.setAverageTemperature(average.getAverageTemperature() + record.getTemperature());
                average.setAveragePressure(average.getAveragePressure() + record.getPressure());
            });
            average.setAverageTemperature(average.getAverageTemperature() / recordSlice.size());
            average.setAveragePressure(average.getAveragePressure() / recordSlice.size());

            averagePerHour.add(average);
        }

        Map<String, Object> test = new HashMap<>();
        averagePerHour.sort(Comparator.comparing(DayEntry::getFrom));
        test.put("avg", averagePerHour);
        return test;
    }

}
