package ex3.bottling;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.time.Duration;

public class BottlingSlot extends AbstractBehavior<BottlingSlot.Command> {
    // Acceptable commands -----------------------------------------------
    interface Command {}

    public static final class BeginProcessing implements Command {
        final ActorRef<Bottling.Command> replyTo;

        public BeginProcessing(ActorRef<Bottling.Command> replyTo) {
            this.replyTo = replyTo;
        }
    }

    // Actor creation ---------------------------------------------------
    public static Behavior<Command> create(int slotNumber, Duration duration) {
        return Behaviors.setup(context -> new BottlingSlot(context, slotNumber, duration));
    }

    // Actor state ------------------------------------------------------
    private final int slotNumber;
    private final Duration duration;

    // Constructor ------------------------------------------------------
    private BottlingSlot(ActorContext<Command> context, int slotNumber, Duration duration) {
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
        getContext().scheduleOnce(duration, command.replyTo, new Bottling.FinishedProcessing(slotNumber));

        return this;
    }
}
