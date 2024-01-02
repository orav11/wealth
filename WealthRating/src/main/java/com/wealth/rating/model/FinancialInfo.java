package com.wealth.rating.model;

import java.math.BigInteger;

public class FinancialInfo {

    private BigInteger cash;
    private int numberOfAssets;

    public FinancialInfo(BigInteger cash, int numberOfAssets) {
        this.cash = cash;
        this.numberOfAssets = numberOfAssets;
    }

    public BigInteger getCash() {
        return cash;
    }

    public void setCash(BigInteger cash) {
        this.cash = cash;
    }

    public int getNumberOfAssets() {
        return numberOfAssets;
    }

    public void setNumberOfAssets(int numberOfAssets) {
        this.numberOfAssets = numberOfAssets;
    }
}
