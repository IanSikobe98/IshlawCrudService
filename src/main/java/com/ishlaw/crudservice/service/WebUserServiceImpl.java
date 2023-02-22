package com.ishlaw.crudservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ishlaw.crudservice.Utils.IshlawConstants;
import com.ishlaw.crudservice.Utils.SharedFunctions;
import com.ishlaw.crudservice.configuration.JwtToken;
import com.ishlaw.crudservice.entity.RoleMap;
import com.ishlaw.crudservice.entity.StaffDetails;
import com.ishlaw.crudservice.entity.UserPermissionReport;
import com.ishlaw.crudservice.exceptions.IshlawException;
import com.ishlaw.crudservice.model.ApiResponse;
import com.ishlaw.crudservice.repositories.CrudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Permission;
import java.util.*;

@Slf4j
@Service
public class WebUserServiceImpl implements WebUserService{

    @Autowired
    CrudTransactionsService crudTransactionsService;

    @Autowired
    Environment environment;

    @Autowired
    JwtToken jwtToken;

    @Autowired
    PasswordEncoder passwordencoder;

    @Autowired
    private CrudService crudService;

    private UserManagerService userManagerService;

    @Override
    public ApiResponse authenticateUser(HashMap<String, String> requestMap){
        log.info("Authenticate User | body :: {}",  requestMap);
        ApiResponse response = new ApiResponse();
        String msisdn = requestMap.getOrDefault("username","");
        String password = requestMap.getOrDefault("password","");
        ObjectMapper mapper = new ObjectMapper();

        try{
            if (!StringUtils.hasText(msisdn)) {
                log.warn("Invalid msisdn :: {}", msisdn);
                throw new IshlawException("Invalid msisdn", IshlawConstants.ApiResponseCodes.GENERAL_ERROR, "Please input Username", HttpStatus.BAD_REQUEST);
            }
            if (!StringUtils.hasText(password)) {
                log.warn("Invalid password :: {}", msisdn);
                throw new IshlawException("Invalid password", IshlawConstants.ApiResponseCodes.GENERAL_ERROR, "Please input password", HttpStatus.BAD_REQUEST);
            }
            log.info("Checking if staff member exists exists ...........");
            StaffDetails member = crudTransactionsService.findStaffByMsisdn(msisdn);
            if(member !=null){
                log.info("Member exists |Details {} | .Checking if staff member exists is active ...........",mapper.writeValueAsString(member));
                if(member.getStatus() == 1){
                    log.info("Member is active.Verifying password ...........");
                    String key = environment.getRequiredProperty("datasource.ishlaw.secret");
                    String hashedPassword = SharedFunctions.hashPassword(password.toCharArray(), key.getBytes(StandardCharsets.UTF_8), 1000, 256);
                    log.info("hashed password " + hashedPassword);
                    if (member.getPassword().equalsIgnoreCase(hashedPassword)) {
                        log.info("Member was verified successfully.Generating token.......");
                        List<UserPermissionReport> permissionReportList =  new ArrayList<>();
                        permissionReportList = crudTransactionsService.fetchRolepermissionsMap(msisdn);
                        HashSet<String> permissions = new HashSet<>();
                        HashSet<String> roles = new HashSet<>();
                        permissionReportList.stream().forEach(userPermissionReport -> {
                            permissions.add(userPermissionReport.getPermissionName());
                            roles.add(userPermissionReport.getTitle());

                        });

                       String token = jwtToken.generateMainToken(member,permissions,roles);
                       HashMap<String,String> responsebody = new HashMap<>();
                       responsebody.put("token",token);

                       response.setResponseBody(responsebody);
                       response.setResponseCode("00");
                       response.setMessage("Successful Login");
                       log.info("Successful login  for msisdn {}" ,msisdn );
                    }
                    else{
                        log.error("Wrong password credentials provided for   msisdn :: {}", msisdn);
                        throw new IshlawException("Wrong password credentials provided for", IshlawConstants.ApiResponseCodes.GENERAL_ERROR, "Wrong credentials", HttpStatus.UNAUTHORIZED);
                    }
                }
                else{
                    log.error("Member is not active in StaffDetails with member  msisdn :: {}", msisdn);
                    throw new IshlawException("Member is not active ", IshlawConstants.ApiResponseCodes.GENERAL_ERROR, "Wrong credentials", HttpStatus.UNAUTHORIZED);
                }
            }
            else{
                log.error("Member not found in StaffDetails with member  msisdn :: {}", msisdn);
                throw new IshlawException("Member does not exist", IshlawConstants.ApiResponseCodes.GENERAL_ERROR, "Wrong credentials", HttpStatus.UNAUTHORIZED);
            }
        }
        catch (IshlawException e) {
            response.setResponseCode(e.getResponseCode().getCode());
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode(IshlawConstants.ApiResponseCodes.GENERAL_ERROR.getCode());
            response.setMessage("Error Ocurred Processing Request.Please try again later");
            return response;

        }


        return response;
    }

    @Override
    public ApiResponse createUser(HashMap<String, String> requestMap){
        userManagerService = new UserManagerService(passwordencoder,environment);
        log.info("Create User | body :: {}",  requestMap);
        ApiResponse response = new ApiResponse<>();
        String msisdn = requestMap.getOrDefault("phoneNumber","");
        String password = requestMap.getOrDefault("password","");
        String emailAddress = requestMap.getOrDefault("emailAddress","");
        String firstname = requestMap.getOrDefault("firstName","");
        String secondName = requestMap.getOrDefault("secondName","");
        String role = requestMap.getOrDefault("TeamId","");
        String key2 = environment.getRequiredProperty("datasource.ishlaw.secret");
        ObjectMapper mapper = new ObjectMapper();

        try {
            if (!StringUtils.hasText(msisdn)) {
                log.warn("Invalid msisdn :: {}", msisdn);
                throw new IshlawException("Invalid msisdn", IshlawConstants.ApiResponseCodes.GENERAL_ERROR, "Please input Username", HttpStatus.BAD_REQUEST);
            }
            if (!StringUtils.hasText(password)) {
                log.warn("Invalid password :: {}", msisdn);
                throw new IshlawException("Invalid password", IshlawConstants.ApiResponseCodes.GENERAL_ERROR, "Please input password", HttpStatus.BAD_REQUEST);
            }

            if (!StringUtils.hasText(emailAddress)) {
                log.warn("Invalid emailAddress :: {}", emailAddress);
                throw new IshlawException("Invalid emailAddress", IshlawConstants.ApiResponseCodes.GENERAL_ERROR, "Please input emailAddress", HttpStatus.BAD_REQUEST);
            }
            if (!StringUtils.hasText(firstname)) {
                log.warn("Invalid firstname :: {}", firstname);
                throw new IshlawException("Invalid firstname", IshlawConstants.ApiResponseCodes.GENERAL_ERROR, "Please input firstname", HttpStatus.BAD_REQUEST);
            }

            if (!StringUtils.hasText(secondName)) {
                log.warn("Invalid secondName :: {}", secondName);
                throw new IshlawException("Invalid secondName", IshlawConstants.ApiResponseCodes.GENERAL_ERROR, "Please input secondName", HttpStatus.BAD_REQUEST);
            }
            if (!StringUtils.hasText(role)) {
                log.warn("Invalid role :: {}", role);
                throw new IshlawException("Invalid role", IshlawConstants.ApiResponseCodes.GENERAL_ERROR, "Please input role", HttpStatus.BAD_REQUEST);
            }

            log.info("Checking if member {} exists....................",msisdn);
            StaffDetails member = crudTransactionsService.findStaffByMsisdn(msisdn);
            if(member!=null){
                throw new IshlawException("Member with Phone Number "+msisdn+" exists", IshlawConstants.ApiResponseCodes.GENERAL_ERROR, "Member with Phone Number {}"+msisdn+" exists", HttpStatus.OK);
            }
            log.info("Member is new.Validating Password....................");
            Map<String, Object> passwordValidation = userManagerService.validatePassword(requestMap ,password);
            Boolean passwordValid = passwordValidation.get("isValid").equals("yes") ? true : false;
            log.info("password validation "+ passwordValid);
            if(passwordValid){
                log.info("Password valid.Creating User...................");
                StaffDetails newMember = new StaffDetails();
                newMember.setPassword(SharedFunctions.hashPassword(password.toCharArray(), key2.getBytes(StandardCharsets.UTF_8), 1000, 256));
                newMember.setFirstName(firstname);
                newMember.setSurname(secondName);
                newMember.setEmailaddress(emailAddress);
                newMember.setPhoneNumber(cleanPhone(msisdn));
                newMember.setDateCreated(new Date());
                newMember.setDateUpdated(new Date());
                newMember.setStatus(1);
                crudService.save(newMember);
                Integer id = newMember.getId();


                if (id != null) {
                    log.info("User created.Setting up roles...................");
                    RoleMap roleMap = new RoleMap();
                    roleMap.setRoleId(Integer.parseInt(role));
                    roleMap.setUserId(id);
                    roleMap.setDateCreated(new Date());
                    crudService.save(roleMap);
                    response.setResponseCode("00");
                    response.setMessage("User Created Successfully");
                    return response;
                }
                else{
                    throw new IshlawException("Error occured during User Creation{}"+msisdn, IshlawConstants.ApiResponseCodes.GENERAL_ERROR, "Error occured during User Creation{}", HttpStatus.OK);
                }
            }

            else {
                log.info("Password not valid:");
                response.setResponseCode("01");
                response.setMessage(String.valueOf((List) passwordValidation.get("errors")));
                return  response;
            }

        }catch (IshlawException e) {
            response.setResponseCode(e.getResponseCode().getCode());
            response.setMessage(e.getMessage());
            return response;
        }
        catch (Exception e){
            e.printStackTrace();
            response.setResponseCode(IshlawConstants.ApiResponseCodes.GENERAL_ERROR.getCode());
            response.setMessage("Error Ocurred Processing Request.Please try again later");
            return response;

        }


    }

    public String cleanPhone(String msisdn){
        String cleanPhone = msisdn;
        cleanPhone = "254".concat(msisdn.substring(msisdn.length() - 9));
        return cleanPhone;
    }
}
