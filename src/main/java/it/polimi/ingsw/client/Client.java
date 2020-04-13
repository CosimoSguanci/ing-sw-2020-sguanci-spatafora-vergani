package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.PlayerCommand;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;


public class Client extends Observable<Object> {

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

    public void sendPlayerCommand(PlayerCommand command) throws Exception {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(command);
    }

    private void listen() {
        try {
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            while(true) {
                Object inputObject = input.readObject();
                notify(inputObject);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}
