package com.mongodb.starter.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.mongodb.starter.dtos.LogDTO;


@Service
public class ConsumerService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private LogMongoService logMongoService;

    @Autowired
    private LogElasticService logElasticService;


    @KafkaListener(topics = {"${spring.kafka.topic}"}, containerFactory = "kafkaListenerStringFactory", groupId = "group_id")
    public void consumeMessage(String message) {
        logger.info("**** -> Consumed message -> {}", message);
    }


    @KafkaListener(topics = {"${spring.kafka.log-topic}"}, containerFactory = "kafkaListenerJsonFactory", groupId = "group_id")
    public void consumeLogDTO(LogDTO logdata) {
        logger.info("**** -> Consumed Log message , pushing it to mongo db and elastic search database :: {}", logdata);
        String id = logMongoService.insertLogMongoDB(logdata);
        logElasticService.insertLogInElasticSearch(id, logdata);
    }

}