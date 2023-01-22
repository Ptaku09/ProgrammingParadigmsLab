package ex3;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import ex3.bottling.Bottling;
import ex3.fermentation.Fermentation;
import ex3.filtration.Filtration;
import ex3.stamping.Stamping;

import java.time.Duration;
import java.time.Instant;

public class Warehouse extends AbstractBehavior<Warehouse.Command> {
    // Acceptable commands -----------------------------------------------
    public interface Command {}

    public enum BeginProduction implements Command {
        BEGIN_INSTANCE
    }

    public enum Shutdown implements Command {
        SHUTDOWN_INSTANCE
    }

    public static final class AddJuice implements Command {
        final int juiceAmount;

        public AddJuice(int juiceAmount) {
            this.juiceAmount = juiceAmount;
        }
    }

    public static final class AddUnfilteredWine implements Command {
        final int unfilteredWineAmount;

        public AddUnfilteredWine(int unfilteredWineAmount) {
            this.unfilteredWineAmount = unfilteredWineAmount;
        }
    }

    public static final class AddFilteredWine implements Command {
        final int filteredWineAmount;

        public AddFilteredWine(int filteredWineAmount) {
            this.filteredWineAmount = filteredWineAmount;
        }
    }

    public static final class AddBottles implements Command {
        final int bottlesAmount;

        public AddBottles(int bottlesAmount) {
            this.bottlesAmount = bottlesAmount;
        }
    }

    // Actor creation ---------------------------------------------------
    public static Behavior<Command> create(int grapes, int water, int sugar, int bottles) {
        return Behaviors.setup(context -> new Warehouse(context, grapes, water, sugar, bottles));
    }

    // Actor state ------------------------------------------------------
    private final ActorRef<Stamping.Command> stamping;
    private final ActorRef<Fermentation.Command> fermentation;
    private final ActorRef<Filtration.Command> filtration;
    private final ActorRef<Bottling.Command> bottling;
    private final int grapes;
    private Instant startTime;
    private Instant endTime;
    private int producedJuice = 0;
    private int producedUnfilteredWine = 0;
    private int producedFilteredWine = 0;
    private int producedBottles = 0;

    // Constructor ------------------------------------------------------
    private Warehouse(ActorContext<Command> context, int grapes, int water, int sugar, int bottles) {
        super(context);

        this.grapes = grapes;
        this.stamping = context.spawn(Stamping.create(getContext().getSelf()), "stamping");
        this.fermentation = context.spawn(Fermentation.create(getContext().getSelf(), stamping, water, sugar), "fermentation");
        this.filtration = context.spawn(Filtration.create(getContext().getSelf(), fermentation), "filtration");
        this.bottling = context.spawn(Bottling.create(getContext().getSelf(), filtration, bottles), "bottling");

        getContext().watch(stamping);
        getContext().watch(fermentation);
        getContext().watch(filtration);
        getContext().watch(bottling);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(BeginProduction.class, this::onBeginProduction)
                .onMessage(AddJuice.class, this::onAddJuice)
                .onMessage(AddUnfilteredWine.class, this::onAddUnfilteredWine)
                .onMessage(AddFilteredWine.class, this::onAddFilteredWine)
                .onMessage(AddBottles.class, this::onAddBottles)
                .onMessage(Shutdown.class, shutdown -> onShutdown())
                .onSignal(Terminated.class, this::onTerminated)
                .build();
    }

    private Behavior<Command> onBeginProduction(BeginProduction msg) {
        startTime = Instant.now();
        informStamping();

        return this;
    }

    private void informStamping() {
        stamping.tell(new Stamping.AddGrapes(grapes));
    }

    private Behavior<Command> onAddJuice(AddJuice msg) {
        producedJuice += msg.juiceAmount;
        informFermentation();

        return this;
    }

    private void informFermentation() {
        fermentation.tell(new Fermentation.AddJuice(producedJuice));
    }

    private Behavior<Command> onAddUnfilteredWine(AddUnfilteredWine msg) {
        producedUnfilteredWine += msg.unfilteredWineAmount;
        informFiltration();

        return this;
    }

    private void informFiltration() {
        filtration.tell(new Filtration.AddUnfilteredWine(producedUnfilteredWine));
    }

    private Behavior<Command> onAddFilteredWine(AddFilteredWine msg) {
        producedFilteredWine += msg.filteredWineAmount;
        informBottling();

        return this;
    }

    private void informBottling() {
        bottling.tell(new Bottling.AddFilteredWine(producedFilteredWine));
    }

    private Behavior<Command> onAddBottles(AddBottles msg) {
        producedBottles += msg.bottlesAmount;
        getContext().getLog().info("🍾🍾🍾 New bottle added 🍾🍾🍾");

        return this;
    }

    private Behavior<Command> onShutdown() {
        endTime = Instant.now();
        generateReport();

        return Behaviors.stopped();
    }

    private void generateReport() {
        getContext().getLog().info("🏁🏁🏁 Production ended 🏁🏁🏁");
        getContext().getLog().info("🕐🕐🕐 Production time: {}s 🕐🕐🕐", Duration.between(startTime, endTime).toSeconds());
        getContext().getLog().info("🍷🍷🍷 Produced {} bottles of wine 🍷🍷🍷", producedBottles);
    }

    private Behavior<Command> onTerminated(Terminated msg) {
        getContext().getLog().info("❌❌❌ Terminated {} ❌❌❌", msg.getRef().path().name());
        return this;
    }
}

class WarehouseTest {
    public static void main(String[] args) {
        final ActorSystem<Warehouse.Command> system = ActorSystem.create(Warehouse.create(100, 100, 100, 100), "warehouse");
        system.tell(Warehouse.BeginProduction.BEGIN_INSTANCE);
    }
}
