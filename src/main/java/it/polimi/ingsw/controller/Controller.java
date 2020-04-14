package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.observer.Observer;


/**
 * This class represents the Controller in MVC design pattern. The whole
 * application, in fact, is based on MVC pattern, and in particular
 * Controller class is View's observer. The "dialogue" between Controller
 * and View is possible through PlayerCommand class: this class represent a
 * move/build that a player wants to do during the game. This is the reason
 * for Controller class to implement Observer(PlayerCommand), since (as
 * explained) View notifies Controller using PlayerCommand as update message.
 * Then, following MVC pattern, Controller invokes Model's methods to
 * modify the model itself.
 *
 * @author Andrea Mario Vergani
 */
public class Controller implements Observer<PlayerCommand> {
    private Model model;

    /**
     * The constructor creates an instance of Controller class. This class
     * needs to reference to the general Model of the game, so that changes
     * and operations in it are possible.
     *
     * @param model class Model of MVC pattern
     */
    public Controller(Model model) {
        this.model = model;
    }


    /**
     * This method is an overriding method of "update" in Observer interface.
     * Its task is to handle player command coming from View.
     *
     * @param message player move/build from View
     */
    @Override
    public void update(PlayerCommand message) {
        try {
            Cell correctCell = model.getBoard().getCell(message.getCell().getRowIdentifier(), message.getCell().getColIdentifier());
            message.setCell(correctCell);
            handleCommand(message);
        } catch (Exception e) {  //must be understood what happens when this exception occurs
            //this exception occurs when a player is giving a command during a turn "owned" by another player
            e.printStackTrace();
        }
    }


    /**
     * This private method simply extends update's task. In fact, to handle
     * a command is necessary to: verify that the player who required it is
     * game's current player (otherwise, there is no need to call the Model,
     * since its state will not modify); call the selected command from the
     * player, as an invocation of one of model's methods.
     *
     * @param playerCommand player command from View
     * @throws Exception when command's player is not current player
     */
    private void handleCommand(PlayerCommand playerCommand) throws Exception {
        Player currentPlayer = model.getCurrentPlayer();
        if (!playerCommand.player.equals(currentPlayer)) {
            throw new Exception();
        } else {

            switch (playerCommand.commandType) {
                case MOVE:
                    if (currentPlayer.getGod().godStrategy.checkMove(playerCommand.worker, playerCommand.getCell())) {
                        currentPlayer.getGod().godStrategy.executeMove(playerCommand.worker, playerCommand.getCell());
                    }
                    else {
                        model.reportError(playerCommand.player, playerCommand.commandType);
                    }
                    break;
                case BUILD:
                    if (currentPlayer.getGod().godStrategy.checkBuild(playerCommand.worker, playerCommand.getCell(), playerCommand.cellBlockType)) {
                        currentPlayer.getGod().godStrategy.executeBuild(playerCommand.worker, playerCommand.getCell(), playerCommand.cellBlockType);
                    }
                    else {
                        model.reportError(playerCommand.player, playerCommand.commandType);
                    }
                    break;
                case END_TURN:
                    model.endTurn();
                    break;

            }
        }
    }
}
