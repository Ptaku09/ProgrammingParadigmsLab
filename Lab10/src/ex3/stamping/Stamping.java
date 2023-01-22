package ex3.stamping;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import ex3.Warehouse;

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

    public enum Shutdown implements Command {
        INSTANCE
    }

    // Actor creation ---------------------------------------------------
    public static Behavior<Command> create(ActorRef<Warehouse.Command> warehouse) {
        return Behaviors.setup(context -> new Stamping(context, warehouse));
    }

    // Actor state ------------------------------------------------------
    private static final int REQUIRED_GRAPES_KG = 15;
    private static final int PRODUCED_JUICE_L = 10;
    private static final int FAILURE_RATE = 0;
    private static final int PROCESSING_TIME_MINUTES = 720;
    private static final int SLOTS = 1;
    private final ActorRef<Warehouse.Command> warehouse;
    private final Map<Integer, ActorRef<StampingSlot.Command>> slots = new HashMap<>();
    private final Queue<Integer> freeSlots = new LinkedList<>();
    private int grapes = 0;

    // Constructor ------------------------------------------------------
    private Stamping(ActorContext<Command> context, ActorRef<Warehouse.Command> warehouse) {
        super(context);
        this.warehouse = warehouse;

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
                .onMessage(Shutdown.class, shutdown -> Behaviors.stopped())
                .build();
    }

    private Behavior<Command> onAddGrapes(AddGrapes msg) {
        grapes += msg.amount;
        beginProcessing();

        return this;
    }

    private void beginProcessing() {
        while (checkProducts() && !freeSlots.isEmpty()) {
            grapes -= REQUIRED_GRAPES_KG;
            int slotNumber = freeSlots.poll();

            slots.get(slotNumber).tell(new StampingSlot.BeginProcessing(getContext().getSelf()));
        }

        if (freeSlots.size() == SLOTS) {
            getContext().getSelf().tell(Shutdown.INSTANCE);
        }
    }

    private boolean checkProducts() {
        return grapes >= REQUIRED_GRAPES_KG;
    }

    private Behavior<Command> onFinishedProcessing(FinishedProcessing msg) {
        getContext().getLog().info("🥳🥳🥳 stamping-slot-{} finished processing 🥳🥳🥳", msg.slotNumber);
        freeSlots.add(msg.slotNumber);

        // If the processing was successful, send the juice to the warehouse
        if (isSuccessful()) {
            warehouse.tell(new Warehouse.AddJuice(PRODUCED_JUICE_L));
        }

        // Begin processing again
        beginProcessing();

        return this;
    }

    private boolean isSuccessful() {
        if (Math.random() < FAILURE_RATE) {
            getContext().getLog().info("❌❌❌ Stamping failed ❌❌❌");

            return false;
        } else {
            getContext().getLog().info("✅✅✅ Stamping successful ✅✅✅");
            getContext().getLog().info("🧃🧃🧃 Produced {}L of juice 🧃🧃🧃", PRODUCED_JUICE_L);
            getContext().getLog().info("📦📦📦 Sending resources to fermentation 📦📦📦");

            return true;
        }
    }

}
