import java.io.*;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.*;

public class ChatServer {
    private static int port;
    private static final int maxThreads = 50;
    private static Set<String> clientNicks = new HashSet<>();
    private static Set<PrintWriter> clients = new HashSet<>();

    public static void main(String[] args) {

        port = 5000;

        if (args.length == 1 && !args[0].isEmpty()) {
            port = Integer.parseInt(args[0]);
        }

        ExecutorService pool = Executors.newFixedThreadPool(maxThreads);
        MyServerSocket serversocket;
        try {
            serversocket = new MyServerSocket(port);
            System.out.print(Codes.CLEAR);
            System.out.println("Server is listening to port " + port);

            while (true) {
                pool.execute(new Handler(serversocket.accept()));
            }
        } catch (Exception ex) {
            System.out.println("Error while listening to port " + port + ": " + ex);
        }
    }

    private static class Handler implements Runnable {

        private MySocket socket;
        private BufferedReader in;
        private PrintWriter out;

        public Handler(MySocket s) {
            this.socket = s;
        }

        public void run() {
            try {
                String name;
                in = new BufferedReader(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    socket.println("ACCEPT:");
                    name = socket.readLine();
                    if (name == null) {
                        return;
                    }
                    
                    synchronized (clientNicks) {
                        if (!name.isEmpty() && !clientNicks.contains(name)) {
                            socket.setNick(name);
                            clientNicks.add(name);
                            break;
                        }
                    }
                }

                //Notify new Join in Chat
                socket.println("JOIN:" + name);
                System.out.println(name + " has joined");
                System.out.println("Users: " + clientNicks.toString());
                socket.println("USERS:" + clientNicks.toString());
                //Broadcast message of join
                for (PrintWriter writer : clients) {
                    writer.println("MESSAGE:" + "Admin" + " -> " + name + " has joined");
                    writer.println("USERS:" + clientNicks.toString());

                }

                clients.add(socket.getOutputStream());

                while (true) {
                    String input = socket.readLine();
                    if (input.toLowerCase().startsWith("QUIT:" + name)) {
                        return;
                    }               
                    for (PrintWriter writer : clients) {
                        writer.println("MESSAGE:" + input);
                    }
                }
            } catch (Exception ex) {
            } finally {
                if (socket.getNick() != null) {
                    if (socket != null) {
                        clients.remove(socket.getOutputStream());
                    }
                    if (socket.getNick() != null) {
                        System.out.println(socket.getNick() + " is leaving");
                        clientNicks.remove(socket.getNick());
                        for (PrintWriter writer : clients) {
                            writer.println("MESSAGE:Admin -> " + socket.getNick() + " has left");
                            writer.println("USERS:" + clientNicks.toString());
                        }
                    }
                    socket.close();
                }
            }
        }
    }
}