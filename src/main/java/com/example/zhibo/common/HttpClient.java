package com.example.zhibo.common;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClient {
    @Bean
    public OkHttpClient okHttpClient(){
        return new OkHttpClient();
    }
}
