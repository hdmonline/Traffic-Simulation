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

    /**
     * Find next green light interval.
     * The period is leftGreen -> leftRed -> Green -> Red.
     *
     * @param time current interval
     * @param num number of green lights to skip
     * @return the next green light interval
     */
    public double nextSouthThroughGreen(double time, double num) {
        double mod = time % SOUTH_TOTAL;
        double numGreens = mod < SOUTH_LEFT_TOTAL ? Math.floor(time / SOUTH_TOTAL) : Math.floor(time / SOUTH_TOTAL) + 1;
        numGreens += num;
        return numGreens * SOUTH_TOTAL + SOUTH_LEFT_TOTAL;
    }

    public double nextSouthThroughGreen(double time) {
        return nextSouthThroughGreen(time, 0);
    }

    public double nextSouthThroughRed(double time, double num) {
        double mod = time % SOUTH_TOTAL;
        double numReds = mod < (SOUTH_LEFT_TOTAL + SOUTH_THROUGH_GREEN_DURATION) ?  Math.floor(time / SOUTH_TOTAL) :
                Math.floor(time / SOUTH_TOTAL) + 1;
        numReds += num;
        return numReds * SOUTH_TOTAL + SOUTH_LEFT_TOTAL + SOUTH_THROUGH_GREEN_DURATION;
    }

    public double nextSouthThroughRed(double time) {
        return nextSouthThroughRed(time, 0);
    }

    public boolean isThroughGreen(double time) {
        return false;
    }

    // Getters
    public int getIntersection() {
        return intersection;
    }

    public int getPos() {
        return pos;
    }

    @Override
    public int compareTo(TrafficLight tl) {
        return tl.pos - this.pos;
    }
}
