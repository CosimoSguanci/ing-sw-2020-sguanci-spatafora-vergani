package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Board;

public class ModelUpdate {
    private Board board;
    //private Player player;

    public void addBoard(Board board) {
        this.board = board;
    }
    /*public void addPlayer(Player player) {
        this.player = player;
    }*/

    public Board getBoard(){
        return this.board;
    }

}
