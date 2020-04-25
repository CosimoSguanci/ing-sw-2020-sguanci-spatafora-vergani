package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.GamePreparationCommand;
import it.polimi.ingsw.controller.GodChoiceCommand;
import it.polimi.ingsw.controller.PlayerCommand;

import java.io.*;
import java.net.Socket;


public class Client {

    private final String IP = "127.0.0.1";
    private final int PORT = 12345;

    private Socket socket;
    DataOutputStream socketOut;
    private ObjectOutputStream objectOutputStream;

    public Client() throws IOException{
        initConnection();
    }

    private void initConnection() throws IOException {
        this.socket = new Socket(IP, PORT);
        this.socketOut = new DataOutputStream(socket.getOutputStream());
    }

    public void sendString(String message) throws Exception {
        socketOut.writeUTF(message);
        socketOut.flush();
    }

    public void sendInt(int message) throws Exception {
        socketOut.writeInt(message);
        socketOut.flush();
    }

    public void sendPlayerCommand(PlayerCommand command) throws Exception {
        if(objectOutputStream == null)
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(command);
    }

    public void sendGodChoiceCommand(GodChoiceCommand command) throws Exception {
        if(objectOutputStream == null)
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(command);
    }

    public void sendGamePreparationCommand(GamePreparationCommand command) throws Exception {
        if(objectOutputStream == null)
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(command);
    }

    public Socket getSocket() {
        return this.socket;
    }

}
