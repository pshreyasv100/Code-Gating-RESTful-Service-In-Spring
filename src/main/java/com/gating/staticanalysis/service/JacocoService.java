package com.gating.staticanalysis.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gating.service.ProcessUtility;
import com.gating.toolconfig.service.ThresholdConfigService;
import com.gating.utility.InvalidInputException;
import com.gating.utility.Utility;

@Service
public class JacocoService {

  Logger logger = LoggerFactory.getLogger(JacocoService.class);



  @Autowired
  ProcessUtility processUtility;

  @Autowired
  ThresholdConfigService thresholdConfigService;

  private List<String> getAllTestCasesPath(File projectTestsCasesPath) {
    final List<String> resultFiles = new ArrayList<>();
    Utility.searchFilesInDirectory(".*\\.class", projectTestsCasesPath, resultFiles);
    return resultFiles;
  }

  public List<String> createExecFileCommand(String srcPath, String pathvar) {

    final StringBuilder jarsRequired = new StringBuilder();
    jarsRequired.append("static-code-analyzers/jacoco/junit-4.12.jar;");
    jarsRequired.append("static-code-analyzers/jacoco/hamcrest-core-1.3.jar;");

    final StringBuilder jacocoCommand = new StringBuilder();
    jacocoCommand.append("java -cp ");
    jacocoCommand.append(jarsRequired);
    jacocoCommand.append(srcPath + "/target/test-classes" + ";");
    jacocoCommand.append(srcPath + "/target/classes");
    jacocoCommand.append(" -javaagent:" + "static-code-analyzers/jacoco/jacocoagent.jar");
    jacocoCommand.append("=destfile=" + "reports/jacoco.exec");
    jacocoCommand.append(" org.junit.runner.JUnitCore ");
    jacocoCommand.append(pathvar);

    final List<String> command = new ArrayList<>();
    command.add("cmd");
    command.add("/c");
    command.add(jacocoCommand.toString());
    return command;
  }

  public String getFullyQualifiedClassName(String pathOfClass) {

    final String[] paths = pathOfClass.split("\\\\");
    boolean flag = false;
    String pathvar = "";
    for (int i = 0; i < paths.length - 1; i++) {
      if (paths[i].equals("test-classes")) {
        flag = true;
        i += 1;
      }
      if (flag) {
        pathvar += paths[i] + ".";
      }
    }
    paths[paths.length - 1] = paths[paths.length - 1].replace(".class", "");
    pathvar += paths[paths.length - 1];
    return pathvar;
  }

  private List<String> createReportCommand(String srcPath) {

    final StringBuilder jacococliJarLocation = new StringBuilder();
    jacococliJarLocation.append("static-code-analyzers/jacoco/jacococli.jar");
    final StringBuilder finalCsvFileLocation = new StringBuilder();
    finalCsvFileLocation.append("reports/jacoco-reports");
    final StringBuilder execFileLocation = new StringBuilder();
    execFileLocation.append("reports/jacoco.exec");

    final StringBuilder jacocoReportGenerationCommand = new StringBuilder();
    jacocoReportGenerationCommand.append("java -jar ");
    jacocoReportGenerationCommand.append(jacococliJarLocation);
    jacocoReportGenerationCommand.append(" report ");
    jacocoReportGenerationCommand.append(execFileLocation);
    jacocoReportGenerationCommand.append(" --classfiles ");
    jacocoReportGenerationCommand.append(srcPath + "/src/main/java");
    jacocoReportGenerationCommand.append(" --classfiles ");
    jacocoReportGenerationCommand.append(srcPath + "/target/classes");
    jacocoReportGenerationCommand.append(" --sourcefiles ");
    jacocoReportGenerationCommand.append(srcPath + "/src");
    jacocoReportGenerationCommand.append(" --html ");
    jacocoReportGenerationCommand.append(finalCsvFileLocation);

    final List<String> reportCommand = new ArrayList<>();
    reportCommand.add("cmd");
    reportCommand.add("/c");
    reportCommand.add(jacocoReportGenerationCommand.toString());

    return reportCommand;
  }

  public static Float getCoverageFromReport() throws IOException {
    final File file = new File("reports/jacoco-reports/index.html");
    final Document doc = Jsoup.parse(file, "UTF-8");
    final Element divTag = doc.getElementById("c0");

    return Float.valueOf(divTag.text().replaceAll("%", ""));
  }

  public List<String> getBuildCommand(String operation, String srcPath) {

    final StringJoiner buildCommand = new StringJoiner("");
    buildCommand.add("mvn");
    buildCommand.add(operation);
    buildCommand.add(srcPath);

    final List<String> command = new ArrayList<>();
    command.add("cmd");
    command.add("/c");
    return command;
  }

  public void buildProject(String srcPath) throws IOException, InterruptedException {
    processUtility.runProcess(getBuildCommand("clean", srcPath), null);
    processUtility.runProcess(getBuildCommand("compile", srcPath), null);
    processUtility.runProcess(getBuildCommand("test-compile", srcPath), null);
    processUtility.runProcess(getBuildCommand("install", srcPath), null);
  }

  public JacocoResponse run(String srcPath) throws IOException, InterruptedException, InvalidInputException {

    final List<String> allTests = getAllTestCasesPath(new File(srcPath + "/target/test-classes"));
    float timeToRunTests = 0;

    for (final String testClass : allTests) {
      final String classFullyQualifiedName = getFullyQualifiedClassName(testClass);

      final long startTime = System.currentTimeMillis();
      processUtility.runProcess(createExecFileCommand(srcPath, classFullyQualifiedName), null);
      final long endTime = System.currentTimeMillis();
      final long timeTaken = (endTime - startTime);
      timeToRunTests = timeToRunTests + timeTaken;
    }

    timeToRunTests = timeToRunTests / 1000;
    processUtility.runProcess(createReportCommand(srcPath), null);

    final float coverageThreshold = thresholdConfigService.getThresholds().getCodeCoverage();
    final float timeThreshold = thresholdConfigService.getThresholds().getTimeToRunTests();
    final float codeCoverage = getCoverageFromReport();

    final String finalDecision =
        Utility.isGreaterThan(codeCoverage, coverageThreshold)
        && Utility.isLessThan(timeToRunTests, timeThreshold) ? "Go"
            : "No Go";

    return new JacocoResponse(timeToRunTests, codeCoverage, finalDecision);
  }

}


