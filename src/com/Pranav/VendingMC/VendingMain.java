package com.Pranav.VendingMC;

import java.util.Scanner;

public class VendingMain {
    public static void main(String[] args) {
        VendingMachine machine = new VendingMachine();
        Scanner ipCommand = new Scanner(System.in);
        String command;

        while( true) {

            System.out.println("Welcome to Vending Machine");
            command = ipCommand.nextLine();
            if(command.equalsIgnoreCase("quit"))
               break;

            try{
                machine.selectItem();
                machine.insertCash();
                machine.collectItemAndChange();
            } catch (RuntimeException e) {
                System.out.println("Error :"+ e.getMessage());
                machine.refund();
            }

        }




        //User1 u1 = new User1(machine);
    }
}

//private class User1 extends Thread {
//
//    private VendingMachine mc;
//
//    public User1(VendingMachine mc){
//        this.mc = mc;
//    }
//
//    @Override
//    public void run() {
//        super.run();
//    }
//}
//
//public class User2 extends Thread {
//
//    private VendingMachine mc;
//
//    public User2(VendingMachine mc){
//        this.mc = mc;
//    }
//
//    @Override
//    public void run() {
//        super.run();
//    }
//}