package ex3;

import akka.actor.AbstractActor;

public class Fermentation extends AbstractActor {
    private static final int REQUIRED_JUICE_KG = 15;
    private static final int REQUIRED_WATER_L = 8;
    private static final int REQUIRED_SUGAR_KG = 2;
    private static final int PRODUCED_UNFILTERED_WINE_L = 25;
    private static final int FAILURE_RATE_PERCENT = 5;
    private static final int PROCESSING_TIME_MINUTES = 14 * 24 * 60;
    private static final int SLOTS = 10;
    private int juice = 0;
    private int water = 0;
    private int sugar = 0;

    @Override
    public Receive createReceive() {
        return null;
    }
}
