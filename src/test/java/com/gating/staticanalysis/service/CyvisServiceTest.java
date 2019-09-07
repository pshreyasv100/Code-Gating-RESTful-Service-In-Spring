package com.gating.staticanalysis.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.gating.Application;
import com.gating.toolconfig.service.ToolResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class CyvisServiceTest {

  @Autowired
  CyvisService cyvisService;

  @Test
  public void testGetMaxComplexity() {

    final Map<String, Integer> dummyMap = new HashMap<String, Integer>();
    dummyMap.put("method1", 2);
    dummyMap.put("method2", 1);

    final int actual = cyvisService.getMaxComplexity(dummyMap);
    final int expected = 2;
    assertEquals(expected, actual);
  }
  

  @Test
  public void testRun() throws IOException, InterruptedException {

    final String SourceCodePath = "C:\\bootcamp\\java\\code\\stack";
    final ToolResponse<Integer> actual = cyvisService.run(SourceCodePath);
    final ToolResponse<Integer> expected = new ToolResponse<Integer>(3, 5, "Go");
    cyvisService.run(SourceCodePath);
    assertEquals(expected.getValue(), actual.getValue());

  }

}
