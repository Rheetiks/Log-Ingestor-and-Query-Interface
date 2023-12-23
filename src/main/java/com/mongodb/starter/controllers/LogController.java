package com.mongodb.starter.controllers;

import com.mongodb.starter.dtos.FilterDTO;
import com.mongodb.starter.dtos.LogDTO;
import com.mongodb.starter.models.LogEntity;
import com.mongodb.starter.services.LogElasticService;
import com.mongodb.starter.services.LogMongoService;
import com.mongodb.starter.services.ProducerService;

import org.elasticsearch.action.index.IndexResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final static Logger LOGGER = LoggerFactory.getLogger(LogController.class);

    @Autowired
    private ProducerService<LogDTO> producerService;

    @Autowired
    private LogMongoService logMongoService;

    @Autowired
    private LogElasticService logElasticService;



    @PostMapping("/insert")
    public String insertLog(@RequestBody LogDTO logdata){

        producerService.sendLogDTOMessage(logdata);

        return "success";
    }

    @PostMapping(value = "/insertToElasticSearch", consumes = "application/json; charset=utf-8")
    public IndexResponse insertToElasticSearch(@RequestBody LogDTO document) {
        return logElasticService.insertLogInElasticSearch("testid", document);
    }

    @PostMapping(value = "/insertToMongoDB", consumes = "application/json; charset=utf-8")
    public String insertToMongoDB(@RequestBody LogDTO document) {
        return logMongoService.insertLogMongoDB(document);
    }



    @PostMapping("/filterBasedSearch")
    public List<LogDTO> filterBasedSearch(@RequestBody FilterDTO filter) {
        return logMongoService.findAllBasedOnFilter(filter.filterType(), filter.filterText());
    }

    @PostMapping("/textBasedSearch")
    public List<LogEntity> textBasedSearch(@RequestBody FilterDTO filter) {
        return logElasticService.findAllBasedOnText(filter.textSearch());
    }

    
    @GetMapping("/deleteAll")
    public String deleteAll() {
        logElasticService.deleteAll();
        logMongoService.deleteAll();
        return "success";
    }



    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Exception handleAllExceptions(RuntimeException e) {
        LOGGER.error("Internal server error.", e);
        return e;
    }
}