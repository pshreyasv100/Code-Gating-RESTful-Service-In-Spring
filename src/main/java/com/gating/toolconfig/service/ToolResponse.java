package com.gating.toolconfig.service;

public class ToolResponse<T> {


  private T value;
  private T threshold;
  private String finalResult;

  public ToolResponse(T value, T threshold, String finalResult) {
    super();
    this.value = value;
    this.threshold = threshold;
    this.finalResult = finalResult;
  }

  public T getValue() {
    return value;
  }

  public void setValue(T value) {
    this.value = value;
  }

  public T getThreshold() {
    return threshold;
  }

  public void setThreshold(T threshold) {
    this.threshold = threshold;
  }

  public String getFinalResult() {
    return finalResult;
  }

  public void setFinalResult(String finalResult) {
    this.finalResult = finalResult;
  }


}
