package com.toyfactory.httpclient;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate simpleRestTemplate() {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(60000);

        return new RestTemplate(factory);
    }

    @Bean
    public RestTemplate restTemplateWithDefaultPool() {
        var factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);

        return new RestTemplate(factory);
    }

    @Bean
    public RestTemplate restTemplateWithPool() {
        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());

       return restTemplate;
    }


    @Bean
    public ClientHttpRequestFactory getClientHttpRequestFactory() {
        int timeout = 5000;

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();

        CloseableHttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .setMaxConnPerRoute(60)
                .setMaxConnTotal(100)
                .build();

        return new HttpComponentsClientHttpRequestFactory(client);
    }
}
