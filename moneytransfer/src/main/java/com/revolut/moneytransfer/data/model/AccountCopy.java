package com.revolut.moneytransfer.data.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.UUID;

@XmlRootElement
public class AccountCopy {
    private String name;
    private String accountId;
    private Double balance;

    public void setBalance(Double balance) {
		this.balance = balance;
	}

	public AccountCopy(String name, UUID uuid,Double balance) {
        this.name = name;
        this.accountId = uuid.toString();
        this.balance = balance;
    }

    public Double getBalance() {
		return balance;
	}

	public AccountCopy() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String uuid) {
        this.accountId = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        AccountCopy that = (AccountCopy) o;

        if (!name.equals(that.name))
            return false;
        if (!accountId.equals(that.accountId))
            return false;
        return balance.equals(that.balance);
    }

    @Override
    public int hashCode() {
        return name.hashCode()+accountId.hashCode()+balance.hashCode();
    }

    @Override
    public String toString() {
        return "AccountCopy name='" + name + '\'' +
                ", accountId='" + accountId + '\'' +
                ", balance=" + balance ;
    }
}
