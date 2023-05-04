package com.devglan.customerservice.feign.config;

import feign.Contract;
import feign.auth.BasicAuthRequestInterceptor;
import feign.okhttp.OkHttpClient;

import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;
import javax.validation.Valid;

import org.springframework.context.annotation.Bean;

public class CustomFeignConfig {

    @Bean
    public Contract feignContract() {
        return new feign.Contract.Default();
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("user", "password");
    }

    @Bean
   public OkHttpClient client() {
        return new OkHttpClient();
    }
 
   
    
}
   
