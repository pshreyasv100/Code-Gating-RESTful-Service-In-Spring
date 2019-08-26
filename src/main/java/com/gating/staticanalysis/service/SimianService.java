package com.gating.staticanalysis.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gating.thresholdconfig.service.ThresholdConfigurationService;

@Service
public class SimianService {

  @Autowired
  Logger logger;

  @Autowired
  ThresholdConfigurationService thresholdConfigurationService;

  private List<String> getCommand(SimianParameters simianParameters) {

    final StringJoiner simianCommand = new StringJoiner(" ");
    simianCommand.add("java -jar");
    simianCommand.add(SimianParameters.SIMIAN_JAR_PATH);
    simianCommand.add(simianParameters.getSourceCodePath());
    simianCommand.add(
        "-threshold=" + thresholdConfigurationService.getThresholds().getDuplicateLinesThreshold());
    simianCommand.add("-formatter=text");
    simianCommand.add("-includes=**/*.java");
    simianCommand.add("-excludes=**/*Test.java");
    simianCommand.add(">");
    simianCommand.add("reports/simian_report.txt");

    final List<String> command = new ArrayList<String>();
    command.add("cmd");
    command.add("/c");
    command.add(simianCommand.toString());
    return command;
  }


  public int run(SimianParameters simianParameters) {

    logger.error("inside simian");

    final ProcessBuilder processBuilder = new ProcessBuilder();
    processBuilder.command(getCommand(simianParameters));
    Process process = null;

    try {
      process = processBuilder.start();
      process.waitFor();
      return process.exitValue();

    } catch (final IOException e) {
      logger.error("IOException occured", e);
    } catch (final InterruptedException e) {
      logger.error("InterruptedException occured", e);
      Thread.currentThread().interrupt();
    }

    return Integer.valueOf(null);
  }
}
