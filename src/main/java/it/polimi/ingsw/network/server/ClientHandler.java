package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.UUID;

public class ClientHandler extends Observable<Command> implements Runnable, Observer<Command> {

    private Server server;
    private Socket clientSocket;
    private ObjectOutputStream objectOutputStream;

    ClientHandler(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    public void sendObject(Object object) throws IOException {
        if(objectOutputStream == null)
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
    }

    @Override
    public void update(Command command) {
        notify(command);
    }

    @Override
    public void run() {
        DataInputStream input;
        String clientID;
        int playersNum;
        try {

            input = new DataInputStream(clientSocket.getInputStream());
            playersNum = input.readInt();
            clientID = UUID.randomUUID().toString();

            CommandListener commandListener = new CommandListener(clientSocket);
            new Thread(commandListener).start();
            commandListener.addObserver(this);

            server.lobby(this, clientID, playersNum);

        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error!" + e.getMessage());
        }
    }
}
