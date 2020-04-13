package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.PlayerCommand;
import it.polimi.ingsw.observer.Observable;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

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
        Scanner inputScanner;
        String nickname;
        String command;
        try {
            inputScanner = new Scanner(clientSocket.getInputStream());
            nickname = inputScanner.nextLine();
            server.lobby(this, nickname);
            while (true) {
                command = inputScanner.nextLine();
                //   notify(read);
            }
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error!" + e.getMessage());
        }
    }
}
