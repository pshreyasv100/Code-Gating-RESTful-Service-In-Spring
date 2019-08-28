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
    for (final File file : folder.listFiles()) {
      if (file.isDirectory()) {
        searchFilesInDirectory(pattern, file, result);
      }
      if (file.isFile() && file.getName().matches(pattern)) {
        result.add(file.getAbsolutePath());
      }
    }
  }

  private List<String> getTestCasesFullyQualifiedName(File projectTestsCasesPath) {

    final List<String> resultFiles = new ArrayList<String>();
    searchFilesInDirectory(".*\\.class", projectTestsCasesPath, resultFiles);
    return resultFiles;
  }

  private List<String> createExecFileCommand(JacocoParameters jacocoParameters) {

    final StringBuilder jarsRequired = new StringBuilder();
    jarsRequired.append("static-code-analyzers/jacoco/junit-4.12.jar");
    jarsRequired.append("static-code-analyzers/jacoco/hamcrest-core-1.3.jar");

    final StringBuilder jacocoCommand = new StringBuilder();
    jacocoCommand.append("java -cp ");
    jacocoCommand.append(jarsRequired);
    jacocoCommand.append(jacocoParameters.getsourceCodePath() + "/target/test-classes");
    jacocoCommand.append(jacocoParameters.getsourceCodePath() + "/target/classes");
    jacocoCommand.append(" -javaagent:" + "static-code-analyzers/jacoco/jacocoagent.jar");
    jacocoCommand.append("=destfile=" + "jacoco.exec");
    jacocoCommand.append(" org.junit.runner.JUnitCore ");

    final List<String> allTests =
        getTestCasesFullyQualifiedName(new File(jacocoParameters.getsourceCodePath() + "/target/test-classes"));

    for(final String testFile : allTests) {
      jacocoCommand.append(testFile);
    }

    final List<String> command = new ArrayList<String>();
    command.add("cmd");
    command.add("/c");
    command.add(jacocoCommand.toString());
    return command;
  }

  private List<String> createReportCommand(JacocoParameters jacocoParameters){

    final StringBuilder jacococliJarLocation = new StringBuilder();
    jacococliJarLocation.append("/static-code-analyzers/jacoco/jacococli.jar");
    final StringBuilder finalCsvFileLocation = new StringBuilder();
    finalCsvFileLocation.append("report2.csv");
    final StringBuilder execFileLocation = new StringBuilder();
    execFileLocation.append("jacoco.exec");

    final StringBuilder jacocoReportGenerationCommand = new StringBuilder();
    jacocoReportGenerationCommand.append("java -jar ");
    jacocoReportGenerationCommand.append(jacococliJarLocation);
    jacocoReportGenerationCommand.append(" report ");
    jacocoReportGenerationCommand.append(execFileLocation);
    jacocoReportGenerationCommand.append(" --classfiles ");
    jacocoReportGenerationCommand.append(jacocoParameters.getsourceCodePath() + "/src/main/java");
    jacocoReportGenerationCommand.append(" --classfiles ");
    jacocoReportGenerationCommand.append(jacocoParameters.getsourceCodePath() + "/target/classes");
    jacocoReportGenerationCommand.append(" --sourcefiles ");

    jacocoReportGenerationCommand.append(jacocoParameters.getsourceCodePath() + "/src");
    jacocoReportGenerationCommand.append(" --csv ");
    jacocoReportGenerationCommand.append(finalCsvFileLocation);

    final List<String> reportCommand = new ArrayList<String>();
    reportCommand.add("cmd");
    reportCommand.add("/c");
    reportCommand.add(jacocoReportGenerationCommand.toString());

    System.out.println(reportCommand);

    return reportCommand;

  }

  public float run(JacocoParameters jacocoParameters) {

    try {
      final ProcessBuilder processBuilder = new ProcessBuilder();
      processBuilder.command(createExecFileCommand(jacocoParameters));
      Process process = null;
      process = processBuilder.start();
      process.waitFor();

      final ProcessBuilder processBuilder1 = new ProcessBuilder();
      Process process1 = null;
      processBuilder1.command(createReportCommand(jacocoParameters));
      process1 = processBuilder1.start();
      process1.waitFor();

    } catch (final IOException e) {
      logger.error("IOException occurred", e);
    } catch (final InterruptedException e) {
      logger.error("InterruptedException occurred", e);
      Thread.currentThread().interrupt();
    }
    return 0;
  }

}
