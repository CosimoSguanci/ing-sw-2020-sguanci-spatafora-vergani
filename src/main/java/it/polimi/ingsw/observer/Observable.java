package it.polimi.ingsw.observer;

import java.util.ArrayList;
import java.util.List;


/**
 * This class represents an observable object in MVC paradigm. An observable
 * object is "listened" by other objects. So, an observable object has one
 * or more observers that are interested to be notified when some changes
 * occur (in observed object).
 * Note that an observer is an object implementing interface Observer<T>.
 * When an observable object is created, no observers are "listening" to it.
 *
 * @author Andrea Mario Vergani
 */
public class Observable<T> {
    private List<Observer<T>> observers = new ArrayList();

    /**
     * This method attaches the parameter object (an observer) to the list
     * of observable object's observers. Only observers in this list can
     * be notified by observable object of any changes on it.
     *
     * @param observer   observer object that wants to attach to observable's list
     *
     */
    public void attach(Observer<T> observer) {
        if(!(observers.contains(observer))) {  //observer not present in the list of observers; otherwise, there is no need to add it
            observers.add(observer);
        }
    }

    /**
     * This method detaches the parameter object (an observer) from the list
     * of observable object's observers (if parameter was one of its observers).
     * Parameter object will not be in the list anymore, so it will not be
     * notified by observable object in the future.
     *
     * @param observer   observer object that wants to detach from observable's list
     *
     */
    public void detach(Observer<T> observer) {
        observers.remove(observer);
    }


    /**
     * This method is useful to notify to all observers (in the list) of a
     * change. In fact, observers are interested in any changes of observable
     * object they are "listening" to; on the basis of this notify, they can
     * update or do some specific operations.
     *
     * @param message   object that represents the change, so that observers can understand what happened
     *
     */
    public void notify(T message) {
        for(Observer<T> obs: observers) {
            obs.update(message);
        }
    }
}
