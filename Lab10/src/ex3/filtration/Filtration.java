package ex3.filtration;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import ex3.Warehouse;
import ex3.fermentation.Fermentation;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Filtration extends AbstractBehavior<Filtration.Command> {
    // Acceptable commands -----------------------------------------------
    public interface Command {}

    public static final class AddUnfilteredWine implements Command {
        final int unfilteredWineAmount;

        public AddUnfilteredWine(int unfilteredWineAmount) {
            this.unfilteredWineAmount = unfilteredWineAmount;
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
    public static Behavior<Command> create(ActorRef<Warehouse.Command> warehouse, ActorRef<Fermentation.Command> fermentation, int speed) {
        return Behaviors.setup(context -> new Filtration(context, warehouse, fermentation, speed));
    }

    // Actor state ------------------------------------------------------
    private static final int REQUIRED_UNFILTERED_WINE_L = 25;
    private static final int PRODUCED_FILTERED_WINE_L = 24;
    private static final int FAILURE_RATE_PERCENT = 0;
    private static final int PROCESSING_TIME_MINUTES = 12 * 60;
    private static final int SLOTS = 10;
    private final ActorRef<Warehouse.Command> warehouse;
    private final Map<Integer, ActorRef<FiltrationSlot.Command>> slots = new HashMap<>();
    private final Queue<Integer> freeSlots = new LinkedList<>();
    private boolean willNewResourcesCome = true;
    private int unfilteredWine = 0;

    // Constructor ------------------------------------------------------
    private Filtration(ActorContext<Command> context, ActorRef<Warehouse.Command> warehouse, ActorRef<Fermentation.Command> fermentation, int speed) {
        super(context);
        this.warehouse = warehouse;

        getContext().watch(fermentation);

        // Create the slots
        for (int i = 0; i < SLOTS; i++) {
            slots.put(i, context.spawn(FiltrationSlot.create(i, Duration.ofMillis(PROCESSING_TIME_MINUTES * 10 / speed)), "fermentation-slot-" + i));
            freeSlots.add(i);
        }
    }

    // Actor behavior ---------------------------------------------------
    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(AddUnfilteredWine.class, this::onAddUnfilteredWine)
                .onMessage(FinishedProcessing.class, this::onFinishedProcessing)
                .onMessage(Shutdown.class, shutdown -> Behaviors.stopped())
                .onSignal(Terminated.class, signal -> onFermentationStop())
                .build();
    }

    private Behavior<Command> onAddUnfilteredWine(AddUnfilteredWine msg) {
        unfilteredWine += msg.unfilteredWineAmount;
        beginProcessing();

        return this;
    }

    private void beginProcessing() {
        while (checkProducts() && !freeSlots.isEmpty()) {
            unfilteredWine -= REQUIRED_UNFILTERED_WINE_L;
            int slotNumber = freeSlots.poll();

            slots.get(slotNumber).tell(new FiltrationSlot.BeginProcessing(getContext().getSelf()));
        }

        checkTermination();
    }

    private boolean checkProducts() {
        return unfilteredWine >= REQUIRED_UNFILTERED_WINE_L;
    }

    private void checkTermination() {
        if (freeSlots.size() == SLOTS && !willNewResourcesCome) {
            getContext().getSelf().tell(Shutdown.INSTANCE);
        }
    }

    private Behavior<Command> onFinishedProcessing(FinishedProcessing msg) {
        getContext().getLog().info("🥳🥳🥳 filtration-slot-{} finished processing 🥳🥳🥳", msg.slotNumber);
        freeSlots.add(msg.slotNumber);

        // If the processing was successful, send the filtered wine to the warehouse
        if (isSuccessful()) {
            warehouse.tell(new Warehouse.AddFilteredWine(PRODUCED_FILTERED_WINE_L));
        }

        // Begin processing again
        beginProcessing();

        return this;
    }

    private boolean isSuccessful() {
        if (Math.random() * 100 < FAILURE_RATE_PERCENT) {
            getContext().getLog().info("❌❌❌ Filtration failed ❌❌❌");

            return false;
        } else {
            getContext().getLog().info("✅✅✅ Filtration successful ✅✅✅");
            getContext().getLog().info("🍷🍷🍷 Produced {}L of filtered wine 🍷🍷🍷", PRODUCED_FILTERED_WINE_L);
            getContext().getLog().info("📦📦📦 Sending resources to bottling 📦📦📦");

            return true;
        }
    }

    private Behavior<Command> onFermentationStop() {
        willNewResourcesCome = false;
        checkTermination();

        return this;
    }
}
