package com.Pranav.VendingMC.states.impl;

import com.Pranav.VendingMC.VendingMachine;
import com.Pranav.VendingMC.enums.Coin;
import com.Pranav.VendingMC.states.IMachineState;

import java.util.List;

public class CollectChange implements IMachineState {

    private static final String STATE_NAME = "COLLECT_CHANGE";
    private VendingMachine machine;

    public CollectChange(VendingMachine mc) {
        this.machine = mc;
    }

    @Override
    public String getStateName() {
        return STATE_NAME;
    }

    @Override
    public void process() {
        List<Coin> change = this.machine.collectChange();

        System.out.println("Please collect your change");
        for(Coin c: change) {
            System.out.println("collect coin:" + c.getDenomination());
        }
        this.machine.toNextState();
    }

    @Override
    public void pressButton() {

    }

    @Override
    public void cancelTransaction() {

    }
}
