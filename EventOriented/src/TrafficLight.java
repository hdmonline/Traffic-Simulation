public class TrafficLight {
    public int id;
    public final double SOUTH_RED_DURATION;
    public final double SOUTH_GREEN_DURATION;
    public final double SOUTH_TOTAL;

    public TrafficLight(int id, double southRed, double southGreen) {
        SOUTH_GREEN_DURATION = southGreen;
        SOUTH_RED_DURATION = southRed;
        SOUTH_TOTAL = southGreen + southRed;
    }

    /**
     * Find next green light time. Green first in a period.
     *
     * @param time current time
     * @return the next green light time
     */
    public double nextSouthGreen(double time) {
        double numGreens = Math.floor(time / SOUTH_TOTAL);
        return (numGreens + 1) * SOUTH_TOTAL;
    }

    public double nextRed(double time) {

    }

    public boolean isGreen(double time) {

    }

    public boolean isRed(double time) {

    }
}
