package com.gating.toolconfig.service;

public class VCGConfig {

  private String outputFormat="-x";
  public static final String VCG_REPORT_PATH = "reports/vcg_report.xml";
  public static final String VCG_BIN_PATH =  "C:\\Program Files (x86)\\VisualCodeGrepper;";



  public String getOutputFormat() {
    return outputFormat;
  }

  public void setOutputFormat(String outputFormat) {
    this.outputFormat = outputFormat;
  }
}
