package com.revolut.moneytransfer;

import java.net.URI;
import java.util.UUID;

import com.revolut.moneytransfer.data.InMemoryDatabase;
import com.revolut.moneytransfer.jsonresource.AccountMapper;
import com.revolut.moneytransfer.jsonresource.TransferMapper;
import com.sun.net.httpserver.HttpServer;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ResourceConfig;

@SuppressWarnings("restriction")
public class MoneyTransferApplication {
	public static void main(String args[]) {
		URI baseUri = UriBuilder.fromUri("http://localhost/").port(9977).build();
		
        ResourceConfig config = new ResourceConfig(MoxyJsonFeature.class, AccountMapper.class,TransferMapper.class);
        UUID myAccountId = UUID.fromString(InMemoryDatabase.createAccount("Akhil Nagpal").getAccountId());
        InMemoryDatabase.addAmountToAccount(myAccountId, 100);
		
		HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, config);
	}

}
