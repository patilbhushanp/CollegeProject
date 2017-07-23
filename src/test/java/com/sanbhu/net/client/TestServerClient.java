package com.sanbhu.net.client;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestServerClient {

	private static ServerClient serverClient;
	
	@BeforeClass
	public static void initClient(){
		serverClient = new ServerClient("localhost", 50680);
	}
	
	@Test
	public void testClientConnection()throws IOException{
		Assert.assertEquals(true, serverClient.connect());
	}
	
	@AfterClass
	public static void closeConnection()throws IOException{
		serverClient.closeConnection();
	}
}
