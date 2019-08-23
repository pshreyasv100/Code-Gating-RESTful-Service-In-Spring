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
public class VCGService {

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
      e.printStackTrace();
    } catch (final InterruptedException e) {
      e.printStackTrace();
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
      e.printStackTrace();
    }

    try {
      doc = builder.parse(VCGParameters.VCG_REPORT_PATH);
    } catch (final SAXException e) {
      e.printStackTrace();
    } catch (final IOException e) {
      e.printStackTrace();
    }

    if (doc.getElementsByTagName("CodeIssueCollection") != null) {
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
