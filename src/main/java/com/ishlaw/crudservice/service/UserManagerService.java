package com.ishlaw.crudservice.service;


import com.ishlaw.crudservice.entity.StaffDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class UserManagerService {
    private final PasswordEncoder passwordencoder;
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
    private int minLength;
    private int minSpecialCharacters;
    private int minLowerCase;
    private int minUpperCase;
    private boolean alphaNumeric;
    private String allowedSpecialCharactes;
    private Pattern pattern;
    private Matcher patternMatcher;
    private Pattern whitespacePattern;
    private Matcher whitespaceMatcher;

    private final Environment environment;

    private void init() {
        minLength = 8;
        minSpecialCharacters = 2;
        minLowerCase = 2;
        minUpperCase = 2;
        alphaNumeric = true;
        allowedSpecialCharactes = "@!$%#*%-_+";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        whitespacePattern = Pattern.compile("(\\s+\\s)");
    }

    public UserManagerService(PasswordEncoder passwordencoder,Environment environment) {
        this.passwordencoder = passwordencoder;
        this.environment = environment;
        init();
    }


    public String getAllowedSpecialCharactes() {
        String specialCharacters = environment.getRequiredProperty("datasource.ishlaw.passwordValidation.passwordSpecCharacters");
        allowedSpecialCharactes = specialCharacters == null?"@!$%#*%-_+" : specialCharacters;
        log.info("allowed Characters {}",allowedSpecialCharactes);
        return allowedSpecialCharactes;
    }

    public int getAllowedPasswordLength() {
        String minimumlength = environment.getRequiredProperty("datasource.ishlaw.passwordValidation.passwordMinLength");
        minLength = minimumlength==null? 8: Integer.parseInt(minimumlength);
        log.info("minlength {}",minLength);

        return minLength;
    }


    public Map validatePassword(HashMap<String, String> requestMap, String pwd) {
        log.info("validation has started");
        Boolean isValid = true;
        Map<String, Object> response = new HashMap<>();
        List<String> errors = new ArrayList<>();
        //ensure no consequtive characters
        if (hasConsecutiveCharacters(pwd)) {
            isValid = false;
            errors.add("Password should not have more than 2 consecutive characters");
        } //ensure password not same as username or email or phone number,first name or last name
        else if ( requestMap.get("phoneNumber").equalsIgnoreCase(pwd)
                || requestMap.get("firstName").equalsIgnoreCase(pwd)
                || requestMap.get("secondName").equalsIgnoreCase(pwd)) {
            isValid = false;
            errors.add("Password should not be any of your personal details");
        }
        //check if password is strong enough
        if (isValid) {
            if (!validate(pwd, "MIN_LENGTH")) {
                errors.add("Password is shorter than allowed length of " + this.minLength);
                isValid = false;
            } else if (!validate(pwd, "SPECIAL_CHARACTERS")) {
                isValid = false;
                errors.add("Password must contain atleast one of these special characters " + this.allowedSpecialCharactes);
            } else if (!validate(pwd, "UPPER_CASE")) {
                isValid = false;
                errors.add("Password must contain atleast one Upper case letter");
            } else if (!validate(pwd, "LOWER_CASE")) {
                isValid = false;
                errors.add("Password must contain atleast one Lower case letter");
            } else if (!validate(pwd, "NUMBER")) {
                isValid = false;
                errors.add("Password must contain atleast one number");
            } else if (!validate(pwd, "WHITE_SPACE")) {
                isValid = false;
                errors.add("Password must not contain spaces");
            } else {
                //password is valid
            }
        }
        response.put("isValid", isValid ? "yes" : "no");
        response.put("errors", errors);
        return response;
    }


    public boolean validate(final String password, String type) {
        boolean valid = false;
        try {
            switch (type) {
                case "MIN_LENGTH":
                    valid = password.length() >= this.getAllowedPasswordLength();
                    break;
                case "SPECIAL_CHARACTERS":
                    valid = true;
                    if (minSpecialCharacters > 0) {
                        Pattern regex = Pattern.compile("[" + this.getAllowedSpecialCharactes() + "]");
                        Matcher matcher = regex.matcher(password);
                        valid = matcher.find();
                    }
                    break;
                case "UPPER_CASE":
                    valid = password.chars().anyMatch(Character::isUpperCase);
                    break;
                case "LOWER_CASE":
                    valid = password.chars().anyMatch(Character::isLowerCase);
                    break;
                case "NUMBER":
                    valid = password.matches(".*\\d+.*");
                    break;
                case "WHITE_SPACE":
                    valid = password.matches("\\S+");
                    break;
                default:
                    valid = false;
            }
        } catch (Exception ex) {
            log.error("Failed to validate password {}",ex.getMessage(),ex);
        }
        log.info("Password validation type: {} passed? {}", type, valid);
        return valid;

    }
    public boolean hasConsecutiveCharacters(String pwd) {
        String[] letter = pwd.split(""); // here you get each letter in to a string array
        boolean res = false;
        for (int i = 0; i < letter.length - 1; i++) {
            if (letter[i].equals(letter[i + 1])) {
                if ((i + 2) < letter.length && letter[i].equals(letter[i + 2])) {
                    res = true;
                    break;
                }
            }
        }
        return res;
    }

    public String encodePassword(String password) {
        return passwordencoder.encode(password);
    }

    public boolean passwordMatches(String rawPassword, String hashedPassword) {
        return passwordencoder.matches(rawPassword, hashedPassword);
    }

}
