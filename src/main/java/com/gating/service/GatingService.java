package com.gating.service;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gating.controller.GatingContext;
import com.gating.staticanalysis.service.PMDService;
import com.gating.staticanalysis.service.SimianService;
import com.gating.thresholdconfig.service.ThresholdConfigurationService;

@Service
public class GatingService {

  @Autowired
  PMDService pmdService;

  @Autowired
  ThresholdConfigurationService thresholdService;

  @Autowired
  SimianService simianService;


  public String gateCode(GatingContext gatingContext) throws IOException, InterruptedException {

    if ((pmdService.run(gatingContext.getPmdParameters())) <= (thresholdService.getThresholds().getNoOfWarnings())){
      if (simianService.run(gatingContext.getSimianParameters()) == 0) {
        return "go";
      }
    }

    return "no go";
  }


}
