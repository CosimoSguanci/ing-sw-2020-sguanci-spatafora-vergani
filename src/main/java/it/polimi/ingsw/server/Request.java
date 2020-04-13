package it.polimi.ingsw.server;

public enum Request {
    ASK_PLAYER_NUM(0);

    private int requestNumber;


    Request(int requestNumber) {
        this.requestNumber = requestNumber;
    }

    public int getRequestNumber() {
        return requestNumber;
    }
}
