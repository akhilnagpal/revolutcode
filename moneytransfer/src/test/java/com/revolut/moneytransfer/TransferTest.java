package com.revolut.moneytransfer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import org.junit.Assert;
import org.junit.Test;

import com.revolut.moneytransfer.data.InMemoryDatabase;
import com.revolut.moneytransfer.data.model.AccountCopy;
import com.revolut.moneytransfer.data.model.Transaction;
import javax.ws.rs.BadRequestException;

public class TransferTest extends AbstractTest{
	
	
	@Test
    public void transferAmountTest() {
		AccountCopy first =  target.path("accounts/create").request().post(Entity.text(""), AccountCopy.class);
		AccountCopy second = target.path("accounts/create").request().post(Entity.text(""), AccountCopy.class);
		
		UUID firstAccountId =  UUID.fromString(first.getAccountId());
		UUID secondAccountId = UUID.fromString(second.getAccountId());
		
        InMemoryDatabase.addAmountToAccount(firstAccountId,1000);
        
        first  = target.path("accounts/" + firstAccountId).request().get(AccountCopy.class);
        Assert.assertEquals(1000, first.getBalance(),0.001);
        
        WebTarget path = target.path("transfer/from/" + firstAccountId + "/to/" + secondAccountId);
        
        WebTarget webTarget = path.queryParam("reference", "TRANSFER OUT");
        Transaction txn = webTarget.queryParam("targetAmount", 20).request().post(Entity.text(""), Transaction.class);
        
        Assert.assertEquals(txn.isSuccess(), true);
        
        first  = target.path("accounts/" + first.getAccountId()).request().get(AccountCopy.class);
        second = target.path("accounts/" + second.getAccountId()).request().get(AccountCopy.class);
        
        Assert.assertEquals(980, first.getBalance(),0.001);
        Assert.assertEquals(20d, second.getBalance(), 0.001);
    }
	
	@Test(expected = BadRequestException.class)
    public void excessWithdrawalTest() {
        AccountCopy first = target.path("accounts/create").request().post(Entity.text(""), AccountCopy.class);
        AccountCopy second = target.path("accounts/create").request().post(Entity.text(""), AccountCopy.class);
        
        UUID firstAccountId = UUID.fromString(first.getAccountId());
		UUID secondAccountId = UUID.fromString(second.getAccountId());
		
        InMemoryDatabase.addAmountToAccount(firstAccountId,100);
        
        WebTarget path = target.path("transfer/from/" + firstAccountId + "/to/" + secondAccountId);
        
        WebTarget webTarget = path.queryParam("reference", "TRANSFER OUT");
        webTarget.queryParam("targetAmount", 200).request().post(Entity.text(""), Transaction.class);	

    }
	
	
	@Test
    public void testConncurrentTransactions() throws ExecutionException, InterruptedException, TimeoutException {
        AccountCopy firstAccount = target.path("accounts/create").request().post(Entity.text(""), AccountCopy.class);
        AccountCopy secondAccount = target.path("accounts/create").request().post(Entity.text(""), AccountCopy.class);
        AccountCopy thirdAccount = target.path("accounts/create").request().post(Entity.text(""), AccountCopy.class);
        
        UUID firstAccountId = UUID.fromString(firstAccount.getAccountId());
	
        InMemoryDatabase.addAmountToAccount(firstAccountId,1010);
        
        WebTarget webTarget = target.queryParam("targetAmount", 1d);
        
        ExecutorService executorService = Executors.newFixedThreadPool(60);
        
        List<Callable<Object>> tasks = new ArrayList<>(500);
        for(int i = 0; i < 250; i++) {
            tasks.add(() -> webTarget.path("/transfer/from/" + firstAccount.getAccountId() + "/to/" + secondAccount.getAccountId()).request().post(Entity.text("")));
            tasks.add(() -> webTarget.path("/transfer/from/" + firstAccount.getAccountId() + "/to/" + thirdAccount.getAccountId()).request().post(Entity.text("")));
        }
        
        List<Future<Object>> futures = executorService.invokeAll(tasks);
        for (Future<Object> future : futures) {
            future.get(100, TimeUnit.MILLISECONDS);
        }
        
        double firstAccountBalance =  target.path("accounts/" + firstAccount.getAccountId()).request().get(AccountCopy.class).getBalance();
        double secondAccountBalance = target.path("accounts/" + secondAccount.getAccountId()).request().get(AccountCopy.class).getBalance();
        double thirdAccountBalance =  target.path("accounts/" + thirdAccount.getAccountId()).request().get(AccountCopy.class).getBalance();
        
        Assert.assertEquals(510d, firstAccountBalance,  0.001);
        Assert.assertEquals(250d, secondAccountBalance,  0.001);
        Assert.assertEquals(250d, thirdAccountBalance,  0.001);
    }

}
