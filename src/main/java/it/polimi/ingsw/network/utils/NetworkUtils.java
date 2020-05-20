package it.polimi.ingsw.network.utils;

import java.net.Socket;

public final class NetworkUtils {

    public static final int SERVER_MODE = 0;
    public static final int CLIENT_MODE = 1;

    private NetworkUtils(){}

    public static String getNetworkIdentifier(Socket socket, int mode) {
        if(mode == CLIENT_MODE) {
            return socket.getLocalPort() + "@" + socket.getLocalAddress();
        }
        else if(mode == SERVER_MODE) {
            return socket.getPort() + "@" + socket.getInetAddress();
        }
        else throw new IllegalArgumentException();
    }
}
