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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class SimianServiceTest {

  @Autowired
  SimianService simianService;

  @Test
  public void testRun() throws IOException, InterruptedException {

    final String SourceCodePath = "C:\\bootcamp\\java\\code\\stack";
    final ToolResponse<Integer> actual =  simianService.run(SourceCodePath);
    final ToolResponse<Integer> expected = new ToolResponse<Integer>(1, 0, "No Go : Code Duplication Present");

    assertEquals(expected.getValue(), actual.getValue());

  }

}
