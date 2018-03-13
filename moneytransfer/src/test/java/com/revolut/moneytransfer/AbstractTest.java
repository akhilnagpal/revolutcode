package com.revolut.moneytransfer;

import java.net.URI;
import java.util.concurrent.Executors;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Before;

import com.revolut.moneytransfer.jsonresource.AccountMapper;
import com.revolut.moneytransfer.jsonresource.TransferMapper;
import com.revolut.moneytransfer.mapper.IllegalArgumentExceptionMapper;

import com.sun.net.httpserver.HttpServer;

public class AbstractTest {
	
	@SuppressWarnings("restriction")
	HttpServer server;
	
	protected WebTarget target = ClientBuilder.newBuilder().register(MoxyJsonFeature.class).build().target("http://localhost:9977");
	
	@Before
    public void setUp() {
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(9977).build();

        ResourceConfig config = new ResourceConfig(MoxyJsonFeature.class,
                AccountMapper.class, TransferMapper.class,IllegalArgumentExceptionMapper.class);
        server = JdkHttpServerFactory.createHttpServer(baseUri, config, false);
        server.setExecutor(Executors.newFixedThreadPool(5));
        server.start();
    }


    @After
    public void tearDown() {
        server.stop(0);
    }

}
