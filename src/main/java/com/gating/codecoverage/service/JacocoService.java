package com.gating.codecoverage.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JacocoService {

  @Autowired
  Logger logger;

  private void searchFilesInDirectory(final String pattern, final File folder, List<String> result) {
    for (final File f : folder.listFiles()) {
      if (f.isDirectory()) {
        searchFilesInDirectory(pattern, f, result);
      }
      if (f.isFile() && f.getName().matches(pattern)) {
        result.add(f.getAbsolutePath());
      }
    }
  }

  private List<String> getTestCasesFullyQualifiedName(File projectTestsCasesPath) {

    final List<String> resultFiles = new ArrayList<String>();
    searchFilesInDirectory(".*\\.class", projectTestsCasesPath, resultFiles);
    return resultFiles;
  }

  private List<String> getCommand(JacocoParameters jacocoParameters) {

    final StringBuilder jarsRequired = new StringBuilder();
    jarsRequired.append("static-code-analyzers/jacoco/junit-4.12.jar");
    jarsRequired.append("static-code-analyzers/jacoco/hamcrest-core-1.3.jar");

    final StringBuilder jacocoCommand = new StringBuilder();
    jacocoCommand.append("java -cp ");
    jacocoCommand.append(jarsRequired);
    jacocoCommand.append(jacocoParameters.getProjectPath() + "/target/test-classes");
    jacocoCommand.append(jacocoParameters.getProjectPath() + "/target/classes");
    jacocoCommand.append(" -javaagent" + "static-code-analyzers/jacoco/jacocoagent.jar");
    jacocoCommand.append("=destfile=" + "jacoco.exec");
    jacocoCommand.append(" org.junit.runner.JUnitCore ");

    final List<String> allTests =
        getTestCasesFullyQualifiedName(new File(jacocoParameters.getProjectPath() + "/target/test-classes"));

    for(final String testFile : allTests) {
      jacocoCommand.append(testFile);
    }


    final List<String> command = new ArrayList<String>();
    command.add("cmd");
    command.add("/c");
    command.add(jacocoCommand.toString());
    return command;
  }

  public float run(JacocoParameters jacocoParameters) {

    final ProcessBuilder processBuilder = new ProcessBuilder();
    processBuilder.command(getCommand(jacocoParameters));
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
