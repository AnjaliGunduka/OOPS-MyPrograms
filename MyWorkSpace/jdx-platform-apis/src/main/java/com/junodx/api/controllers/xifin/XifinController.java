package com.junodx.api.controllers.xifin;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.controllers.payloads.MessageResponse;
import com.junodx.api.controllers.salesforce.SalesforceController;
import com.junodx.api.controllers.xifin.payloads.accession.CreateAccessionPayload;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.exceptions.JdxServiceException;
import com.junodx.api.services.xifin.XifinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("api/xifin")
public class XifinController {

  @Autowired
  private XifinService xifinService;
  private ObjectMapper mapper;

  public XifinController() {
    mapper = new ObjectMapper();
  }

  private static final Logger logger = LoggerFactory.getLogger(SalesforceController.class);

  @PostMapping("/createAccession")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<?> createAccession(@RequestBody CreateAccessionPayload payload) {
    Authentication authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userContext = (UserDetailsImpl) authentication.getPrincipal();
    logger.info("Creating Accession for Xifin...");
    try {
      if (payload != null) {
        try {
          logger.info("trying to create a new Xifin Accession: " + payload);
          return ResponseEntity.ok().body(xifinService.createAccession(payload));
        } catch (JdxServiceException e) {
          return ResponseEntity.badRequest().body(new MessageResponse("Cannot create Accession for Xifin", 400, new Date(), e.getMessage()));
        }
      }
    } catch(Exception e){
      e.printStackTrace();
    }
    return ResponseEntity.badRequest().body(new MessageResponse("Cannot create Xifin Accession", 404, new Date(), "Accession Create object to create was missing or malformed"));
  }
}
