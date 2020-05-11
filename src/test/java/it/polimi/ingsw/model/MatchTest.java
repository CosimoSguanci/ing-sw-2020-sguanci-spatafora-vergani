package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyInsidePlayerException;
import it.polimi.ingsw.exceptions.NicknameAlreadyTakenException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class MatchTest {

    @Test
    public void addPlayerTest2() {
        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea",  new Model(match), match);
        Player p2 = new Player("Cosimo",  new Model(match), match);
        Player p3 = new Player("Roberto", new Model(match), match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        assertThrows(AlreadyInsidePlayerException.class, () -> match.addPlayer(p2));
        match.addPlayer(p3);
    }

    @Disabled // todo exception not thrown here
    @Test
    public void addPlayerTest1() {
        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea",  new Model(match), match);
        Player p2 = new Player("Cosimo",  new Model(match), match);
        Player p3 = new Player("ValerioAndoni",  new Model(match), match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        assertThrows(NicknameAlreadyTakenException.class, () -> match.addPlayer(p3));
    }

    @Test
    public void removePlayerTest() {
        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea", new Model(match), match);
        Player p2 = new Player("Cosimo", new Model(match), match);
        Player p3 = new Player("Roberto", new Model(match), match);
        try {
            match.addPlayer(p1);
            match.addPlayer(p2);
            match.addPlayer(p3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(p1, match.getCurrentPlayer());
        match.nextTurn();
        assertEquals(p2, match.getCurrentPlayer());
        match.nextTurn();
        assertEquals(p3, match.getCurrentPlayer());
        match.nextTurn();
        assertEquals(p1, match.getCurrentPlayer());
        match.nextTurn();
        assertEquals(p2, match.getCurrentPlayer());
        match.nextTurn();
        assertEquals(p3, match.getCurrentPlayer());

        match.removePlayer(p3);
        assertEquals(p1, match.getCurrentPlayer());
        match.nextTurn();
        assertEquals(p2, match.getCurrentPlayer());
        match.nextTurn();
        assertEquals(p1, match.getCurrentPlayer());
        match.nextTurn();
        assertEquals(p2, match.getCurrentPlayer());
        match.nextTurn();
        assertEquals(p1, match.getCurrentPlayer());

        match.removePlayer(p1);
        assertEquals(p2, match.getCurrentPlayer());
        match.nextTurn();
        assertEquals(p2, match.getCurrentPlayer());
    }


    @Test
    public void nextTurnTest()  {
        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea", new Model(match), match);
        Player p2 = new Player("Cosimo",  new Model(match), match);
        Player p3 = new Player("Roberto",  new Model(match), match);
        try {
            match.addPlayer(p1);
            match.addPlayer(p2);
            match.addPlayer(p3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(p1, match.getCurrentPlayer());
        match.nextTurn();
        assertEquals(p2, match.getCurrentPlayer());
        match.nextTurn();
        assertEquals(p3, match.getCurrentPlayer());
        match.nextTurn();
        assertEquals(p1, match.getCurrentPlayer());
        match.nextTurn();
        assertEquals(p2, match.getCurrentPlayer());

        match.removePlayer(p2);
        assertEquals(p3, match.getCurrentPlayer());
        match.nextTurn();
        assertEquals(p1, match.getCurrentPlayer());

    }

    @Test
    public void getCurrentPlayerTest()  {
        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Marco", new Model(match), match);
        Player p2 = new Player("Alessandro", new Model(match), match);
        try{
            match.addPlayer(p1);
            match.addPlayer(p2);
        } catch(Exception e) {
            e.printStackTrace();
        }

        assertEquals(p1, match.getCurrentPlayer());
        match.nextTurn();
        assertEquals(p2, match.getCurrentPlayer());
        match.nextTurn();
        assertEquals(p1, match.getCurrentPlayer());
        match.nextTurn();
        assertEquals(p2, match.getCurrentPlayer());
    }

    @Disabled
    @Test
    public void getMatchBoardTest() {
    }
}