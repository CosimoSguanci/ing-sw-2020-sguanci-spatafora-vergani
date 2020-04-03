package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ModelTest {

    @Test
    public void getCurrentPlayerTest() throws Exception {
        Match match = new Match(2);
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

    @Test
    public void endTurnTest() throws Exception {
        Match match = new Match(3);
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