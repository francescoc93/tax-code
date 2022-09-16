package com.example.taxcode.application.utils;

public class StringUtils {
    public static String normalizeSpace(String string){
        if(string==null){
            return null;
        }

        return string.replaceAll("\\s+", " ").trim();
    }

    private StringUtils(){}
}
