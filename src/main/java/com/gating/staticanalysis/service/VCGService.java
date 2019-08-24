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
public class VCGService {

  @Autowired
  Logger logger;

  private ProcessBuilder processBuilder;

  private void createProcess() {
    processBuilder = new ProcessBuilder();
    final Map<String, String> envMap = processBuilder.environment();
    String path = envMap.get("Path");
    path += "C:/Program Files (x86)/VisualCodeGrepper;";
    envMap.put("Path", path);
  }

  private List<String> getCommand(VCGParameters vcgParameters) {
    final StringJoiner vcgCommand = new StringJoiner(" ");
    vcgCommand.add("Visualcodegrepper.exe");
    vcgCommand.add("-c");
    vcgCommand.add("-l");
    vcgCommand.add("Java");
    vcgCommand.add("-t");
    vcgCommand.add(vcgParameters.getSourceCodePath());
    vcgCommand.add(vcgParameters.getOutputFormat());
    vcgCommand.add(VCGParameters.VCG_REPORT_PATH);

    final List<String> command = new ArrayList<String>();
    command.add("cmd");
    command.add("/c");
    command.add(vcgCommand.toString());
    return command;
  }

  public int run(VCGParameters vcgParameters) {

    createProcess();
    processBuilder.command(getCommand(vcgParameters));
    Process process = null;

    try {
      process = processBuilder.start();
      process.waitFor();
    } catch (final IOException e) {
      logger.error("IOException occured", e);
    } catch (final InterruptedException e) {
      logger.error("InterruptedException occured", e);
      Thread.currentThread().interrupt();
    }

    return getCountOfSecurityIssues();
  }

  private int getCountOfSecurityIssues() {

    int securityIssuesCount = 0;
    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;
    Document doc = null;

    try {
      builder = factory.newDocumentBuilder();
    } catch (final ParserConfigurationException e) {
      logger.error("Report file could not be parsed", e);
    }

    try {
      if(builder != null) {
        doc = builder.parse(VCGParameters.VCG_REPORT_PATH);
      }
    } catch (final SAXException e) {
      logger.error("SAXException occurred", e);
    } catch (final IOException e) {
      logger.error("IOException occurred", e);
    }


    if (doc != null && doc.getElementsByTagName("CodeIssueCollection") != null) {
      final NodeList issueCollection = doc.getElementsByTagName("CodeIssueCollection");
      for (int i = 0; i < issueCollection.getLength(); i++) {
        final Node p = issueCollection.item(i);
        if (p.getNodeType() == Node.ELEMENT_NODE) {
          final Element file = (Element) p;
          final NodeList codeIssueList = file.getChildNodes();
          securityIssuesCount += codeIssueList.getLength();
        }
      }
    }

    return securityIssuesCount;
  }

}
