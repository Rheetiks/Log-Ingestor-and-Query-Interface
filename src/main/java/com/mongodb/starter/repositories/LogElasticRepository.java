package com.mongodb.starter.repositories;

import java.util.List;

import org.elasticsearch.action.index.IndexResponse;

import com.mongodb.starter.dtos.LogDTO;
import com.mongodb.starter.models.LogEntity;

public interface LogElasticRepository {

    IndexResponse insertLogInElasticSearch(String id,LogDTO logdata);
    public List<LogEntity> wildcardQuery(String query);
    void deleteAll();

}
