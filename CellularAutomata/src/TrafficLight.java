/**
 * TrafficLight.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * Class for traffic lights. Yellow lights are counted into greens.
 */

public class TrafficLight implements Comparable<TrafficLight> {
    private int intersection;
    private int pos;
    private final double SOUTH_THROUGH_RED_DURATION, SOUTH_THROUGH_GREEN_DURATION, SOUTH_THROUGH_TOTAL;
    private final double SOUTH_LEFT_RED_DURATION, SOUTH_LEFT_GREEN_DURATION, SOUTH_LEFT_TOTAL;
    private final double SOUTH_TOTAL;

    public TrafficLight(int intersection, int pos, double southThroughRed, double southThroughGreen, double southLeftGreen, double southLeftRed) {
        this.intersection = intersection;
        this.pos = pos;
        SOUTH_THROUGH_GREEN_DURATION = southThroughGreen;
        SOUTH_THROUGH_RED_DURATION = southThroughRed;
        SOUTH_LEFT_GREEN_DURATION = southLeftGreen;
        SOUTH_LEFT_RED_DURATION = southLeftRed;
        SOUTH_LEFT_TOTAL = southLeftGreen + southLeftRed;
        SOUTH_THROUGH_TOTAL = southThroughGreen + southThroughRed;
        SOUTH_TOTAL =  SOUTH_THROUGH_TOTAL + SOUTH_LEFT_TOTAL;
    }

    public boolean isSouthThroughGreen(double time) {
        double numLights = Math.floor(time / SOUTH_TOTAL);
        double beforeGreen = numLights * SOUTH_TOTAL + SOUTH_LEFT_TOTAL;
        double afterGreen = numLights * SOUTH_TOTAL + SOUTH_LEFT_TOTAL + SOUTH_THROUGH_GREEN_DURATION;
        boolean isGreen = beforeGreen <= time && time < afterGreen;
        return isGreen;
    }


    //

    // Getters
    public int getPos() {
        return pos;
    }

    public int getIntersection() {
        return intersection;
    }

    @Override
    public int compareTo(TrafficLight tl) {
        return tl.pos - this.pos;
    }
}
