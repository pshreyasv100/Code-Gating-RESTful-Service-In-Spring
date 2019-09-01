package com.gating.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.gating.service.GatingService;
import com.gating.service.QualityParameters;
import com.gating.thresholdconfig.service.ThresholdConfig;
import com.gating.thresholdconfig.service.ThresholdConfigService;

@RestController
public class GatingController {

  @Autowired
  GatingService gatingService;

  @Autowired
  ThresholdConfigService thresholdConfService;

  @GetMapping(path = "/gating/analysis", consumes = {"application/json"})
  public QualityParameters handler(@RequestBody GatingInput gatingContext)
      throws IOException, InterruptedException {

    return gatingService.gateCode(gatingContext);
  }


  @GetMapping(path = "/gating/thresholds/config")
  public ThresholdConfig getThresholds() {
    return thresholdConfService.getThresholds();
  }

  @PostMapping(path = "/gating/thresholds/config/new")
  public void setThresholds(@RequestBody ThresholdConfig newThresholds){
    thresholdConfService.setThresholds(newThresholds);
  }

}

