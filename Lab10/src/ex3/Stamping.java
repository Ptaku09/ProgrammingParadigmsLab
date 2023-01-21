package ex3;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Stamping extends AbstractBehavior<Stamping.Command> {
    // Acceptable commands -----------------------------------------------
    public interface Command {}

    public static final class AddGrapes implements Command {
        final int amount;

        public AddGrapes(int amount) {
            this.amount = amount;
        }
    }

    public static final class FinishedProcessing implements Command {
        final int slotNumber;

        public FinishedProcessing(int slotNumber) {
            this.slotNumber = slotNumber;
        }
    }

    // Actor creation ---------------------------------------------------
    public static Behavior<Command> create() {
        return Behaviors.setup(Stamping::new);
    }

    // Actor state ------------------------------------------------------
    private static final int REQUIRED_GRAPES_KG = 15;
    private static final int PRODUCED_JUICE_L = 10;
    private static final int FAILURE_RATE = 0;
    private static final int PROCESSING_TIME_MINUTES = 720;
    private static final int SLOTS = 1;
    private final Map<Integer, ActorRef<StampingSlot.Command>> slots = new HashMap<>();
    private final Queue<Integer> freeSlots = new LinkedList<>();
    private int grapes = 0;

    // Constructor ------------------------------------------------------
    private Stamping(ActorContext<Command> context) {
        super(context);

        // Create the slots
        for (int i = 0; i < SLOTS; i++) {
            slots.put(i, context.spawn(StampingSlot.create(i, Duration.ofMillis(PROCESSING_TIME_MINUTES * 10)), "stamping-slot-" + i));
            freeSlots.add(i);
        }
    }

    // Actor behavior ---------------------------------------------------
    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(AddGrapes.class, this::onAddGrapes)
                .onMessage(FinishedProcessing.class, this::onFinishedProcessing)
                .build();
    }

    private Behavior<Command> onAddGrapes(AddGrapes msg) {
        grapes += msg.amount;

        if (grapes >= REQUIRED_GRAPES_KG) {
            beginProcessing();
        }

        return this;
    }

    private Behavior<Command> onFinishedProcessing(FinishedProcessing command) {
        getContext().getLog().info("stamping-slot-{} finished processing", command.slotNumber);
        freeSlots.add(command.slotNumber);

        // Tell fermentation to begin
        // * HERE *

        // If there are grapes left, begin processing again
        if (grapes >= REQUIRED_GRAPES_KG) {
            beginProcessing();
        }

        return this;
    }

    private void beginProcessing() {
        if (!freeSlots.isEmpty()) {
            grapes -= REQUIRED_GRAPES_KG;
            int slotNumber = freeSlots.poll();

            slots.get(slotNumber).tell(new StampingSlot.BeginProcessing(getContext().getSelf()));
        } else {
            getContext().getLog().info("Stamping - no free slots");
        }
    }
}
