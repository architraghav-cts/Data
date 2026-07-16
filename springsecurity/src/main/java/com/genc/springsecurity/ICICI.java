package com.genc.springsecurity;

public class ICICI extends Bank {
    private String accountHolder;

    public ICICI(String bankAccountNo, String accountHolder) {
        super(bankAccountNo);
        this.accountHolder = accountHolder;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }
}
