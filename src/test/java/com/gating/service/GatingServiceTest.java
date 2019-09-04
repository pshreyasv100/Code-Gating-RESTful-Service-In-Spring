package com.gating.service;

import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.gating.Application;
import com.gating.staticanalysis.service.CyvisService;
import com.gating.staticanalysis.service.JacocoService;
import com.gating.staticanalysis.service.PMDService;
import com.gating.staticanalysis.service.SimianService;
import com.gating.staticanalysis.service.VCGService;
import com.gating.toolconfig.service.ThresholdConfigService;
import com.gating.utility.InvalidInputException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class GatingServiceTest {

  @Autowired
  GatingService gatingService;

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



  @Test(expected = InvalidInputException.class)
  public void JacocoDoesNotRun_WhenProjectDoesNotContainTestCases() throws IOException, InterruptedException, InvalidInputException {
    final String srcPath = "C:\\bootcamp\\java\\code\\stack";
    gatingService.gateCode(srcPath);
  }


  @Test
  public void testGateCode() throws IOException, InterruptedException, InvalidInputException {

    final String SourceCodePath = "C:\\Users\\320052310\\Desktop\\Test1";
    gatingService.gateCode(SourceCodePath);
    assert(true);
  }


}
