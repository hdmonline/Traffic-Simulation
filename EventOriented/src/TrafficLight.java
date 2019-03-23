/**
 * TrafficLight.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * Class for traffic lights. Yellow lights are counted into greens.
 */

public class TrafficLight {
    public int id;
    private final double SOUTH_RED_DURATION;
    private final double SOUTH_GREEN_DURATION;
    private final double SOUTH_LEFT_RED_DUATION;
    private final double SOUTH_LEFT_GREEN_DURATION;
    private final double SOUTH_TOTAL;

    public TrafficLight(int id, double southRed, double southGreen, double southLeftGreen, double southLeftRed) {
        this.id = id;
        SOUTH_GREEN_DURATION = southGreen;
        SOUTH_RED_DURATION = southRed;
        SOUTH_ =
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
