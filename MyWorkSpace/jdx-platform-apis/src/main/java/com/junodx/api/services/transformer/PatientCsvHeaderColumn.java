package com.junodx.api.services.transformer;

import com.junodx.api.models.auth.User;
import org.apache.commons.lang3.StringUtils;

import java.net.UnknownServiceException;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum PatientCsvHeaderColumn implements PatientTransformer {

  VALIDATION_SUBJECT_NAME("ValidationSubjectName") {
    public void transform(User patient, String value) {
      String[] names = StringUtils.split(value," ");
      //String[] names = StringUtils.split("\\s");
      patient.setFirstName(names.length>0 ? names[0] : "");
      patient.setLastName(names.length>1 ? names[1] : "");
      patient.setEmail(names.length>1 ? names[0]+"."+names[1]+"@junodx.com" : names[0]+"@junodx.com");
    }
  },

  SAMPLE_ID("Sample_ID") {
    public void transform(User patient, String value) {
      //do nothing
    }
  },

  REFERENCE_SAMPLE_ID("ReferenceSample_ID") {
    public void transform(User patient, String value) {
      //do nothing
    }
  };

  PatientCsvHeaderColumn(String title) {
    this.title = title;
  }

  public static PatientCsvHeaderColumn fromTitle(String value) {
    return valueMap.get(value);
  }

  private static Map<String, PatientCsvHeaderColumn> valueMap = new HashMap<>();
  private String title;

  public String getTitle() {
    return title;
  }

  static {
    valueMap = Arrays.stream(PatientCsvHeaderColumn.values())
            .collect(Collectors.toMap(PatientCsvHeaderColumn::getTitle, type -> type));
  }


}
