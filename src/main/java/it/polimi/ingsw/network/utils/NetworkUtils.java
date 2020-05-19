package it.polimi.ingsw.network.utils;

import java.net.Socket;

public final class NetworkUtils {

    public static final int LOCAL_PORT = 0;
    public static final int REMOTE_PORT = 1;

    private NetworkUtils(){}

    public static String getNetworkIdentifier(Socket socket, int portMode) {
        if(portMode == LOCAL_PORT) {
            return socket.getLocalPort() + "@" + socket.getInetAddress();
        }
        else if(portMode == REMOTE_PORT) {
            return socket.getPort() + "@" + socket.getInetAddress();
        }
        else throw new IllegalArgumentException();
    }
}
