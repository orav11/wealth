package com.wealth.rating.model;

public class Person {

    private long id;
    private FinancialInfo financialInfo;
    private PersonalInfo personalInfo;

    public Person(long id, FinancialInfo financialInfo, PersonalInfo personalInfo) {
        this.id = id;
        this.financialInfo = financialInfo;
        this.personalInfo = personalInfo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FinancialInfo getFinancialInfo() {
        return financialInfo;
    }

    public void setFinancialInfo(FinancialInfo financialInfo) {
        this.financialInfo = financialInfo;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }
}
