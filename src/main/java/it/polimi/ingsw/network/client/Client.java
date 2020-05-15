package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.commands.*;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class Client {

    private final static String IP = "127.0.0.1";
    //private final static String IP = "cosimosguanci.ddns.net";
    private final static int PORT = 12345;
    private final static int TIMEOUT_MS = 2000;

    private Socket socket;
    private DataOutputStream dataOutputStream;
    private ObjectOutputStream objectOutputStream;
    private UpdateListener updateListener;

    private final ExecutorService executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            0L, TimeUnit.SECONDS,
            new SynchronousQueue<>());


    public Client() throws IOException {
        initConnection();
    }

    private void initConnection() throws IOException {
        this.socket = new Socket(IP, PORT);
        this.socket.setSoTimeout(TIMEOUT_MS);
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        this.objectOutputStream = null;

        this.updateListener = new UpdateListener(socket);
        executor.execute(updateListener);
    }

    public void reinitializeConnection() throws IOException {
        initConnection();
    }

    public void sendInt(int message) throws IOException {
        dataOutputStream.writeInt(message);
        dataOutputStream.flush();
    }

    public void sendCommand(Command command) throws IOException {

        if(this.objectOutputStream == null) {
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

}
