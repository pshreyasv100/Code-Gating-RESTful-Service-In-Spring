package com.gating.thresholdconfig.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.springframework.stereotype.Service;

@Service
public class ThresholdConfigService {

  public ThresholdConfig getThresholds() {

    FileInputStream fileInput = null;
    try{

      fileInput = new FileInputStream(new File("src/main/resources/thresholdconfig.properties"));
      final Properties prop = new Properties();
      prop.load(fileInput);
      final ThresholdConfig thresholdConfig = new ThresholdConfig();

      thresholdConfig.setCyclomaticComplexity(Integer.valueOf(prop.getProperty("cyclomaticComplexity")));
      thresholdConfig.setCodeCoverage(Float.valueOf(prop.getProperty("codeCoverage")));
      thresholdConfig.setTimeToRunTests(Integer.valueOf(prop.getProperty("timeToRunTests")));
      thresholdConfig.setNoOfWarnings(Integer.valueOf(prop.getProperty("noOfWarnings")));
      thresholdConfig.setSecurityIssuesCount(Integer.valueOf(prop.getProperty("securityIssuesCount")));

      return thresholdConfig;

    } catch (final IOException ex) {
      ex.printStackTrace();
    }

    finally {
      try {
        fileInput.close();
      } catch (final IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }



  public void setThresholds(ThresholdConfig newThresholds) {

    try {
      final Properties properties = new Properties();
      properties.setProperty("cyclomaticComplexity",String.valueOf(newThresholds.getCyclomaticComplexity()));
      properties.setProperty("codeCoverage", String.valueOf(newThresholds.getCodeCoverage()));
      properties.setProperty("timeToRunTests", String.valueOf(newThresholds.getTimeToRunTests()));
      properties.setProperty("noOfWarnings", String.valueOf(newThresholds.getNoOfWarnings()));
      properties.setProperty("securityIssuesCount", String.valueOf(newThresholds.getSecurityIssuesCount()));

      final FileOutputStream fileOut =
          new FileOutputStream(new File("src/main/resources/thresholdconfig.properties"));
      properties.store(fileOut, null);
      fileOut.close();

    } catch (final FileNotFoundException e) {
      e.printStackTrace();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

}
