package com.ap.greenpole.transactioncomponent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.ap.greenpole.transactioncomponent.interceptor.TransactionRequestAspectInterceptor;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:48 PM
 */
@Configuration
@EnableAspectJAutoProxy
public class AspectConfig {

    @Bean
    public TransactionRequestAspectInterceptor requestAspectInterceptor() {
        return new TransactionRequestAspectInterceptor();
    }


}
