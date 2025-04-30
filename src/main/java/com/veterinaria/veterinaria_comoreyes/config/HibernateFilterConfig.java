package com.veterinaria.veterinaria_comoreyes.config;

import com.veterinaria.veterinaria_comoreyes.util.FilterStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateFilterConfig {

    @Bean
    public FilterStatus filterStatus() {
        return new FilterStatus() {}; // clase anónima porque FiltroEstado es abstract
    }
}