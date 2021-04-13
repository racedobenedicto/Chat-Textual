import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySocket {
    public Socket socket;
    BufferedReader in;
    PrintWriter out;
    String nick;

    public MySocket(String host, int port) {
        try {
            this.socket = new Socket(host, port);
            //initialize text streams
            this.initializeStreams();
        } catch (Exception ex) {}
    }

    public MySocket(String host, int port, String nick) {
        try {
            this.socket = new Socket(host, port);
            this.nick = nick;
            //initialize text streams
            this.initializeStreams();
        } catch (Exception ex) {}
    }

    public MySocket(Socket s) {
        this.socket = s;
        //initialize text streams
        this.initializeStreams();
    }

    private void initializeStreams() {
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception ex) {}
    }

    public Socket getSocket() {
        return this.socket;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String name) {
        this.nick = name;
    }    

    public BufferedReader getInputStream() {
        return this.in;
    }

    public PrintWriter getOutputStream() {
        return this.out;
    }

    public int getLocalPort() {
        return this.socket.getLocalPort();
    }

    public SocketAddress getRemoteSocketAddress() {
        return this.socket.getRemoteSocketAddress();
    }

    public void flush() {
        this.out.flush();
    }

    public int getPort() {
        return this.socket.getPort();
    }

    public void close() {
        try {
            this.in.close();
            this.out.close();
            this.socket.close();
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    public boolean isClosed() {
        return this.socket.isClosed();
    }

    public boolean ready() {
        try {
            return this.in.ready();
        } catch (IOException ex) { ex.printStackTrace();
            //System.out.println("Not ready to read: " + ex);
        }
        return false;
    }

    public String readLine() {
        String s = null;
        try {
            s = this.in.readLine();
        } catch (IOException ex) { ex.printStackTrace(); }

        return s;
    }

    public void writeLine(String s) {
        this.out.write(s);
    }

    public void println(String s) {
        this.out.println(s);
    }

    public void sendNick() {
        //this.writeLine(this.nick);
        this.out.println(this.nick);
        System.out.println("NICK SENT: " + this.nick);
    }

    public String receiveNick() {
        return this.readLine();
    }

    public String setNick() {
        this.nick = this.readLine();
        return this.nick;
    }
}