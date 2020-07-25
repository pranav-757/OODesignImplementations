package com.Pranav.VendingMC.states.impl;

import com.Pranav.VendingMC.VendingMachine;
import com.Pranav.VendingMC.states.IMachineState;

public class InsertMoney implements IMachineState {

    private static final String STATE_NAME = "INSERT_MONEY";
    private VendingMachine machine;

    public InsertMoney(VendingMachine mc) {
        this.machine = mc;
    }

    @Override
    public String getStateName() {
       return STATE_NAME;
    }

    @Override
    public void process() {

        //simulate coin collection and validation
        try {
            this.machine.insertMoney();
            Thread.currentThread().sleep(10);
            this.machine.toNextState();
        } catch (InterruptedException e) {
            this.machine.refund();
            throw new RuntimeException("insert money interrupted");
        }


    }

    @Override
    public void pressButton() {
        throw new RuntimeException("insert money already in progress");
    }

    @Override
    public void cancelTransaction() {
        //TO DO run cancel flow
        //throw new RuntimeException("item selection already in progress");
    }
}
