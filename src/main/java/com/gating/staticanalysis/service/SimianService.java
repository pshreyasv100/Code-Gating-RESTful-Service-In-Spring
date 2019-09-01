package com.gating.staticanalysis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gating.service.ProcessUtility;
import com.gating.thresholdconfig.service.ThresholdConfigService;

@Service
public class SimianService {

  @Autowired
  Logger logger;

  @Autowired
  ThresholdConfigService thresholdConfigurationService;

  @Autowired
  ProcessUtility processUtility;

  private List<String> getCommand(String srcPath, SimianParameters simianParameters) {

    final StringJoiner simianCommand = new StringJoiner(" ");
    simianCommand.add("java -jar");
    simianCommand.add(SimianParameters.SIMIAN_JAR_PATH);
    simianCommand.add(srcPath);
    simianCommand.add(
        "-threshold=" + simianParameters.getDuplicateLinesThreshold());
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


  public int run(String srcPath, SimianParameters simianParameters) {

    processUtility.initProcessBuilder();
    return processUtility.runProcess(getCommand(srcPath, simianParameters));
  }
}
