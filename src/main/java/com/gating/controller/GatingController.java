package com.gating.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.gating.codecoverage.service.JacocoParameters;
import com.gating.codecoverage.service.JacocoService;
import com.gating.service.GatingService;
import com.gating.service.QualityParameters;
import com.gating.staticanalysis.service.CyvisParameters;
import com.gating.staticanalysis.service.CyvisService;
import com.gating.staticanalysis.service.PMDParameters;
import com.gating.staticanalysis.service.PMDService;
import com.gating.staticanalysis.service.SimianParameters;
import com.gating.staticanalysis.service.SimianService;
import com.gating.staticanalysis.service.VCGParameters;
import com.gating.staticanalysis.service.VCGService;
import com.gating.thresholdconfig.service.ThresholdConfig;
import com.gating.thresholdconfig.service.ThresholdConfigService;

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
  ThresholdConfigService thresholdConfService;

  @GetMapping(path = "/allservices", consumes = {"application/json"})
  public QualityParameters allServices(@RequestBody GatingInput gatingContext)
      throws IOException, InterruptedException {

    return gatingService.gateCode(gatingContext);
  }

  @GetMapping(path = "/pmdservice")
  public String pmdRequestHandler(@RequestParam String sourceCodePath, @RequestParam String ruleSet) {

    final PMDParameters params = new PMDParameters();
    params.setRuleSet(ruleSet);

    final int warningsCount = pmdService.run(sourceCodePath, params);
    final int warningsThreshold = thresholdConfService.getThresholds().getNoOfWarnings();
    final String resultString = "Warnings found = " + warningsCount + "(" + warningsThreshold + ")";
    if (warningsThreshold < warningsCount) {
      return "PMD Service : no go :" + resultString;
    }
    return "PMD Service : go : " + resultString;
  }


  @GetMapping(path = "/simianservice")
  public String simianRequestHandler(@RequestParam String sourceCodePath,
      @RequestParam int duplicationThreshold) {


    final SimianParameters params = new SimianParameters();
    params.setDuplicateLinesThreshold(duplicationThreshold);

    final int simianExitStatus = simianService.run(sourceCodePath, params);

    if (simianExitStatus == 0) {
      return "Simian Service : Go " + simianExitStatus;
    }
    return "Simian Service : No go : Code Duplication present " + simianExitStatus;
  }


  @GetMapping(path = "/cyvisservice")
  public String cyvisRequestHandler(@RequestParam String sourceCodePath) {

    final CyvisParameters params = new CyvisParameters();

    final int cyclomaticComplexity = cyvisService.run(sourceCodePath, params);
    final int thresholdComplexity = thresholdConfService.getThresholds().getCyclomaticComplexity();

    final String resultString = "Max CyclomaticComplexity found = " + cyclomaticComplexity + "("
        + thresholdComplexity + ")";

    if (cyclomaticComplexity > thresholdComplexity) {
      return "CYVIS Service : No go :" + resultString;
    }
    return "CYVIS Service : Go :" + resultString;
  }



  @GetMapping(path = "/vcgservice")
  public String vcgRequestHandler(@RequestParam String sourceCodePath) {

    final VCGParameters params = new VCGParameters();

    final int securityIssuesCount = vcgService.run(sourceCodePath, params);
    final int thresholdSecurityIssuesCount =
        thresholdConfService.getThresholds().getSecurityIssuesCount();

    final String resultString = "No of Security issues found = " + securityIssuesCount + "("
        + thresholdSecurityIssuesCount + ")";

    if (securityIssuesCount > thresholdSecurityIssuesCount) {
      return "VCG Service : No go :" + resultString;
    }
    return "VCG Service : Go :" + resultString;
  }



  @GetMapping(path = "/jacocoservice")
  public String jacocoRequestHandler(@RequestParam String sourceCodePath)
      throws IOException, InterruptedException {


    final JacocoParameters params = new JacocoParameters();

    final float codeCoverage = jacocoService.run(sourceCodePath, params);
    final float thresholdCoverage = thresholdConfService.getThresholds().getCodeCoverage();

    final String resultString = "Code Coverage = " + codeCoverage + "(" + thresholdCoverage + ")";

    if (codeCoverage < thresholdCoverage) {
      return "Jacoco Service : No go :" + resultString;
    }
    return "Jacoco Service : Go :" + resultString;
  }


  @GetMapping(path = "/thresholds/config")
  public ThresholdConfig getThresholds() {
    return thresholdConfService.getThresholds();
  }

  @PostMapping(path = "/thresholds/config/new")
  public void setThresholds(@RequestBody ThresholdConfig newThresholds) {
    thresholdConfService.setThresholds(newThresholds);
  }

}

