package it.polimi.ingsw.model;

public class Player {
    public final String ID;
    public final String nickname;
    private String color;
    private Worker workerFirst;
    private Worker workerSecond;
    private God god;
    private Boolean isGodChooser; //public final

    public Player(String id, String nickname) {
        this.ID = id;
        this.nickname = nickname;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Worker getWorkerFirst() {
        return this.workerFirst;
    }

    public void setWorkerFirst(Worker firstWorker) {
        this.workerFirst = firstWorker;
    }

    public Worker getWorkerSecond() {
        return this.workerSecond;
    }

    public void setWorkerSecond(Worker secondWorker) {
        this.workerSecond = secondWorker;
    }

    public God getGod() {
        return this.god;
    }

    public void setGod(God god) {
        this.god = god;
    }

}