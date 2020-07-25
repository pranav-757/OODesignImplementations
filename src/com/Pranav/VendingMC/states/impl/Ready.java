package com.Pranav.VendingMC.states.impl;

import com.Pranav.VendingMC.VendingMachine;
import com.Pranav.VendingMC.states.IMachineState;

public class Ready implements IMachineState {

    private static final String STATE_NAME = "READY";
    private VendingMachine machine;

    public Ready(VendingMachine mc) {
        this.machine = mc;
    }

    @Override
    public String getStateName() {
        return STATE_NAME;
    }

    @Override
    public void process() {
        this.machine.selectItem();
        this.machine.toNextState();
    }

    @Override
    public void pressButton() {
        throw new RuntimeException("item selection already in progress");
    }

    @Override
    public void cancelTransaction() {
        throw new RuntimeException("Currently selecting item. Cannot cancel Txn");
    }
}
