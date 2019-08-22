package com.gating.controller;

import com.gating.staticanalysis.service.PMDParameters;
import com.gating.staticanalysis.service.SimianParameters;

public class GatingContext {

  private SimianParameters simianParameters;
  private PMDParameters pmdParameters;

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


}
