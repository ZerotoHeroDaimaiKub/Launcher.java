package seproject.model.edgedetector.imagederivatives;

public enum Gradient {
    LEFT, RIGHT, SIMPLE_SYMMETRIC, DOUBLE_SYMMETRIC;

    @Override
    public String toString() {
        switch (this) {
            case LEFT:             return "Left gradient: (f_n) - (f_n-1)";
            case RIGHT:            return "Right gradient: (f_n+1) - (f_n)";
            case SIMPLE_SYMMETRIC: return "Simple_symmetric gradient: (f_n+1) - (f_n-1)";
            case DOUBLE_SYMMETRIC: return "Double_symmetric gradient: (f_n+1) - 2 * (f_n) + (f_n-1)";
        }
        return super.toString();
    }
}
