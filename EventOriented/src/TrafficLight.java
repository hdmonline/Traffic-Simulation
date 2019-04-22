/**
 * TrafficLight.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * Class for traffic lights. Yellow lights are counted into greens.
 */

// TODO: add generateEvent(), move queue to this class, get rid of nextGreen() etc.
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

    public TrafficLight(int intersection,  double nbLtG, double nbLtR, double nbTrG, double nbTrR,
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
    }

    // Generate the flow of traffic lights event
    public void generateTrafficLights() {
        double time = 0;

        while (time < Parameter.SIMULATION_TIME) {
            // North bound
            if (NORTH_BOUND_LEFT_TOTAL > 0) {
                ProcessEvents.getEventQueue().add(new Event(time, EventType.GreenSouth, intersection, Direction.W));
                ProcessEvents.getEventQueue().add(new Event(time + NORTH_BOUND_LEFT_GREEN_DURATION,
                        EventType.RedSouth, intersection, Direction.W));
            } else {
                ProcessEvents.getEventQueue().add(new Event(time + NORTH_BOUND_LEFT_TOTAL,
                        EventType.GreenSouth, intersection, Direction.W));
                ProcessEvents.getEventQueue().add(new Event(time + NORTH_BOUND_LEFT_TOTAL + NORTH_BOUND_THROUGH_GREEN_DURATION,
                        EventType.RedSouth, intersection, Direction.W));
            }
            ProcessEvents.getEventQueue().add(new Event(time + NORTH_BOUND_LEFT_TOTAL, EventType.GreenSouth,
                    intersection, Direction.N));
            ProcessEvents.getEventQueue().add(new Event(time + NORTH_BOUND_LEFT_TOTAL + NORTH_BOUND_THROUGH_GREEN_DURATION,
                    EventType.RedSouth, intersection, Direction.N));

            // West Bound
            ProcessEvents.getEventQueue().add(new Event(time, EventType.RedEastTurnRight, intersection));
            ProcessEvents.getEventQueue().add(new Event(time + WEST_BOUND_THROUGH_RED_DURATION + WEST_BOUND_LEFT_TOTAL,
                    EventType.GreenEastTurnRight, intersection));

            // Eastbound
            if (intersection == 5) {
                ProcessEvents.getEventQueue().add(new Event(time, EventType.RedWestTurnLeft, intersection));
                ProcessEvents.getEventQueue().add(new Event(time + EAST_BOUND_LEFT_RED_DURATION,
                        EventType.GreenWestTurnLeft, intersection));
                ProcessEvents.getEventQueue().add(new Event(time + EAST_BOUND_LEFT_TOTAL,
                        EventType.RedWestTurnLeft, intersection));
            } else if (EAST_BOUND_LEFT_TOTAL > 0) {
                ProcessEvents.getEventQueue().add(new Event(time,
                        EventType.RedWestTurnLeft, intersection));
                ProcessEvents.getEventQueue().add(new Event(time + EAST_BOUND_THROUGH_RED_DURATION,
                        EventType.GreenWestTurnLeft, intersection));
                ProcessEvents.getEventQueue().add(new Event(time + EAST_BOUND_THROUGH_RED_DURATION + EAST_BOUND_LEFT_GREEN_DURATION,
                        EventType.RedWestTurnLeft, intersection));
            } else {
                ProcessEvents.getEventQueue().add(new Event(time,
                        EventType.RedWestTurnLeft, intersection));
                ProcessEvents.getEventQueue().add(new Event(time + EAST_BOUND_THROUGH_RED_DURATION,
                        EventType.GreenWestTurnLeft, intersection));
            }

            time += TOTAL_DURATION;
        }
    }
}
