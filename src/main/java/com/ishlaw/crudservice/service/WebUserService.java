package com.ishlaw.crudservice.service;


import com.ishlaw.crudservice.model.ApiResponse;


import java.util.HashMap;

public interface WebUserService {
    ApiResponse authenticateUser(HashMap<String, String> requestMap);

    ApiResponse createUser(HashMap<String, String> requestMap);

    ApiResponse decodeToken(String tokenString);
}
