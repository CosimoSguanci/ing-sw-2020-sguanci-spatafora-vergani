package it.polimi.ingsw.model;

public class ModelUpdate {
    private Board board;
    private Player player;

    public void addBoard(Board board) {
        this.board = board;
    }
    public void addPlayer(Player player) {
        this.player = player;
    }

}
