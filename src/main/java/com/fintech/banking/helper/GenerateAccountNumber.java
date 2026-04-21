package com.fintech.banking.helper;


import java.security.SecureRandom;

public class GenerateAccountNumber {

    private static final SecureRandom random = new SecureRandom();

    private static final String BANK_CODE = "044";

    public static String generateAccountNumber() {

        String branchCode = String.format("%03d", random.nextInt(1000));
        String uniquePart = String.format("%07d", random.nextInt(10_000_000));

        return BANK_CODE + " " + branchCode + " " + uniquePart;
    }
}