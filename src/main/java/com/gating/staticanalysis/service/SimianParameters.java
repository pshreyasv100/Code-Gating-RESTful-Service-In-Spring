package com.gating.staticanalysis.service;

public class SimianParameters {

  public static final String simianJarPath = "static-code-analyzers/simian/bin/simian-2.5.10.jar";
  private String sourceCodePath;

  public String getSourceCodePath() {
    return sourceCodePath;
  }

  public void setSourceCodePath(String sourceCodePath) {
    this.sourceCodePath = sourceCodePath;
  }
}
