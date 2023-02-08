package com.ishlaw.crudservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ishlaw.crudservice.entity.StaffDetails;
import com.ishlaw.crudservice.model.ApiResponse;
import com.ishlaw.crudservice.service.CrudTransactionsService;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {
    @Autowired
    CrudTransactionsService crudTransactionsService;

    @Autowired
    private Environment environment;


    @RequestMapping(value = "/getstaffdetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getStaffDetails() throws JsonProcessingException {
        ApiResponse apiResponse = new ApiResponse();
        crudTransactionsService.getStaffMembers();
        List<StaffDetails> staffDetails = new ArrayList<>();
        staffDetails=crudTransactionsService.getStaffMembers();
        apiResponse.setResponseCode("00");
        apiResponse.setResponseBody(staffDetails);
        apiResponse.setMessage("success");




    return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    }
