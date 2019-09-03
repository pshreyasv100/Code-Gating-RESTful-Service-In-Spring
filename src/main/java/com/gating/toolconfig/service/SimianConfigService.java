package com.gating.toolconfig.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.springframework.stereotype.Service;

@Service
public class SimianConfigService {

  public SimianConfig getConfig() {

    FileInputStream fileInput = null;
    try {

      fileInput = new FileInputStream(new File("src/main/resources/simian.config.properties"));
      final Properties prop = new Properties();
      prop.load(fileInput);
      final SimianConfig simianConfig = new SimianConfig();
      simianConfig
      .setDuplicateLinesThreshold(Integer.valueOf(prop.getProperty("duplicateLinesThreshold")));

      return simianConfig;
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



  public void setConfig(SimianConfig simianConfig) {

    try {
      final Properties properties = new Properties();
      properties.setProperty("duplicateLinesThreshold",
          String.valueOf(simianConfig.getDuplicateLinesThreshold()));

      final FileOutputStream fileOut =
          new FileOutputStream(new File("src/main/resources/simian.config.properties"));
      properties.store(fileOut, null);
      fileOut.close();

    } catch (final FileNotFoundException e) {
      e.printStackTrace();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }
}


