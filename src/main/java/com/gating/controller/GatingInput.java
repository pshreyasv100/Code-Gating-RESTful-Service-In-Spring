package com.gating.controller;

import com.gating.codecoverage.service.JacocoParameters;
import com.gating.staticanalysis.service.PMDParameters;
import com.gating.staticanalysis.service.SimianParameters;
import com.gating.staticanalysis.service.VCGParameters;



public class GatingInput {

  private SimianParameters simianParameters;
  private PMDParameters pmdParameters;
  private VCGParameters vcgParameters;
  private JacocoParameters jacocoParameters;

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
    return pmdParameters;
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


}
