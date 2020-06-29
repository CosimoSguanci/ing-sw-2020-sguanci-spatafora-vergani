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

/**
 * Runnable implementation which is instantiated every time a new client establishes a connection with the server.
 * It handles the initial messages exchanged by client and server, and gets notified from {@link CommandListener}
 * every time a new {@link Command} is received from a player.
 *
 * @author Andrea Mario Vergani
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 */
public class ClientHandler extends Observable<Command> implements Runnable, Observer<Command> {

    /**
     * The client identifier (random UUID String)
     */
    public final String clientID;
    final Socket clientSocket;
    private final Server server;
    private final ExecutorService executor = CustomThreadPoolExecutor.createNew();
    int playersNum;
    private ObjectOutputStream objectOutputStream;

    /**
     * In the constructor, the random client ID is generated.
     *
     * @param server       server instance to call the lobby method.
     * @param clientSocket the socket which identifies the client to be handled by this instance.
     */
    ClientHandler(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.clientID = UUID.randomUUID().toString();
    }

    /**
     * Method called to send an {@link Update} from server to client (Model-View communication in MVC pattern).
     *
     * @param update the update representing game state changes, used to notify all the clients.
     */
    public void sendUpdate(Update update) {
        try {
            objectOutputStream.reset(); // necessary to avoid cached objects
            objectOutputStream.writeObject(update);
            objectOutputStream.flush();
        } catch (IOException e) {
            server.handleConnectionReset(this.clientSocket);
        }
    }

    /**
     * update method from Observer pattern. Specifically, this class observes {@link CommandListener} in order to notify the {@link it.polimi.ingsw.controller.Controller}
     * that a new {@link Command} has been sent. The controller will then handle the command and change the game state accordingly.
     *
     * @param command the command object received from the client.
     */
    @Override
    public void update(Command command) {
        notify(command);
    }

    /**
     * In the Thread execution flow, this class handle the initial communication between client and server. It receives the
     * players number chosen by the client (and checks its correctness), and send to the client its unique ID, generate using UUID random method.
     * Then, the {@link CommandListener} Thread responsible to listen for commands is started, and the {@link Server#lobby(ClientHandler)} method is called
     * to insert the client in the waiting connections list (or start a new match if the right number of players is reached).
     */
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
