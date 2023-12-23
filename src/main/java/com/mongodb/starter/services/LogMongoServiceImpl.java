package com.mongodb.starter.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.starter.dtos.LogDTO;
import com.mongodb.starter.repositories.LogMongoRepository;


@Service
public class LogMongoServiceImpl implements LogMongoService{

    @Autowired
    private LogMongoRepository logMongoRepository;

    @Override
    public String insertLogMongoDB(LogDTO logdata) {

        return logMongoRepository.insertLogMongoDB(logdata.toLogEntity());
    }

    
    @Override
    public List<LogDTO> findAllBasedOnFilter(String filterType, String filterText) {
        return logMongoRepository.findAllBasedOnFilter(filterType, filterText).stream().map(LogDTO::new).toList();
    }

    @Override
    public long deleteAll() {
        return logMongoRepository.deleteAll();
    }
    
}
