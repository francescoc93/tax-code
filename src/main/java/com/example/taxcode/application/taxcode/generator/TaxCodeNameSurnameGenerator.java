package com.example.taxcode.application.taxcode.generator;

public interface TaxCodeNameSurnameGenerator {

    String calculateNameCode(String name);
    String calculateSurnameCode(String name);
}
