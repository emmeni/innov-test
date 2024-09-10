package com.emmeni.innov.config;

public final class Constants {

    public static final int PASSWORD_MIN_LENGTH = 4;
    
    public static final int PASSWORD_MAX_LENGTH = 100;

    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";
    
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    
    public static final String SPRING_PROFILE_PRODUCTION = "prod";

    private Constants() {
    }
}
