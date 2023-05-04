package com.junodx.api.services.transformer;

import com.junodx.api.models.auth.User;

public interface PatientTransformer {
  void transform(User patient, String value);
}
