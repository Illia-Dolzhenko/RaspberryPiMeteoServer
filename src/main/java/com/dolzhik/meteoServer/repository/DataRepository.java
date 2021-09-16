package com.dolzhik.meteoServer.repository;

import com.dolzhik.meteoServer.entity.DataEntry;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;

public interface DataRepository extends CrudRepository<DataEntry, Long> {

    DataEntry findTopByOrderByLogTimeDesc();

    List<DataEntry> findAllByLogTimeAfter(Timestamp afterDate);
}
