package com.sanbhu.net.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.sanbhu.net.server.StartServer;

public class TestStartServer {

	@Ignore
	@Test
	public void testServerStart(){
		StartServer.start("localhost", 50680);
		System.out.println("Server Started Successfully...");
	}
	
	@Test
	public void testConnectionToServer(){
		StartServer.start("localhost", 50680);
        try {
        	InetSocketAddress hostAddress = new InetSocketAddress("localhost", 50680);
			SocketChannel client = SocketChannel.open(hostAddress);
			Assert.assertEquals(true, client.isConnected());
			client.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
        System.out.println("Client... connection successful");
	}
}
