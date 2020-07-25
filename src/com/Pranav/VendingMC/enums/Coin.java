package com.Pranav.VendingMC.enums;

import java.util.HashMap;
import java.util.Map;

public enum Coin {
    //ONE(1), TWO(2), FIVE(5), TEN(10);

    PENNY(1), NICKLE(5), DIME(10), QUARTER(25);

    private final int denomination;

    // Reverse-lookup map for getting a day from an abbreviation
    private static final Map<Integer, Coin> lookup = new HashMap<Integer, Coin>();

    static {
        for (Coin c: Coin.values()) {
            lookup.put(c.getDenomination(), c);
        }
    }

    //private access modifier is redundant here
    private Coin(int denomination){
        this.denomination = denomination;
    }

    public int getDenomination(){
        return denomination;
    }

    public static Coin get(int denomination) {
        return lookup.getOrDefault(denomination, null);
    }
}
