package com.gating.staticanalysis.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.gating.service.ProcessUtility;
import com.gating.toolconfig.service.PMDConfig;
import com.gating.toolconfig.service.PMDConfigService;
import com.gating.toolconfig.service.ThresholdConfigService;
import com.gating.toolconfig.service.ToolResponse;
import com.gating.utility.InvalidInputException;
import com.gating.utility.ThresholdComparison;

@Service
public class PMDService {

  Logger logger = LoggerFactory.getLogger(PMDService.class);

  @Autowired
  ProcessUtility processUtility;

  @Autowired
  ThresholdConfigService thresholdConfService;

  @Autowired
  PMDConfigService pmdConfigService;


  public List<String> getCommand(String srcPath, PMDConfig params, String outputFormat,
      String pmdReportPath) throws InvalidInputException {

    if (!Pattern.matches(".*\\.xml", params.getRuleSet())) {
      throw new InvalidInputException("ruleset required by pmd must be an xml file, input given ",
          params.getRuleSet());
    }

    final StringJoiner pmdCommand = new StringJoiner(" ");
    pmdCommand.add("pmd -d");
    pmdCommand.add(srcPath);
    pmdCommand.add("-f");
    pmdCommand.add(outputFormat);
    pmdCommand.add("-R");
    pmdCommand.add(params.getRuleSet());
    pmdCommand.add(">");
    pmdCommand.add(pmdReportPath);

    final List<String> command = new ArrayList<String>();
    command.add("cmd");
    command.add("/c");
    command.add(pmdCommand.toString());
    return command;
  }

  public int getNumberOfViolations(String pmdreportpath) {

    final int violations = 0;
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
        doc = builder.parse(pmdreportpath);
      }
    } catch (final SAXException e) {
      logger.error("SAXException occured", e);
    } catch (final IOException e) {
      logger.error("File not found exception occured", e);
    }

    if (doc != null && doc.getElementsByTagName("violation") != null) {
      final NodeList violationList = doc.getElementsByTagName("violation");
      return violationList.getLength();
    }
    return 0;
  }


  public ToolResponse<Integer> run(String srcPath) throws InvalidInputException {

    final PMDConfig params = pmdConfigService.getConfig();
    List<String> command;

    try {
      command = getCommand(srcPath, params, PMDConfig.OUTPUT_FORMAT, PMDConfig.PMD_REPORT_PATH);
      processUtility.runProcess(command, PMDConfig.PMD_BIN_PATH);
    } catch (final InvalidInputException e) {
      logger.error("InvalidInputException raised" + e.getMessage());
    }

    final int warnings = getNumberOfViolations(PMDConfig.PMD_REPORT_PATH);
    final int warningsThreshold = thresholdConfService.getThresholds().getNoOfWarnings();
    final String decision =
        ThresholdComparison.isLessThanThreshold(warnings, warningsThreshold) ? "Go" : "No Go";

    final ToolResponse<Integer> res = new ToolResponse<Integer>(warnings, warningsThreshold, decision);
    return new ToolResponse<Integer>(warnings, warningsThreshold, decision);
  }

}
