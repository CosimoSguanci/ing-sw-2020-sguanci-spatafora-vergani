package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.InvalidPlayerNumberException;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.CustomThreadPoolExecutor;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.RemoteView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;


/**
 * Server class handles all what is related to network for server-side. In particular, this project
 * is based on a distributed client-server approach through socket: server is where Model and Controller (MVC
 * pattern) reside, so it is game's handler in all the senses; it waits for clients' connections on a specific
 * PORT, organizes matches when player number has been reached and, once a match started, shares
 * appropriate objects with clients.
 * Server can handle multiple matches at the same time.
 * Network connection between server and client is based on TCP.
 *
 * @author Andrea Mario Vergani
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 */
public class Server implements Observer<Controller> {

    public final static int TIMEOUT_MS = 5000;
    public final static String PING_MSG = "PING";
    private static final int PORT = 12345;
    private static final int MAX_PLAYERS_NUM = 3;
    private static boolean isActive;
    private final ServerSocket serverSocket;
    /**
     * Cached ThreadPool without idle threads termination
     */
    private final ExecutorService executor = CustomThreadPoolExecutor.createNew();

    private final Set<ClientHandler> waitingConnections = new HashSet<>();
    private final Set<ClientHandler> playingConnections = new HashSet<>();
    private final Map<Controller, Set<ClientHandler>> controllerClientsMap = new HashMap<>();

    private final Set<Socket> pingWaitingList = new HashSet<>();

    /**
     * The constructor creates a server-side socket on the given port (class attribute). Server waits for
     * connections coming from clients.
     *
     * @throws IOException if an I/O error occurs when opening the socket
     */
    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    /**
     * The method gives information about server socket's availability.
     *
     * @return true if server is active (available for connection), false otherwise
     */
    public static boolean isActive() {
        return isActive;
    }

    /**
     * This method is the setter for isActive attribute, which keeps information about server socket's
     * availability.
     *
     * @param active true to set that Server is active (available), false otherwise
     */
    public static void setIsActive(boolean active) {
        isActive = active;
    }

    /**
     * The method checks if a player-number (for a match) received by client is valid or not. Remember that
     * Santorini is an online game for 2 or 3 players; opponent's number can be decided during connection to
     * a match.
     *
     * @param playersNum match's number of players (selected client-side)
     * @return true if parameter is a valid player number for an online match; false otherwise
     */
    public static boolean isValidPlayerNumber(int playersNum) {
        return playersNum > 1 && playersNum <= MAX_PLAYERS_NUM;
    }

    /**
     * The method returns all client handlers that are currently waiting for a specific kind of match (2-players
     * or 3-players match).
     *
     * @param playersNum player-number that identifies the kind of match
     * @return a set of all client handlers waiting for the given kind of match
     */
    private Set<ClientHandler> getSuitableConnectionsForMatch(int playersNum) {
        return this.waitingConnections.stream().filter((connection) -> connection.playersNum == playersNum).collect(Collectors.toSet());
    }


    /**
     * The method handles decisions about wait or start a match, based on the number of players that are waiting.
     * A 2-players match can start only when there are two players waiting for it; so, the first player waits
     * until a second one connects to server and selects he/she wants to play a 2-players match. Same for a
     * 3-players match, where the first two players wait and, as soon as a third player connects, the match starts.
     * Server supports multiple matches, so if (for example) a 3-player match is active and three other players
     * want to start another match, they can.
     * Handling 2-players and 3-players matches is separated, in the sense that a player who wants to play a
     * 2-players match will not start until another player wants to play the same kind of match (while if another
     * player selects 3-players match, no match will immediately start).
     * If correct player-number for a match is reached, the match starts.
     *
     * @param newClientHandler object that handles connection between new client in the lobby and server
     * @throws InvalidPlayerNumberException if player-number selected client-side is not valid
     */
    public synchronized void lobby(ClientHandler newClientHandler) {

        if (!isValidPlayerNumber(newClientHandler.playersNum)) {
            throw new InvalidPlayerNumberException();
        }

        int playersNum = newClientHandler.playersNum;

        this.waitingConnections.add(newClientHandler);

        Set<ClientHandler> suitableConnections = getSuitableConnectionsForMatch(playersNum);

        if (suitableConnections.size() == playersNum) {

            Match match = new Match(playersNum);
            Model model = new Model(match);
            Controller controller = new Controller(model);

            for (ClientHandler clientHandler : suitableConnections) {
                Player player = new Player(clientHandler.clientID, model, match);
                this.playingConnections.add(clientHandler);
                this.waitingConnections.remove(clientHandler);
                match.addPlayer(player);
                RemoteView remoteView = new RemoteView(clientHandler);
                model.addObserver(remoteView);
                remoteView.addObserver(controller);

                clientHandler.clientSocket.getRemoteSocketAddress();
            }


            this.controllerClientsMap.put(controller, suitableConnections);

            controller.initialPhase();
        }
    }

    /**
     * The method makes server active (effectively); through this, server is able to accept connections coming
     * from clients. A client-handler is created and executed for every client connected to server.
     */
    public void runServer() {
        System.out.println("Waiting for incoming connections...");
        while (isActive()) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Player connected " + socket.getInetAddress() + " : " + socket.getPort());
                ClientHandler clientHandler = new ClientHandler(this, socket);
                this.executor.execute(clientHandler);
            } catch (IOException e) {
                System.err.println("Connection error");
            }
        }
    }

    /**
     * Server must always control that all connected clients are always active, so there are no connection errors
     * or something like this. To do that, a ping-message mechanism is performed: pings are continuously sent
     * only by clients, while server waits for them.
     * The method adds an active client to ping waiting list; from this moment on, client's connection is checked
     * by server, and if a ping-message does not arrive client is considered to have disconnected.
     *
     * @param socket socket on which client must send ping-messages
     */
    public synchronized void addToPingWaitingList(Socket socket) {
        this.pingWaitingList.add(socket);
    }

    /**
     * Server must always control that all connected clients are always active, so there are no connection errors
     * or something like this. To do that, a ping-message mechanism is performed: pings are continuously sent
     * only by clients, while server waits for them.
     * The method removes a client from ping waiting list.
     *
     * @param socket socket on which client was sending ping-messages
     */
    public synchronized void removeFromPingWaitingList(Socket socket) {
        this.pingWaitingList.remove(socket);
    }

    /**
     * Server must always control that all connected clients are always active, so there are no connection errors
     * or something like this. To do that, a ping-message mechanism is performed: pings are continuously sent
     * only by clients, while server waits for them.
     * The method tells if server is currently waiting for a ping-message on the given socket.
     *
     * @param socket socket on which client sends ping-messages
     * @return true if server is waiting for ping-message on the given socket, false otherwise
     */
    public synchronized boolean waitingPingFrom(Socket socket) {
        return this.pingWaitingList.contains(socket);
    }

    /**
     * The method handles a request for connection reset by one of the clients. When this request happens, the
     * match in which client is involved must end, and all other clients connected are notified.
     *
     * @param clientSocket socket of client that requested a disconnection from server
     */
    void handleConnectionReset(Socket clientSocket) {

        Optional<ClientHandler> clientHandlerToRemoveOpt = this.playingConnections.stream().filter((clientHandler -> clientHandler.clientSocket.equals(clientSocket))).findFirst();

        if (clientHandlerToRemoveOpt.isPresent()) {
            // need to end the match

            ClientHandler clientHandlerToRemove = clientHandlerToRemoveOpt.get();

            Optional<Controller> controllerToRemoveOpt = this.controllerClientsMap.entrySet().stream().filter((mapping) -> mapping.getValue().contains(clientHandlerToRemove)).map(Map.Entry::getKey).findFirst();

            if (controllerToRemoveOpt.isPresent()) {

                Controller controllerToRemove = controllerToRemoveOpt.get();
                Set<String> clientsIDToRemove = this.controllerClientsMap.get(controllerToRemove).stream().map(clientHandler -> clientHandler.clientID).collect(Collectors.toSet());
                this.playingConnections.removeIf((clientHandler -> clientsIDToRemove.contains(clientHandler.clientID)));
                this.controllerClientsMap.remove(controllerToRemove);

                controllerToRemove.onPlayerDisconnected(clientHandlerToRemove.clientID); // Notify users through Controller
            }

        } else {
            this.waitingConnections.removeIf((clientHandler -> clientHandler.clientSocket.equals(clientSocket)));
        }

    }

    /**
     * Updates implemented following Observer Pattern.
     * This method is fired from the Controller (Server is an observer of Controller) when a Player wins the Match,
     * in order to start the procedures needed to gracefully end a match and remove all the reference to it.
     *
     * @param controller the Controller instance corresponding to the Match which is about to end.
     */
    @Override
    public void update(Controller controller) {

        Set<ClientHandler> clientHandlers = controllerClientsMap.get(controller);

        for (ClientHandler clientHandler : clientHandlers) {
            playingConnections.remove(clientHandler);
        }

        controllerClientsMap.remove(controller);
    }
}