package com.gating.staticanalysis.service;

public class VCGParameters {

  private String outputFormat="-x";
  public static final String VCG_REPORT_PATH = "reports/vcg_report.xml";


  public String getOutputFormat() {
    return outputFormat;
  }

  public void setOutputFormat(String outputFormat) {
    this.outputFormat = outputFormat;
  }
}
