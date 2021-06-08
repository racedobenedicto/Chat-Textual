
import java.io.*;
import java.net.*;

public class MyServerSocket {
    public ServerSocket serverSocket;

    public MyServerSocket(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (Exception ex) {}   
    }

    public MyServerSocket(int port, int backlog) {
        try {
            this.serverSocket = new ServerSocket(port, backlog);
        } catch (Exception ex) {}   
    }
    
    public MyServerSocket(ServerSocket ss) {
        try {
            this.serverSocket = ss;
        } catch (Exception ex) {}   
    }

    public void bind(SocketAddress endpoint) {
        try{
            this.serverSocket.bind(endpoint);
        } catch (IOException ex) { ex.printStackTrace(); }
    }
    //bind con backlog

    //accept que retorna Socket
    public MySocket accept() {
        MySocket socket = null;
        try {
            Socket s = serverSocket.accept();
            socket = new MySocket(s);
        } catch (Exception ex) { ex.printStackTrace(); }
        return socket;
    }

    public void close() {
        try {
		    this.serverSocket.close();
		} catch (IOException ex) { ex.printStackTrace(); }		
	}	
	
	public void setReceiveBufferSize(int size) {
		try {
		    this.serverSocket.setReceiveBufferSize(size);
		} catch (IOException ex) { ex.printStackTrace(); }
	}
	
	public int getReceiveBufferSize() {
		int bufferSize = 0;
		try {
		    bufferSize = this.serverSocket.getReceiveBufferSize();
		} catch (IOException ex) { ex.printStackTrace(); }

		return bufferSize;
	}
}
