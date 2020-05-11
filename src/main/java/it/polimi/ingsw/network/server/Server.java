package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.RemoteView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Server implements Observer<Model> {
    private static final int PORT = 12345;

    private static boolean isActive;

    public static boolean isActive() {
        return isActive;
    }

    public static void setIsActive(boolean active) {
        isActive = active;
    }

    private ServerSocket serverSocket;

    private ExecutorService executor = Executors.newFixedThreadPool(128);

    private Map<Socket, ClientHandler> clientHandlersMap = new HashMap<>();

    private Map<Socket, String> waitingConnectionTwoPlayers = new HashMap<>(); // associates clientSocket with corresponding clientID
    private Map<Socket, String> waitingConnectionThreePlayers = new HashMap<>();
    private Map<Socket, String> playingConnections = new HashMap<>();

    private Map<Model, Controller> modelsControllersMap = new HashMap<>();


    public synchronized void lobby(ClientHandler c, String clientId, int playersNum) {
        Map<Socket, String> waitingConnection = playersNum == 2 ? waitingConnectionTwoPlayers : waitingConnectionThreePlayers;

        waitingConnection.put(c.getClientSocket(), clientId);

        if (waitingConnection.size() == playersNum) {

            try {

                Match match = new Match(playersNum);
                Model model = new Model(match);
                Controller controller = new Controller(model);

                modelsControllersMap.put(model, controller); // todo REMOVE when match ends

                List<Socket> keys = new ArrayList<>(waitingConnection.keySet());

                int i = 0;
                for (Socket key : keys) {
                    ClientHandler clientHandler = clientHandlersMap.get(key);
                    Player player = new Player(waitingConnection.get(keys.get(i++)), model, match);

                    playingConnections.put(key, player.ID); // todo REMOVE when match ends

                    waitingConnection.remove(key);

                    match.addPlayer(player);
                    RemoteView remoteView = new RemoteView(player, clientHandler);
                    model.addObserver(remoteView);
                    remoteView.addObserver(controller);
                    model.playerUpdate(player);
                }
                // todo start a new thread for the match
                controller.initialPhase();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    public void runServer() {
        System.out.println("Waiting for incoming connections...");
        while (isActive()) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Player connected " + socket.getInetAddress() + " : " + socket.getPort());
                ClientHandler clientHandler = new ClientHandler(this, socket);
                executor.submit(clientHandler);
                clientHandlersMap.put(socket, clientHandler);
            } catch (IOException e) {
                System.err.println("Connection error");
            }
        }
    }

    void handleConnectionReset(Socket clientSocket) { // Removes client form waiting Maps
        clientHandlersMap.remove(clientSocket);

        String clientPlayerID = playingConnections.remove(clientSocket);

        if(clientPlayerID != null) {
            searchAndRemovePlayerInMatch(clientPlayerID);

            playingConnections.remove(clientSocket);
        }
        else {
            waitingConnectionTwoPlayers.remove(clientSocket);
            waitingConnectionThreePlayers.remove(clientSocket);
        }

    }

    private void searchAndRemovePlayerInMatch(String playerID) {

        modelsControllersMap.entrySet().parallelStream().forEach((entry) -> {
            Model model = entry.getKey();
            Controller matchController = entry.getValue();

            List<Player> players = model.getPlayers().stream().filter((player) -> player.ID.equals(playerID)).collect(Collectors.toList());

            if(players.size() > 0) { // Match found
                matchController.onPlayerDisconnected(playerID);
            }

        });

    }

    @Override
    public void update(Model model) {
        modelsControllersMap.remove(model);
    }
}