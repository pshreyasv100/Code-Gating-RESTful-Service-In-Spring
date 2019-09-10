package com.gating.staticanalysis.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gating.service.ProcessUtility;
import com.gating.toolconfig.service.SimianConfig;
import com.gating.toolconfig.service.SimianConfigService;
import com.gating.toolconfig.service.ThresholdConfigService;
import com.gating.toolconfig.service.ToolResponse;
import com.gating.utility.InternalServiceException;

@Service
public class SimianService {

  @Autowired
  ThresholdConfigService thresholdService;

  @Autowired
  SimianConfigService simianConfigService;

  @Autowired
  ProcessUtility processUtility;


  private static final String SIMIAN_JAR_PATH =
      System.getProperty("user.dir") + "\\static-code-analyzers\\simian\\bin\\simian-2.5.10.jar";
  private static final String SIMIAN_REPORT_PATH =
      System.getProperty("user.dir") + "\\reports\\simian_report.txt";

  public List<String> getCommand(SimianConfig simianParameters, String srcPath) {

    final StringJoiner simianCommand = new StringJoiner(" ");
    simianCommand.add("cd");
    simianCommand.add(srcPath);
    simianCommand.add("&&");
    simianCommand.add("java");
    simianCommand.add("-jar");
    simianCommand.add(SIMIAN_JAR_PATH);
    simianCommand.add("-threshold=" + simianParameters.getDuplicateLinesThreshold());
    simianCommand.add("-includes=**/*.java");
    simianCommand.add("-excludes=**/*Test.java");
    simianCommand.add("-formatter=plain");
    simianCommand.add(">");
    simianCommand.add(SIMIAN_REPORT_PATH);

    final List<String> command = new ArrayList<>();
    command.add("cmd");
    command.add("/c");
    command.add(simianCommand.toString());
    return command;
  }

  public int parseSimianTextReport(String reportPath){

    try (BufferedReader reader = new BufferedReader(new FileReader(new File(reportPath)))) {

      String line;
      String prevLine = "";
      String secondPrevLine = "";
      String thirdPrevLine = "";

      while ((line = reader.readLine()) != null) {
        thirdPrevLine = secondPrevLine;
        secondPrevLine = prevLine;
        prevLine = line;
      }

      if("".equals(thirdPrevLine)) {
        return Integer.valueOf(thirdPrevLine.split(" ")[1]);
      }

    } catch (final FileNotFoundException e) {
      throw new InternalServiceException("Simian report not found", e);
    }catch(final IOException e) {
      throw new InternalServiceException("Error occured while parsing simian report", e);
    }

    return -1;
  }


  public ToolResponse<Integer> run(String srcPath)
  {

    final int threshold = thresholdService.getThresholds().getCodeDuplication();
    final SimianConfig simianConfig = simianConfigService.getConfig();
    final int simianReturnValue =
        processUtility.runProcess(getCommand(simianConfig, srcPath), null);

    if (simianReturnValue == 0) {
      return new ToolResponse<>(srcPath, 0, threshold, "Go");
    }

    final int duplicateLinesFound = parseSimianTextReport(SIMIAN_REPORT_PATH);
    return new ToolResponse<>(srcPath, duplicateLinesFound, threshold, "No Go");

  }
}
