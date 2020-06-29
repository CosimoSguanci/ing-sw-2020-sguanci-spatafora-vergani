package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.GamePreparationCommand;
import it.polimi.ingsw.controller.commands.GodChoiceCommand;
import it.polimi.ingsw.controller.commands.InitialInfoCommand;
import it.polimi.ingsw.controller.commands.PlayerCommand;


/**
 * CommandHandler is an interface thought to handle all commands received by Server. Its core is
 * polymorphism: a general command arrives to Server, but the different ways
 * to handle it depend on the dynamic type of the command itself (Visitor pattern is applied).
 * This interface represent in practice the "abstract Visitor", while {@link CommandHandlerImpl}
 * is the "concrete Visitor".
 *
 * @author Cosimo Sguanci
 */
public interface CommandHandler {

    /**
     * This method handles an {@link InitialInfoCommand}.
     *
     * @param command command containing player's nickname and colour
     */
    void handle(InitialInfoCommand command);


    /**
     * This method handles a {@link PlayerCommand}.
     *
     * @param command command containing player's move, build, ... in RealGame phase (the
     *                proper match)
     */
    void handle(PlayerCommand command);


    /**
     * This method handles a {@link GodChoiceCommand}.
     *
     * @param command command containing player's chosen god (or gods if god-chooser player)
     */
    void handle(GodChoiceCommand command);


    /**
     * This method handles a {@link GamePreparationCommand}.
     *
     * @param gamePreparationCommand command containing initial positions of workers on the board
     */
    void handle(GamePreparationCommand gamePreparationCommand);
}
