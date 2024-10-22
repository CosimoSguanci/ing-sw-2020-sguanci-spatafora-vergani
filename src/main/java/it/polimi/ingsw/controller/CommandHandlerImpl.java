package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.*;
import it.polimi.ingsw.exceptions.WrongGamePhaseException;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;


/**
 * CommandHandlerImpl is thought to handle all commands received by Server. Its methods
 * are based on polymorphism: a general command arrives to Server, but the different ways
 * to handle it depend on the dynamic type of the command itself. Then, a proper call to
 * Controller is made, in order to interact directly with Model and modify it in the right
 * way (if necessary). This follows Visitor Pattern guidelines.
 *
 * @author Cosimo Sguanci
 * @see CommandHandler
 */
class CommandHandlerImpl implements CommandHandler {

    /**
     * The instance of {@link Controller} which receives the {@link Command} objects from players.
     */
    private final Controller controllerInstance;

    /**
     * The constructor creates a {@link CommandHandler} related to a specific instance of
     * Controller class.
     *
     * @param controllerInstance controller associated with the object
     */
    CommandHandlerImpl(Controller controllerInstance) {
        this.controllerInstance = controllerInstance;
    }


    /**
     * This method handles an {@link InitialInfoCommand}. In particular, it sets the {@link Player} who
     * gave the command to the command itself, then calls an appropriate {@link Controller}
     * method for further things to do with it.
     *
     * @param command command containing player's nickname and colour
     * @throws WrongGamePhaseException if current phase is not InitialInfo phase
     * @see Controller#handleInitialInfoCommand(InitialInfoCommand)
     */
    public synchronized void handle(InitialInfoCommand command) {
        if (controllerInstance.getCurrentGamePhase() == GamePhase.INITIAL_INFO) {
            setCommandPlayerInstance(command);
            controllerInstance.handleInitialInfoCommand(command);
        } else {
            throw new WrongGamePhaseException();
        }
    }


    /**
     * This method handles a {@link PlayerCommand}. In particular, it sets the {@link Player} who
     * gave the command to the command itself, associates the coordinates to the
     * "real" {@link Cell} (Model-side) and worker ID to the "real" {@link Worker} (Model-side);
     * then, an appropriate {@link Controller} method is called for further things to do with
     * the command.
     *
     * @param command command containing player's move, build, ... in RealGame phase (the
     *                proper match)
     * @throws WrongGamePhaseException if current phase is not RealGame phase
     * @see Controller#handlePlayerCommand(PlayerCommand)
     */
    public synchronized void handle(PlayerCommand command) {
        if (controllerInstance.getCurrentGamePhase() == GamePhase.REAL_GAME) {

            setCommandPlayerInstance(command);

            if (command.commandType != CommandType.END_TURN) { // command.row != -1 && command.col != -1
                Cell correctCell = controllerInstance.getBoard().getCell(command.row, command.col);
                command.setCell(correctCell);
            }

            Worker worker = command.workerID != null ? command.workerID.equals(PlayerCommand.WORKER_FIRST) ? command.getPlayer().getWorkerFirst() : command.getPlayer().getWorkerSecond() : null;
            command.setWorker(worker);

            controllerInstance.handlePlayerCommand(command);
        } else {
            throw new WrongGamePhaseException();
        }
    }


    /**
     * This method handles a {@link GodChoiceCommand}. In particular, it sets the {@link Player} who
     * gave the command to the command itself, then calls an appropriate {@link Controller}
     * method for further things to do with it.
     *
     * @param command command containing player's chosen god (or gods if god-chooser player)
     * @throws WrongGamePhaseException if current phase is not ChooseGods phase
     * @see Controller#handleGodChoiceCommand(GodChoiceCommand)
     */
    public synchronized void handle(GodChoiceCommand command) {
        if (controllerInstance.getCurrentGamePhase() == GamePhase.CHOOSE_GODS) {
            setCommandPlayerInstance(command);
            controllerInstance.handleGodChoiceCommand(command);
        } else {
            throw new WrongGamePhaseException();
        }
    }


    /**
     * This method handles a {@link GamePreparationCommand}. In particular, it sets the {@link Player} who
     * gave the command to the command itself and associates the coordinates to the
     * "real" {@link Cell} (Model-side); then, an appropriate {@link Controller} method is called for further
     * things to do with the command.
     *
     * @param command command containing initial positions of workers on the board
     * @throws WrongGamePhaseException if current phase is not GamePreparation phase
     * @see Controller#handleGamePreparationCommand(GamePreparationCommand)
     */
    public synchronized void handle(GamePreparationCommand command) {
        if (controllerInstance.getCurrentGamePhase() == GamePhase.GAME_PREPARATION) {

            setCommandPlayerInstance(command);

            Cell workerFirstCell = controllerInstance.getBoard().getCell(command.workerFirstRow, command.workerFirstCol);
            Cell workerSecondCell = controllerInstance.getBoard().getCell(command.workerSecondRow, command.workerSecondCol);
            command.setWorkerFirstCell(workerFirstCell);
            command.setWorkerSecondCell(workerSecondCell);

            controllerInstance.handleGamePreparationCommand(command);
        } else {
            throw new WrongGamePhaseException();
        }
    }


    /**
     * This private method associates a given {@link Command} to the {@link Player} who gave it.
     *
     * @param command general command coming from one of the players (Client) to Server
     */
    private void setCommandPlayerInstance(Command command) {
        for (Player player : controllerInstance.getPlayers()) {
            if (player.getPlayerID().equals(command.getPlayerID())) {
                command.setPlayer(player);
                break;
            }
        }
    }
}
