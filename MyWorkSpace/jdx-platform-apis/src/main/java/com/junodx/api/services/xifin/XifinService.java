package com.junodx.api.services.xifin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.junodx.api.connectors.http.HttpClient;
import com.junodx.api.controllers.salesforce.SalesforceController;
import com.junodx.api.controllers.xifin.payloads.accession.CreateAccessionPayload;
import com.junodx.api.controllers.xifin.payloads.TokenResponse;
import com.junodx.api.services.exceptions.JdxServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class XifinService {


  @Value("${xifin.api.username}")
  private String xifinUsername;
  @Value("${xifin.api.passcode}")
  private String xifinPasscode;
  @Value("${xifin.api.orgalias}")
  private String xifinOrgalias;

  @Value("${xifin.api.baseUrlProd}")
  private String baseUrlXifin;

  @Value("${xifin.api.accessionVersion}")
  private String accessionAPIVersion;
  @Value("${xifin.api.token}")
  private String xifinToken;

  private ObjectMapper mapper;

  public XifinService() {
    mapper = new ObjectMapper();
  }

  private static final Logger logger = LoggerFactory.getLogger(XifinService.class);
  public String createAccession(CreateAccessionPayload payload) throws JsonProcessingException {
    try {
      logger.info("the payload for Create Accession ::" + mapper.writeValueAsString(payload));
      this.xifinToken = refreshXifinToken().replace("Bearer ", "");
      logger.info("the token for Create Accession ::" + this.xifinToken);
      HttpClient client = new HttpClient();
      HttpHeaders requestHeader = new HttpHeaders();
      requestHeader.setContentType(MediaType.APPLICATION_JSON);
      client.setRequestHeaders(requestHeader);
      client.setBearerAuthToken(this.xifinToken);
      client.setUrl(baseUrlXifin + "/" + accessionAPIVersion + "/restful/createAccession?orgalias=" + xifinOrgalias + "&username=" + xifinUsername);
      logger.info("payload is ::" + mapper.writeValueAsString(payload));
      ResponseEntity<String> response = client.post(mapper.writeValueAsString(payload));
      if (response.getStatusCode() == HttpStatus.OK) {
        if (response.getBody() == null)
          throw new JdxServiceException("Did not receive a positive confirmation for accession creation back from Xifin");
        else {
          logger.info("responseBody is ::" + response.getBody());
          return response.getBody();
        }
      }
    } catch (Exception e) {
      throw new JdxServiceException("Did not receive a positive confirmation for accession creation back from Xifin " + e.getMessage());
    }
    return null;
  }

  private String refreshXifinToken() throws JdxServiceException {
    try {
      HttpClient client = new HttpClient();
      HttpHeaders requestHeader = new HttpHeaders();
      requestHeader.setContentType(MediaType.APPLICATION_JSON);
      client.setRequestHeaders(requestHeader);
      Map<String, String> postmap = new HashMap<>();
      postmap.put("orgalias", xifinOrgalias);
      postmap.put("password", xifinPasscode);
      postmap.put("username", xifinUsername);
      Gson json = new Gson();
      String postBody = json.toJson(postmap);
      client.setRequestHeaders(requestHeader);
      client.setUrl(baseUrlXifin + "/" + accessionAPIVersion + "/generatetoken");
      ResponseEntity<String> response = client.post(postBody);
      if (response.getStatusCode() == HttpStatus.OK) {
        if (response.getBody() == null)
          throw new JdxServiceException("Did not receive a positive confirmation for accession creation back from Xifin");
        else {
          logger.info("responseBody is ::" + response.getBody());
          TokenResponse tokenResponse = mapper.readValue(response.getBody(), TokenResponse.class);
          return tokenResponse.getToken();
        }
      }
    } catch (Exception e) {
      throw new JdxServiceException("Did not receive a positive confirmation for accession creation back from Xifin " + e.getMessage());
    }
    return null;
  }

}
