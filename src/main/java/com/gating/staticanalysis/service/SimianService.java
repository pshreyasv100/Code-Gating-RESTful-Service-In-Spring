package com.gating.staticanalysis.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gating.service.ProcessUtility;
import com.gating.toolconfig.service.SimianConfig;
import com.gating.toolconfig.service.SimianConfigService;
import com.gating.toolconfig.service.ThresholdConfigService;
import com.gating.toolconfig.service.ToolResponse;

@Service
public class SimianService {

  Logger logger = LoggerFactory.getLogger(SimianService.class);

  private static final String SIMIAN_BIN_PATH = "static-code-analyzers/simian/bin;";

  @Autowired
  ThresholdConfigService thresholdService;

  @Autowired
  SimianConfigService simianConfigService;

  @Autowired
  ProcessUtility processUtility;

  public List<String> getCommand(SimianConfig simianParameters, String srcPath) {

    final StringJoiner simianCommand = new StringJoiner(" ");
    simianCommand.add("cd");
    simianCommand.add(srcPath);
    simianCommand.add("&&");
    simianCommand.add("java");
    simianCommand.add("-jar");
    simianCommand.add("simian-2.5.10.jar");
    simianCommand.add("-threshold=" + simianParameters.getDuplicateLinesThreshold());
    simianCommand.add("-includes=**/*.java");
    simianCommand.add("-excludes=**/*Test.java");
    simianCommand.add("-formatter=plain");
    simianCommand.add(">");
    simianCommand.add(System.getProperty("user.dir") + "\\reports\\simian_report.txt");

    final List<String> command = new ArrayList<String>();
    command.add("cmd");
    command.add("/c");
    command.add(simianCommand.toString());
    return command;
  }

  public int parseSimianReport(String reportPath) throws IOException {

    final BufferedReader reader = new BufferedReader(new FileReader(new File(reportPath)));

    String line;
    String prevLine = null;
    String secondPrevLine = null;
    String thirdPrevLine = null;


    while ((line = reader.readLine()) != null) {
      thirdPrevLine = secondPrevLine;
      secondPrevLine = prevLine;
      prevLine = line;
    }
    reader.close();

    return Integer.valueOf(thirdPrevLine.split(" ")[1]);
  }


  public ToolResponse<Integer> run(String srcPath) throws IOException, InterruptedException {

    int duplicateLinesFound = 0;
    final int threshold = thresholdService.getThresholds().getCodeDuplication();
    final SimianConfig simianConfig = simianConfigService.getConfig();
    final int simianReturnValue = processUtility.runProcess(getCommand(simianConfig, srcPath),
        new File(SIMIAN_BIN_PATH).getAbsolutePath());

    if (simianReturnValue  == 0) {
      return new ToolResponse<Integer>(0, threshold, "Go");
    }
    duplicateLinesFound = parseSimianReport(System.getProperty("user.dir") + "\\reports\\simian_report.txt");
    return new ToolResponse<Integer>(duplicateLinesFound, threshold, "No Go");

  }
}
