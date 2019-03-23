public class TrafficLight {
    public int id;
    public final double SOUTH_RED_DURATION;
    public final double SOUTH_GREEN_DURATION;

    public TrafficLight(int id, double southRed, double southGreen) {
        SOUTH_GREEN_DURATION = southGreen;
        SOUTH_RED_DURATION = southRed;
    }
}
