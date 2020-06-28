package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.network.CustomThreadPoolExecutor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Client class handles all what is related to network for client-side. In particular, this project
 * is based on a distributed client-server approach through socket: client is the "place" from which
 * a player sends commands to server, where he/she receives updates, ...; it connects to server's
 * specific (IP,port) and shares appropriate objects with it.
 * Network connection between client and server is based on TCP.
 *
 * @author Andrea Mario Vergani
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 */
public class Client {

    //private final static String IP = "SantoriniServer-env.eba-idxatybv.us-east-1.elasticbeanstalk.com";
    private final static String IP = "cosimosguanci.ddns.net";
    //private final static String IP = "116.203.106.110";
    //private final static String IP = "127.0.0.1";
    private final static int PORT = 12345;
    private final static int TIMEOUT_MS = 2000;
    private final static int PONG_SCHEDULE_TIME_MS = 500;
    private final ExecutorService executor = CustomThreadPoolExecutor.createNew();
    private ScheduledExecutorService pongScheduler;
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private ObjectOutputStream objectOutputStream;
    private UpdateListener updateListener;

    /**
     * The constructor creates a Client object and initializes client-server socket connection.
     * Server is supposed to be active on specified IP and PORT.
     *
     * @throws IOException if connection can't be established, for any reason (network error,
     *                     unavailable server, ...)
     */
    public Client() throws IOException {
        initConnection();
    }

    /**
     * This private method establishes effective connection between Client object and server.
     * Streams for socket communication, both input and output, are created.
     *
     * @throws IOException if connection can't be established, for any reason (network error,
     *                     unavailable server, ...)
     */
    private void initConnection() throws IOException {
        this.socket = new Socket(IP, PORT);
        this.socket.setSoTimeout(TIMEOUT_MS);
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.objectOutputStream = null;
    }


    /**
     * The method's task is to initialize a client-server socket connection from an already existing
     * Client object; so, this method differs from constructor because the object exists. The method's
     * main usage is that of establishing a new connection after closing one: in fact, after a match,
     * a player can decide to play another match; in this case, this method is called, in order to have
     * a new connection to server.
     *
     * @throws IOException if connection can't be established, for any reason (network error,
     *                     unavailable server, ...)
     */
    public void reinitializeConnection() throws IOException {
        initConnection();
    }


    /**
     * This method initializes listener for updates coming from Server; in fact, client and
     * server send/receive objects through socket. In this method, ping message sender is created
     * too, and it becomes active: this allows server to control if all clients are connected (if
     * not, the match must immediately end).
     *
     * @throws IOException if an I/O error occurs when creating the output stream or if the socket
     *                     is not connected
     */
    public void setupUpdateListener() throws IOException {
        if (this.objectOutputStream == null) {
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        }
        this.updateListener = new UpdateListener(socket);
        executor.execute(updateListener);
        this.pongScheduler = Executors.newScheduledThreadPool(1);
        pongScheduler.scheduleAtFixedRate(new PingSender(this.objectOutputStream, this.pongScheduler), 0, PONG_SCHEDULE_TIME_MS, TimeUnit.MILLISECONDS);
    }


    /**
     * This method sends to server the number of players selected for the match. A Santorini match
     * can be played by 2 or 3 people, and every player decides, before the match itself, his/her
     * opponent's number.
     *
     * @param playersNum number of players of the match: must be 2 or 3
     * @throws IOException if an I/O error occurs
     */
    public void sendPlayersNumber(int playersNum) throws IOException {
        dataOutputStream.writeInt(playersNum);
        dataOutputStream.flush();
    }


    /**
     * This method simply returns a String representing the unique ID given to player by server.
     *
     * @return unique player ID given by server
     * @throws IOException if an I/O error occurs when doing some of the operations (like reading
     *                     or closing socket, if server is unavailable)
     */
    public String readPlayerID() throws IOException {
        while (true) {
            try {
                return dataInputStream.readUTF();
            } catch (SocketTimeoutException e) {
                if (!isServerReachable()) {
                    socket.close();
                    break;
                }
            }
        }
        throw new IOException();
    }


    /**
     * The method sends a Command, generated client-side, to server. In general, Client sends
     * Commands to Server, while receives Updates from it.
     *
     * @param command the command to send to server, through socket
     */
    public void sendCommand(Command command) {

        try {

            if (this.objectOutputStream == null) {
                this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            }

            objectOutputStream.writeObject(command);
            objectOutputStream.flush();

        } catch (IOException e) {
            updateListener.handleConnectionReset();
        }

    }

    /**
     * This method is the getter for UpdateListener of Client Class; UpdateListener is Client's
     * listener, always ready to receive objects form Server.
     *
     * @return updateListener attribute of Client class
     */
    public UpdateListener getUpdateListener() {
        return this.updateListener;
    }

    /**
     * The method checks whether server is reachable through network (socket) or not. Remind that
     * server is supposed to be "located" at (IP,PORT) attributes.
     *
     * @return true if server is reachable, so a socket connection with servercan be established;
     * false otherwise
     */
    private boolean isServerReachable() {
        try {
            Socket socket = new Socket(IP, PORT);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
