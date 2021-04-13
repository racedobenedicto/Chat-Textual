
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
	
	/*public void setSoTimeout(int timeout) {
		try {
		       this.serverSocket.setSoTimeout(timeout);
		        }
		    catch (IOException e) {
		       System.out.println("Error setting the timeout: \n"+e);
		    }
	}*/
	
	/*public int getSoTimeout() {
		try {
		       return this.serverSocket.getSoTimeout();
		        }
		    catch (IOException e) {
		       System.out.println("Error getting the timeout: \n"+e);
		    }
		return 0;
	}*/
	
	/*public void setReuseAddress(boolean on) {
		try {
		       this.serverSocket.setReuseAddress(on);
		        }
		    catch (IOException e) {
		       System.out.println("Error setting the reused address: \n"+e);
		    }
	}*/
	
	/*public boolean getReuseAddress() {
		try {
		       return this.serverSocket.getReuseAddress();
		        }
		    catch (IOException e) {
		       System.out.println("Error getting the reused address: \n"+e);
		    }
		return false;
	}*/
	
	
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