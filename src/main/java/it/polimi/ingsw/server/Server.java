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
    private Map<String, ClientHandler> waitingConnection = new HashMap<>();
    private Map<String, ClientHandler> playingConnection = new HashMap<>();


    public synchronized void lobby(ClientHandler c, String nickname) {

        waitingConnection.put(nickname, c);
        if (waitingConnection.size() == 2) { // 3/4? Chiediamo all'inizio?

            try {
                List<String> keys = new ArrayList<>(waitingConnection.keySet());
                ClientHandler c1 = waitingConnection.get(keys.get(0));
                ClientHandler c2 = waitingConnection.get(keys.get(1));
                Match match = new Match(2);
                Player player1 = new Player(UUID.randomUUID().toString(), keys.get(0), match);
                Player player2 = new Player(UUID.randomUUID().toString(), keys.get(1), match);
                RemoteView remoteView1 = new RemoteView(player1, c1);
                RemoteView remoteView2 = new RemoteView(player2, c2);
                Model model = new Model(match);
                Controller controller = new Controller(model);
                model.addObserver(remoteView1);
                model.addObserver(remoteView2);
                remoteView1.addObserver(controller);
                remoteView2.addObserver(controller);

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