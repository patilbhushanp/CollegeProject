package com.sanbhu.net.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class ServerClient {
	private InetSocketAddress hostAddress;
	private SocketChannel client;
	ServerClient(String hostName, int portNumber){
		hostAddress = new InetSocketAddress(hostName, portNumber);
	}

	public boolean connect() throws IOException {
		boolean result = false;
		client = SocketChannel.open(hostAddress);
		if(client != null)
			result = client.isConnected();
		return result;
	}

	public void closeConnection() throws IOException{
		client.close();
	}
	
}
