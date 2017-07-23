package com.sanbhu.net.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StartServer {
    private Map<SocketChannel,List<?>> dataMapper;
    private InetSocketAddress listenAddress;
	private Selector selector;
	
	public StartServer(String hostName, int portNumber){
		listenAddress = new InetSocketAddress(hostName, portNumber);
        dataMapper = new HashMap<SocketChannel,List<?>>();
	}
	
	public static void main(String[] args){
		start("localhost", 50680);
		System.out.println("Server Started...");
	}
	
	public static void start(final String hostName, final int portNumber){
		Runnable startServerThread = new Runnable(){
			public void run() {
				try {
					new StartServer(hostName, portNumber).startServer();
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			};
		};
		Thread serverThread = new Thread(startServerThread);
		serverThread.start();
	}
	
	private void startServer()throws IOException{
		this.selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(this.listenAddress);
        serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        
        while(true) {
        	this.selector.select();
        	Iterator<SelectionKey> keyIterator = this.selector.selectedKeys().iterator();
        	while(keyIterator.hasNext()){
        		SelectionKey selectionKey = keyIterator.next();
        		keyIterator.remove();
        		if(!selectionKey.isValid()){
        			continue;
        		}
        		if(selectionKey.isAcceptable()){
                    this.accept(selectionKey);
                }else if(selectionKey.isReadable()){
                    this.read(selectionKey);
                }
        	}
        }
	}
	
    @SuppressWarnings("rawtypes")
	private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        Socket socket = channel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        System.out.println("Connected to: " + remoteAddr);
        dataMapper.put(channel, new ArrayList());
        channel.register(this.selector, SelectionKey.OP_READ);
    }
    
    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int numRead = -1;
        numRead = channel.read(buffer);
        if (numRead == -1) {
            this.dataMapper.remove(channel);
            Socket socket = channel.socket();
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
            System.out.println("Connection closed by client: " + remoteAddr);
            channel.close();
            key.cancel();
            return;
        }
        byte[] data = new byte[numRead];
        System.arraycopy(buffer.array(), 0, data, 0, numRead);
        System.out.println("Got: " + new String(data));
    }
}
