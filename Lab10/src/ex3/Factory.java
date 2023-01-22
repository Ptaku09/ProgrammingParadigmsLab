package ex3;

import akka.actor.typed.ActorSystem;

public class Factory {
    public static void main(String[] args) {
        final ActorSystem<Warehouse.Command> system = ActorSystem.create(Warehouse.create(100, 100, 100, 100, 70), "warehouse");
        system.tell(Warehouse.BeginProduction.BEGIN_INSTANCE);
    }
}
