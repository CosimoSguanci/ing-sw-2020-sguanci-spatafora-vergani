package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.commands.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ModelTest {

    private Match match;
    private Model model;
    private Player p1;
    private Player p2;
    private Player p3;

    @BeforeEach
    public void initModelProperties() {
        int playersNum = 3;
        this.match = new Match(playersNum);
        this.model = new Model(match);
        this.p1 = new Player("Roberto", model, match);
        this.p2 = new Player("Cosimo", model, match);
        this.p3 = new Player("Andrea", model, match);
    }

    @Test
    public void getCurrentPlayerTest() {

        try {
            match.addPlayer(p1);
            match.addPlayer(p2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(p1, model.getCurrentPlayer());
        model.endTurn();
        assertEquals(p2, model.getCurrentPlayer());
        model.endTurn();
        assertEquals(p1, model.getCurrentPlayer());
        model.endTurn();
        assertEquals(p2, model.getCurrentPlayer());
    }


    @Test
    public void getBoardTest() {

        Board board = match.getMatchBoard();

        assertEquals(board, model.getBoard());

        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Worker w1 = new Worker(p1, board, Command.WORKER_FIRST);
        w1.setInitialPosition(4, 3);

        assertEquals(board, model.getBoard());
    }

    @Test
    public void endTurnTest() {

        try {
            match.addPlayer(p1);
            match.addPlayer(p2);
            match.addPlayer(p3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(p1, model.getCurrentPlayer());
        model.endTurn();
        assertEquals(p2, model.getCurrentPlayer());
        model.endTurn();
        assertEquals(p3, model.getCurrentPlayer());
        model.endTurn();
        assertEquals(p1, model.getCurrentPlayer());
        model.endTurn();
        assertEquals(p2, model.getCurrentPlayer());
    }

    @Test
    public void getPlayersTest() {

        ArrayList<Player> players = new ArrayList<>();

        match.addPlayer(p1);
        players.add(p1);
        assertEquals(players, new ArrayList<>(model.getPlayers()));

        match.addPlayer(p2);
        players.add(p2);
        assertEquals(players, new ArrayList<>(model.getPlayers()));

        match.addPlayer(p3);
        players.add(p3);
        assertEquals(players, new ArrayList<>(model.getPlayers()));
    }
}