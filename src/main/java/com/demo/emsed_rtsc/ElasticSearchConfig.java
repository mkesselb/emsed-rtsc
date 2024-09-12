package com.demo.emsed_rtsc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = { "com.demo.emsed_rtsc.article", "com.demo.emsed_rtsc.incidents" })
@ComponentScan(basePackages = { "com.demo.emsed_rtsc.article", "com.demo.emsed_rtsc.incidents" })
public class ElasticSearchConfig extends ElasticsearchConfiguration {

    @Value("${spring.elasticsearch.uris:}")
    private String elasticSearchURIs;

    private String defaultURI = "localhost:9200";

    Logger logger = LoggerFactory.getLogger(ElasticSearchConfig.class);

    @Override
    public ClientConfiguration clientConfiguration() {
      logger.info("connecting to: " + elasticSearchURIs);

      return ClientConfiguration.builder()           
        .connectedTo(elasticSearchURIs.length() > 0 ? elasticSearchURIs : defaultURI)
        .build();
    }
}
