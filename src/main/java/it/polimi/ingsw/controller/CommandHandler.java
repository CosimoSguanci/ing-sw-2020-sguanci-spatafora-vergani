package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.GamePreparationCommand;
import it.polimi.ingsw.controller.commands.GodChoiceCommand;
import it.polimi.ingsw.controller.commands.InitialInfoCommand;
import it.polimi.ingsw.controller.commands.PlayerCommand;


/**
 * CommandHandler is an interface thought to handle all commands received by Server. Its core is
 * polymorphism: a general command arrives to Server, but the different ways
 * to handle it depend on the dynamic type of the command itself.
 *
 * @author Cosimo Sguanci
 */
public interface CommandHandler {

    /**
     * This method handles an InitialInfoCommand.
     *
     * @param command command containing player's nickname and colour
     */
    void handle(InitialInfoCommand command);


    /**
     * This method handles a PlayerCommand.
     *
     * @param command command containing player's move, build, ... in RealGame phase (the
     *                proper match)
     */
    void handle(PlayerCommand command);


    /**
     * This method handles a GodChoiceCommand.
     *
     * @param command command containing player's chosen god (or gods if god-chooser player)
     */
    void handle(GodChoiceCommand command);


    /**
     * This method handles a GamePreparationCommand.
     *
     * @param gamePreparationCommand command containing initial positions of workers on the board
     */
    void handle(GamePreparationCommand gamePreparationCommand);
}
