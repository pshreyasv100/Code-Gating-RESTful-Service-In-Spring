package com.gating.toolconfig.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.springframework.stereotype.Service;

@Service
public class VCGConfigService {


  public VCGConfig getConfig() {

    FileInputStream fileInput = null;
    try {

      fileInput = new FileInputStream(new File("src/main/resources/vcg.config.properties"));
      final Properties prop = new Properties();
      prop.load(fileInput);
      final VCGConfig vcgConfig = new VCGConfig();
      vcgConfig.setOutputFormat(String.valueOf(prop.getProperty("outputFormat")));

      return vcgConfig;
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



  public void setConfig(VCGConfig vcgConfig) {

    try {
      final Properties properties = new Properties();
      properties.setProperty("outputFormat", String.valueOf(vcgConfig.getOutputFormat()));

      final FileOutputStream fileOut =
          new FileOutputStream(new File("src/main/resources/vcg.config.properties"));
      properties.store(fileOut, null);
      fileOut.close();

    } catch (final FileNotFoundException e) {
      e.printStackTrace();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

}
