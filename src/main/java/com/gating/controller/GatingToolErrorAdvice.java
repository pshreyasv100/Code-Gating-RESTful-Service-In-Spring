package com.gating.controller;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GatingToolErrorAdvice {

  private static final Logger LOGGER = LoggerFactory.getLogger(GatingToolErrorAdvice.class);

  private static ResponseEntity<String> error(HttpStatus status, Exception e) {
    LOGGER.error("Exception : ", e);
    return ResponseEntity.status(status).body(e.getMessage());
  }

  @ExceptionHandler({RuntimeException.class})
  public ResponseEntity<String> handleRunTimeException(RuntimeException e) {
    return error(INTERNAL_SERVER_ERROR, e);
  }


}
