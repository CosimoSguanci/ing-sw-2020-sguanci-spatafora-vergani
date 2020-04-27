package it.polimi.ingsw.model.updates;

public class GamePreparationUpdate extends PlayerSpecificUpdate {
    public final String board;

    public GamePreparationUpdate(String playerID, String board) {
        super(playerID);
        this.board = board;
    }
}
