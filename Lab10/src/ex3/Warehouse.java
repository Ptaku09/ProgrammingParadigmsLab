package ex3;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class Warehouse extends AbstractActor {
    private int grapes;
    private int water;
    private int sugar;
    private int bottles;
    private int producedBottles = 0;

    public Warehouse(int grapes, int water, int sugar, int bottles) {
        this.grapes = grapes;
        this.water = water;
        this.sugar = sugar;
        this.bottles = bottles;
    }

    public static Props props(int grapes, int water, int sugar, int bottles) {
        return Props.create(Warehouse.class, () -> new Warehouse(grapes, water, sugar, bottles));
    }

    @Override
    public Receive createReceive() {
        return null;
//        return receiveBuilder().match(WarehouseProtocol.GetGrapes.class, g -> {
//            if (this.grapes >= g.amount) {
//                this.grapes -= g.amount;
//                getSender().tell(new WarehouseProtocol.Grapes(g.amount), getSelf());
//            } else {
//                getSender().tell(new WarehouseProtocol.Grapes(0), getSelf());
//            }
//        }).match(WarehouseProtocol.GetWater.class, w -> {
//            if (this.water >= w.amount) {
//                this.water -= w.amount;
//                getSender().tell(new WarehouseProtocol.Water(w.amount), getSelf());
//            } else {
//                getSender().tell(new WarehouseProtocol.Water(0), getSelf());
//            }
//        }).match(WarehouseProtocol.GetSugar.class, s -> {
//            if (this.sugar >= s.amount) {
//                this.sugar -= s.amount;
//                getSender().tell(new WarehouseProtocol.Sugar(s.amount), getSelf());
//            } else {
//                getSender().tell(new WarehouseProtocol.Sugar(0), getSelf());
//            }
//        }).match(WarehouseProtocol.GetBottles.class, b -> {
//            if (this.bottles >= b.amount) {
//                this.bottles -= b.amount;
//                getSender().tell(new WarehouseProtocol.Bottles(b.amount), getSelf());
//            } else {
//                getSender().tell(new WarehouseProtocol.Bottles(0), getSelf());
//            }
//        }).build();
    }
}
