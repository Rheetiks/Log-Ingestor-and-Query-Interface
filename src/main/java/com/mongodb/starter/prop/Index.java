package com.mongodb.starter.prop;


public class Index {
    private String name;
    private int shard;
    private int replica;
    private int from;
    private int size;
    private int timeout;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getShard() {
        return shard;
    }
    public void setShard(int shard) {
        this.shard = shard;
    }
    public int getReplica() {
        return replica;
    }
    public void setReplica(int replica) {
        this.replica = replica;
    }
    public int getFrom() {
        return from;
    }
    public void setFrom(int from) {
        this.from = from;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public int getTimeout() {
        return timeout;
    }
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    
}
