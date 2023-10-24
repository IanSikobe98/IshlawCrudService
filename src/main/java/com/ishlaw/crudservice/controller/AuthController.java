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

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    @Autowired
    CrudTransactionsService crudTransactionsService;

    @Autowired
    private Environment environment;

    @Autowired
    WebUserService webUserService;


    @RequestMapping(value = "/authenticate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authentiate(@RequestBody HashMap<String,String> requestMap){
        ApiResponse response = new ApiResponse();
        log.info("POST /authenticate | body :: {}",  requestMap);
        try{
            response = webUserService.authenticateUser(requestMap);
        }
        catch (Exception e) {
            log.info(e.getMessage());
            response.setResponseCode(IshlawConstants.ApiResponseCodes.GENERAL_ERROR.getCode());
            response.setMessage("Error Ocurred Processing Request.Please try again later");


        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resetPassword(@RequestBody HashMap<String,String> requestMap,HttpServletRequest request){
        ApiResponse response = new ApiResponse();
        log.info("POST /resetPassword| body :: {}",  requestMap);
        try{
            response = webUserService.resetPassword(requestMap);
        }
        catch (Exception e) {
            log.info(e.getMessage());
            response.setResponseCode(IshlawConstants.ApiResponseCodes.GENERAL_ERROR.getCode());
            response.setMessage("Error Ocurred Processing Request.Please try again later");


        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody HashMap<String,String> requestMap){
        ApiResponse response = new ApiResponse();
        log.info("POST /createUser | body :: {}",  requestMap);
        try{
            response = webUserService.createUser(requestMap);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode(IshlawConstants.ApiResponseCodes.GENERAL_ERROR.getCode());
            response.setMessage("Error Ocurred Processing Request.Please try again later");


        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/getUserDetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserDetails( HttpServletRequest request){
        ApiResponse response = new ApiResponse();
        log.info("POST /getUserDetails | body :: {}",  request.getHeader("Ulinzi"));
        try{
            String requestTokenHeader = request.getHeader("Ulinzi");
            response = webUserService.decodeToken(requestTokenHeader.substring(7));
        }
        catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode(IshlawConstants.ApiResponseCodes.GENERAL_ERROR.getCode());
            response.setMessage("Error Ocurred Processing Request.Please try again later");
            log.info("Error occurred decodeing token  for payload{}",request);


        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
