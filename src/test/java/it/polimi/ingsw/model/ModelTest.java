package it.polimi.ingsw.model;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ModelTest {

    @Test
    public void getCurrentPlayerTest()  {
        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Model model = new Model(match);
        Player p1 = new Player("Marco", "mc", match);
        Player p2 = new Player("Alessandro", "ale", match);
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

    @Disabled
    @Test
    public void getBoardTest() {
    }

    @Test
    public void endTurnTest()  {
        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Model model = new Model(match);
        Player p1 = new Player("Roberto", "robe", match);
        Player p2 = new Player("Cosimo", "cos", match);
        Player p3 = new Player("Andrea", "andmar", match);
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
}