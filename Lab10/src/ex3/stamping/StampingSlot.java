package ex3.stamping;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.time.Duration;

public class StampingSlot extends AbstractBehavior<StampingSlot.Command> {
    // Acceptable commands -----------------------------------------------
    interface Command {}

    public static final class BeginProcessing implements Command {
        final ActorRef<Stamping.Command> replyTo;

        public BeginProcessing(ActorRef<Stamping.Command> replyTo) {
            this.replyTo = replyTo;
        }
    }

    // Actor creation ---------------------------------------------------
    public static Behavior<Command> create(int slotNumber, Duration duration) {
        return Behaviors.setup(context -> new StampingSlot(context, slotNumber, duration));
    }

    // Actor state ------------------------------------------------------
    private final int slotNumber;
    private final Duration duration;

    // Constructor ------------------------------------------------------
    private StampingSlot(ActorContext<Command> context, int slotNumber, Duration duration) {
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
        getContext().getLog().info("⚙️️️️️⚙️️️️️⚙️️️️️ stamping-slot-{} processing ⚙️️️️️⚙️️️️️⚙️️️️️", slotNumber);
        getContext().scheduleOnce(duration, command.replyTo, new Stamping.FinishedProcessing(slotNumber));

        return this;
    }
}
