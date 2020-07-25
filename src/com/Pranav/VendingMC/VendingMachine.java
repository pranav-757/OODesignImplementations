package com.Pranav.VendingMC;

import com.Pranav.VendingMC.common.Bucket;
import com.Pranav.VendingMC.common.Inventory;
import com.Pranav.VendingMC.enums.Coin;
import com.Pranav.VendingMC.enums.Item;
import com.Pranav.VendingMC.exceptions.NotFullPaidException;
import com.Pranav.VendingMC.exceptions.NotSufficientChangeException;
import com.Pranav.VendingMC.exceptions.SoldOutException;
import com.Pranav.VendingMC.states.IMachineState;
import com.Pranav.VendingMC.states.impl.CollectChange;
import com.Pranav.VendingMC.states.impl.DispenseItem;
import com.Pranav.VendingMC.states.impl.InsertMoney;
import com.Pranav.VendingMC.states.impl.Ready;
import com.Pranav.VendingMC.util.IChangeCalcutaionService;
import com.Pranav.VendingMC.util.impl.GreedyCalculation;

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
    private IMachineState state;
    private long totalSales;
    private Item currentItem;
    private long currentBalance;
    private IChangeCalcutaionService changeCalcutaionService;

    public VendingMachine(){

        this.state = new Ready(this);
        this.changeCalcutaionService = new GreedyCalculation();
        initialize();
    }

    private void initialize(){
        //initialize machine with 5 coins of each denomination
        //and 5 cans of each Item
        for(Coin c : Coin.values()){
            cashInventory.put(c, 5);
        }

        for(Item i : Item.values()){
            itemInventory.put(i, 2);
        }

        itemCodeMap = new HashMap<>();

        for(int i=1; i<=Item.values().length; i++) {
            itemCodeMap.put(i, Item.values()[i-1] );
        }
    }

    public IMachineState getState() {
        return state;
    }

    public void setState(IMachineState state) {
        this.state = state;
    }

    public void toNextState() {
            String stateName = this.getState().getStateName();
            switch (stateName) {
                case "READY":
                    this.state = new InsertMoney(this);
                    this.state.process();
                    break;
                case "INSERT_MONEY" :
                    this.state = new DispenseItem(this);
                    this.state.process();
                    break;
                case "DISPENSE_ITEM":
                    this.state = new CollectChange(this);
                    this.state.process();
                    break;
                case "COLLECT_CHANGE":
                    this.state = new Ready(this);
                    //this.state.process();
                default:
                    throw new RuntimeException("Invalid Machine state");
            }
    }

    //1. Selecting Item
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

    private long selectItemAndGetPrice(Item item) {
        if(itemInventory.hasItem(item)){
            currentItem = item;
            return currentItem.getPrice();
        }
        throw new SoldOutException("Sold Out, Please buy another item");
    }


    //2. insert money
    public void insertMoney() {
        System.out.println("Please insert 1, 5, 10, 25. -1 to quit");

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

    //3.dispense Item
    public Item dispenseItem() throws NotSufficientChangeException,
            NotFullPaidException {
        if(isFullPaid()){
            if(hasSufficientChange()){
                itemInventory.deductItem(currentItem);
                totalSales = totalSales + currentItem.getPrice();
                return currentItem;
            }
            throw new NotSufficientChangeException("Not Sufficient change in Inventory");

        }
        long remainingBalance = currentItem.getPrice() - currentBalance;
        throw new NotFullPaidException("Price not full paid, remaining : ",
                remainingBalance);
    }

    //4.collect change
    public List<Coin> collectChange() {
        long changeAmount = currentBalance - currentItem.getPrice();
        List<Coin> change = changeCalcutaionService.calculateChange(changeAmount, cashInventory);
        updateCashInventory(change);
        currentBalance = 0;
        currentItem = null;
        return change;
    }

    //5.refund
    public List<Coin> refund(){
        List<Coin> refund = changeCalcutaionService.calculateChange(currentBalance, cashInventory);;
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
            changeCalcutaionService.calculateChange(amount, cashInventory);;
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
