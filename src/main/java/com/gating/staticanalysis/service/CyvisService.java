package com.gating.staticanalysis.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gating.service.ProcessUtility;
import com.gating.toolconfig.service.ThresholdConfigService;
import com.gating.toolconfig.service.ToolResponse;
import com.gating.utility.Utility;


@Service
public class CyvisService {

  Logger logger = LoggerFactory.getLogger(CyvisService.class);

  @Autowired
  ProcessUtility processUtility;

  @Autowired
  ThresholdConfigService thresholdConfigService;

  private static final String CYVIS_BIN_PATH = System.getProperty("user.dir")+"\\static-code-analyzers\\cyvis-0.9";
  private static final String PROJECT_JAR_PATH = "code.jar";
  private static final String CYVIS_REPORT_PATH = System.getProperty("user.dir")+ "\\reports\\cyvis_report.txt";

  public List<String> getCommand(String srcPath){

    final StringJoiner cyvisCommand = new StringJoiner(" ");
    cyvisCommand.add("cd");
    cyvisCommand.add(CYVIS_BIN_PATH);
    cyvisCommand.add("&&");
    cyvisCommand.add("jar");
    cyvisCommand.add("cf");
    cyvisCommand.add(PROJECT_JAR_PATH);
    cyvisCommand.add(srcPath);
    cyvisCommand.add("&&");
    cyvisCommand.add("java");
    cyvisCommand.add("-jar");
    cyvisCommand.add("cyvis-0.9.jar");
    cyvisCommand.add("-p");
    cyvisCommand.add(PROJECT_JAR_PATH);
    cyvisCommand.add("-t");
    cyvisCommand.add(CYVIS_REPORT_PATH);

    final List<String> command = new ArrayList<String>();
    command.add("cmd");
    command.add("/c");
    command.add(cyvisCommand.toString());

    return command;
  }


  public int getMaxComplexity(Map<String, Integer> methodComplexityMap) {

    final Set<Map.Entry<String, Integer>> st = methodComplexityMap.entrySet();
    int maxComplexity = 0;

    for (final Map.Entry<String, Integer> map : st) {
      if (maxComplexity < map.getValue()) {
        maxComplexity = map.getValue();
      }
    }
    return maxComplexity;

  }

  public Map<String, Integer> parseCyvisReport(String csvFile) throws NumberFormatException, IOException {

    BufferedReader reader = null;
    String line = "";
    final String cvsSplitBy = ",";

    final Map<String, Integer> methodComplexityMap = new HashMap<String, Integer>();


    reader = new BufferedReader(new FileReader(csvFile));
    while ((line = reader.readLine()) != null) {
      final String[] complexity = line.split(cvsSplitBy);
      int column = 3;
      while (column < complexity.length) {
        methodComplexityMap.put(complexity[1].concat(".".concat(complexity[column - 1])),
            new Integer(complexity[column]));
        column += 4;
      }
    }

    if(reader != null) {
      reader.close();
    }

    return methodComplexityMap;
  }

  public ToolResponse<Integer> run(String srcPath) throws IOException, InterruptedException{

    processUtility.runProcess(getCommand(srcPath), null);

    final Map<String, Integer> complexityMap = parseCyvisReport(CYVIS_REPORT_PATH);
    final int maxComplexity =  getMaxComplexity(complexityMap);
    final int threshold = thresholdConfigService.getThresholds().getCyclomaticComplexity();
    final String finalDecision = Utility.isLessThan(maxComplexity, threshold) ? "Go" : "No Go";

    return new ToolResponse<Integer>(maxComplexity, threshold, finalDecision);
  }






}
