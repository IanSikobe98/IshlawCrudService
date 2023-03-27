package com.ishlaw.crudservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ishlaw.crudservice.Utils.IshlawConstants;
import com.ishlaw.crudservice.entity.StaffDetails;
import com.ishlaw.crudservice.model.ApiResponse;
import com.ishlaw.crudservice.service.CrudTransactionsService;
import com.ishlaw.crudservice.service.WebUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {
    @Autowired
    CrudTransactionsService crudTransactionsService;

    @Autowired
    Environment environment;

    @Autowired
    WebUserService webUserService;


    @RequestMapping(value = "/getMembers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMemberDetails(HttpServletRequest request) {
        ApiResponse response = new ApiResponse();
        log.info("POST /getMembers | body :: {}",  request);
        try{
            response = webUserService.getMembers();
        }
        catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode(IshlawConstants.ApiResponseCodes.GENERAL_ERROR.getCode());
            response.setMessage("Error Ocurred Processing Request.Please try again later");
            log.info("Error occurred fetching members token  for payload{}",request);


        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/getMemberById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMember(HttpServletRequest request, @RequestBody HashMap<String,String> requestMap) {
        ApiResponse response = new ApiResponse();
        log.info("POST /getMemberById | body :: {}",  request);
        try{
            response = webUserService.getMemberById(Integer.valueOf(requestMap.getOrDefault("id","")));
        }
        catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode(IshlawConstants.ApiResponseCodes.GENERAL_ERROR.getCode());
            response.setMessage("Error Ocurred Processing Request.Please try again later");
            log.info("Error occurred fetching members for payload{}",requestMap);


        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @RequestMapping(value = "/getRoles", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRoles(HttpServletRequest request) {
        ApiResponse response = new ApiResponse();
        log.info("POST /getRoles | body :: {}",  request);
        try{
            response = webUserService.getRoles();
        }
        catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode(IshlawConstants.ApiResponseCodes.GENERAL_ERROR.getCode());
            response.setMessage("Error Ocurred Processing Request.Please try again later");
            log.info("Error occurred fetching roles  for payload{}",request);


        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
