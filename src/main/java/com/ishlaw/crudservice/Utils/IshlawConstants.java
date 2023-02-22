package com.ishlaw.crudservice.Utils;

public class IshlawConstants {


    public enum ApiResponseCodes {
        OK("00"),
        GENERAL_ERROR("01");
        private final String code;
        ApiResponseCodes(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}
