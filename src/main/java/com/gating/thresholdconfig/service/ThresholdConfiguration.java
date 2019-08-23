package com.gating.thresholdconfig.service;

import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement
public class ThresholdConfiguration {

  private int cyclomaticComplexity;
  private float codeCoverage;
  private int timeToRunTests;
  private int duplicateLinesThreshold;
  private int noOfWarnings;
  private int securityIssuesCount;


  public ThresholdConfiguration() {}

  public int getDuplicateLinesThreshold() {
    return duplicateLinesThreshold;
  }

  public void setDuplicateLinesThreshold(int duplicateLinesThreshold) {
    this.duplicateLinesThreshold = duplicateLinesThreshold;
  }

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
