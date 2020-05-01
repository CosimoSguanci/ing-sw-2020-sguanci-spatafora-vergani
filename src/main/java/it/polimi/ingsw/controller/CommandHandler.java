package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.GamePreparationCommand;
import it.polimi.ingsw.controller.commands.GodChoiceCommand;
import it.polimi.ingsw.controller.commands.InitialInfoCommand;
import it.polimi.ingsw.controller.commands.PlayerCommand;

public interface CommandHandler {
    void handle(InitialInfoCommand command);
    void handle(PlayerCommand command);
    void handle(GodChoiceCommand command);
    void handle(GamePreparationCommand gamePreparationCommand);
}
