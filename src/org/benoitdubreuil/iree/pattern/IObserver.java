package org.benoitdubreuil.iree.pattern;

/**
 * Interface that for the observer pattern, which is useful for the MVC architecture.
 *
 * @param <M> The model type.
 *
 * @see <a href="Source Making - Design Patterns - Observer">https://sourcemaking.com/design_patterns/observer</a>
 */
public interface IObserver<M> {

    /**
     * Called when an attached IObservable has his observable value updated
     *
     * @param newValue The new value of the observed.
     */
    void observableChanged(M newValue);

}
