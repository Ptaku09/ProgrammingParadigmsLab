package ex3.fermentation;

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

public class Fermentation extends AbstractBehavior<Fermentation.Command> {
    // Acceptable commands -----------------------------------------------
    public interface Command {}

    public static final class AddJuice implements Command {
        final int juiceAmount;

        public AddJuice(int juiceAmount) {
            this.juiceAmount = juiceAmount;
        }
    }

    public static final class AddWater implements Command {
        final int waterAmount;

        public AddWater(int waterAmount) {
            this.waterAmount = waterAmount;
        }
    }

    public static final class AddSugar implements Command {
        final int sugarAmount;

        public AddSugar(int sugarAmount) {
            this.sugarAmount = sugarAmount;
        }
    }

    public static final class FinishedProcessing implements Command {
        final int slotNumber;

        public FinishedProcessing(int slotNumber) {
            this.slotNumber = slotNumber;
        }
    }

    // Actor creation ---------------------------------------------------
    public static Behavior<Command> create(ActorRef<Warehouse.Command> warehouse, int water, int sugar) {
        return Behaviors.setup(context -> new Fermentation(context, warehouse, water, sugar));
    }

    // Actor state ------------------------------------------------------
    private static final int REQUIRED_JUICE_KG = 15;
    private static final int REQUIRED_WATER_L = 8;
    private static final int REQUIRED_SUGAR_KG = 2;
    private static final int PRODUCED_UNFILTERED_WINE_L = 25;
    private static final int FAILURE_RATE_PERCENT = 5;
    private static final int PROCESSING_TIME_MINUTES = 20160;
    private static final int SLOTS = 10;
    private final ActorRef<Warehouse.Command> warehouse;
    private final Map<Integer, ActorRef<FermentationSlot.Command>> slots = new HashMap<>();
    private final Queue<Integer> freeSlots = new LinkedList<>();
    private int juice = 0;
    private int water;
    private int sugar;

    // Constructor ------------------------------------------------------
    private Fermentation(ActorContext<Command> context, ActorRef<Warehouse.Command> warehouse, int water, int sugar) {
        super(context);
        this.warehouse = warehouse;
        this.water = water;
        this.sugar = sugar;

        // Create the slots
        for (int i = 0; i < SLOTS; i++) {
            slots.put(i, context.spawn(FermentationSlot.create(i, Duration.ofMillis(PROCESSING_TIME_MINUTES * 10)), "fermentation-slot-" + i));
            freeSlots.add(i);
        }
    }

    // Actor behavior ---------------------------------------------------
    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(AddJuice.class, this::onAddJuice)
                .onMessage(AddWater.class, this::onAddWater)
                .onMessage(AddSugar.class, this::onAddSugar)
                .onMessage(FinishedProcessing.class, this::onFinishedProcessing)
                .build();
    }

    private Behavior<Command> onAddJuice(AddJuice msg) {
        juice += msg.juiceAmount;
        beginProcessing();

        return this;
    }

    private Behavior<Command> onAddWater(AddWater msg) {
        water += msg.waterAmount;
        beginProcessing();

        return this;
    }

    private Behavior<Command> onAddSugar(AddSugar msg) {
        sugar += msg.sugarAmount;
        beginProcessing();

        return this;
    }

    private void beginProcessing() {
        while (checkProducts() && !freeSlots.isEmpty()) {
            juice -= REQUIRED_JUICE_KG;
            water -= REQUIRED_WATER_L;
            sugar -= REQUIRED_SUGAR_KG;
            int slotNumber = freeSlots.poll();

            slots.get(slotNumber).tell(new FermentationSlot.BeginProcessing(getContext().getSelf()));
        }
    }

    private boolean checkProducts() {
        return juice >= REQUIRED_JUICE_KG && water >= REQUIRED_WATER_L && sugar >= REQUIRED_SUGAR_KG;
    }

    private Behavior<Command> onFinishedProcessing(FinishedProcessing msg) {
        getContext().getLog().info("fermentation-slot-{} finished processing 🥳", msg.slotNumber);
        freeSlots.add(msg.slotNumber);

        // If the processing was successful, send the unfiltered wine to the warehouse
        if (isSuccessful()) {
            warehouse.tell(new Warehouse.AddUnfilteredWine(PRODUCED_UNFILTERED_WINE_L));
        }

        // Begin processing again
        beginProcessing();

        return this;
    }

    private boolean isSuccessful() {
        if (Math.random() * 100 < FAILURE_RATE_PERCENT) {
            getContext().getLog().info("Fermentation failed ❌");

            return false;
        } else {
            getContext().getLog().info("Fermentation successful ✅");
            getContext().getLog().info("Produced {}L of unfiltered wine", PRODUCED_UNFILTERED_WINE_L);
            getContext().getLog().info("Sending resources to filtration");

            return true;
        }
    }
}
