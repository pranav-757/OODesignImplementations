package com.Pranav.VendingMC;

import com.Pranav.VendingMC.common.Bucket;
import com.Pranav.VendingMC.common.Inventory;
import com.Pranav.VendingMC.enums.Coin;
import com.Pranav.VendingMC.enums.Item;
import com.Pranav.VendingMC.exceptions.NotFullPaidException;
import com.Pranav.VendingMC.exceptions.NotSufficientChangeException;
import com.Pranav.VendingMC.exceptions.SoldOutException;

import java.util.*;

/**
 * Sample implementation of Vending Machine in Java
 * @author Javin Paul
 * @reference https://javarevisited.blogspot.com/2016/06/design-vending-machine-in-java.html
 * https://medium.com/swlh/vending-machine-design-a-state-design-pattern-approach-5b7e1a026cd2
 * https://leetcode.com/discuss/interview-question/125218/design-a-vending-machine
 * Read more: https://javarevisited.blogspot.com/2016/06/design-vending-machine-in-java.html#ixzz6T4fSIcR7
 */
public class VendingMachine  {
    private Inventory<Coin> cashInventory = new Inventory<Coin>();
    private Inventory<Item> itemInventory = new Inventory<Item>();
    private Map<Integer, Item> itemCodeMap;
    private long totalSales;
    private Item currentItem;
    private long currentBalance;

    public VendingMachine(){
        initialize();
    }

    private void initialize(){
        //initialize machine with 5 coins of each denomination
        //and 5 cans of each Item
        for(Coin c : Coin.values()){
            cashInventory.put(c, 5);
        }

        for(Item i : Item.values()){
            itemInventory.put(i, 5);
        }

        itemCodeMap = new HashMap<>();

        for(int i=1; i<=Item.values().length; i++) {
            itemCodeMap.put(i, Item.values()[i-1] );
        }

    }

    public void selectItem() {

        System.out.println("Please select an item");
        for(Map.Entry<Integer, Item> entry : itemCodeMap.entrySet()) {
            System.out.println("Item code:"+ entry.getKey() + " for " + entry.getValue().getName());
        }

        Scanner ip = new Scanner(System.in);
        int itemCode = ip.nextInt();

        Item item = itemCodeMap.get(itemCode);
        long price = selectItemAndGetPrice(item);

        System.out.println("Please pay:" + price);
    }

    public long selectItemAndGetPrice(Item item) {
        if(itemInventory.hasItem(item)){
            currentItem = item;
            return currentItem.getPrice();
        }
        throw new SoldOutException("Sold Out, Please buy another item");
    }

    public void insertCash() {
        System.out.println("Please insert 1, 2, 5 OR 10. -1 to quit");

        Scanner ip = new Scanner(System.in);
        int coinValue = ip.nextInt();

        while(coinValue != -1) {
            Coin c = Coin.get(coinValue);

            if(c != null) {
                insertCoin(c);
            } else {
                System.out.println("Cannot identify coin. Try again");
            }
            coinValue = ip.nextInt();
        }
    }

    private void insertCoin(Coin coin) {
        currentBalance = currentBalance + coin.getDenomination();
        cashInventory.add(coin);
    }

    public void collectItemAndChange() {
        Item item = collectItem();
        totalSales = totalSales + currentItem.getPrice();

        System.out.println("Collect " + item.getName());

        List<Coin> change = collectChange();

        System.out.println("Please collect your change");
        for(Coin c: change) {
            System.out.println("collect coin:" + c.getDenomination());
        }
        //return new Bucket<Item, List<Coin>>(item, change);
    }

    private Item collectItem() throws NotSufficientChangeException,
            NotFullPaidException {
        if(isFullPaid()){
            if(hasSufficientChange()){
                itemInventory.deductItem(currentItem);
                return currentItem;
            }
            throw new NotSufficientChangeException("Not Sufficient change in Inventory");

        }
        long remainingBalance = currentItem.getPrice() - currentBalance;
        throw new NotFullPaidException("Price not full paid, remaining : ",
                remainingBalance);
    }

    private List<Coin> collectChange() {
        long changeAmount = currentBalance - currentItem.getPrice();
        List<Coin> change = getChange(changeAmount);
        updateCashInventory(change);
        currentBalance = 0;
        currentItem = null;
        return change;
    }

    public List<Coin> refund(){
        List<Coin> refund = getChange(currentBalance);
        updateCashInventory(refund);
        System.out.println("Please collect your refund");
        for(Coin c: refund) {
            System.out.println("collect coin:" + c.getDenomination());
        }
        currentBalance = 0;
        currentItem = null;
        return refund;
    }


    private boolean isFullPaid() {
        if(currentBalance >= currentItem.getPrice()){
            return true;
        }
        return false;
    }


    private List<Coin> getChange(long amount) throws NotSufficientChangeException{
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

    public void reset(){
        cashInventory.clear();
        itemInventory.clear();
        totalSales = 0;
        currentItem = null;
        currentBalance = 0;
    }

    public void printStats(){
        System.out.println("Total Sales : " + totalSales);
        System.out.println("Current Item Inventory : " + itemInventory);
        System.out.println("Current Cash Inventory : " + cashInventory);
    }


    private boolean hasSufficientChange(){
        return hasSufficientChangeForAmount(currentBalance - currentItem.getPrice());
    }

    private boolean hasSufficientChangeForAmount(long amount){
        boolean hasChange = true;
        try{
            getChange(amount);
        }catch(NotSufficientChangeException nsce){
            return hasChange = false;
        }

        return hasChange;
    }

    private void updateCashInventory(List<Coin> change) {
        for(Coin c : change){
            cashInventory.deductItem(c);
        }
    }

    public long getTotalSales(){
        return totalSales;
    }

}
