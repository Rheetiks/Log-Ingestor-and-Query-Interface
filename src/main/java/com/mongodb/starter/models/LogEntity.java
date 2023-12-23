package com.mongodb.starter.models;

import org.bson.types.ObjectId;

public class LogEntity {
    private ObjectId id;
    private String level;
    private String message;
    private String resourceId;
    private String timestamp;
    private String traceId;
    public LogEntity(){
        
    }
    public LogEntity(ObjectId id, String level, String message, String resourceId, String timestamp, String traceId, String spanId,
            String commit, Metadata metadata) {
        this.level = level;
        this.message = message;
        this.resourceId = resourceId;
        this.timestamp = timestamp;
        this.traceId = traceId;
        this.spanId = spanId;
        this.commit = commit;
        this.metadata = metadata;
    }

    private String spanId;
    private String commit;
    private Metadata metadata;

    
    
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getResourceId() {
        return resourceId;
    }
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public String getTraceId() {
        return traceId;
    }
    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
    public String getSpanId() {
        return spanId;
    }
    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }
    public String getCommit() {
        return commit;
    }
    public void setCommit(String commit) {
        this.commit = commit;
    }
    public Metadata getMetadata() {
        return metadata;
    }
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
    public ObjectId getId() {
        return id;
    }
    public void setId(ObjectId id) {
        this.id = id;
    }


    

}
