package com.gating.codecoverage.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class JacocoService {

  private List<String> getTestCasesFullyQualifiedName(String projectTestsCasesPath){

    try {
      final Stream<Path> walk = Files.walk(Paths.get(projectTestsCasesPath));
      final List<String> result = walk.map(x -> x.toString()).filter(f -> f.endsWith(".class")).collect(Collectors.toList());
      return result;

    } catch (final IOException e) {
      e.printStackTrace();
    }
    return null;
  }


  private List<String> getCommand(JacocoParameters jacocoParameters) {

    final StringBuilder jarsRequired = new StringBuilder();
    jarsRequired.append("static-code-analyzers/jacoco/junit-4.12.jar");
    jarsRequired.append("static-code-analyzers/jacoco/hamcrest-core-1.3.jar");

    final StringBuilder jacocoCommand = new StringBuilder();
    jacocoCommand.append("java -cp ");
    jacocoCommand.append(jarsRequired);
    jacocoCommand.append(jacocoParameters.getProjectPath()+"/target/test-classes");
    jacocoCommand.append(jacocoParameters.getProjectPath()+"/target/classes");
    jacocoCommand.append(" -javaagent"+"static-code-analyzers/jacoco/jacocoagent.jar");
    jacocoCommand.append("=destfile="+"jacoco.exec");
    jacocoCommand.append(" org.junit.runner.JUnitCore ");


    final List<String> allTests = getTestCasesFullyQualifiedName(jacocoParameters.getProjectPath()+"/target/test-classes");

    final List<String> command = new ArrayList<>();
    command.add("cmd");
    command.add("/c");
    command.add(jacocoCommand.toString());
    return command;
  }



  public float run(JacocoParameters jacocoParameters) {
    return 0;




  }

}
