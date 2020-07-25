package com.Pranav.VendingMC.states.impl;

import com.Pranav.VendingMC.VendingMachine;
import com.Pranav.VendingMC.states.IMachineState;

public class Cancel implements IMachineState {

    private static final String STATE_NAME = "CANCEL";
    private VendingMachine machine;

    public void Ready(VendingMachine mc) {
        this.machine = mc;
    }

    @Override
    public String getStateName() {
        return STATE_NAME;
    }

    @Override
    public void process() {

    }

    @Override
    public void pressButton() {

    }

    @Override
    public void cancelTransaction() {

    }
}
