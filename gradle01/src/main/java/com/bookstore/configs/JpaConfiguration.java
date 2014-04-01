package com.bookstore.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by InSeok on 2014-04-01.
 */
@Configuration
@ImportResource("classpath:/springDataContext.xml")
public class JpaConfiguration {
}
