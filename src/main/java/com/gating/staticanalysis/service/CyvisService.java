package com.gating.staticanalysis.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CyvisService {

  @Autowired
  Logger logger;

  private String getCommand(CyvisParameters cyvisParameters) {

    final StringJoiner cyvisCommand = new StringJoiner(" ");
    cyvisCommand.add("/staticanalysis.service/static-code-analyzers/cyvis-0.9");
    cyvisCommand.add("&&");
    cyvisCommand.add("jar");
    cyvisCommand.add("cf");
    cyvisCommand.add("code.jar");
    cyvisCommand.add(cyvisParameters.getSourceCodePath());
    cyvisCommand.add("&&");
    cyvisCommand.add("java -jar");
    cyvisCommand.add("/staticanalysis.service/static-code-analyzers/cyvis-0.9/cyvis-0.9.jar");
    cyvisCommand.add("-p");
    cyvisCommand.add("code.jar");
    cyvisCommand.add("-t");
    cyvisCommand.add(CyvisParameters.CYVIS_REPORT_PATH);

    final List<String> command = new ArrayList<String>();
    command.add("cmd");
    command.add("/c");
    command.add(cyvisCommand.toString());
    return command.toString();
  }

  public int run(CyvisParameters cyvisParameters) throws IOException {


    final ProcessBuilder processBuilder = new ProcessBuilder();
    processBuilder.command(getCommand(cyvisParameters));
    Process process = null;
    try {
      process = processBuilder.start();
      process.waitFor();
    } catch (final IOException e) {
      logger.error("IOException occurred", e);
    } catch (final InterruptedException e) {
      logger.error("InterruptedException occurred", e);
      Thread.currentThread().interrupt();
    }

    return 0;
  }


}
