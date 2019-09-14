package com.gating.staticanalysis.service;

public class JacocoResponse {

  private final  double timeToRunTest;
  private float codeCoverage;
  private String finalResult;

  public JacocoResponse(double timeToRunTests, float codeCoverage, String finalResult) {
    super();
    this.timeToRunTest = timeToRunTests;
    this.codeCoverage = codeCoverage;
    this.finalResult = finalResult;
  }


  public float getCodeCoverage() {
    return codeCoverage;
  }

  public void setCodeCoverage(float codeCoverage) {
    this.codeCoverage = codeCoverage;
  }

  public double getTimeToRunTest() {
    return timeToRunTest;
  }

  public String getFinalResult() {
    return finalResult;
  }

  public void setFinalResult(String finalResult) {
    this.finalResult = finalResult;
  }

}
