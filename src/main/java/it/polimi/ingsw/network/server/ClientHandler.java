package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.MessageListener;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;

public class ClientHandler extends Observable<Object> implements Runnable, Observer<Object> {

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
        //ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
    }

    @Override
    public void update(Object message) {
        notify(message);
    }

    @Override
    public void run() {
        DataInputStream input;
        String nickname;
        int playersNum;
        try {

            /*MessageListener messageListener = new MessageListener(clientSocket);
            new Thread(messageListener).start();
            messageListener.addObserver(this);*/

            input = new DataInputStream(clientSocket.getInputStream());
            nickname = input.readUTF();
            playersNum = input.readInt();

            MessageListener messageListener = new MessageListener(clientSocket);
            new Thread(messageListener).start();
            messageListener.addObserver(this);

            server.lobby(this, nickname, playersNum);

        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error!" + e.getMessage());
        }
    }
}
