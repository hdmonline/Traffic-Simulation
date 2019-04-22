import java.util.LinkedList;

/**
 * TrafficLight.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * Class for traffic lights. Yellow lights are counted into greens.
 */

public class TrafficLight {
    private int intersection;
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

    private final double delay;

    // Status variables
    private boolean isThroughRightSouthGreen, isThroughRightWestGreen, isThroughRightEastGreen;
    private boolean isLeftSouthGreen, isLeftWestGreen;
    private boolean availableThroughRightSouth, availableThroughRightEast;
    private boolean availableLeftSouth, availableLeftWest;

    // Vehicle queues for going through, First -> In, Last -> Out
    private LinkedList<VehicleProcess> southThroughRightVehs, eastThroughRightVehs;
    // Vehicle queues for turning left
    private LinkedList<VehicleProcess> southLeftVehs, westLeftVehs;

    public TrafficLight(int intersection, double delay,
                        double nbLtG, double nbLtR, double nbTrG, double nbTrR,
                        double wbLtG, double wbLtR, double wbTrG, double wbTrR,
                        double ebLtG, double ebLtR, double ebTrG, double ebTrR) {
        this.intersection = intersection;
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

        this.delay = delay % TOTAL_DURATION;

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

    /**
     * Generate all traffic light events with delay offset. delay <= TOTAL_DURATION
     * The time line can be splitted into two parts:
     * [0 : delay] -> [(TOTAL_DURATION - delay) : TOTAL_DURATION]
     * [delay : end] -> normal cyecles with offset
     */
    public void generateLightEvents() {
        double time = TOTAL_DURATION - delay;
        // North bound
        // North bound
        if (NORTH_BOUND_LEFT_TOTAL > 0) {
            if (time == 0) {
                EventHandler.getInstance().addEvent(new Event(0,
                        EventType.TurnGreenLeft, intersection, Direction.N));
            }
            if (time <= NORTH_BOUND_LEFT_GREEN_DURATION) {
                EventHandler.getInstance().addEvent(new Event(NORTH_BOUND_LEFT_GREEN_DURATION - time,
                        EventType.TurnRedLeft, intersection, Direction.N));
            }
        }
        if (time <= NORTH_BOUND_LEFT_TOTAL) {
            EventHandler.getInstance().addEvent(new Event(NORTH_BOUND_LEFT_TOTAL - time,
                    EventType.TurnGreenThrough, intersection, Direction.N));
        }
        if (time <= NORTH_BOUND_LEFT_TOTAL + NORTH_BOUND_THROUGH_GREEN_DURATION) {
            EventHandler.getInstance().addEvent(new Event(NORTH_BOUND_LEFT_TOTAL + NORTH_BOUND_THROUGH_GREEN_DURATION - time,
                    EventType.TurnRedThrough, intersection, Direction.N));
        }

        // West bound
        if (time == 0) {
            EventHandler.getInstance().addEvent(new Event(0,
                    EventType.TurnRedThrough, intersection, Direction.W));
        }
        if (time <= WEST_BOUND_THROUGH_RED_DURATION + WEST_BOUND_LEFT_TOTAL) {
            EventHandler.getInstance().addEvent(new Event(WEST_BOUND_THROUGH_RED_DURATION + WEST_BOUND_LEFT_TOTAL - time,
                    EventType.TurnGreenThrough, intersection, Direction.W));
        }

        // East bound
        if (time == 0) {
            EventHandler.getInstance().addEvent(new Event(0,
                    EventType.TurnRedThrough, intersection, Direction.E));
        }

        if (EAST_BOUND_LEFT_TOTAL > 0) {
            if (intersection == 5) {
                if (time ==0 ) {
                    EventHandler.getInstance().addEvent(new Event(0,
                            EventType.TurnRedLeft, intersection, Direction.E));
                }
                if (time <= EAST_BOUND_LEFT_RED_DURATION) {
                    EventHandler.getInstance().addEvent(new Event(EAST_BOUND_LEFT_RED_DURATION - time,
                            EventType.TurnGreenLeft, intersection, Direction.E));
                }
                if (time <= EAST_BOUND_LEFT_TOTAL) {
                    EventHandler.getInstance().addEvent(new Event(EAST_BOUND_LEFT_TOTAL - time,
                            EventType.TurnRedLeft, intersection, Direction.E));
                }
            } else {
                if (time <= EAST_BOUND_THROUGH_RED_DURATION) {
                    EventHandler.getInstance().addEvent(new Event(EAST_BOUND_THROUGH_RED_DURATION - time,
                            EventType.TurnGreenLeft, intersection, Direction.E));
                }
                if (time <= EAST_BOUND_THROUGH_RED_DURATION + EAST_BOUND_LEFT_GREEN_DURATION) {
                    EventHandler.getInstance().addEvent(new Event(EAST_BOUND_THROUGH_RED_DURATION + EAST_BOUND_LEFT_GREEN_DURATION - time,
                            EventType.TurnRedLeft, intersection, Direction.E));
                }
            }
        }
        if (time <= EAST_BOUND_THROUGH_RED_DURATION + EAST_BOUND_LEFT_TOTAL) {
            EventHandler.getInstance().addEvent(new Event(EAST_BOUND_THROUGH_RED_DURATION + EAST_BOUND_LEFT_TOTAL - time,
                    EventType.TurnGreenThrough, intersection, Direction.E));
        }

        time = 0;
        while (time < Parameter.SIMULATION_TIME - delay) {
            // North bound
            if (NORTH_BOUND_LEFT_TOTAL > 0) {
                EventHandler.getInstance().addEvent(new Event(time + delay,
                        EventType.TurnGreenLeft, intersection, Direction.N));
                EventHandler.getInstance().addEvent(new Event(time + NORTH_BOUND_LEFT_GREEN_DURATION,
                        EventType.TurnRedLeft, intersection, Direction.N));
            }
            EventHandler.getInstance().addEvent(new Event(time + NORTH_BOUND_LEFT_TOTAL + delay,
                    EventType.TurnGreenThrough, intersection, Direction.N));
            EventHandler.getInstance().addEvent(new Event(time + NORTH_BOUND_LEFT_TOTAL + NORTH_BOUND_THROUGH_GREEN_DURATION + delay,
                    EventType.TurnRedThrough, intersection, Direction.N));

            // West bound
            EventHandler.getInstance().addEvent(new Event(time + delay,
                    EventType.TurnRedThrough, intersection, Direction.W));
            EventHandler.getInstance().addEvent(new Event(time + WEST_BOUND_THROUGH_RED_DURATION + WEST_BOUND_LEFT_TOTAL + delay,
                    EventType.TurnGreenThrough, intersection, Direction.W));

            // East bound
            EventHandler.getInstance().addEvent(new Event(time + delay,
                    EventType.TurnRedThrough, intersection, Direction.E));
            if (EAST_BOUND_LEFT_TOTAL > 0) {
                if (intersection == 5) {
                    EventHandler.getInstance().addEvent(new Event(time + delay,
                            EventType.TurnRedLeft, intersection, Direction.E));
                    EventHandler.getInstance().addEvent(new Event(time + EAST_BOUND_LEFT_RED_DURATION + delay,
                            EventType.TurnGreenLeft, intersection, Direction.E));
                    EventHandler.getInstance().addEvent(new Event(time + EAST_BOUND_LEFT_TOTAL + delay,
                            EventType.TurnRedLeft, intersection, Direction.E));
                } else {
                    EventHandler.getInstance().addEvent(new Event(time + EAST_BOUND_THROUGH_RED_DURATION + delay,
                            EventType.TurnGreenLeft, intersection, Direction.E));
                    EventHandler.getInstance().addEvent(new Event(time + EAST_BOUND_THROUGH_RED_DURATION + EAST_BOUND_LEFT_GREEN_DURATION + delay,
                            EventType.TurnRedLeft, intersection, Direction.E));
                }
            }
            EventHandler.getInstance().addEvent(new Event(time + EAST_BOUND_THROUGH_RED_DURATION + EAST_BOUND_LEFT_TOTAL + delay,
                    EventType.TurnGreenThrough, intersection, Direction.E));

            time += TOTAL_DURATION;
        }
    }

    public boolean available(Direction direction, boolean turningLeft) {
        switch (direction) {
            case S:
                if (turningLeft && NORTH_BOUND_LEFT_TOTAL > 0) {
                    return availableLeftSouth;
                } else {
                    return availableThroughRightSouth;
                }
            case W:
                return availableLeftWest;
            case E:
                return availableThroughRightEast;
            default:
                System.err.println("Error - TrafficLight.available: Wrong Direction!");
                return false;
        }
    }

    public boolean isGreen(Direction direction, boolean turningLeft) {
        switch (direction) {
            case S:
                if (turningLeft && NORTH_BOUND_LEFT_TOTAL > 0) {
                    return isLeftSouthGreen;
                } else {
                    return isThroughRightSouthGreen;
                }
            case W:
                if (turningLeft && EAST_BOUND_LEFT_TOTAL > 0) {
                    return isLeftWestGreen;
                } else {
                    return isThroughRightWestGreen;
                }
            case E:
                return isThroughRightEastGreen;
            default:
                System.err.println("Error - TrafficLight.isGreen: Wrong Direction!");
                return false;
        }
    }

    public LinkedList<VehicleProcess> getVehsQueue(Direction direction, boolean turningLeft) {
        switch (direction) {
            case S:
                if (turningLeft && NORTH_BOUND_LEFT_TOTAL > 0) {
                    return southLeftVehs;
                } else {
                    return southThroughRightVehs;
                }
            case W:
                return westLeftVehs;
            case E:
                return eastThroughRightVehs;
            default:
                System.err.println("Error - TrafficLight.getVehsQueue: Wrong Direction!");
                return null;
        }
    }

    public void setAvailable(Direction direction, boolean turningLeft, boolean available) {
        switch (direction) {
            case S:
                if (turningLeft && NORTH_BOUND_LEFT_TOTAL > 0) {
                    availableLeftSouth = available;
                } else {
                    availableThroughRightSouth = available;
                }
                break;
            case W:
                availableLeftWest = available;
                break;
            case E:
                availableThroughRightEast = available;
                break;
            default:
                System.err.println("Error - TrafficLight.setAvailable: Wrong Direction!");
        }
    }

    public void setLight(Direction direction, boolean turningLeft, boolean green) {
        switch (direction) {
            case N:
                if (turningLeft && NORTH_BOUND_LEFT_TOTAL > 0) {
                    isLeftSouthGreen = green;
                } else {
                    isThroughRightSouthGreen = green;
                }
                break;
            case E:
                if (turningLeft && EAST_BOUND_LEFT_TOTAL > 0) {
                    isLeftWestGreen = green;
                } else {
                    isThroughRightWestGreen = green;
                }
                break;
            case W:
                isThroughRightEastGreen = green;
                break;
            default:
                System.err.println("Error - TrafficLight.setLight: Wrong Direction!");
        }
    }

    /**
     * Turn all lights green, for debugging
     */
    public void turnAllGreen() {
        isThroughRightSouthGreen = true;
        isThroughRightWestGreen = true;
        isThroughRightEastGreen = true;
        isLeftSouthGreen = true;
        isLeftWestGreen = true;
    }
}
