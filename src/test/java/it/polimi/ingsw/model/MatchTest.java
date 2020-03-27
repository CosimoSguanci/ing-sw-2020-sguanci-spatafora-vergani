package it.polimi.ingsw.model;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class MatchTest {

    @Ignore
    @Test
    public void addPlayerTest() {
    }

    @Ignore
    @Test
    public void removePlayer() {
    }

    @Test
    public void getCanMove() throws Exception {
        Match match = new Match(3);
        assertTrue(match.getCanMove());

        match.setCanMove(true);
        assertTrue(match.getCanMove());

        match.setCanMove(false);
        assertFalse(match.getCanMove());

        match.setCanMove(true);
        assertTrue(match.getCanMove());
    }

    @Test
    public void setCanMove() throws Exception {
        Match match = new Match(2);
        Player p1 = new Player("Marco", "mc", match);
        Player p2 = new Player("Alessandro", "ale", match);
        try{
            match.addPlayer(p1);
            match.addPlayer(p2);
        } catch(Exception e) {
            e.printStackTrace();
        }

        match.setCanMove(false);
        assertFalse(match.getCanMove());

        match.setCanMove(false);
        assertFalse(match.getCanMove());

        match.setCanMove(true);
        assertTrue(match.getCanMove());
    }

    @Test
    public void nextTurnTest() throws Exception {
        Match match = new Match(3);
        Player p1 = new Player("Andrea", "and", match);
        Player p2 = new Player("Cosimo", "cosimo", match);
        Player p3 = new Player("Roberto", "rob", match);
        try {
            match.addPlayer(p1);
            match.addPlayer(p2);
            match.addPlayer(p3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, match.getTurn());
        match.nextTurn();
        assertEquals(1, match.getTurn());
        match.nextTurn();
        assertEquals(2, match.getTurn());
        match.nextTurn();
        assertEquals(0, match.getTurn());
        match.nextTurn();
        assertEquals(1, match.getTurn());

        match.removePlayer(p2);
        assertEquals(1, match.getTurn());
        match.nextTurn();
        assertEquals(0, match.getTurn());

    }

    @Test
    public void getTurn() throws Exception {
        Match match = new Match(2);
        Player p1 = new Player("Marco", "mc", match);
        Player p2 = new Player("Alessandro", "ale", match);
        try{
            match.addPlayer(p1);
            match.addPlayer(p2);
        } catch(Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, match.getTurn());
        match.nextTurn();
        assertEquals(1, match.getTurn());
        match.nextTurn();
        assertEquals(0, match.getTurn());
        match.nextTurn();
        assertEquals(1, match.getTurn());
    }
}