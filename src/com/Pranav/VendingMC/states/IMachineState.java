package com.Pranav.VendingMC.states;

public interface IMachineState {

    public String getStateName();
    public void process();
    public void pressButton();
//    public void selectItem();
//    public void collectCash(int cash);
//    public void dispenseChange(String productCode);
//    public void dispenseItem(String productCode);
    public void cancelTransaction();
}
