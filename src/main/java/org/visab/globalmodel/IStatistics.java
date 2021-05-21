package org.visab.globalmodel;

/**
 * The interface, that all statistic POJOs have to implement. IStatistic POJOs
 * are essentially mirrors of the class which was serialized on the game side.
 *
 * @author moritz
 *
 */
public interface IStatistics {

    String getGame();

}
