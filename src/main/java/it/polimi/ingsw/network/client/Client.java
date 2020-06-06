package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.network.CustomThreadPoolExecutor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;


public class Client {

    //private final static String IP = "127.0.0.1";
    private final static String IP = "cosimosguanci.ddns.net";
    private final static int PORT = 12345;
    private final static int TIMEOUT_MS = 2000;
    private final ExecutorService executor = CustomThreadPoolExecutor.createNew();
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private ObjectOutputStream objectOutputStream;
    private UpdateListener updateListener;


    public Client() throws IOException {
        initConnection();
    }

    private void initConnection() throws IOException {
        this.socket = new Socket(IP, PORT);
        this.socket.setSoTimeout(TIMEOUT_MS);
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.objectOutputStream = null;
    }

    public void reinitializeConnection() throws IOException {
        initConnection();
    }

    public void setupUpdateListener() throws IOException {
        if (this.objectOutputStream == null) {
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        }
        this.updateListener = new UpdateListener(socket, objectOutputStream);
        executor.execute(updateListener);
    }


    public void sendPlayersNumber(int playersNum) throws IOException {
        dataOutputStream.writeInt(playersNum);
        dataOutputStream.flush();
    }

    public String readPlayerID() throws IOException {
        while (true) {
            try {
                return dataInputStream.readUTF();
            } catch (SocketTimeoutException e) {
                if (!isServerReachable()) {
                    socket.close();
                    break;
                }
            }
        }
        throw new IOException();
    }

    public void sendCommand(Command command) throws IOException {

        if (this.objectOutputStream == null) {
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        }

        objectOutputStream.writeObject(command);
        objectOutputStream.flush();
    }

    public Socket getSocket() {
        return this.socket;
    }

    public UpdateListener getUpdateListener() {
        return this.updateListener;
    }

    private boolean isServerReachable() {
        try {
            Socket socket = new Socket(IP, PORT);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
