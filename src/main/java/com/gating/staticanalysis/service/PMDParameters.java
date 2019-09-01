package com.gating.staticanalysis.service;

public class PMDParameters {


  private String ruleSet;
  public static final String pmdReportPath = "reports/pmd_report.xml";
  public static final String outputFormat = "xml";



  public void setRuleSet(String resultSet) {
    this.ruleSet = resultSet;
  }

  public String getRuleSet() {
    return ruleSet;
  }

}
