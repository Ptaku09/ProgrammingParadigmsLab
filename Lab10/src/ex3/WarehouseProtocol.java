package ex3;

public class WarehouseProtocol {
    public static final class GetGrapes {
        public final int amount;

        public GetGrapes(int amount) {
            this.amount = amount;
        }
    }

    public static final class GetWater {
        public final int amount;

        public GetWater(int amount) {
            this.amount = amount;
        }
    }

    public static final class GetSugar {
        public final int amount;

        public GetSugar(int amount) {
            this.amount = amount;
        }
    }

    public static final class GetBottles {
        public final int amount;

        public GetBottles(int amount) {
            this.amount = amount;
        }
    }
}
