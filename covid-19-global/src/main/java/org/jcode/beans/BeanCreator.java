package org.jcode.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class BeanCreator {

    @Bean
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }
}
