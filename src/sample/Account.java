package sample;

import java.io.Serializable;

public class Account implements Serializable {

    private String name;
    private int id;
    private double balance;
    public static transient int numOfAccounts = 0;

    public String getName(){
        return name;
    }

    public double getBalance(){
        return balance;
    }

    public void setBalance(double amount){
        this.balance = amount;
    }

    public void transferBalance(double amount){
        this.balance += amount;
    }

    public Account(String name, int id){
        this.name = name;
        this.id = id;
        this.balance = 0;
        numOfAccounts++;
    }

    @Override
    public String toString(){
        return "[" + id + "] " + name;
    }
}
