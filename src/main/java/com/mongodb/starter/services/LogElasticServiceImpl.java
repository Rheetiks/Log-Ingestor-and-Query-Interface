package com.mongodb.starter.services;

import java.util.List;

import org.elasticsearch.action.index.IndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.starter.dtos.LogDTO;
import com.mongodb.starter.models.LogEntity;
import com.mongodb.starter.repositories.LogElasticRepository;


@Service
public class LogElasticServiceImpl  implements LogElasticService{

    @Autowired
    private LogElasticRepository logElasticRepository;

    @Override
    public IndexResponse insertLogInElasticSearch(String id, LogDTO logdata) {
        return logElasticRepository.insertLogInElasticSearch(id, logdata);
    }

    @Override
    public List<LogEntity> findAllBasedOnText(String filterText) {
        // TODO Auto-generated method stub
        return logElasticRepository.wildcardQuery(filterText);
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        logElasticRepository.deleteAll();
    }
    
}
