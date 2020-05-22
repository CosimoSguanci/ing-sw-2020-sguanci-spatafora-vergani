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

public class Server implements Observer<Controller> {

    private static final int PORT = 12345;
    private static final int MAX_PLAYERS_NUM = 3;
    private static boolean isActive;
    private final ServerSocket serverSocket;
    public final static int TIMEOUT_MS = 2000;
    public final static String PING_MSG = "PING";
    public final static String PONG_MSG = "PONG";

    /**
     * Cached ThreadPool without idle threads termination
     */
    private final ExecutorService executor = CustomThreadPoolExecutor.createNew();

    private final Set<ClientHandler> waitingConnections = new HashSet<>();
    private final Set<ClientHandler> playingConnections = new HashSet<>();
    private final Map<Controller, Set<ClientHandler>> controllerClientsMap = new HashMap<>();

    private final Set<Socket> pingWaitingList = new HashSet<>();

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    public static boolean isActive() {
        return isActive;
    }

    public static void setIsActive(boolean active) {
        isActive = active;
    }

    public static boolean isValidPlayerNumber(int playersNum) {
        return playersNum > 1 && playersNum <= MAX_PLAYERS_NUM;
    }

    private Set<ClientHandler> getSuitableConnectionsForMatch(int playersNum) {
        return this.waitingConnections.stream().filter((connection) -> connection.playersNum == playersNum).collect(Collectors.toSet());
    }

    public synchronized void lobby(ClientHandler newClientHandler) {

        if (!isValidPlayerNumber(newClientHandler.playersNum)) {
            throw new InvalidPlayerNumberException();
        }

        int playersNum = newClientHandler.playersNum;

        waitingConnections.add(newClientHandler);

        Set<ClientHandler> suitableConnections = getSuitableConnectionsForMatch(playersNum);

        if (suitableConnections.size() == playersNum) {

            Match match = new Match(playersNum);
            Model model = new Model(match);
            Controller controller = new Controller(model);

            for (ClientHandler clientHandler : suitableConnections) {
                Player player = new Player(clientHandler.clientID, model, match);
                playingConnections.add(clientHandler);
                waitingConnections.remove(clientHandler);
                match.addPlayer(player);
                RemoteView remoteView = new RemoteView(clientHandler);
                model.addObserver(remoteView);
                remoteView.addObserver(controller);

                clientHandler.clientSocket.getRemoteSocketAddress();
            }


            controllerClientsMap.put(controller, suitableConnections);

            controller.initialPhase();
        }
    }

    public void runServer() {
        System.out.println("Waiting for incoming connections...");
        while (isActive()) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Player connected " + socket.getInetAddress() + " : " + socket.getPort());
                ClientHandler clientHandler = new ClientHandler(this, socket);
                executor.execute(clientHandler);
            } catch (IOException e) {
                System.err.println("Connection error");
            }
        }
    }

    public synchronized void addToPingWaitingList(Socket socket) {
        this.pingWaitingList.add(socket);
    }

    public synchronized void removeFromPingWaitingList(Socket socket) {
        this.pingWaitingList.remove(socket);
    }

    public synchronized boolean waitingPingFrom(Socket socket) {
        return this.pingWaitingList.contains(socket);
    }

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


    @Override
    public void update(Controller controller) { // Notify by the Controller when a match ends, so a Player won

        Set<ClientHandler> clientHandlers = controllerClientsMap.get(controller);

        for (ClientHandler clientHandler : clientHandlers) {
            playingConnections.remove(clientHandler);
        }

        controllerClientsMap.remove(controller);
    }
}