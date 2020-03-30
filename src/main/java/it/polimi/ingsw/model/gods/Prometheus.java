package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;

public class Prometheus implements GodStrategy {
    @Override
    public boolean checkMovement(Worker worker, Cell moveCell) {
        return false;
    }

    @Override
    public boolean checkBuild(Worker worker, Cell buildCell) {
        return false;
    }

    @Override
    public void executeBuild(Worker worker, Cell buildCell) {

    }

    @Override
    public void executeMovement(Worker worker, Cell moveCell) {

    }

    @Override
    public void prepareGame() {

    }

    @Override
    public boolean checkGamePreparation() {
        return true;
    }

    @Override
    public void endTurn(Match match) {

    }
}
