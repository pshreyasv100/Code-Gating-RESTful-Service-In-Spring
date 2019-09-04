package com.gating.utility;

public class InvalidInputException  extends Exception{


  public InvalidInputException(String message, String userInput) {
    super(message + ":" + userInput);
  }

}
