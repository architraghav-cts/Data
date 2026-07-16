package com.genc.springsecurity;

public class Bank {
    private String bankAccountNo;

    public Bank(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }
}
