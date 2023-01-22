package ex3.filtration;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.time.Duration;

public class FiltrationSlot extends AbstractBehavior<FiltrationSlot.Command> {
    // Acceptable commands -----------------------------------------------
    interface Command {}

    public static final class BeginProcessing implements Command {
        final ActorRef<Filtration.Command> replyTo;

        public BeginProcessing(ActorRef<Filtration.Command> replyTo) {
            this.replyTo = replyTo;
        }
    }

    // Actor creation ---------------------------------------------------
    public static Behavior<Command> create(int slotNumber, Duration duration) {
        return Behaviors.setup(context -> new FiltrationSlot(context, slotNumber, duration));
    }

    // Actor state ------------------------------------------------------
    private final int slotNumber;
    private final Duration duration;

    // Constructor ------------------------------------------------------
    private FiltrationSlot(ActorContext<Command> context, int slotNumber, Duration duration) {
        super(context);
        this.slotNumber = slotNumber;
        this.duration = duration;
    }

    // Actor behavior ---------------------------------------------------
    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder().onMessage(BeginProcessing.class, this::onBeginProcessing).build();
    }

    private Behavior<Command> onBeginProcessing(BeginProcessing command) {
        getContext().getLog().info("⚙️️️️️⚙️️️️️⚙️️️️️ filtration-slot-{} processing ⚙️️️️️⚙️️️️️⚙️️️️️", slotNumber);
        getContext().scheduleOnce(duration, command.replyTo, new Filtration.FinishedProcessing(slotNumber));

        return this;
    }
}
