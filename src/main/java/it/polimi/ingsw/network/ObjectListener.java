package it.polimi.ingsw.network;

public interface ObjectListener {
    void setIsActive(boolean active);
    boolean isActive();
    void forwardNotify(Object object);
}
