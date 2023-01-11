public abstract class Shape extends Primitive {
    private boolean isFilled;

    public Shape(MyPoint position, boolean isFilled) {
        super(position);
        this.isFilled = isFilled;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public void setFilled(boolean filled) {
        isFilled = filled;
    }
}
