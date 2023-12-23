package com.mongodb.starter.Util;

import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.mongodb.starter.prop.ConfigProps;

import jakarta.annotation.PostConstruct;


@Component
@Profile({"production", "docker", "test"})
public class IndexConfigurator {

    private final RestHighLevelClient client;
    private final ConfigProps props;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public IndexConfigurator(final RestHighLevelClient client, final ConfigProps props) {
        this.client = client;
        this.props = props;
    }

    @PostConstruct
    private void createIndexWithMapping() {

        try {

            final GetIndexRequest request = new GetIndexRequest(props.getIndex().getName());
            final boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);

            if (!exists) {

                final CreateIndexRequest indexRequest = new CreateIndexRequest(props.getIndex().getName());
                indexRequest.settings(Settings.builder()
                        .put("index.number_of_shards", props.getIndex().getShard())
                        .put("index.number_of_replicas", props.getIndex().getReplica())
                );

                final CreateIndexResponse createIndexResponse = client.indices().create(indexRequest, RequestOptions.DEFAULT);
                if (createIndexResponse.isAcknowledged() && createIndexResponse.isShardsAcknowledged()) {
                    log.info("{} index created successfully", props.getIndex().getName());
                } else {
                    log.debug("Failed to create {} index", props.getIndex().getName());
                }

                final PutMappingRequest mappingRequest = new PutMappingRequest(props.getIndex().getName());
                final XContentBuilder builder = XContentFactory.jsonBuilder();
                
                builder.startObject();
                {
                    builder.startObject("properties");
                    {
                        builder.startObject("level");
                        {
                            builder.field("type", "text");
                        }
                        builder.endObject();

                        builder.startObject("message");
                        {
                            builder.field("type", "text");
                        }
                        builder.endObject();

                        builder.startObject("resourceId");
                        {
                            builder.field("type", "text");
                        }
                        builder.endObject();

                        builder.startObject("timestamp");
                        {
                            builder.field("type", "text");
                        }
                        builder.endObject();
 
                        builder.startObject("traceId");
                        {
                            builder.field("type", "text");
                        }
                        builder.endObject();
                        
                        builder.startObject("spanId");
                        {
                            builder.field("type", "text");
                        }
                        builder.endObject();
                        
                        builder.startObject("commit");
                        {
                            builder.field("type", "text");
                        }
                        builder.endObject();
                                    
                        // Add the "metadata" field as an inner object
                        builder.startObject("metadata");
                        {
                            builder.startObject("properties");
                            {
                                builder.startObject("parentResourceId");
                                {
                                    builder.field("type", "text");
                                }
                                builder.endObject();
                            }
                            builder.endObject();
                        }
                        builder.endObject();
                    }
                    builder.endObject();
                }
                builder.endObject();
                mappingRequest.source(builder);
                final AcknowledgedResponse putMappingResponse = client.indices().putMapping(mappingRequest, RequestOptions.DEFAULT);

                if (putMappingResponse.isAcknowledged()) {
                    log.info("Mapping of {} was successfully created", props.getIndex().getName());
                } else {
                    log.debug("Creating mapping of {} failed", props.getIndex().getName());
                }
            }
        } catch (Exception ex) {
            log.error("An exception was thrown in createIndexWithMapping method.", ex);
        }
    }
}
