package com.gating.staticanalysis.service;

public class JacocoResponse {

  private final float timeToRunTest;

  public JacocoResponse(float timeToRunTest, float codeCoverage, String finalResult) {
    super();
    this.timeToRunTest = timeToRunTest;
    this.codeCoverage = codeCoverage;
    this.finalResult = finalResult;
  }

  private float codeCoverage;
  private String finalResult;

  public float getCodeCoverage() {
    return codeCoverage;
  }

  public void setCodeCoverage(float codeCoverage) {
    this.codeCoverage = codeCoverage;
  }

  public float getTimeToRunTest() {
    return timeToRunTest;
  }

  public String getFinalResult() {
    return finalResult;
  }

  public void setFinalResult(String finalResult) {
    this.finalResult = finalResult;
  }

}
