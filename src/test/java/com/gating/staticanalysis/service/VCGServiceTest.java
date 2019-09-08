package com.gating.staticanalysis.service;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.gating.Application;
import com.gating.toolconfig.service.ToolResponse;
import com.gating.utility.InvalidInputException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class VCGServiceTest {

  @Autowired
  VCGService vcgService;

  @Test(expected = InvalidInputException.class)
  public void GetIssuesCount_throwsExceptionInputReportIsNotXML() throws InvalidInputException {
    final int expected = 23;
    final int actual = vcgService
        .getIssuesCountFromXML(System.getProperty("user.dir") + "\\reports\\vcg_reports.txt");
    assertEquals(expected, actual);
  }

  @Test
  public void testRun() throws InvalidInputException, IOException, InterruptedException {
    final String SourceCodePath = "C:\\bootcamp\\java\\code\\stack";
    final ToolResponse<Integer> actual = vcgService.run(SourceCodePath);
    final ToolResponse<Integer> expected = new ToolResponse<Integer>(23, 0, "No Go");
    assertEquals(expected.getValue(), actual.getValue());
  }

}
