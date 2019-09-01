package com.gating.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gating.codecoverage.service.JacocoParameters;
import com.gating.staticanalysis.service.CyvisParameters;
import com.gating.staticanalysis.service.PMDParameters;
import com.gating.staticanalysis.service.SimianParameters;
import com.gating.staticanalysis.service.VCGParameters;



public class GatingInput {

  @JsonProperty
  private String sourceCodePath;

  @JsonProperty
  private SimianParameters simianParameters;

  @JsonProperty
  private PMDParameters pmdParameters;

  @JsonProperty
  private VCGParameters vcgParameters;

  @JsonProperty
  private JacocoParameters jacocoParameters;

  @JsonProperty
  private CyvisParameters cyvisParameters;


  public CyvisParameters getCyvisParameters() {
    return cyvisParameters;
  }

  public void setCyvisParameters(CyvisParameters cyvisParameters) {
    this.cyvisParameters = cyvisParameters;
  }


  public JacocoParameters getJacocoParameters() {
    return jacocoParameters;
  }

  public void setJacocoParameters(JacocoParameters jacocoParameters) {
    this.jacocoParameters = jacocoParameters;
  }

  public SimianParameters getSimianParameters() {
    return simianParameters;
  }

  public void setSimianParameters(SimianParameters simianParameters) {
    this.simianParameters = simianParameters;
  }

  public PMDParameters getPmdParameters() {
    return this.pmdParameters;
  }

  public void setPmdParameters(PMDParameters pmdParameters) {
    this.pmdParameters = pmdParameters;
  }

  public VCGParameters getVcgParameters() {
    return vcgParameters;
  }

  public void setVcgParameters(VCGParameters vcgParameters) {
    this.vcgParameters = vcgParameters;
  }

  public String getSourceCodePath() {
    return sourceCodePath;
  }

  public void setSourceCodePath(String sourceCodePath) {
    this.sourceCodePath = sourceCodePath;
  }


}
