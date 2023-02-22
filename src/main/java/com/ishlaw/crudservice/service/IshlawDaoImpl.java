package com.ishlaw.crudservice.service;

import com.ishlaw.crudservice.entity.StaffDetails;
import com.ishlaw.crudservice.entity.UserPermissionReport;
import com.ishlaw.crudservice.repositories.CrudService;
import org.springframework.core.env.Environment;

import java.util.Collections;
import java.util.List;

public class IshlawDaoImpl {

    private CrudService databaseCrudService;

    private Environment environment;


    public IshlawDaoImpl(CrudService databaseCrudService,Environment environment){
        this.environment = environment;
        this.databaseCrudService = databaseCrudService;
    }

    public List<StaffDetails> getStaffMembers(String query) {
        List<StaffDetails> staff = databaseCrudService.fetchWithHibernateQuery(query, Collections.EMPTY_MAP);
        return staff;
    }

    public StaffDetails findStaffByMsisdn(String query){
        List<StaffDetails> members = databaseCrudService.fetchWithHibernateQuery(query, Collections.EMPTY_MAP);
        StaffDetails member = members.isEmpty()?null:members.get(0);

        return member;

    }

    public List <UserPermissionReport> fetchRolePermissionsMap(String query){
        List<UserPermissionReport> permissions = databaseCrudService.fetchWithHibernateQuery(query,Collections.EMPTY_MAP);
        return   permissions;
    }
}
