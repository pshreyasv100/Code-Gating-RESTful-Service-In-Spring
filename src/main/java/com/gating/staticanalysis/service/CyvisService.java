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
    command.add(
        "C:\\eclipse-workspace\\staticanalysis.service\\static-code-analyzers\\cyvis-0.9\\cyvis-0.9.jar");
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
    command.add(
        "C:\\eclipse-workspace\\staticanalysis.service\\static-code-analyzers\\cyvis-0.9\\cyvis-0.9.jar");
    command.add("&&");

    command.add("jar");
    command.add("cf");
    command.add("code.jar");
    command.add(srcPath);
    command.add("\"");

    return command.toString();

  }

  private void parseCyvisReport() {

    final String csvFile = "C:\\eclipse-workspace\\staticanalysis.service\\static-code-analyzers\\cyvis-0.9\\report.txt";
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
    final Set<Map.Entry<String, Integer>> st = methodComplexityMap.entrySet();

    for (final Map.Entry<String, Integer> me : st) {
      System.out.print(me.getKey() + ":");
      System.out.println(me.getValue());

    }

  }



  // private String getCommand(CyvisParameters cyvisParameters) {
  //
  // final StringJoiner cyvisCommand = new StringJoiner(" ");
  // cyvisCommand.add("jar");
  // cyvisCommand.add("cf");
  // cyvisCommand.add("code.jar");
  // cyvisCommand.add(cyvisParameters.getSourceCodePath());
  // cyvisCommand.add("&&");
  // cyvisCommand.add("java");
  // cyvisCommand.add("-jar");
  // cyvisCommand.add("cyvis-0.9.jar");
  // cyvisCommand.add("-p");
  // cyvisCommand.add("code.jar");
  // cyvisCommand.add("-t");
  // cyvisCommand.add("report");
  //
  //
  //
  // final List<String> command = new ArrayList<String>();
  // command.add("cmd");
  // command.add("/c");
  // command.add(cyvisCommand.toString());
  // return command.toString();
  // }

  public int run(CyvisParameters cyvisParameters) throws IOException, InterruptedException {

    final Process p1 =
        Runtime.getRuntime().exec(getJarFromSourceCodeCommand(cyvisParameters.getSourceCodePath()));
    p1.waitFor();

    final Process p2 = Runtime.getRuntime().exec(getReportFromJarCommand());
    p2.waitFor();

    parseCyvisReport();

    return 0;
  }


}
