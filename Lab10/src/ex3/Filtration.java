package ex3;

import akka.actor.AbstractActor;

public class Filtration extends AbstractActor {
    private static final int REQUIRED_UNFILTERED_WINE_L = 25;
    private static final int PRODUCED_FILTERED_WINE_L = 24;
    private static final int FAILURE_RATE = 0;
    private static final int PROCESSING_TIME_MINUTES = 12 * 60;
    private static final int SLOTS = 10;
    private int unfilteredWine = 0;

    @Override
    public Receive createReceive() {
        return null;
    }
}
