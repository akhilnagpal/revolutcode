package com.revolut.moneytransfer.jsonresource;

import java.util.UUID;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.revolut.moneytransfer.data.InMemoryDatabase;
import com.revolut.moneytransfer.data.model.Transaction;

@Path("/transfer/from/{from}/to/{to}")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class TransferMapper {
	
		@POST
	    public Transaction transfer(@PathParam("from") UUID from,
	                                @PathParam("to") UUID to,	                               
	                                @QueryParam("targetAmount") double targetAmount,
	                                @QueryParam("reference") String reference) {
			return InMemoryDatabase.transferMoney(from, to,targetAmount, reference);
	        
	    }

}
