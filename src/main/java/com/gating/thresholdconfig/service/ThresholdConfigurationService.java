package com.gating.thresholdconfig.service;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.springframework.stereotype.Service;

@SuppressWarnings("restriction")
@Service
public class ThresholdConfigurationService {

  public ThresholdConfiguration getThresholds() {

    final File xmlFile = new File("thresholdconf.xml");
    JAXBContext jaxbContext;
    try
    {
      jaxbContext = JAXBContext.newInstance(ThresholdConfiguration.class);
      final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      return (ThresholdConfiguration) jaxbUnmarshaller.unmarshal(xmlFile);
    }
    catch (final JAXBException e)
    {
      e.printStackTrace();
    }

    return null;
  }

  public void setThresholds(ThresholdConfiguration newThresholds) {

    try
    {
      final JAXBContext jaxbContext = JAXBContext.newInstance(ThresholdConfiguration.class);
      final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      final File file = new File("thresholdconf.xml");
      jaxbMarshaller.marshal(newThresholds, file);
    }
    catch (final JAXBException e)
    {
      e.printStackTrace();
    }
  }


}
