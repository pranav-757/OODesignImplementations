package com.Pranav.VendingMC.util;

import com.Pranav.VendingMC.common.Inventory;
import com.Pranav.VendingMC.enums.Coin;
import com.Pranav.VendingMC.exceptions.NotSufficientChangeException;

import java.util.List;

public interface IChangeCalcutaionService {
    List<Coin>  calculateChange(long amount, Inventory<Coin> cashInventory) throws NotSufficientChangeException;
}
