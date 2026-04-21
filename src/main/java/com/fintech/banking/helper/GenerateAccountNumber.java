package com.fintech.banking.helper;

import java.security.SecureRandom;

public class GenerateAccountNumber {

    private static final SecureRandom random = new SecureRandom();

    public static String generateAccountNumber() {

        String bankCode = "044";
        String branchCode = "123";

        long number = 1000000000L + (long)(Math.random() * 9000000000L);

        return bankCode + " " + branchCode + " " + number;
    }
}