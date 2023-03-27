package com.ishlaw.crudservice.service;

import com.ishlaw.crudservice.entity.IsRoles;
import com.ishlaw.crudservice.entity.StaffDetails;
import com.ishlaw.crudservice.entity.UserPermissionReport;
import com.ishlaw.crudservice.repositories.CrudService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class CrudTransactionsService {
    @Autowired
    Environment environment;

    @Autowired
    CrudService crudService;

    public List<StaffDetails> getStaffMembers() {
        String query ="select s from StaffDetails s where status = 1";
        List<StaffDetails> staff = new IshlawDaoImpl(crudService,environment).getStaffMembers(query);
        return staff;
    }

    public List<IsRoles> getRoles() {
        String query ="select s from IsRoles s";
        List<IsRoles> roles = new IshlawDaoImpl(crudService,environment).getRoles(query);
        return roles;
    }

    public StaffDetails getMemberById(int id) {
        String query ="select s from StaffDetails s where status = 1 and id ="+id+"";
        List<StaffDetails> staff = new IshlawDaoImpl(crudService,environment).getStaffMembers(query);
        StaffDetails member = staff.isEmpty()?null: staff.get(0);
        return member;
    }

    public StaffDetails findStaffByMsisdn(String msisdn) {
        try {
            log.info("Fetching staff details using msisdn {}",msisdn);
            String query = "select s from StaffDetails s where status = 1 and phoneNumber ="+"254".concat(msisdn.substring(msisdn.length() - 9));
            log.debug("query {}",query);
            StaffDetails member = new IshlawDaoImpl(crudService,environment).findStaffByMsisdn(query);
            return member;
        }
        catch (Exception e){
            log.info("Error Occured Fetching staff details using msisdn {}",msisdn);
            e.printStackTrace();
            return null;
        }
    }

    public List<UserPermissionReport> fetchRolepermissionsMap(String msisdn) {
        try {
            log.info("Fetching staff permissions using msisdn {}",msisdn);
            String query = "select s from UserPermissionReport s where phone_number ="+"254".concat(msisdn.substring(msisdn.length() - 9));
            List<UserPermissionReport> permissions= new IshlawDaoImpl(crudService,environment).fetchRolePermissionsMap(query);
            return permissions;
        }
        catch (Exception e){
            log.info("Error Occured Fetching staff permissions using msisdn {}",msisdn);
            e.printStackTrace();
            return null;
        }
    }

}
