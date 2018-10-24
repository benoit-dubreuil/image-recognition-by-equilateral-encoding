package org.benoitdubreuil.iree.pattern.observer;

/**
 * The parent class for the models in the Observer design pattern.
 *
 * @param <M> The model type.
 *
 * @see <a href="Source Making - Design Patterns - Observer">https://sourcemaking.com/design_patterns/observer</a>
 */
public interface IObservable<M> {

    /**
     * Updates all the owned observers.
     */
    void modelChanged(M newValue);

    /**
     * Adds the observer to this observable's list of observers. The observers can now be updated from the observable.
     *
     * @param observer The observer to add to the owned observer list.
     */
    void addObserver(IObserver<M> observer);

    /**
     * Removes the observers from the owned list of observers. The observer won't be updated anymore from this observable.
     *
     * @param observer The observer to remove from the observer list.
     */
    void removeObserver(IObserver<M> observer);

    /**
     * Removes all observers from the owned list of observers. The observers won't be updated anymore from this observable.
     */
    void clearObservers();
}
