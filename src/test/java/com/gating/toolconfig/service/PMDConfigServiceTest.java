package com.gating.toolconfig.service;

import java.io.IOException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.gating.utility.InvalidInputException;

public class PMDConfigServiceTest {

  @Autowired
  PMDConfigService pmdConfigService;

  @Test(expected = InvalidInputException.class)
  public void getConfigThrowsException_WhenConfigFileNotFound() throws IOException, InvalidInputException {
    pmdConfigService.getConfig();
  }

}
