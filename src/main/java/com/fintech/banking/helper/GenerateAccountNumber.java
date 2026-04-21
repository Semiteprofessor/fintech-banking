package com.fintech.banking.helper;

import java.security.SecureRandom;

public class GenerateAccountNumber {

    private static final SecureRandom random = new SecureRandom();

    public static String generateAccountNumber(long id) {

        String bankCode = "044";
        String branchCode = "123";

        long randomPart = 1000000000L + random.nextInt(900000000);

        String uniquePart = String.valueOf(randomPart);

        return bankCode + " " + branchCode + " " + uniquePart;
    }
}