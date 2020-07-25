package com.Pranav.VendingMC.util.impl;

import com.Pranav.VendingMC.common.Inventory;
import com.Pranav.VendingMC.enums.Coin;
import com.Pranav.VendingMC.exceptions.NotSufficientChangeException;
import com.Pranav.VendingMC.util.IChangeCalcutaionService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GreedyCalculation implements IChangeCalcutaionService {
    @Override
    public List<Coin> calculateChange(long amount, Inventory<Coin> cashInventory) throws NotSufficientChangeException {
        List<Coin> changes = Collections.EMPTY_LIST;

        if(amount > 0){
            changes = new ArrayList<Coin>();
            long balance = amount;
            while(balance > 0){
                if(balance >= Coin.QUARTER.getDenomination()
                        && cashInventory.hasItem(Coin.QUARTER)){
                    changes.add(Coin.QUARTER);
                    balance = balance - Coin.QUARTER.getDenomination();
                    continue;

                }else if(balance >= Coin.DIME.getDenomination()
                        && cashInventory.hasItem(Coin.DIME)) {
                    changes.add(Coin.DIME);
                    balance = balance - Coin.DIME.getDenomination();
                    continue;

                }else if(balance >= Coin.NICKLE.getDenomination()
                        && cashInventory.hasItem(Coin.NICKLE)) {
                    changes.add(Coin.NICKLE);
                    balance = balance - Coin.NICKLE.getDenomination();
                    continue;

                }else if(balance >= Coin.PENNY.getDenomination()
                        && cashInventory.hasItem(Coin.PENNY)) {
                    changes.add(Coin.PENNY);
                    balance = balance - Coin.PENNY.getDenomination();
                    continue;

                }else{
                    throw new NotSufficientChangeException("NotSufficientChange, Please try another product");
                }
            }
        }

        return changes;
    }
}
