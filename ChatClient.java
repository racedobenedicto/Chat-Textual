import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatClient {

    private static String host;
    private static int port = 5000;
    private String nick;
    private String state;
    private List<String> users;

    public ChatClient() {
        this.state = "ACCEPT";
        this.users = new ArrayList<>();
    }

    public void startClient() {
        try {
            Scanner scan = new Scanner(System.in);
            ClientThread clientThread = new ClientThread(this);
            Thread serverAccessThread = new Thread(clientThread);
            serverAccessThread.start();
            System.out.print(Codes.CLEAR);
            while (serverAccessThread.isAlive()) {
                switch (state) {
                    case "ACCEPT":
                        clientThread.addNextMessage(this.getNick());
                        this.state = "JOIN";
                        break;
                    case "JOIN":
                        break;
                    case "MESSAGE":
                        if (scan.hasNextLine()) {
                            clientThread.addNextMessage(scan.nextLine());
                        }
                        break;
                }
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    public void updateUsers(String users) {
        System.out.println("Users connected: " + users);
        users = users.substring(1, users.length()-1);
        for (String name : users.split(", ")) {
            if (!this.users.contains(name)) {
                this.users.add(name);
            }
        }
    }

    public String getColour(String name) {
        if(name.equals("Admin")){
            return(Codes.WHITE);
        }
        return (Codes.COLOURS[this.users.indexOf(name)% (Codes.COLOURS.length-1)]);
    }

    public String getNick() {
        String readNick = "";
        Scanner scan = new Scanner(System.in);
        System.out.print("Please input nick: ");
        while (readNick == null || readNick.trim().equals("")) {
            // null, empty, whitespace(s) not allowed.
            readNick = scan.nextLine();
            if (readNick.trim().equals("") || this.users.contains(readNick)) {
                System.out.println("Invalid. Please enter nick again:");
                readNick="";
            }
        }
        return readNick;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getState() {
        return this.state;
    }

    public static void main(String[] args) {
        host = "localhost";
        port = 5000;
        if (args.length <= 2 && args.length > 0) {
            if (!args[0].isEmpty()) {
                host = args[0];
                if (!args[1].isEmpty()) {
                    port = Integer.parseInt(args[1]);
                }
            }
        }
        ChatClient client = new ChatClient();
        client.startClient();
    }
}