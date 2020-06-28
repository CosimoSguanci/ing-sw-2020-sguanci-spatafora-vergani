package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.model.updates.Update;
import it.polimi.ingsw.network.CustomThreadPoolExecutor;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import static it.polimi.ingsw.network.server.Server.TIMEOUT_MS;

public class ClientHandler extends Observable<Command> implements Runnable, Observer<Command> {

    public final String clientID;
    final Socket clientSocket;
    private final Server server;
    private final ExecutorService executor = CustomThreadPoolExecutor.createNew();
    int playersNum;
    private ObjectOutputStream objectOutputStream;

    ClientHandler(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.clientID = UUID.randomUUID().toString();
    }

    public void sendUpdate(Update update) {
        try {
            objectOutputStream.reset(); // necessary to avoid cached objects
            objectOutputStream.writeObject(update);
            objectOutputStream.flush();
        } catch (IOException e) {
            server.handleConnectionReset(this.clientSocket);
        }
    }

    @Override
    public void update(Command command) {
        notify(command);
    }

    @Override
    public void run() {
        try {
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());

            this.playersNum = input.readInt();

            while (!Server.isValidPlayerNumber(this.playersNum)) {
                this.playersNum = input.readInt();
            }

            output.writeUTF(this.clientID);

            this.clientSocket.setSoTimeout(TIMEOUT_MS);

            // Note that, if we don't open now the output stream from server to client, the client's getInputStream would block
            this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

            CommandListener commandListener = new CommandListener(clientSocket, server);
            commandListener.addObserver(this);
            executor.execute(commandListener);

            server.lobby(this);

        } catch (IOException | NoSuchElementException e) {
            server.handleConnectionReset(clientSocket);
        }
    }
}
