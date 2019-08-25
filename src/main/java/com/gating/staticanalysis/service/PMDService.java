package com.gating.staticanalysis.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Service
public class PMDService {


  @Autowired
  Logger logger;

  private ProcessBuilder processBuilder;

  private void createProcess() {
    processBuilder = new ProcessBuilder();
    final Map<String, String> envMap = processBuilder.environment();
    String path = envMap.get("Path");
    path += "static-code-analyzers/pmd/bin;";
    envMap.put("Path", path);
  }

  private List<String> getCommand(PMDParameters params) {

    final StringJoiner pmdCommand = new StringJoiner(" ");
    pmdCommand.add("pmd -d");
    pmdCommand.add(params.getSourceCodePath());
    pmdCommand.add("-f");
    pmdCommand.add(PMDParameters.outputFormat);
    pmdCommand.add("-R");
    pmdCommand.add(params.getRuleSet());
    pmdCommand.add(">");
    pmdCommand.add(PMDParameters.pmdReportPath);

    final List<String> command = new ArrayList<String>();
    command.add("cmd");
    command.add("/c");
    command.add(pmdCommand.toString());
    return command;
  }


  private int getNumberOfViolations() {

    logger.error("inside pmd");
    int violations = 0;

    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;
    Document doc = null;

    try {
      builder = factory.newDocumentBuilder();
    } catch (final ParserConfigurationException e) {
      logger.error("ParserConfigurationException occured", e);
    }

    try {
      if (builder != null) {
        doc = builder.parse(PMDParameters.pmdReportPath);
      }
    } catch (final SAXException e) {
      logger.error("SAXException occured", e);
    } catch (final IOException e) {
      logger.error("File not found exception occured", e);
    }

    if (doc != null && doc.getElementsByTagName("file") != null) {
      final NodeList fileList = doc.getElementsByTagName("file");
      for (int i = 0; i < fileList.getLength(); i++) {
        final Node p = fileList.item(i);
        if (p.getNodeType() == Node.ELEMENT_NODE) {
          final Element file = (Element) p;
          final NodeList violationList = file.getChildNodes();
          violations += violationList.getLength();
        }
      }
    }

    return violations;
  }


  public int run(PMDParameters params) throws IOException, InterruptedException {
    createProcess();
    processBuilder.command(getCommand(params));
    final Process process = processBuilder.start();
    process.waitFor();
    return getNumberOfViolations();
  }

}
