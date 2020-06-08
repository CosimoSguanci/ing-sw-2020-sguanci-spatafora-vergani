package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.UpdateHandler;
import it.polimi.ingsw.view.cli.CliUpdateHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BoardUpdateTest {

    @Test
    public void getExecutedCommandTest() {
        BoardUpdate boardUpdate = new BoardUpdate("");
        PlayerCommand command = PlayerCommand.parseInput("move w1 a1");
        boardUpdate.setExecutedCommand(command);
        assertEquals(command, boardUpdate.getExecutedCommand());
    }

    @Test
    public void getBoardTest() {
        BoardUpdate boardUpdate = new BoardUpdate("A Test board");
        assertEquals("A Test board", boardUpdate.getBoard());
    }

    @Test
    public void handleUpdateTest() {
        UpdateHandler updateHandler = UpdateTest.getMockUpdateHandler();
        BoardUpdate boardUpdate = new BoardUpdate("A Test board");
        boardUpdate.handleUpdate(updateHandler);

        verify(updateHandler, times(1)).handle(boardUpdate);
    }
}
