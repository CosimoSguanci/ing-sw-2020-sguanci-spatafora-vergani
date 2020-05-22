package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.*;
import it.polimi.ingsw.exceptions.WrongGamePhaseException;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;

class CommandHandlerImpl implements CommandHandler {
    private final Controller controllerInstance;

    CommandHandlerImpl(Controller controllerInstance) {
        this.controllerInstance = controllerInstance;
    }

    public synchronized void handle(InitialInfoCommand command) {
        if (controllerInstance.getCurrentGamePhase() == GamePhase.INITIAL_INFO) {
            setCommandPlayerInstance(command);
            controllerInstance.handleInitialInfoCommand(command);
        } else {
            throw new WrongGamePhaseException();
        }
    }

    public synchronized void handle(PlayerCommand command) {
        if (controllerInstance.getCurrentGamePhase() == GamePhase.REAL_GAME) {

            setCommandPlayerInstance(command);

            if (command.row != -1 && command.col != -1) {
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

    public synchronized void handle(GodChoiceCommand command) {
        if (controllerInstance.getCurrentGamePhase() == GamePhase.CHOOSE_GODS) {
            setCommandPlayerInstance(command);
            controllerInstance.handleGodChoiceCommand(command);
        } else {
            throw new WrongGamePhaseException();
        }
    }

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

    private void setCommandPlayerInstance(Command command) {
        for (Player player : controllerInstance.getPlayers()) {
            if (player.getPlayerID().equals(command.getPlayerID())) {
                command.setPlayer(player);
                break;
            }
        }
    }
}
