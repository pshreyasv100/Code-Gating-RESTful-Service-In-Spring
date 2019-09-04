package com.gating.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gating.staticanalysis.service.CyvisService;
import com.gating.staticanalysis.service.JacocoService;
import com.gating.staticanalysis.service.PMDService;
import com.gating.staticanalysis.service.SimianService;
import com.gating.staticanalysis.service.VCGService;
import com.gating.toolconfig.service.ThresholdConfig;
import com.gating.toolconfig.service.ThresholdConfigService;
import com.gating.utility.InvalidInputException;

@Service
public class GatingService {

  @Autowired
  PMDService pmdService;

  @Autowired
  SimianService simianService;

  @Autowired
  VCGService vcgService;

  @Autowired
  CyvisService cyvisService;

  @Autowired
  JacocoService jacocoService;

  @Autowired
  ThresholdConfigService thresholdService;


  private void determineCodeQuality(QualityParameters response, QualityParameters lastRunResults) {

    final ThresholdConfig thresholds = thresholdService.getThresholds();

    // Comparing the current results with previous results
    if (lastRunResults != null && response.getNoOfWarnings() <= lastRunResults.getNoOfWarnings()
        && response.getSecurityIssuesCount() <= lastRunResults.getSecurityIssuesCount()
        && response.getCyclomaticComplexity() <= lastRunResults.getCyclomaticComplexity()) {
      response.setComparedToPreviousRun("Better than previous result");
    } else {
      response.setComparedToPreviousRun("Worse than previous result");
    }

    // Comparing the current results with thresholds
    if (response.getNoOfWarnings() <= thresholds.getNoOfWarnings() && response.isCodeDuplication()
        && response.getSecurityIssuesCount() <= thresholds.getSecurityIssuesCount()
        && response.getCyclomaticComplexity() <= thresholds.getCyclomaticComplexity()
        && response.getCodeCoverage() >= thresholds.getCodeCoverage()) {
      response.setFinalDecision("Go");
    } else {
      response.setFinalDecision("No Go");
    }
  }


  private void saveResults(QualityParameters response, String resultsLogPath) {

    try {
      final BufferedWriter resultsCsvWriter = new BufferedWriter(
          new OutputStreamWriter(new FileOutputStream(resultsLogPath, true), "UTF-8"));

      final String CSV_SEPARATOR = ",";
      final StringBuilder responseLine = new StringBuilder();

      responseLine.append(response.getTimeToRunTests());
      responseLine.append(CSV_SEPARATOR);
      responseLine.append(response.getNoOfWarnings());
      responseLine.append(CSV_SEPARATOR);
      responseLine.append(response.getCodeCoverage());
      responseLine.append(CSV_SEPARATOR);
      responseLine.append(response.getCyclomaticComplexity());
      responseLine.append(CSV_SEPARATOR);
      responseLine.append(response.getSecurityIssuesCount());
      responseLine.append(CSV_SEPARATOR);
      responseLine.append(response.isCodeDuplication());
      responseLine.append(CSV_SEPARATOR);
      responseLine.append(response.getFinalDecision());
      responseLine.append(CSV_SEPARATOR);
      responseLine.append(response.getComparedToPreviousRun());


      resultsCsvWriter.newLine();
      resultsCsvWriter.write(responseLine.toString());
      resultsCsvWriter.close();
    } catch (final UnsupportedEncodingException e) {
    } catch (final FileNotFoundException e) {
    } catch (final IOException e) {
    }

  }


  private QualityParameters getLastRunResults() throws IOException {

    String currentRow;
    String lastRow = null;
    QualityParameters lastResult = null;
    final BufferedReader reader = new BufferedReader(new FileReader("resultsLog.csv"));
    currentRow = reader.readLine();

    while ((currentRow = reader.readLine()) != null) {
      lastRow = currentRow;
    }

    reader.close();
    if (lastRow != null) {
      final String[] lastRowArray = lastRow.split(",");
      lastResult = new QualityParameters();
      lastResult.setTimeToRunTests(Integer.valueOf(lastRowArray[0]));
      lastResult.setNoOfWarnings(Integer.valueOf(lastRowArray[1]));
      lastResult.setCodeCoverage(Float.valueOf(lastRowArray[2]));
      lastResult.setCyclomaticComplexity(Integer.valueOf(lastRowArray[3]));
      lastResult.setSecurityIssuesCount(Integer.valueOf(lastRowArray[4]));
      lastResult.setCodeDuplication(Boolean.valueOf(lastRowArray[5]));
    }
    return lastResult;
  }


  public QualityParameters gateCode(String srcPath)
      throws IOException, InterruptedException, InvalidInputException {

    final QualityParameters response = new QualityParameters();
    final QualityParameters lastRunResults = getLastRunResults();

    response.setNoOfWarnings(pmdService.run(srcPath).getValue());
    response.setCodeDuplication(simianService.run(srcPath).getValue() == 0);
    response.setSecurityIssuesCount(vcgService.run(srcPath).getValue());
    response.setCyclomaticComplexity(cyvisService.run(srcPath).getValue());

    if (!(new File(srcPath + "/target/test-classes")).exists()) {
      throw new InvalidInputException(
          "Cannot run jacoco since project does not contain testcase classes,", null);
    } else {
      response.setCodeCoverage(jacocoService.run(srcPath).getValue());
    }


    determineCodeQuality(response, lastRunResults);
    saveResults(response, "resultsLog.csv");
    return response;
  }



}
