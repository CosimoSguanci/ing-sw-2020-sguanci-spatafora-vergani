package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

class OpponentWorkerMoverDelegate {
    void moveOpponentWorker(Worker worker, Worker opponent) {
        Cell prev = worker.getPosition();
        prev.setWorker(null);
        try {
            opponent.move(prev);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
