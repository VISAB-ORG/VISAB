package processing;

public interface ISessionListenerWithStatistics<TStatistics> {

    void processStatistics(TStatistics statistics);

}
