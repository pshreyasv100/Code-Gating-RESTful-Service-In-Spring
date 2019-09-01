package com.gating.thresholdconfig.service;

public class ThresholdConfig {

  private int cyclomaticComplexity;
  private float codeCoverage;
  private int timeToRunTests;
  private int noOfWarnings;
  private int securityIssuesCount;


  public ThresholdConfig() {}

  public int getSecurityIssuesCount() {
    return securityIssuesCount;
  }

  public void setSecurityIssuesCount(int securityIssuesCount) {
    this.securityIssuesCount = securityIssuesCount;
  }


  public int getCyclomaticComplexity() {
    return cyclomaticComplexity;
  }

  public void setCyclomaticComplexity(int cyclomaticComplexity) {
    this.cyclomaticComplexity = cyclomaticComplexity;
  }

  public float getCodeCoverage() {
    return codeCoverage;
  }

  public void setCodeCoverage(float codeCoverage) {
    this.codeCoverage = codeCoverage;
  }

  public int getTimeToRunTests() {
    return timeToRunTests;
  }

  public void setTimeToRunTests(int timeToRunTests) {
    this.timeToRunTests = timeToRunTests;
  }

  public int getNoOfWarnings() {
    return noOfWarnings;
  }

  public void setNoOfWarnings(int noOfWarnings) {
    this.noOfWarnings = noOfWarnings;
  }



}
