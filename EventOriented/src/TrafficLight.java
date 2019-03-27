/**
 * TrafficLight.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * Class for traffic lights. Yellow lights are counted into greens.
 */


public class TrafficLight {
    private int id;
    private final double SOUTH_THROUGH_RED_DURATION;
    private final double SOUTH_THROUGH_GREEN_DURATION;
    private final double SOUTH_LEFT_RED_DURATION;
    private final double SOUTH_LEFT_GREEN_DURATION;
    private final double SOUTH_LEFT_TOTAL;
    private final double SOUTH_THROUGH_TOTAL;
    private final double SOUTH_TOTAL;

    public TrafficLight(int id, double southThroughRed, double southThroughGreen, double southLeftGreen, double southLeftRed) {
        this.id = id;
        SOUTH_THROUGH_GREEN_DURATION = southThroughGreen;
        SOUTH_THROUGH_RED_DURATION = southThroughRed;
        SOUTH_LEFT_GREEN_DURATION = southLeftGreen;
        SOUTH_LEFT_RED_DURATION = southLeftRed;
        SOUTH_LEFT_TOTAL = southLeftGreen + southLeftRed;
        SOUTH_THROUGH_TOTAL = southThroughGreen + southThroughRed;
        SOUTH_TOTAL =  SOUTH_THROUGH_TOTAL + SOUTH_LEFT_TOTAL;
    }

    /**
     * Find next green light time.
     * The period is leftGreen -> leftRed -> Green -> Red.
     *
     * @param time current time
     * @param num number of green lights to skip
     * @return the next green light time
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
        double numLights = Math.floor(time / SOUTH_TOTAL);
        double beforeGreen = numLights * SOUTH_TOTAL + SOUTH_LEFT_TOTAL;
        double afterGreen = numLights * SOUTH_TOTAL + SOUTH_LEFT_TOTAL + SOUTH_THROUGH_GREEN_DURATION;
        boolean isGreen = beforeGreen <= time && time < afterGreen;
        return isGreen;
    }

    public boolean isThroughRed(double time) {
        return !isThroughGreen(time);
    }

    public boolean isLeftGreen(double time) {
        double numLights = Math.floor(time / SOUTH_TOTAL);
        double beforeGreen = numLights * SOUTH_TOTAL;
        double afterGreen = numLights * SOUTH_TOTAL + SOUTH_LEFT_GREEN_DURATION;
        boolean isGreen = beforeGreen <= time && time < afterGreen;
        return isGreen;
    }

    public boolean isLeftRed(double time) {
        return !isLeftGreen(time);
    }

    // Getters
    public int getId() {
        return id;
    }

    public double getSouthThroughRed() {
        return SOUTH_THROUGH_RED_DURATION;
    }

    public double getSouthThroughGreen() {
        return SOUTH_THROUGH_GREEN_DURATION;
    }

    public double getSouthLeftRad() {
        return SOUTH_LEFT_RED_DURATION;
    }

    public double getSouthLeftGreen() {
        return SOUTH_LEFT_GREEN_DURATION;
    }

    public double getSouthLeftTotal() {
        return SOUTH_LEFT_TOTAL;
    }

    public double getSouthThroughTotal() {
        return SOUTH_THROUGH_TOTAL;
    }

    public double getSouthTotal() {
        return SOUTH_TOTAL;
    }
}
