package it.polimi.ingsw.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class Client {

    private final String IP = "127.0.0.1";
    private final int PORT = 12345;

    private Socket socket;
    PrintWriter socketOut;


    public void initConnection() throws IOException {
        this.socket = new Socket(IP, PORT);
        this.socketOut = new PrintWriter(socket.getOutputStream());
    }

    public void sendString(String message) throws Exception {
        socketOut.println(message);
        socketOut.flush();
    }

}
