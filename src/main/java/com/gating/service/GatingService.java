package com.gating.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gating.controller.GatingInput;
import com.gating.staticanalysis.service.CyvisService;
import com.gating.staticanalysis.service.PMDService;
import com.gating.staticanalysis.service.SimianService;
import com.gating.staticanalysis.service.VCGService;
import com.gating.thresholdconfig.service.ThresholdConfiguration;
import com.gating.thresholdconfig.service.ThresholdConfigurationService;

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
  ThresholdConfigurationService thresholdService;


  private void determineCodeQuality(QualityParameters response, QualityParameters lastRunResults) {

    final ThresholdConfiguration thresholds = thresholdService.getThresholds();
    if (lastRunResults != null && response.getNoOfWarnings() < lastRunResults.getNoOfWarnings()
        && lastRunResults.getSecurityIssuesCount() < lastRunResults.getSecurityIssuesCount()) {
      response.setFinalDecision("Better than previous result");
    } else {
      response.setFinalDecision("Worse than previous result");
    }


    if (response.getNoOfWarnings() <= thresholds.getNoOfWarnings() && response.isCodeDuplication()
        && response.getSecurityIssuesCount() <= thresholds.getSecurityIssuesCount()) {
      response.setFinalDecision(response.getFinalDecision() + " : Go");
    } else {
      response.setFinalDecision(response.getFinalDecision() + " : No Go");
    }

  }


  private void saveResults(QualityParameters response) {

    try {
      final BufferedWriter resultsCsvWriter = new BufferedWriter(
          new OutputStreamWriter(new FileOutputStream("resultsLog.csv", true), "UTF-8"));

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
    // reading header and moving to next line
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


  public QualityParameters gateCode(GatingInput gatingContext)
      throws IOException, InterruptedException {

    final QualityParameters response = new QualityParameters();
    final QualityParameters lastRunResults = getLastRunResults();

    response.setNoOfWarnings(pmdService.run(gatingContext.getPmdParameters()));
    response.setCodeDuplication(simianService.run(gatingContext.getSimianParameters()) == 0);
    response.setSecurityIssuesCount(vcgService.run(gatingContext.getVcgParameters()));
    // response.setCyclomaticComplexity(cyvisService.run(gatingContext.getCyvisParameters()));

    saveResults(response);
    determineCodeQuality(response, lastRunResults);
    return response;
  }




}
