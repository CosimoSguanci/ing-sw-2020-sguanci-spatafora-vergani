package it.polimi.ingsw.observer;


/**
 * This interface represents an observer, typical of MVC paradigm. An
 * observer is an object that is synchronized on changes of other objects
 * (called observable): observers are interested to be notified by observable
 * when something new occurs.
 * In MVC pattern, an observer must implement this interface.
 * When a observer is created, it is synchronized on no observable objects.
 *
 * @author Andrea Mario Vergani
 */
public interface Observer<T> {

    /**
     * The method tells the observer what to do after a change. In fact,
     * observers are interested in any changes of observable object they
     * are "listening" to; on the basis of this notify, they can update or
     * do something else: the instructions are in this method.
     *
     * @param message object that represents the change on observable object
     */
    void update(T message);
}
