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

    private final List<Observer<T>> observers = new ArrayList<>();

    /**
     * This method attaches the parameter object (an observer) to the list
     * of observable object's observers. Only observers in this list can
     * be notified by observable object of any changes on it.
     *
     * @param observer   observer object that wants to attach to observable's list
     *
     */
    public void addObserver(Observer<T> observer){
        synchronized (observers) {
            observers.add(observer);
        }
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
    public void notify(T message){
        synchronized (observers) {
            for (Observer<T> observer : observers) {
                observer.update(message);
            }
        }
    }

}
