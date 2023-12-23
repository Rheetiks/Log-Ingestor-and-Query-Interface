package com.mongodb.starter.repositories;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;

import java.util.ArrayList;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.starter.dtos.LogDTO;
import com.mongodb.starter.models.LogEntity;
import com.mongodb.starter.models.PersonEntity;

import jakarta.annotation.PostConstruct;


@Repository
public class LogMongoRepositoryImpl implements LogMongoRepository{


    @Autowired
    private MongoClient mongoClient;

    private MongoCollection<LogEntity> LogCollection;

    private static final TransactionOptions txnOptions = TransactionOptions.builder()
                                                                           .readPreference(ReadPreference.primary())
                                                                           .readConcern(ReadConcern.MAJORITY)
                                                                           .writeConcern(WriteConcern.MAJORITY)
                                                                           .build();

    @PostConstruct
    void init() {
        LogCollection = mongoClient.getDatabase("LogIngestor").getCollection("Log", LogEntity.class);
    }


    @Override
    public String insertLogMongoDB(LogEntity logdata) {
        logdata.setId(new ObjectId());
        LogCollection.insertOne(logdata);

        return logdata.getId().toString();
    }
    
    @Override
    public List<LogEntity> findAllBasedOnFilter(String filterType, String filterText) {
        return LogCollection.find(eq(filterType, filterText)).into(new ArrayList<>());
    }


    @Override
    public long deleteAll() {
        try (ClientSession clientSession = mongoClient.startSession()) {
            return clientSession.withTransaction(
                    () -> LogCollection.deleteMany(clientSession, new BsonDocument()).getDeletedCount(), txnOptions);
        }
    }


    
}
