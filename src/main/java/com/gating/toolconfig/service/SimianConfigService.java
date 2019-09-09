package com.gating.toolconfig.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.springframework.stereotype.Service;
import com.gating.utility.InvalidInputException;

@Service
public class SimianConfigService {

  public SimianConfig getConfig() throws IOException, InvalidInputException {

    FileInputStream fileInput = null;
    final File propFile = new File("src/main/resources/simian.config.properties");

    if(propFile.exists()) {
      fileInput = new FileInputStream(propFile);
    }
    else {
      throw new InvalidInputException("Server Error : simian config properties file not found", null);
    }

    final Properties prop = new Properties();
    prop.load(fileInput);
    final SimianConfig simianConfig = new SimianConfig();
    simianConfig
    .setDuplicateLinesThreshold(Integer.valueOf(prop.getProperty("duplicateLinesThreshold")));

    fileInput.close();
    return simianConfig;

  }



  public void setConfig(SimianConfig simianConfig) throws IOException {

    final Properties properties = new Properties();
    properties.setProperty("duplicateLinesThreshold",
        String.valueOf(simianConfig.getDuplicateLinesThreshold()));

    final FileOutputStream fileOut =
        new FileOutputStream(new File("src/main/resources/simian.config.properties"));
    properties.store(fileOut, null);
    fileOut.close();

  }
}



