package ex3;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import ex3.fermentation.Fermentation;
import ex3.filtration.Filtration;
import ex3.stamping.Stamping;

public class Warehouse extends AbstractBehavior<Warehouse.Command> {
    // Acceptable commands -----------------------------------------------
    public interface Command {}

    public enum BeginProduction implements Command {
        INSTANCE
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

    // Actor creation ---------------------------------------------------
    public static Behavior<Command> create(int grapes, int water, int sugar, int bottles) {
        return Behaviors.setup(context -> new Warehouse(context, grapes, water, sugar, bottles));
    }

    // Actor state ------------------------------------------------------
    private final ActorRef<Stamping.Command> stamping;
    private final ActorRef<Fermentation.Command> fermentation;
    private final ActorRef<Filtration.Command> filtration;
    private int grapes;
    private int water;
    private int sugar;
    private int bottles;
    private int producedJuice = 0;
    private int producedUnfilteredWine = 0;
    private int producedFilteredWine = 0;
    private int producedBottles = 0;

    // Constructor ------------------------------------------------------
    private Warehouse(ActorContext<Command> context, int grapes, int water, int sugar, int bottles) {
        super(context);

        this.grapes = grapes;
        this.water = water;
        this.sugar = sugar;
        this.bottles = bottles;
        this.stamping = context.spawn(Stamping.create(getContext().getSelf()), "stamping");
        this.fermentation = context.spawn(Fermentation.create(getContext().getSelf()), "fermentation");
        this.filtration = context.spawn(Filtration.create(getContext().getSelf()), "filtration");
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(BeginProduction.class, this::onBeginProduction)
                .onMessage(AddJuice.class, this::onAddJuice)
                .onMessage(AddUnfilteredWine.class, this::onAddUnfilteredWine)
                .onMessage(AddFilteredWine.class, this::onAddFilteredWine)
                .build();
    }

    private Behavior<Command> onBeginProduction(BeginProduction msg) {
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
        fermentation.tell(new Fermentation.AddWater(water));
        fermentation.tell(new Fermentation.AddSugar(sugar));
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
        //TODO: implement
    }
}
