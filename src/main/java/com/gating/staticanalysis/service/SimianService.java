package com.gating.staticanalysis.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gating.thresholdconfig.service.ThresholdConfigurationService;

@Service
public class SimianService {


  @Autowired
  ThresholdConfigurationService thresholdConfigurationService;

  private List<String> getCommand(SimianParameters simianParameters) {

    final StringJoiner simianCommand = new StringJoiner(" ");
    simianCommand.add("java -jar");
    simianCommand.add(SimianParameters.simianJarPath);
    simianCommand.add(simianParameters.getSourceCodePath());
    simianCommand.add("-threshold=" + thresholdConfigurationService.getThresholds().getduplicateLinesThreshold());
    simianCommand.add("-includes=**/*.java");
    simianCommand.add("-excludes=**/*Test.java");

    final List<String> command = new ArrayList<String>();
    command.add("cmd");
    command.add("/c");
    command.add(simianCommand.toString());
    return command;
  }


  public int run(SimianParameters simianParameters) {
    final ProcessBuilder processBuilder = new ProcessBuilder();
    processBuilder.command(getCommand(simianParameters));
    Process process = null;
    try {
      process = processBuilder.start();
      process.waitFor();
    } catch (final IOException e) {
      e.printStackTrace();
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println(process.exitValue());
    return process.exitValue();

  }
}
