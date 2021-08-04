package com.dolzhik.meteoServer.provider;

import com.dolzhik.meteoServer.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ScheduledTask {

    @Autowired
    DataRepository dataRepository;
    @Autowired
    DataProvider dataProvider;

    @Scheduled(fixedDelay = 60000)
    public void updateData(){
        try {
            dataRepository.save(dataProvider.getData());
        } catch (IOException e) {
            System.out.println("Can't save data entry");
        }
    }
}
