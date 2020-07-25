package com.Pranav.VendingMC.common;

import com.Pranav.VendingMC.exceptions.SoldOutException;

import java.util.HashMap;
import java.util.Map;

public class Inventory<T> {
    private Map<T, Integer> inventory = new HashMap<>();

    public int getQuantity(T item) {
        return inventory.getOrDefault(item, 0);
    }

    public void add(T item) {
        int count = inventory.getOrDefault(item, 0);
        inventory.put(item, count+1);
    }

    public void deductItem(T item) {
        if(getQuantity(item) > 0) {
            int count = inventory.getOrDefault(item, 0);
            inventory.put(item, count-1);
            return;
        }
        //throw new SoldOutException()
    }

    public boolean hasItem(T item){
        return getQuantity(item) > 0;
    }

    public void clear(){
        inventory.clear();
    }

    public void put(T item, int quantity) {
        inventory.put(item, quantity);
    }
}
