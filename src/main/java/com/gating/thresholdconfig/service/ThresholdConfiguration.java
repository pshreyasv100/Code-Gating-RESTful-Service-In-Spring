package com.gating.thresholdconfig.service;

import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement
public class ThresholdConfiguration{

  private int cyclomaticComplexity;
  private int codeCoverage;
  private int timeToRunTests;
  private int duplicateLinesThreshold;
  private int noOfWarnings;

  public ThresholdConfiguration() {
  }

  public int getCyclomaticComplexity() {
    return cyclomaticComplexity;
  }

  public void setCyclomaticComplexity(int cyclomaticComplexity) {
    this.cyclomaticComplexity = cyclomaticComplexity;
  }

  public int getCodeCoverage() {
    return codeCoverage;
  }

  public void setCodeCoverage(int codeCoverage) {
    this.codeCoverage = codeCoverage;
  }

  public int getTimeToRunTests() {
    return timeToRunTests;
  }

  public void setTimeToRunTests(int timeToRunTests) {
    this.timeToRunTests = timeToRunTests;
  }

  public int getduplicateLinesThreshold() {
    return duplicateLinesThreshold;
  }

  public void setduplicateLinesThreshold(int duplicateLinesThreshold) {
    this.duplicateLinesThreshold = duplicateLinesThreshold;
  }

  public int getNoOfWarnings() {
    return noOfWarnings;
  }

  public void setNoOfWarnings(int noOfWarnings) {
    this.noOfWarnings = noOfWarnings;
  }



}
