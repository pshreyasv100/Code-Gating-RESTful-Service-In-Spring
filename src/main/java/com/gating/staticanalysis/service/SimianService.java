package com.gating.staticanalysis.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gating.service.ProcessUtility;
import com.gating.thresholdconfig.service.ThresholdConfigService;

@Service
public class SimianService {

  Logger logger = LoggerFactory.getLogger(SimianService.class);

  private static final String SIMIAN_BIN_PATH = "static-code-analyzers/simian/bin;";
  private static final String SIMIAN_REPORT_PATH = "static-code-analyzers/reports/simian_report.txt";

  @Autowired
  ThresholdConfigService thresholdConfigurationService;

  @Autowired
  ProcessUtility processUtility;

  private List<String> getCommand(SimianParameters simianParameters) {

    final StringJoiner simianCommand = new StringJoiner(" ");
    simianCommand.add("java");
    simianCommand.add("-jar");
    simianCommand.add("simian-2.5.10.jar");
    simianCommand.add(
        "-threshold=" + simianParameters.getDuplicateLinesThreshold());
    simianCommand.add("-includes=**/*.java");
    simianCommand.add("-excludes=**/*Test.java");
    simianCommand.add("-formatter=plain");
    simianCommand.add(">");
    simianCommand.add("report.txt");

    final List<String> command = new ArrayList<String>();
    command.add("cmd");
    command.add("/c");
    command.add(simianCommand.toString());
    System.out.println(command);
    return command;
  }


  public int run(String srcPath, SimianParameters simianParameters) {

    processUtility.initProcessBuilder(new File(SIMIAN_BIN_PATH).getAbsolutePath());
    return processUtility.runProcess(getCommand(simianParameters), new File(srcPath));
  }
}
