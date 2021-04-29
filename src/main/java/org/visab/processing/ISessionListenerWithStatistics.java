package org.visab.processing;

/**
 * The ISessionListenerWithStatistics interface, that all SessionListeners have
 * to implement.
 *
 * @author moritz
 *
 * @param <TStatistics> The statistics type, that will be processed.
 */
public interface ISessionListenerWithStatistics<TStatistics> {

    void processStatistics(TStatistics statistics);

}
