package com.dolzhik.meteoServer.repository;

import com.dolzhik.meteoServer.entity.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {
}
