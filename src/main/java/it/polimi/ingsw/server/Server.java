package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.RemoteView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 12345;
    private ServerSocket serverSocket;

    private ExecutorService executor = Executors.newFixedThreadPool(128);

    private List<ClientHandler> connections = new ArrayList<ClientHandler>();
    private Map<String, ClientHandler> waitingConnectionTwoPlayers = new HashMap<>();
    private Map<String, ClientHandler> waitingConnectionThreePlayers = new HashMap<>();

    private Map<String, ClientHandler> playingConnection = new HashMap<>();


    public synchronized void lobby(ClientHandler c, String nickname, int playersNum) {
        Map<String, ClientHandler> waitingConnection = playersNum == 2 ? waitingConnectionTwoPlayers : waitingConnectionThreePlayers;

        waitingConnection.put(nickname, c);

        if (waitingConnection.size() == playersNum) {

            try {

                Match match = Match.getInstance(String.valueOf(Thread.currentThread().getId()), playersNum);
                Model model = new Model(match);
                Controller controller = new Controller(model);

                List<String> keys = new ArrayList<>(waitingConnection.keySet());


                int i = 0;
                for(String key : keys) {
                    ClientHandler clientHandler = waitingConnection.get(key);
                    Player player = new Player(UUID.randomUUID().toString(), keys.get(i++), match);
                    match.addPlayer(player);
                    RemoteView remoteView = new RemoteView(player, clientHandler);
                    model.addObserver(remoteView);
                    remoteView.addObserver(controller);

                    model.playerUpdate(player);

                }


              //model.startMatch();


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                ClientHandler connection = new ClientHandler(this, socket);
                executor.submit(connection);
            } catch (IOException e) {
                System.err.println("Connection error");
            }
        }
    }

}