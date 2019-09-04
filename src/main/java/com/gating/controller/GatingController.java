package com.gating.controller;

import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.gating.service.GatingService;
import com.gating.service.QualityParameters;
import com.gating.staticanalysis.service.CyvisService;
import com.gating.staticanalysis.service.JacocoService;
import com.gating.staticanalysis.service.PMDService;
import com.gating.staticanalysis.service.SimianService;
import com.gating.staticanalysis.service.VCGService;
import com.gating.toolconfig.service.PMDConfig;
import com.gating.toolconfig.service.PMDConfigService;
import com.gating.toolconfig.service.SimianConfig;
import com.gating.toolconfig.service.SimianConfigService;
import com.gating.toolconfig.service.ThresholdConfig;
import com.gating.toolconfig.service.ThresholdConfigService;
import com.gating.toolconfig.service.ToolResponse;
import com.gating.utility.InvalidInputException;

@RestController
@RequestMapping(path = "/gating")
public class GatingController {

  @Autowired
  GatingService gatingService;

  @Autowired
  PMDService pmdService;

  @Autowired
  SimianService simianService;

  @Autowired
  CyvisService cyvisService;

  @Autowired
  VCGService vcgService;

  @Autowired
  JacocoService jacocoService;

  @Autowired
  ThresholdConfigService thresholdConfigService;

  @Autowired
  PMDConfigService pmdConfigService;

  @Autowired
  SimianConfigService simianConfigService;


  private void validateSourceCodePath(String src) throws InvalidInputException {

    final File sourcePath = new File(src);
    if(!(sourcePath.isDirectory() && sourcePath.exists())) {
      throw new InvalidInputException("InvalidInputException raised, source code project does not exist", src);
    }


  }

  @GetMapping(path = "/allservices")
  public QualityParameters allServices(@RequestParam String sourceCodePath)
      throws IOException, InterruptedException, InvalidInputException {

    validateSourceCodePath(sourceCodePath);
    return gatingService.gateCode(sourceCodePath);
  }

  @GetMapping(path = "/pmdservice")
  public ToolResponse<Integer> pmdRequestHandler(@RequestParam String sourceCodePath) throws InvalidInputException {

    validateSourceCodePath(sourceCodePath);
    return pmdService.run(sourceCodePath);
  }

  @GetMapping(path = "/simianservice")
  public ToolResponse<Integer> simianRequestHandler(@RequestParam String sourceCodePath) throws InvalidInputException {
    validateSourceCodePath(sourceCodePath);
    return simianService.run(sourceCodePath);
  }

  @GetMapping(path = "/cyvisservice")
  public ToolResponse<Integer> cyvisRequestHandler(@RequestParam String sourceCodePath) throws InvalidInputException {
    validateSourceCodePath(sourceCodePath);
    return cyvisService.run(sourceCodePath);
  }

  @GetMapping(path = "/vcgservice")
  public ToolResponse<Integer> vcgRequestHandler(@RequestParam String sourceCodePath) throws InvalidInputException {
    validateSourceCodePath(sourceCodePath);
    return vcgService.run(sourceCodePath);
  }

  @GetMapping(path = "/jacocoservice")
  public ToolResponse<Float> jacocoRequestHandler(@RequestParam String sourceCodePath)
      throws IOException, InterruptedException, InvalidInputException {

    validateSourceCodePath(sourceCodePath);
    return jacocoService.run(sourceCodePath);
  }

  @GetMapping(path = "/thresholds/config")
  public ThresholdConfig getThresholds() {
    return thresholdConfigService.getThresholds();
  }

  @PostMapping(path = "/thresholds/config/new")
  public void setThresholds(@RequestBody ThresholdConfig newThresholds) {
    thresholdConfigService.setThresholds(newThresholds);
  }

  @PostMapping(path = "/pmd/config/new")
  public void setPmdConfig(@RequestBody PMDConfig newConfig) {
    pmdConfigService.setConfig(newConfig);
  }

  @GetMapping(path= "/pmd/config")
  public PMDConfig getPmdConfig() {
    return pmdConfigService.getConfig();
  }


  @PostMapping(path = "/simian/config/new")
  public void setSimianConfig(@RequestBody SimianConfig newConfig) {
    simianConfigService.setConfig(newConfig);
  }

  @GetMapping(path= "/simian/config")
  public SimianConfig getSimianConfig() {
    return simianConfigService.getConfig();
  }


}

