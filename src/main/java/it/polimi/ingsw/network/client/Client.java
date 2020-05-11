package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.commands.GamePreparationCommand;
import it.polimi.ingsw.controller.commands.GodChoiceCommand;
import it.polimi.ingsw.controller.commands.InitialInfoCommand;
import it.polimi.ingsw.controller.commands.PlayerCommand;

import java.io.*;
import java.net.Socket;


public class Client {

    private final static String IP = "127.0.0.1";
    //private final static String IP = "cosimosguanci.ddns.net";
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

    public void closeConnection() throws IOException {
        this.socket.close();
    }

    public void reinitializeConnection() throws IOException {
        initConnection();
        this.objectOutputStream = null;
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

    public void sendInitialInfoCommand(InitialInfoCommand command) throws IOException { // todo make only one method to send commands
        if(objectOutputStream == null)
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(command);
        objectOutputStream.flush();
    }

    public Socket getSocket() {
        return this.socket;
    }

    public static boolean isServerReachable() {

        try {
            Socket pingSocket = new Socket(IP, PORT);
            pingSocket.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
