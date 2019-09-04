package com.gating.staticanalysis.service;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.util.List;
import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.gating.Application;
import com.gating.toolconfig.service.PMDConfig;
import com.gating.toolconfig.service.PMDConfigService;
import com.gating.toolconfig.service.ThresholdConfigService;
import com.gating.toolconfig.service.ToolResponse;
import com.gating.utility.InvalidInputException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class PMDServiceTest {

  @Autowired
  PMDService pmd;

  @Autowired
  PMDConfigService pmdConfigService;

  @Autowired
  ThresholdConfigService thresholdConfigService;

  String PMD_REPORT_PATH;
  String OUTPUT_FORMAT;
  String SourceCodePath;
  PMDConfig pmdConfig;

  @Before()
  public void setUp() {

    PMD_REPORT_PATH = "reports/pmd_report.xml";
    OUTPUT_FORMAT = "xml";
    SourceCodePath = "C:\\eclipse-workspace\\demoo";
    pmdConfig = new PMDConfig();
    pmdConfig.setRuleSet("rulesets/java/quickstart.xml");
  }


  @Test
  public void testGetCommandForCorrectnessOfCommand() throws InvalidInputException {

    final String expectedPMDCommand = "pmd " + "-d " + SourceCodePath + " -f " + "xml " + "-R "
        + "rulesets/java/quickstart.xml " + "> " + "reports/pmd_report.xml";
    final String[] commandArray = {"cmd", "/c", expectedPMDCommand};

    final List<Object> expected = Arrays.asList(commandArray);
    final List<String> actual =
        pmd.getCommand(SourceCodePath, pmdConfig, OUTPUT_FORMAT, PMD_REPORT_PATH);
    assertEquals(actual, expected);
  }


  @Test(expected = InvalidInputException.class)
  public void testGetCommandWhenGivenInvalidRulesetFile() throws InvalidInputException {
    pmdConfig.setRuleSet("invalidruleset.txt");
    pmd.getCommand(SourceCodePath, pmdConfig, OUTPUT_FORMAT, PMD_REPORT_PATH);
  }


  @Test
  public void testRun() throws InvalidInputException, IOException, InterruptedException {
    final ToolResponse<Integer> actual = pmd.run("C:\\eclipse-workspace\\demoo");
    final ToolResponse<Integer> expected = new ToolResponse<Integer>(1, 1, "No Go");
    assertEquals(expected.getValue(), actual.getValue());
  }

}
