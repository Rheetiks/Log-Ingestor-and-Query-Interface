package com.mongodb.starter.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.mongodb.starter.dtos.LogDTO;
import com.mongodb.starter.models.LogEntity;
import com.mongodb.starter.models.PersonEntity;

@Repository
public interface LogMongoRepository {

    String insertLogMongoDB(LogEntity logdata);

    long deleteAll();

    List<LogEntity> findAllBasedOnFilter(String filterType, String filterText);

}
