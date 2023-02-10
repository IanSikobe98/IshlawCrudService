package com.ishlaw.crudservice.service;

import com.ishlaw.crudservice.entity.StaffDetails;
import com.ishlaw.crudservice.repositories.CrudService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CrudTransactionsService {
    @Autowired
    Environment environment;

    @Autowired
    CrudService crudService;

    public List<StaffDetails> getStaffMembers() {
        String query ="select s from StaffDetails s";
        List<StaffDetails> staff = new IshlawDaoImpl(crudService,environment).getStaffMembers(query);
        return staff;
    }

    public StaffDetails findStaffByMsisdn(String msisdn) {
        String query = "select s from StaffDetails s where msisdn ="+"254".concat(msisdn.substring(msisdn.length() - 9));
        StaffDetails member = new IshlawDaoImpl(crudService,environment).findStaffByMsisdn(query);
        return member;
    }
}
