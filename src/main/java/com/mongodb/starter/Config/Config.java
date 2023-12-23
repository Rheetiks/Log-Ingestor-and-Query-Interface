package com.mongodb.starter.Config;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.google.gson.Gson;
import com.mongodb.starter.prop.ConfigProps;

import java.util.concurrent.TimeUnit;

@Configuration
public class Config {

    private final ConfigProps props;
    String serverUrl = "https://dyte-elastic-search.es.asia-south1.gcp.elastic-cloud.com";
    String apiKey = "TnBBdjU0c0JvQmJFM2phSEhKb246WHF4VUhNaERSNVdfd0taMnFZQzdMUQ==";
    
    public Config(final ConfigProps props){
        this.props = props;
    }

    @Profile({"production", "docker", "test"})
    @Bean(destroyMethod = "close")
    public RestHighLevelClient getRestClient() {
        return new RestHighLevelClient(RestClient
            .builder(HttpHost.create(serverUrl))
            .setDefaultHeaders(compatibilityHeaders()));
    }
    private Header[] compatibilityHeaders() {
        return new Header[]{new BasicHeader(HttpHeaders.ACCEPT, "application/vnd.elasticsearch+json;compatible-with=7")
        , new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.elasticsearch+json;compatible-with=7")
        , new BasicHeader("Authorization", "ApiKey " + apiKey)

        };
     }
    
    @Bean
    public SearchSourceBuilder getSearchSourceBuilder(){
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(props.getIndex().getFrom());
        sourceBuilder.size(props.getIndex().getSize());
        sourceBuilder.timeout(new TimeValue(props.getIndex().getTimeout(), TimeUnit.SECONDS));

        return sourceBuilder;
    }

    @Bean
    public Gson getGson(){
        return new Gson();
    }
}
