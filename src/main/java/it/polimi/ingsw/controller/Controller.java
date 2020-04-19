package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.observer.Observer;

import java.util.List;

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
 * @author Cosimo Sguanci
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

            for(Player player : model.getPlayers()) {
                if(player.ID.equals(message.playerID)) {
                    message.setPlayer(player);
                    break;
                }
            }

            Worker worker = message.workerID.equals(PlayerCommand.WORKER_FIRST) ? message.getPlayer().getWorkerFirst() : message.getPlayer().getWorkerSecond();
            message.setWorker(worker);

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
        if (!playerCommand.getPlayer().equals(currentPlayer)) {
            throw new Exception();
        } else {
            // TODO When Player Lose?
            // TODO When Game Prep?
            switch (playerCommand.commandType) {
                case MOVE:
                    if (checkAllMoveConstraints(playerCommand) &&
                            currentPlayer.getGod().godStrategy.checkMove(playerCommand.getWorker(), playerCommand.getCell())) {

                        currentPlayer.getGod().godStrategy.executeMove(playerCommand.getWorker(), playerCommand.getCell());

                        boolean hasWon = checkAllWinConstraints(playerCommand) && // TODO first checkWinCond?
                                currentPlayer.getGod().godStrategy.checkWinCondition(playerCommand.getWorker());
                    } else {
                        model.reportError(playerCommand.getPlayer(), playerCommand.commandType);
                    }
                    break;
                case BUILD:
                    if (checkAllBuildConstraints(playerCommand) && currentPlayer.getGod().godStrategy.checkBuild(playerCommand.getWorker(), playerCommand.getCell(), playerCommand.cellBlockType)) {
                        currentPlayer.getGod().godStrategy.executeBuild(playerCommand.getWorker(), playerCommand.getCell(), playerCommand.cellBlockType);

                        /*
                         *  Necessary to cover the possibility that a Gods could have a Win Conditions that triggers when it builds a block under certain conditions.
                         *  Example: "If XXX built a Dome and it's adjacent to another Worker, it wins"
                         */
                        boolean hasWon = checkAllWinConstraints(playerCommand) && currentPlayer.getGod().godStrategy.checkWinCondition(playerCommand.getWorker());

                    } else {
                        model.reportError(playerCommand.getPlayer(), playerCommand.commandType);
                    }
                    break;
                case END_TURN:
                    model.endTurn();
                    break;

            }
        }
    }

    /**
     * This methods checks all Players God's Win Constraints to check if there is a power which
     * prevents the Player which executed the PlayerCommand given to win.
     *
     * @param playerCommand player command from View
     * @return true if Win is permitted, false otherwise.
     */
    private boolean checkAllWinConstraints(PlayerCommand playerCommand) {

        for (Player p : model.getPlayers()) {
            if (!p.equals(playerCommand.getPlayer()) && !p.getGod().godStrategy.checkWinConstraints(playerCommand.getWorker(), playerCommand.getCell())) {
                return false;
            }
        }

        return true;
    }

    /**
     * This methods checks all Players God's Move Constraints to check if there is a power which
     * prevents the Player which executed the PlayerCommand given to move.
     *
     * @param playerCommand player command from View
     * @return true if move is permitted, false otherwise.
     */
    private boolean checkAllMoveConstraints(PlayerCommand playerCommand) {

        for (Player p : model.getPlayers()) {
            if (!p.equals(playerCommand.getPlayer()) && !p.getGod().godStrategy.checkMoveConstraints(playerCommand.getWorker(), playerCommand.getCell())) {
                return false;
            }
        }

        return true;
    }

    /**
     * This methods checks all Players God's Build Constraints to check if there is a power which
     * prevents the Player which executed the PlayerCommand given to build.
     *
     * @param playerCommand player command from View
     * @return true if build is permitted, false otherwise.
     */
    private boolean checkAllBuildConstraints(PlayerCommand playerCommand) {

        for (Player p : model.getPlayers()) {
            if (!p.equals(playerCommand.getPlayer()) && !p.getGod().godStrategy.checkBuildConstraints(playerCommand.getWorker(), playerCommand.getCell(), playerCommand.cellBlockType)) {
                return false;
            }
        }

        return true;
    }
}
