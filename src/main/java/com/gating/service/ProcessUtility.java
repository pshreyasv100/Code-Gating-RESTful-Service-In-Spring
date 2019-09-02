package com.gating.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProcessUtility {

  private ProcessBuilder processBuilder;

  Logger logger = LoggerFactory.getLogger(ProcessUtility.class);


  public void initProcessBuilder() {
    processBuilder = new ProcessBuilder();
  }

  public void initProcessBuilder(String toolBinPath) {

    processBuilder = new ProcessBuilder();
    final Map<String, String> envMap = processBuilder.environment();
    String path = envMap.get("Path");
    path += toolBinPath;
    envMap.put("Path", path);
  }


  public int runProcess(List<String> command) {

    processBuilder.command(command);
    Process process = null;

    try {
      process = processBuilder.start();
      process.waitFor();
      return process.exitValue();
    } catch (final IOException e) {
      e.printStackTrace();
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }

    return 1;

  }

  public int runProcess(List<String> command, File directoryToRunProcessIn) {

    processBuilder.directory(directoryToRunProcessIn);
    processBuilder.command(command);
    final Map<String,String> map = processBuilder.environment();
    Process process = null;
    try {
      process = processBuilder.start();
      process.waitFor();
      System.out.println(process.exitValue());
      return process.exitValue();
    } catch (final IOException e) {
      e.printStackTrace();
    } catch (final InterruptedException e) {
      e.printStackTrace();

    }

    return 1;
  }
}
