package com.demo.emsed_rtsc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = { "com.demo.emsed_rtsc.article", "com.demo.emsed_rtsc.incidents" })
@ComponentScan(basePackages = { "com.demo.emsed_rtsc.article", "com.demo.emsed_rtsc.incidents" })
public class ElasticSearchConfig extends ElasticsearchConfiguration {

    @Override
    public ClientConfiguration clientConfiguration() {
		return ClientConfiguration.builder()           
			.connectedTo("localhost:9200")
			.build();
    }
}
