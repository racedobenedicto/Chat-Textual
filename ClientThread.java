import java.util.LinkedList;

public class ClientThread implements Runnable {

    private MySocket socket;
    private String nick;
    private ChatClient c;
    private final LinkedList<String> messagesToSend;
    private boolean hasMessages = false;

    public ClientThread(ChatClient c) {
        this.c = c;
        this.socket = new MySocket(c.getHost(), c.getPort());
        this.messagesToSend = new LinkedList<String>();
    }

    public void addNextMessage(String message) {
        synchronized (messagesToSend) {
            this.hasMessages = true;
            this.messagesToSend.push(message);
        }
    }

    @Override
    public void run() {
        try {
            socket.flush();
            Thread thInput=new Thread(){
                public void run(){
                    
                    while (socket != null && !socket.isClosed()) {
                        if (socket.ready()) {
                            String line = socket.readLine();
                            //socket.closeOut();
                            if (line != null) {
                                String[] message = line.split(":", 2);
                                switch (message[0]) {
                                    case "ACCEPT":
                                        c.setState("JOIN");
                                        break;
                                    case "JOIN":
                                        nick = message[1];
                                        c.setNick(message[1]);
                                        c.setState("MESSAGE");
                                        break;
                                    case "QUIT":
                                        socket.println("QUIT:" + nick);
                                        System.out.println("GoodBye");
                                        return;
                                    case "MESSAGE":
                                        System.out.print(c.getColour(message[1].split(" ->")[0]));
                                        System.out.println(message[1]);
                                        System.out.print(Codes.WHITE);
                                        break;
                                    case "USERS":
                                        c.updateUsers(message[1]);
                                        break;
                                }
                            }
                        }
                    }
                }
            };
            thInput.start();
            
            Thread thOutput=new Thread(){
                public void run(){
                   // socket.closeIn();
                    while (socket != null && !socket.isClosed()) {
                        hasMessages = !messagesToSend.isEmpty();
                        if (hasMessages) {
                            String nextSend = "";
                            synchronized (messagesToSend) {
                                nextSend = messagesToSend.pop();
                                if (nextSend.startsWith("QUIT:")) {
                                    c.setState("QUIT");
                                    //break;
                                }
                                hasMessages = !messagesToSend.isEmpty();
                            }

                            if (c.getState() == "ACCEPT" || c.getState() == "JOIN") {
                                socket.println(nextSend);
                            } else {
                                System.out.print(Codes.ERASE_LINE_UP);
                                socket.println(nick + " -> " + nextSend);
                            }
                            socket.flush();
                        }
                    }
                }        
            };
            thOutput.start();
            thOutput.join();
            thInput.join();
            //while (socket != null && !socket.isClosed()){}
        } catch (Exception ex) {
            System.out.println("Server not ready. \nMake sure server is listening before start connection!");
            System.exit(0);
        }
    }
}
