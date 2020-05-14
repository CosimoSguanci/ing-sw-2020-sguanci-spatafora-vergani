package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.model.updates.Update;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.UUID;

public class ClientHandler extends Observable<Command> implements Runnable, Observer<Command> {

    private final Server server;
    private ObjectOutputStream objectOutputStream;
    final Socket clientSocket;
    final String clientID;
    int playersNum;

    ClientHandler(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.clientID = UUID.randomUUID().toString();
    }

    public void sendUpdate(Update update) throws IOException {
        objectOutputStream.writeObject(update);
        objectOutputStream.flush();
    }

    @Override
    public void update(Command command) {
        notify(command);
    }

    @Override
    public void run() {
        DataInputStream input;
        try {

            this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream()); // NB SE NON APRI LO STREAM IN OUTPUT DA SERVER A CLIENT LA GETINPUTSTREAM DEL CLIENT SI BLOCCA -> e va in timeoutexception se c'è un timeout

            input = new DataInputStream(clientSocket.getInputStream());
            this.playersNum = input.readInt();

            CommandListener commandListener = new CommandListener(clientSocket, server);
            new Thread(commandListener).start();
            commandListener.addObserver(this);

            server.lobby(this);

        } catch (IOException | NoSuchElementException e) {
            // todo remove from clientHandlersMap
            server.handleConnectionReset(clientSocket);
            System.err.println("Error! " + e.getMessage());
        }
    }
}
