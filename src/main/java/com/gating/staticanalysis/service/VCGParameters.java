package com.gating.staticanalysis.service;

public class VCGParameters {

  private String sourceCodePath;
  private String outputFormat="-x";
  public static final String VCG_REPORT_PATH = "reports/vcg_report.xml";

  public String getSourceCodePath() {
    return sourceCodePath;
  }

  public void setSourceCodePath(String sourceCodePath) {
    this.sourceCodePath = sourceCodePath;
  }

  public String getOutputFormat() {
    return outputFormat;
  }

  public void setOutputFormat(String outputFormat) {
    this.outputFormat = outputFormat;
  }
}
