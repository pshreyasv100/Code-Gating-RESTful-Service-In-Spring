package com.gating.toolconfig.service;

public class ThresholdConfig {

  private int cyclomaticComplexity;
  private float codeCoverage;
  private Float timeToRunTests;
  private int noOfWarnings;
  private int securityIssuesCount;
  private int codeDuplication;


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

  public float getTimeToRunTests() {
    return timeToRunTests;
  }

  public void setTimeToRunTests(Float time) {
    this.timeToRunTests = time;
  }

  public int getNoOfWarnings() {
    return noOfWarnings;
  }

  public void setNoOfWarnings(int noOfWarnings) {
    this.noOfWarnings = noOfWarnings;
  }

  public int getCodeDuplication() {
    return codeDuplication;
  }

  public void setCodeDuplication(int codeDuplication) {
    this.codeDuplication = codeDuplication;
  }



}
