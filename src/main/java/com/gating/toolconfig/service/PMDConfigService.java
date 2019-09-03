package com.gating.toolconfig.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.springframework.stereotype.Service;

@Service
public class PMDConfigService {

  public PMDConfig getConfig() {

    FileInputStream fileInput = null;
    try{

      fileInput = new FileInputStream(new File("src/main/resources/pmd.config.properties"));
      final Properties prop = new Properties();
      prop.load(fileInput);
      final PMDConfig pmdConfig = new PMDConfig();

      pmdConfig.setRuleSet(String.valueOf(prop.getProperty("ruleSet")));

      return pmdConfig;

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



  public void setConfig(PMDConfig pmdConfig) {

    try {
      final Properties properties = new Properties();
      properties.setProperty("ruleSet", String.valueOf(pmdConfig.getRuleSet()));

      final FileOutputStream fileOut =
          new FileOutputStream(new File("src/main/resources/pmd.config.properties"));
      properties.store(fileOut, null);
      fileOut.close();

    } catch (final FileNotFoundException e) {
      e.printStackTrace();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }
}
