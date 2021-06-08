
import java.io.*;
import java.net.*;

public class MyServerSocket {
    public ServerSocket serverSocket;

    public MyServerSocket(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (Exception ex) {}   
    }

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
}
