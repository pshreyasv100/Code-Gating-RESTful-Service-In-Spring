package com.gating.staticanalysis.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CyvisService {

  @Autowired
  Logger logger;


  // private ProcessBuilder processBuilder;
  //
  // private void createProcess() {
  // processBuilder = new ProcessBuilder();
  // final Map<String, String> envMap = processBuilder.environment();
  // String path = envMap.get("Path");
  // path += "static-code-analyzers/cyvis-0.9;";
  // envMap.put("Path", path);
  // }



  private String getReportFromJarCommand() {

    final StringJoiner command = new StringJoiner(" ");
    command.add("cmd");
    command.add("/c");
    command.add("\"");
    command.add("cd");
    command.add("/static-code-analyzers/cyvis-0.9");
    command.add("&&");

    command.add("java");
    command.add("-jar");
    command.add("cyvis-0.9.jar");
    command.add("-p");
    command.add("code.jar");
    command.add("-t");
    command.add("report");
    command.add("\"");
    return command.toString();
  }


  private String getJarFromSourceCodeCommand(String srcPath) throws IOException {

    final StringJoiner command = new StringJoiner(" ");
    command.add("cmd");
    command.add("/c");
    command.add("\"");
    command.add("cd");
    command.add("/static-code-analyzers/cyvis-0.9");
    command.add("&&");

    command.add("jar");
    command.add("cf");
    command.add("code.jar");
    command.add(srcPath);
    command.add("\"");

    return command.toString();
  }

  private Map<String, Integer> parseCyvisReport() {

    final String csvFile =
        "C:\\eclipse-workspace\\staticanalysis.service\\static-code-analyzers\\cyvis-0.9\\report.txt";

    // final String csvFile = "/staticanalysis.service/static-code-analyzers/cyvis-0.9/report.txt";
    BufferedReader br = null;
    String line = "";
    final String cvsSplitBy = ",";

    final Map<String, Integer> methodComplexityMap = new HashMap<String, Integer>();

    try {

      br = new BufferedReader(new FileReader(csvFile));
      while ((line = br.readLine()) != null) {
        // use comma as separator
        final String[] complexity = line.split(cvsSplitBy);
        int column = 3;
        while (column < complexity.length) {
          methodComplexityMap.put(complexity[1].concat(".".concat(complexity[column - 1])),
              new Integer(complexity[column]));
          column += 4;
        }
      }

    } catch (final FileNotFoundException e) {
      e.printStackTrace();
    } catch (final IOException e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (final IOException e) {
          e.printStackTrace();
        }
      }
    }

    return methodComplexityMap;
  }


  public int run(CyvisParameters cyvisParameters) throws IOException, InterruptedException {

    final Process p1 =
        Runtime.getRuntime().exec(getJarFromSourceCodeCommand(cyvisParameters.getSourceCodePath()));
    p1.waitFor();

    final Process p2 = Runtime.getRuntime().exec(getReportFromJarCommand());
    p2.waitFor();

    return getMaxComplexity(parseCyvisReport());
  }


  private int getMaxComplexity(Map<String, Integer> methodComplexityMap) {

    final Set<Map.Entry<String, Integer>> st = methodComplexityMap.entrySet();
    int maxComplexity = 0;

    for (final Map.Entry<String, Integer> map : st) {
      if (maxComplexity < map.getValue()) {
        maxComplexity = map.getValue();
      }
    }
    return maxComplexity;

  }


}
