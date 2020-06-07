package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.GamePreparationCommand;
import it.polimi.ingsw.controller.commands.GodChoiceCommand;
import it.polimi.ingsw.controller.commands.InitialInfoCommand;
import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.exceptions.WrongGamePhaseException;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PrintableColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommandHandlerImplTest {
    @Test
    public void wrongGamePhaseTest() {
        int playersNum = 3;
        Match match = new Match(playersNum);
        Player p1 = new Player("Andrea", new Model(match), match);
        Player p2 = new Player("Cosimo", new Model(match), match);
        Player p3 = new Player("Roberto", new Model(match), match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Model model = new Model(match);
        Controller controller = new Controller(model);

        CommandHandlerImpl commandHandler = new CommandHandlerImpl(controller);

        PlayerCommand playerCommand = PlayerCommand.parseInput("move w1 a1");
        assertThrows(WrongGamePhaseException.class, () -> commandHandler.handle(playerCommand));

        GodChoiceCommand godChoiceCommand = new GodChoiceCommand(new ArrayList<String>() {{
            add("apollo");
        }});

        assertThrows(WrongGamePhaseException.class, () -> commandHandler.handle(godChoiceCommand));

        GamePreparationCommand gamePreparationCommand = GamePreparationCommand.parseInput("place w1 a1 w2 a2");

        assertThrows(WrongGamePhaseException.class, () -> commandHandler.handle(gamePreparationCommand));

        model.nextGamePhase(); // Change game phase, so currentGamePhase != INITIAL_INFO

        InitialInfoCommand initialInfoCommand = new InitialInfoCommand("player", PrintableColor.RED);

        assertThrows(WrongGamePhaseException.class, () -> commandHandler.handle(initialInfoCommand));
    }
}
