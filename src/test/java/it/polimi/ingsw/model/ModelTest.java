package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.commands.Command;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ModelTest {

    @Test
    public void getCurrentPlayerTest()  {
        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Model model = new Model(match);
        Player p1 = new Player("Marco",  model, match);
        Player p2 = new Player("Alessandro", model,  match);
        try{
            match.addPlayer(p1);
            match.addPlayer(p2);
        } catch(Exception e) {
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
        Match.clearInstances();


        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Board board = match.getMatchBoard();
        Model model = new Model(match);

        assertEquals(board, model.getBoard());

        Player p1 = new Player("Andrea",  model, match);
        Player p2 = new Player("Cosimo",  model, match);
        Player p3 = new Player("Roberto",  model, match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Worker w1 = new Worker(p1, board, Command.WORKER_FIRST);
        w1.setInitialPosition(4, 3);

        assertEquals(board, model.getBoard());
    }

    @Test
    public void endTurnTest()  {
        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Model model = new Model(match);
        Player p1 = new Player("Roberto",  model, match);
        Player p2 = new Player("Cosimo",  model, match);
        Player p3 = new Player("Andrea",  model, match);
        try{
            match.addPlayer(p1);
            match.addPlayer(p2);
            match.addPlayer(p3);
        } catch(Exception e) {
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
        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Model model = new Model(match);

        Player p1 = new Player("Andrea",  model, match);
        Player p2 = new Player("Cosimo",  model, match);
        Player p3 = new Player("Roberto", model,  match);
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