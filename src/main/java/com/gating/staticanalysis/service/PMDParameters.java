package com.gating.staticanalysis.service;

public class PMDParameters {

  private String sourceCodePath;
  private String ruleSet = "rulesets/java/quickstart.xml";
  public static final String pmdReportPath = "reports/pmd_report.xml";
  public static final String outputFormat = "xml";


  public void setSourceCodePath(String sourceCodePath) {
    this.sourceCodePath = sourceCodePath;
  }

  public void setRuleSet(String resultSet) {
    this.ruleSet = resultSet;
  }

  public String getSourceCodePath() {
    return sourceCodePath;
  }

  public String getRuleSet() {
    return ruleSet;
  }

}
