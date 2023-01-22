package ex3.bottling;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import ex3.Warehouse;
import ex3.filtration.Filtration;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Bottling extends AbstractBehavior<Bottling.Command> {
    // Acceptable commands -----------------------------------------------
    public interface Command {}

    public static final class AddFilteredWine implements Command {
        final int filteredWineAmount;

        public AddFilteredWine(int filteredWineAmount) {
            this.filteredWineAmount = filteredWineAmount;
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
    public static Behavior<Command> create(ActorRef<Warehouse.Command> warehouse, ActorRef<Filtration.Command> filtration, int bottles) {
        return Behaviors.setup(context -> new Bottling(context, warehouse, filtration, bottles));
    }

    // Actor state ------------------------------------------------------
    private static final float REQUIRED_FILTERED_WINE_L = 0.75f;
    private static final int PRODUCED_BOTTLES = 1;
    private static final int FAILURE_RATE_PERCENT = 5;
    private static final int PROCESSING_TIME_MINUTES = 5;
    private static final int SLOTS = 1;
    private final ActorRef<Warehouse.Command> warehouse;
    private final Map<Integer, ActorRef<BottlingSlot.Command>> slots = new HashMap<>();
    private final Queue<Integer> freeSlots = new LinkedList<>();
    private boolean willNewResourcesCome = true;
    private float filteredWine = 0;
    private int bottles;

    // Constructor ------------------------------------------------------
    private Bottling(ActorContext<Command> context, ActorRef<Warehouse.Command> warehouse, ActorRef<Filtration.Command> filtration, int bottles) {
        super(context);
        this.warehouse = warehouse;
        this.bottles = bottles;

        getContext().watch(filtration);

        // Create the slots
        for (int i = 0; i < SLOTS; i++) {
            slots.put(i, context.spawn(BottlingSlot.create(i, Duration.ofMillis(PROCESSING_TIME_MINUTES * 10)), "stamping-slot-" + i));
            freeSlots.add(i);
        }
    }

    // Actor behavior ---------------------------------------------------
    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(AddFilteredWine.class, this::onAddFilteredWine)
                .onMessage(FinishedProcessing.class, this::onFinishedProcessing)
                .onMessage(Shutdown.class, shutdown -> Behaviors.stopped())
                .onSignal(Terminated.class, signal -> onFiltrationStop())
                .build();
    }

    private Behavior<Command> onAddFilteredWine(AddFilteredWine msg) {
        filteredWine += msg.filteredWineAmount;
        beginProcessing();

        return this;
    }

    private void beginProcessing() {
        while (checkProducts() && !freeSlots.isEmpty()) {
            filteredWine -= REQUIRED_FILTERED_WINE_L;
            bottles -= PRODUCED_BOTTLES;
            int slotNumber = freeSlots.poll();

            slots.get(slotNumber).tell(new BottlingSlot.BeginProcessing(getContext().getSelf()));
        }

        checkTermination();
    }

    private boolean checkProducts() {
        return filteredWine >= REQUIRED_FILTERED_WINE_L && bottles >= PRODUCED_BOTTLES;
    }

    private void checkTermination() {
        if (freeSlots.size() == SLOTS && !willNewResourcesCome) {
            getContext().getSelf().tell(Shutdown.INSTANCE);
            warehouse.tell(Warehouse.Shutdown.SHUTDOWN_INSTANCE);
        }
    }

    private Behavior<Command> onFinishedProcessing(FinishedProcessing msg) {
        getContext().getLog().info("🥳🥳🥳 bottling-slot-{} finished processing 🥳🥳🥳", msg.slotNumber);
        freeSlots.add(msg.slotNumber);

        // If the processing was successful, send the wine bottles to the warehouse
        if (isSuccessful()) {
            warehouse.tell(new Warehouse.AddBottles(PRODUCED_BOTTLES));
        }

        // Begin processing again
        beginProcessing();

        return this;
    }

    private boolean isSuccessful() {
        if (Math.random() * 100 < FAILURE_RATE_PERCENT) {
            getContext().getLog().info("❌❌❌ Bottling failed ❌❌❌");

            return false;
        } else {
            getContext().getLog().info("✅✅✅ Bottling successful ✅✅✅");
            getContext().getLog().info("🍾🍾🍾 Bottles produced: {} 🍾🍾🍾", PRODUCED_BOTTLES);
            getContext().getLog().info("📦📦📦 Sending resources to warehouse 📦📦📦");

            return true;
        }
    }

    private Behavior<Command> onFiltrationStop() {
        willNewResourcesCome = false;
        checkTermination();

        return this;
    }
}
