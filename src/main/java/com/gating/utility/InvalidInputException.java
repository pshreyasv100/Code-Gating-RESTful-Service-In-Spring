package com.gating.utility;

public class InvalidInputException extends RuntimeException{

  private static final long serialVersionUID = 1L;

  public InvalidInputException(String message, String userInput) {
    super(message + ":" + userInput);
  }

}
