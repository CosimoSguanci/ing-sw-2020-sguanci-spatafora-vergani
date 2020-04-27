package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.commands.GamePreparationCommand;
import it.polimi.ingsw.controller.commands.GodChoiceCommand;
import it.polimi.ingsw.controller.commands.PlayerCommand;

import java.io.*;
import java.net.Socket;


public class Client {

    private final static String IP = "127.0.0.1";
    private final static int PORT = 12345;

    private Socket socket;
    DataOutputStream socketOut;
    private ObjectOutputStream objectOutputStream;

    public Client() throws IOException {
        initConnection();
    }

    private void initConnection() throws IOException {
        this.socket = new Socket(IP, PORT);
        this.socketOut = new DataOutputStream(socket.getOutputStream());
    }

    public void sendString(String message) throws IOException {
        socketOut.writeUTF(message);
        socketOut.flush();
    }

    public void sendInt(int message) throws IOException {
        socketOut.writeInt(message);
        socketOut.flush();
    }

    public void sendPlayerCommand(PlayerCommand command) throws IOException {
        if(objectOutputStream == null)
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(command);
        objectOutputStream.flush();
    }

    public void sendGodChoiceCommand(GodChoiceCommand command) throws IOException {
        if(objectOutputStream == null)
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(command);
        objectOutputStream.flush();
    }

    public void sendGamePreparationCommand(GamePreparationCommand command) throws IOException {
        if(objectOutputStream == null)
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(command);
        objectOutputStream.flush();
    }

    public Socket getSocket() {
        return this.socket;
    }

}
