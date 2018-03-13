package com.revolut.moneytransfer.data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.revolut.moneytransfer.data.model.Account;
import com.revolut.moneytransfer.data.model.AccountCopy;
import com.revolut.moneytransfer.data.model.Transaction;

public class InMemoryDatabase {
	private final static Map<UUID, Account> accounts = Maps.newConcurrentMap();
	
	public static AccountCopy createAccount(String name) {
        Account newAccount = new Account(name);
        accounts.put(newAccount.getAccountId(), newAccount);
        return newAccount.getAccountDetails();
    }
	
	public static void addAmountToAccount(UUID accountId, double amount) {
        Account account = Preconditions.checkNotNull(accounts.get(accountId));
        account.add(amount);
    }
	
	public static AccountCopy getAccountDetails(UUID accountId) {
	    Account account = Preconditions.checkNotNull(accounts.get(accountId), "Account with id %s does not exist", accountId);
	    return account.getAccountDetails();
	}
	
	
	public static Transaction transferMoney(UUID fromAccountId, UUID destAccountId, double transferAmount, String reference) {
		Account sourceAccount = Preconditions.checkNotNull(accounts.get(fromAccountId), "Account with id %s does not exist", String.valueOf(fromAccountId));
		Account targetAccount = Preconditions.checkNotNull(accounts.get(destAccountId), "Account with id %s does not exist", String.valueOf(destAccountId));
		Preconditions.checkArgument(transferAmount > 0, "Transfer Amount should be greater than zero, is %s", transferAmount);
		Transaction transaction = new Transaction();		
		transaction.setSourceAccountId(fromAccountId);
		transaction.setDestAccountId(destAccountId);
		transaction.setSourceAmount(transferAmount);
		transaction.setTargetAmount(transferAmount);
		transaction.setTime(LocalDateTime.now());
		transaction.setReference(reference);
		if (sourceAccount.withdraw(transferAmount)) {
			transaction.setSuccess(true);
			targetAccount.add(transferAmount);
			sourceAccount.addTransaction(transaction);
			targetAccount.addTransaction(transaction);
		} else {
			transaction.setSuccess(false);
			transaction.setReference("Not enough money on account");
			sourceAccount.addTransaction(transaction);
			throw new IllegalArgumentException("Not enough money at " + fromAccountId);
		}
		return transaction;
	}

}
