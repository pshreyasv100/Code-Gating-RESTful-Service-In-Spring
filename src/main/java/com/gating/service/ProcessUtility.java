package com.gating.service;

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

  public int runProcess(List<String> command, String toolBinPath) {

    final ProcessBuilder processBuilder = new ProcessBuilder();
    final Map<String, String> envMap = processBuilder.environment();
    String path = envMap.get("Path");
    path += toolBinPath;
    envMap.put("Path", path);


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
}
