package com.Pranav.VendingMC.states.impl;

import com.Pranav.VendingMC.VendingMachine;
import com.Pranav.VendingMC.enums.Item;
import com.Pranav.VendingMC.states.IMachineState;

public class DispenseItem implements IMachineState {

    private static final String STATE_NAME = "DISPENSE_ITEM";
    private VendingMachine machine;

    public DispenseItem(VendingMachine mc) {
        this.machine = mc;
    }

    @Override
    public String getStateName() {
        return STATE_NAME;
    }

    @Override
    public void process() {
        try {
            Item item = this.machine.dispenseItem();
            System.out.println("Collect " + item.getName());
            Thread.currentThread().sleep(10);
            this.machine.toNextState();
        } catch (RuntimeException e) {
            System.out.println("Error while dispesing item: -> "+ e.getMessage());
        } catch (InterruptedException e) {
            this.machine.refund();
            throw new RuntimeException("Dispense Item interrupted");
        }
        //incorrect bcoz it executes event no exception is there
//        finally {
//            this.machine.refund();
//        }

    }

    @Override
    public void pressButton() {

    }

    @Override
    public void cancelTransaction() {

    }
}
