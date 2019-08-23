package com.gating.staticanalysis.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Service
public class PMDService {


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

    int violations = 0;
    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;
    Document doc = null;

    try {
      builder = factory.newDocumentBuilder();
    } catch (final ParserConfigurationException e) {
      e.printStackTrace();
    }

    try {
      doc = builder.parse(PMDParameters.pmdReportPath);
    } catch (final SAXException e) {
      e.printStackTrace();
    } catch (final IOException e) {
      e.printStackTrace();
    }

    if (doc.getElementsByTagName("file") != null) {
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
