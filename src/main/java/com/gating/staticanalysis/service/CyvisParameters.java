package com.gating.staticanalysis.service;

public class CyvisParameters {

  private String sourceCodePath;
  public static final String CYVIS_REPORT_PATH = "reports/cyvis_reports";

  public String getSourceCodePath() {
    return sourceCodePath;
  }

  public void setSourceCodePath(String sourceCodePath) {
    this.sourceCodePath = sourceCodePath;
  }


}
