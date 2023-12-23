package com.mongodb.starter.dtos;

import org.bson.types.ObjectId;

import com.mongodb.starter.models.LogEntity;
import com.mongodb.starter.models.Metadata;

public record LogDTO( String id, String level, String message, String resourceId,String timestamp,
 String traceId, String spanId,String commit, Metadata metadata) {
    
    public LogDTO(LogEntity a) {
        this(a.getId() == null ? new ObjectId().toHexString() : a.getId().toHexString(), a.getLevel(), a.getMessage(), a.getResourceId(), a.getTimestamp(), a.getTraceId(), a.getSpanId(), a.getCommit(), a.getMetadata());
    }

    public LogEntity toLogEntity() {
        ObjectId _id = id == null ? new ObjectId() : new ObjectId(id);

        return new LogEntity(_id, level, message, resourceId, timestamp, traceId, spanId, commit, metadata);
    }
}
