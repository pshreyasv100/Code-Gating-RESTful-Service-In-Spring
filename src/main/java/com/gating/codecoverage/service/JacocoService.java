package com.gating.codecoverage.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gating.service.ProcessUtility;

@Service
public class JacocoService {

  Logger logger = LoggerFactory.getLogger(JacocoService.class);


  @Autowired
  ProcessUtility processUtility;

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

  private List<String> getAllTestCasesPath(File projectTestsCasesPath) {

    final List<String> resultFiles = new ArrayList<String>();
    searchFilesInDirectory(".*\\.class", projectTestsCasesPath, resultFiles);
    return resultFiles;
  }

  public List<String> createExecFileCommand(String srcPath, JacocoParameters jacocoParameters, String pathvar) {

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

    final List<String> command = new ArrayList<String>();
    command.add("cmd");
    command.add("/c");
    command.add(jacocoCommand.toString());
    return command;
  }

  public String getFullyQualifiedClassName(String pathOfClass) {

    final String[] paths=pathOfClass.split("\\\\");
    boolean flag=false;
    String pathvar="";
    for(int i=0;i<paths.length-1;i++) {
      if(paths[i].equals("test-classes")) {
        flag=true;
        i+=1;
      }
      if(flag) {
        pathvar+=paths[i]+".";
      }
    }
    paths[paths.length-1] = paths[paths.length-1].replace(".class", "");
    pathvar+=paths[paths.length-1];
    return pathvar;
  }

  private List<String> createReportCommand(String srcPath, JacocoParameters jacocoParameters){

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

    final List<String> reportCommand = new ArrayList<String>();
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



  public float run(String srcPath, JacocoParameters jacocoParameters) throws IOException, InterruptedException {


    final String[] mavenclean = {"cmd", "/c", "mvn", "clean"};
    final String[] mavencompile = {"cmd", "/c", "mvn", "compile"};
    final String[] maventestcompile = {"cmd", "/c", "mvn", "test-compile"};
    final String[] maveninstall = {"cmd", "/c", "mvn", "install"};


    //processUtility.initProcessBuilder();
    //processUtility.runProcess(mavenclean, new  File(srcPath));

    //    processUtility.initProcessBuilder();
    //    processUtility.runProcess(mavenclean, new  File(jacocoParameters.getsourceCodePath()));

    //    processBuilder.command(mavenclean);
    //    final Process process1 = processBuilder.start();
    //    process1.waitFor();
    //
    //    processBuilder.command(mavencompile);
    //    final Process process2 = processBuilder.start();
    //    process2.waitFor();
    //
    //
    //    processBuilder.command(maventestcompile);
    //    final Process process3 = processBuilder.start();
    //    process3.waitFor();
    //
    //
    //    processBuilder.command(maveninstall);
    //    final Process process4 = processBuilder.start();
    //    process4.waitFor();


    final List<String> allTests =
        getAllTestCasesPath(new File(srcPath + "/target/test-classes"));

    for(final String testClass:allTests) {
      final String classFullyQualifiedName = getFullyQualifiedClassName(testClass);
      processUtility.initProcessBuilder();
      processUtility.runProcess(createExecFileCommand(srcPath, jacocoParameters, classFullyQualifiedName));
    }

    processUtility.initProcessBuilder();
    processUtility.runProcess(createReportCommand(srcPath, jacocoParameters));

    return getCoverageFromReport();
  }



}


