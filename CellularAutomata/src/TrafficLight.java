import java.time.OffsetDateTime;
import java.util.LinkedList;

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
    private final double NORTH_BOUND_THROUGH_RED_DURATION, NORTH_BOUND_THROUGH_GREEN_DURATION,
            NORTH_BOUND_LEFT_RED_DURATION, NORTH_BOUND_LEFT_GREEN_DURATION,
            NORTH_BOUND_LEFT_TOTAL, NORTH_BOUND_THROUGH_TOTAL;
    private final double WEST_BOUND_THROUGH_RED_DURATION, WEST_BOUND_THROUGH_GREEN_DURATION,
            WEST_BOUND_LEFT_RED_DURATION, WEST_BOUND_LEFT_GREEN_DURATION,
            WEST_BOUND_LEFT_TOTAL;
    private final double EAST_BOUND_THROUGH_RED_DURATION, EAST_BOUND_THROUGH_GREEN_DURATION,
            EAST_BOUND_LEFT_RED_DURATION, EAST_BOUND_LEFT_GREEN_DURATION,
            EAST_BOUND_LEFT_TOTAL;
    private final double TOTAL_DURATION;

    // Status variables
    private boolean isThroughRightSouthGreen, isThroughRightWestGreen, isThroughRightEastGreen;
    private boolean isLeftSouthGreen, isLeftWestGreen;
    private boolean availableThroughRightSouth, availableThroughRightEast;
    private boolean availableLeftSouth, availableLeftWest;

    // Vehicle queues for going through, First -> In, Last -> Out
    private LinkedList<Vehicle> southThroughRightVehs, eastThroughRightVehs;
    // Vehicle queues for turning left
    private LinkedList<Vehicle> southLeftVehs, westLeftVehs;

    public TrafficLight(int intersection,  int pos, double nbLtG, double nbLtR, double nbTrG, double nbTrR,
                        double wbLtG, double wbLtR, double wbTrG, double wbTrR,
                        double ebLtG, double ebLtR, double ebTrG, double ebTrR) {
        this.intersection = intersection;
        this.pos = pos;
        // North bound
        NORTH_BOUND_THROUGH_GREEN_DURATION = nbTrG;
        NORTH_BOUND_THROUGH_RED_DURATION = nbTrR;
        NORTH_BOUND_LEFT_GREEN_DURATION = nbLtG;
        NORTH_BOUND_LEFT_RED_DURATION = nbLtR;
        NORTH_BOUND_LEFT_TOTAL = nbLtG + nbLtR;
        NORTH_BOUND_THROUGH_TOTAL = nbTrG + nbTrR;

        // West bound
        WEST_BOUND_THROUGH_RED_DURATION = wbTrR;
        WEST_BOUND_THROUGH_GREEN_DURATION = wbTrG;
        WEST_BOUND_LEFT_RED_DURATION = wbLtR;
        WEST_BOUND_LEFT_GREEN_DURATION = wbLtG;
        WEST_BOUND_LEFT_TOTAL = WEST_BOUND_LEFT_GREEN_DURATION + WEST_BOUND_LEFT_RED_DURATION;

        // East bound
        EAST_BOUND_THROUGH_RED_DURATION = ebTrR;
        EAST_BOUND_THROUGH_GREEN_DURATION = ebTrG;
        EAST_BOUND_LEFT_RED_DURATION = ebLtR;
        EAST_BOUND_LEFT_GREEN_DURATION = ebLtG;
        EAST_BOUND_LEFT_TOTAL = EAST_BOUND_LEFT_RED_DURATION + EAST_BOUND_LEFT_GREEN_DURATION;

        TOTAL_DURATION =  NORTH_BOUND_THROUGH_TOTAL + NORTH_BOUND_LEFT_TOTAL;

        // TODO: Check if durations can be aligned correctly
        assert(true);

        // Initialize traffic light status
        availableThroughRightSouth = true;
        availableThroughRightEast = true;
        availableLeftSouth = true;
        availableLeftWest = true;

        // Initialize the vehicle queues for each direction
        southLeftVehs = new LinkedList<>();
        westLeftVehs = new LinkedList<>();
        southThroughRightVehs = new LinkedList<>();
        eastThroughRightVehs = new LinkedList<>();
    }

    public boolean isGreen(Direction direction, boolean turningLeft, double time) {
        switch (direction) {
            case S:
                return isSouthGreen(turningLeft, time);
            case W:
                return isWestGreen(turningLeft, time);
            case E:
                return isEastGreen(turningLeft, time);
            default:
                System.out.println("Error - TrafficLight.isGreen: Wrong Direction!");
                return false;
        }
    }

    public boolean isSouthGreen(boolean turningLeft, double time) {
        double offsetTime = time % TOTAL_DURATION;
        // Left turning and has left turning light
        if (turningLeft && NORTH_BOUND_LEFT_TOTAL > 0) {
            return 0 <= offsetTime && offsetTime < NORTH_BOUND_LEFT_GREEN_DURATION;
        }
        return NORTH_BOUND_LEFT_TOTAL <= offsetTime &&
                offsetTime < NORTH_BOUND_LEFT_TOTAL + NORTH_BOUND_THROUGH_GREEN_DURATION;
    }

    public boolean isWestGreen(boolean turningLeft, double time) {
        // TODO
        double offsetTime = time % TOTAL_DURATION;
        // intersection 5 is special
        if (intersection == 5) {
            if (turningLeft) {
                return EAST_BOUND_LEFT_RED_DURATION <= offsetTime &&
                        offsetTime < EAST_BOUND_LEFT_RED_DURATION + EAST_BOUND_LEFT_GREEN_DURATION;
            }
            return EAST_BOUND_THROUGH_RED_DURATION <= offsetTime && offsetTime < TOTAL_DURATION;
        }

        if (turningLeft && EAST_BOUND_LEFT_TOTAL > 0) {
            return EAST_BOUND_THROUGH_RED_DURATION <= offsetTime &&
                    offsetTime < EAST_BOUND_THROUGH_RED_DURATION + EAST_BOUND_LEFT_GREEN_DURATION;
        }
        return EAST_BOUND_THROUGH_RED_DURATION + EAST_BOUND_LEFT_TOTAL <= offsetTime && offsetTime < TOTAL_DURATION;
    }

    public boolean isEastGreen(boolean turningLeft, double time) {
        double offsetTime = time % TOTAL_DURATION;
        if (turningLeft && WEST_BOUND_LEFT_TOTAL > 0) {
            return WEST_BOUND_THROUGH_RED_DURATION <= offsetTime &&
                    offsetTime < WEST_BOUND_THROUGH_RED_DURATION + WEST_BOUND_LEFT_GREEN_DURATION;
        }
        return WEST_BOUND_THROUGH_RED_DURATION + WEST_BOUND_LEFT_TOTAL <= offsetTime && offsetTime < TOTAL_DURATION;
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
