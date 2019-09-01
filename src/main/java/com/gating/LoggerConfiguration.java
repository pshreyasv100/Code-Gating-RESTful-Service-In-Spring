package com.gating;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class LoggerConfiguration {
  @Bean
  public Logger log() {
    return LoggerFactory.getLogger(Application.class);
  }

}
