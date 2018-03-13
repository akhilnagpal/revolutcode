package com.revolut.moneytransfer.jsonresource;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.revolut.moneytransfer.data.InMemoryDatabase;
import com.revolut.moneytransfer.data.model.AccountCopy;

@Path("/accounts")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class AccountMapper {
	
	@GET
    @Path("/{accountid}")
    public AccountCopy  getAccount(@PathParam("accountid") UUID accountId) {
        return InMemoryDatabase.getAccountDetails(accountId);
    }
	
	@POST
    @Path("/create")
    public AccountCopy  createAccount(@QueryParam("name") String name) {
        return InMemoryDatabase.createAccount(name == null ? "" : name);
    }


}
