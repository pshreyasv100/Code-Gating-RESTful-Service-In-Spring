package com.gating.service;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gating.controller.GatingInput;
import com.gating.staticanalysis.service.PMDService;
import com.gating.staticanalysis.service.SimianService;
import com.gating.staticanalysis.service.VCGService;
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
  ThresholdConfigurationService thresholdService;



  public GatingResponse gateCode(GatingInput gatingContext)
      throws IOException, InterruptedException {

    final GatingResponse response = new GatingResponse();

    response.setNoOfWarnings(pmdService.run(gatingContext.getPmdParameters()));
    response.setCodeDuplication(simianService.run(gatingContext.getSimianParameters()) == 0);
    response.setSecurityIssuesCount(vcgService.run(gatingContext.getVcgParameters()));


    if (response.getNoOfWarnings() <= thresholdService.getThresholds().getNoOfWarnings()
        && response.isCodeDuplication() && response.getSecurityIssuesCount() <= thresholdService
        .getThresholds().getSecurityIssuesCount()) {

      response.setFinalDecision("Go");
    }

    response.setFinalDecision("No Go");
    return response;

  }


}
