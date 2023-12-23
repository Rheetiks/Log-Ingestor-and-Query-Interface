package com.mongodb.starter.repositories;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.mongodb.starter.dtos.LogDTO;
import com.mongodb.starter.models.LogEntity;
import com.mongodb.starter.prop.ConfigProps;

import org.bson.types.ObjectId;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Component;

import static com.mongodb.client.model.Aggregates.documents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LogElasticRepositoryImpl implements LogElasticRepository{

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final RestHighLevelClient client;
    private final SearchSourceBuilder sourceBuilder;
    private final ConfigProps props;
    private final Gson gson;

    @Autowired
    public LogElasticRepositoryImpl(RestHighLevelClient client, SearchSourceBuilder sourceBuilder,
                    ConfigProps props, Gson gson){
        this.client = client;
        this.sourceBuilder = sourceBuilder;
        this.props = props;
        this.gson = gson;
    }

    @Override
    public IndexResponse insertLogInElasticSearch(String id, LogDTO document) {

        try {
            final IndexRequest indexRequest = new IndexRequest(props.getIndex().getName())
                    .id(id)
                    .source(XContentType.JSON,
                            "level", document.level(),
                            "message", document.message(),
                            "resourceId",document.resourceId(),
                            "timestamp",document.timestamp(),
                            "traceId",document.traceId(),
                            "spanId",document.spanId(),
                            "commit",document.commit()
                            // ,"metadata",(Object) (document.metadata())
                            );
            System.out.println(client.index(indexRequest, RequestOptions.DEFAULT));
            final IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
            return response;

        } catch (Exception ex) {
            log.error("The exception was thrown in createIndex method.",ex);
        }

        return null;

    }
    @Override
    public List<LogEntity> wildcardQuery(String query){

        List<LogEntity> result = new ArrayList<>();

        try {
            result = getDocuments(QueryBuilders.queryStringQuery("*" + query.toLowerCase() + "*"));
        } catch (Exception ex){
            log.error("The exception was thrown in wildcardQuery method.", ex);
        }

        return result;
    }

    public void deleteAll(){
        try {
            // final DeleteRequest deleteRequest = new DeleteRequest(props.getIndex().getName());
            // client.delete(deleteRequest, RequestOptions.DEFAULT);
            
            
            DeleteByQueryRequest delete = new DeleteByQueryRequest(props.getIndex().getName());
            delete.setQuery(QueryBuilders.matchAllQuery());
            client.deleteByQuery(delete, RequestOptions.DEFAULT);//RestHighLevelClient

        } catch (Exception ex){
            log.error("The exception was thrown in deleteDocument method.", ex);
        }
    }


    private SearchRequest getSearchRequest(){
        SearchRequest searchRequest = new SearchRequest(props.getIndex().getName());
        searchRequest.source(sourceBuilder);
        return searchRequest;
    }


    private List<LogEntity> getDocuments(AbstractQueryBuilder builder) throws IOException {
        List<LogEntity> result = new ArrayList<>();

        sourceBuilder.query(builder);
        SearchRequest searchRequest = getSearchRequest();

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            LogEntity doc = gson.fromJson(hit.getSourceAsString(), LogEntity.class);
            // doc.setId(new ObjectId(hit.getId()));
            result.add(doc);
        }

        return result;
    }

}
