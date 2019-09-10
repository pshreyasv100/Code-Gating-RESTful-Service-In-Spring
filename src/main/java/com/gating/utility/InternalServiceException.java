package com.gating.utility;

public class InternalServiceException extends RuntimeException{

  private static final long serialVersionUID = 1L;

  public InternalServiceException(String message, Throwable e) {
    super(message, e);
  }

}
