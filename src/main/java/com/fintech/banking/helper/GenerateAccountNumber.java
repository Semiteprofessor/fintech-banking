package com.fintech.banking.helper;


import java.security.SecureRandom;

public class GenerateAccountNumber {

    private static final SecureRandom random = new SecureRandom();

    public static String generateAccountNumber(long id) {
        String bankCode = "044";
        String branchCode = "123";
        String uniquePart = String.format("%07d", id);

        return bankCode + " " + branchCode + " " + uniquePart;
    }
}