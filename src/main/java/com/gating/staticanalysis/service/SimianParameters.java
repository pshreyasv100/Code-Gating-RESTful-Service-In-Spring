package com.gating.staticanalysis.service;

public class SimianParameters {

  public static final String SIMIAN_JAR_PATH = "static-code-analyzers/simian/bin/simian-2.5.10.jar";

  private int duplicateLinesThreshold;

  public int getDuplicateLinesThreshold() {
    return duplicateLinesThreshold;
  }

  public void setDuplicateLinesThreshold(int duplicateLinesThreshold) {
    this.duplicateLinesThreshold = duplicateLinesThreshold;
  }

}
