package com.junodx.api.services.transformer;

//import com.junodx.api.connectors.lims.dto.LimsKitDto;
import com.junodx.api.connectors.lims.elements.client.ElementsClient;
import com.junodx.api.connectors.lims.elements.entities.CollectionType;
import com.junodx.api.connectors.lims.elements.entities.ElementsClinic;
import com.junodx.api.connectors.lims.elements.entities.ElementsKit;
import com.junodx.api.connectors.lims.elements.entities.ElementsLocation;
import com.junodx.api.connectors.lims.elements.entities.ElementsOrder;
import com.junodx.api.connectors.lims.elements.entities.ElementsPatient;
import com.junodx.api.connectors.lims.elements.entities.ElementsPractitioner;
import com.junodx.api.connectors.lims.elements.entities.ElementsReport;
import com.junodx.api.models.auth.User;
import com.opencsv.CSVReader;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class PatientCsvTransformerService {

  @Autowired
  ElementsClient elementsClient;

  private static final Logger logger = LoggerFactory.getLogger(PatientCsvTransformerService.class);

  public void importPatientsFromCsv(File csvFile) {
    logger.info("importing patients from csv file");
    try (CSVReader csvReader = new CSVReader(new FileReader(csvFile));) {
      String[] headers = csvReader.readNext();
      logger.info("Found csv headers {} ", Arrays.toString(headers));
      List<PatientCsvHeaderColumn> headerColumns = new ArrayList<>();
      for (String header : headers) {
        logger.info("header is : " + header);
        PatientCsvHeaderColumn column = PatientCsvHeaderColumn.fromTitle(header);
        if (Objects.nonNull(column)) {
          headerColumns.add(column);
        }
      }
      logger.info("Matched header columns: {} ", headerColumns);
      String[] columnValues = null;
      List<User> patients = new ArrayList<>();
      int count = 1;
      while ((columnValues = csvReader.readNext()) != null) {
        logger.info("Found values {} ", Arrays.toString(columnValues));
        User patient = new User();
        for (int i=0; i<headerColumns.size(); i++) {
          headerColumns.get(i).transform(patient, columnValues[i]);
        }
        createOrder(patient, count++, columnValues[1]);
        //patients.add(patient);
      }
      logger.info("Imported {} patients from csv file.", patients.size());

      /*
      for (User usr : patients) {

      }

       */
    } catch (Exception e) {
      logger.error("Error while processing to read csv file");
      e.printStackTrace();
    }
  }

  private void createOrder(User user, int rowNumber, String sampleId) {
    logger.info("creating order");
    ElementsOrder order = new ElementsOrder();
    ElementsClinic clinic = new ElementsClinic();
    clinic.setName("Test Clinic3");
    clinic.setHostCode("testertest3");
    order.setClinic(clinic);

    ElementsLocation location = new ElementsLocation();
    location.setAddressOne("1140 Broadway");
    location.setAddressTwo("11th Floor");
    location.setCity("New York");
    location.setCountry("NY");
    location.setZip("10001");
    location.setCountry("US");
    //order.setClinicLocation(location);
    order.setPatientLocation(location);

    ElementsPractitioner practitioner = new ElementsPractitioner();
    practitioner.setFirstName("Doctor");
    practitioner.setLastName("Phosphorus");
    practitioner.setNpi("1234567890");
    practitioner.setPhone("1234567890");
    practitioner.setEmail("doctor@phosphorus.com");
    practitioner.setExternalId("client-practitioner-identifier2");
    order.setPractitioner(practitioner);

    ElementsPatient patient = new ElementsPatient();
    patient.setFirstName(user.getFirstName());
    patient.setLastName(user.getLastName());
    //patient.setBirthDate("2000-01-01");
    patient.setDeceased(false);
    patient.setGender("female");
    patient.setEthnicities(new String[]{"East Asian","Mediterranean"});
    patient.setEmail(user.getEmail());
    patient.setPhone("0123456789");
    patient.setExternalId(user.getId());
    order.setPatient(patient);

    ElementsKit kit = new ElementsKit();
    String extId = RandomStringUtils.randomAlphanumeric(10)+
            StringUtils.leftPad(String.valueOf(rowNumber),4, "0");
    String kitId = sampleId;
    kit.setExternalId(kitId);
    kit.setBarcode(kitId);
    kit.setExternalBarcode(kitId);
    kit.setCreateShipment(false);
    kit.setCollectionType(CollectionType.Blood);
    order.setKit(kit);

    ElementsReport report = new ElementsReport();
    report.setExternalId("VALRPT"+ extId);
    report.setReportTypeId("304");
    order.setReport(report);

    List<String> consents = new ArrayList<>();
    consents.add("general");
    order.setConsents(consents);

    try {
      ElementsOrder response = elementsClient.createOrder(order);
      if(response != null) {
        //save the results to DB as success
      } else {
        //save the results to DB as failed
      }
    } catch (Exception ex) {
      //save the results to DB as failed
    }
  }
}
