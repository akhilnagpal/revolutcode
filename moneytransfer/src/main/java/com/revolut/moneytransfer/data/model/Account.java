package com.revolut.moneytransfer.data.model;

import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;


public class Account {
	private final UUID accountId;    
	private final String name;
	private double balance=0.0d;
	
	public double getBalance() {
		return balance;
	}

	private final List<Transaction> transactions = Lists.newArrayList();
    
    public Account(String name) {
        accountId = UUID.randomUUID();
        this.name = name;
    }
    
    public UUID getAccountId() {
		return accountId;
	}

	public String getName() {
		return name;
	}
	
	public synchronized AccountCopy getAccountDetails() {
        return new AccountCopy(name, accountId, balance);
    }
	
	public synchronized boolean withdraw(double amount) {
		Preconditions.checkArgument(amount > 0);
		if (balance<amount) {
			return false;
		}
		else {
			balance-=amount;
			return true;
		}
	}
	
	public synchronized  void add(double amount) {
		Preconditions.checkArgument(amount > 0);
		balance+=amount;
	}
	
	public synchronized void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }	

}
