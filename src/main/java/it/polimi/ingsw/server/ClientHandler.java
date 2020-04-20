package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.PlayerCommand;
import it.polimi.ingsw.observer.Observable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;

public class ClientHandler extends Observable<PlayerCommand> implements Runnable {

    private Server server;
    private Socket clientSocket;

    ClientHandler(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    void sendRequest(Request req) throws IOException {
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        out.writeInt(req.getRequestNumber());
    }

    public void sendObject(Object object) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        objectOutputStream.writeObject(object);
    }

    @Override
    public void run() {
        DataInputStream input;
        String nickname;
        int playersNum;
        String command;
        try {
            input = new DataInputStream(clientSocket.getInputStream());
            nickname = input.readUTF();
            playersNum = input.readInt();
            server.lobby(this, nickname, playersNum);
            while (true) {
                //command = inputScanner.nextLine();
                //   notify(read);
            }
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error!" + e.getMessage());
        }
    }
}
