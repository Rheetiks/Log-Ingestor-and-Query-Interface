package com.mongodb.starter.services;

import java.util.List;

import org.elasticsearch.action.index.IndexResponse;

import com.mongodb.starter.dtos.LogDTO;
import com.mongodb.starter.models.LogEntity;

public interface LogElasticService {

    IndexResponse insertLogInElasticSearch(String id,LogDTO logdata);

    List<LogEntity> findAllBasedOnText(String filterText);

    void deleteAll();
    
}
