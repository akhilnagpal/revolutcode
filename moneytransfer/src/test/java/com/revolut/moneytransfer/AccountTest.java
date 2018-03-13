package com.revolut.moneytransfer;

import javax.ws.rs.client.Entity;

import com.revolut.moneytransfer.data.model.Account;
import com.revolut.moneytransfer.data.model.AccountCopy;

import org.junit.Assert;
import org.junit.Test;

public class AccountTest extends AbstractTest {
	
	@Test
    public void testCreateAccount() {
        String accountName = "Akhil Nagpal";
        AccountCopy account = target.queryParam("name", accountName).path("accounts/create").request().post(Entity.text(""), AccountCopy.class);
        Assert.assertEquals(accountName, account.getName());        
    }

}
