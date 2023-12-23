package com.mongodb.starter.services;

import java.util.List;

import com.mongodb.starter.dtos.FilterDTO;
import com.mongodb.starter.dtos.LogDTO;

public interface LogMongoService {

    String insertLogMongoDB(LogDTO logdata);

    List<LogDTO> findAllBasedOnFilter(String filterType, String filterText);

    long deleteAll();

}
