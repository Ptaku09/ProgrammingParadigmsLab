package ex3;

import akka.actor.AbstractActor;

public class Bottling extends AbstractActor {
    private static final float REQUIRED_FILTERED_WINE_L = 0.75f;
    private static final int PRODUCED_BOTTLES = 1;
    private static final int FAILURE_RATE = 5;
    private static final int PROCESSING_TIME_MINUTES = 5;
    private static final int SLOTS = 1;
    private float filteredWine = 0;

    @Override
    public Receive createReceive() {
        return null;
    }
}
