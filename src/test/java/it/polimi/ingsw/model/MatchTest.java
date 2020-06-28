package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyInsidePlayerException;
import it.polimi.ingsw.exceptions.InvalidPlayerNumberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class MatchTest {

    private Match match;
    private Player p1;
    private Player p2;
    private Player p3;

    @BeforeEach
    public void initMatchProperties() {

        int playersNum = 3;
        this.match = new Match(playersNum);
        this.p1 = new Player("Andrea", new Model(match), match);
        this.p2 = new Player("Cosimo", new Model(match), match);
        this.p3 = new Player("Roberto", new Model(match), match);

    }

    @Test
    public void addPlayerTest2() {

        match.addPlayer(p1);
        match.addPlayer(p2);
        assertThrows(AlreadyInsidePlayerException.class, () -> match.addPlayer(p2));
        match.addPlayer(p3);
        assertThrows(InvalidPlayerNumberException.class, () -> match.addPlayer(new Player("id", new Model(new Match(3)), new Match(3))));
    }

    @Test
    public void removePlayerTest() {

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
    public void nextTurnTest() {

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
    public void getCurrentPlayerTest() {

        try {
            match.addPlayer(p1);
            match.addPlayer(p2);
        } catch (Exception e) {
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
}