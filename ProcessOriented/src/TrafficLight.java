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

    public TrafficLight(int intersection, double nbLtG, double nbLtR, double nbTrG, double nbTrR) {
        this.intersection = intersection;
        // North bound
        NORTH_BOUND_THROUGH_GREEN_DURATION = nbTrG;
        NORTH_BOUND_THROUGH_RED_DURATION = nbTrR;
        NORTH_BOUND_LEFT_GREEN_DURATION = nbLtG;
        NORTH_BOUND_LEFT_RED_DURATION = nbLtR;
        NORTH_BOUND_LEFT_TOTAL = nbLtG + nbLtR;
        NORTH_BOUND_THROUGH_TOTAL = nbTrG + nbTrR;

        // West bound
        WEST_BOUND_THROUGH_RED_DURATION = 0;
        WEST_BOUND_THROUGH_GREEN_DURATION = 0;
        WEST_BOUND_LEFT_RED_DURATION = 0;
        WEST_BOUND_LEFT_GREEN_DURATION = 0;
        WEST_BOUND_LEFT_TOTAL = WEST_BOUND_LEFT_GREEN_DURATION + WEST_BOUND_LEFT_RED_DURATION;

        // West bound
        EAST_BOUND_THROUGH_RED_DURATION = 0;
        EAST_BOUND_THROUGH_GREEN_DURATION = 0;
        EAST_BOUND_LEFT_RED_DURATION = 0;
        EAST_BOUND_LEFT_GREEN_DURATION = 0;
        EAST_BOUND_LEFT_TOTAL = EAST_BOUND_LEFT_RED_DURATION + EAST_BOUND_LEFT_GREEN_DURATION;

        TOTAL_DURATION =  NORTH_BOUND_THROUGH_TOTAL + NORTH_BOUND_LEFT_TOTAL;
    }

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

        // West bound
        EAST_BOUND_THROUGH_RED_DURATION = ebTrR;
        EAST_BOUND_THROUGH_GREEN_DURATION = ebTrG;
        EAST_BOUND_LEFT_RED_DURATION = ebLtR;
        EAST_BOUND_LEFT_GREEN_DURATION = ebLtG;
        EAST_BOUND_LEFT_TOTAL = EAST_BOUND_LEFT_RED_DURATION + EAST_BOUND_LEFT_GREEN_DURATION;

        TOTAL_DURATION =  NORTH_BOUND_THROUGH_TOTAL + NORTH_BOUND_LEFT_TOTAL;

        // TODO: Check if durations can be aligned correctly
        assert(true);
    }

    public void generateLightEvents() {
        double time = 0;
        while (time < Parameter.SIMULATION_TIME) {
            // North bound
            if (NORTH_BOUND_LEFT_TOTAL > 0) {
                EventHandler.getInstance().addScheduleEvent(new Event(time, EventType.TurnGreenLeft, intersection, Direction.N));
                EventHandler.getInstance().addScheduleEvent(new Event(time + NORTH_BOUND_LEFT_GREEN_DURATION,
                        EventType.TurnRedLeft, intersection, Direction.N));
            }
            EventHandler.getInstance().addScheduleEvent(new Event(time + NORTH_BOUND_LEFT_TOTAL,
                    EventType.TurnGreenThrough, intersection, Direction.N));
            EventHandler.getInstance().addScheduleEvent(new Event(time + NORTH_BOUND_LEFT_TOTAL + NORTH_BOUND_THROUGH_GREEN_DURATION,
                    EventType.TurnRedThrough, intersection, Direction.N));

            // West bound
            EventHandler.getInstance().addScheduleEvent(new Event(time, EventType.TurnRedThrough, intersection, Direction.W));
            if (WEST_BOUND_LEFT_TOTAL > 0) {
                EventHandler.getInstance().addScheduleEvent(new Event(time + WEST_BOUND_THROUGH_RED_DURATION,
                        EventType.TurnGreenLeft, intersection, Direction.W));
                EventHandler.getInstance().addScheduleEvent(new Event(time + WEST_BOUND_THROUGH_RED_DURATION + WEST_BOUND_LEFT_GREEN_DURATION,
                        EventType.TurnRedLeft, intersection, Direction.W));
            }
            EventHandler.getInstance().addScheduleEvent(new Event(time + WEST_BOUND_THROUGH_RED_DURATION + WEST_BOUND_LEFT_TOTAL,
                    EventType.TurnGreenThrough, intersection, Direction.W));
            time += TOTAL_DURATION;

            // East bound
            EventHandler.getInstance().addScheduleEvent(new Event(time, EventType.TurnRedThrough, intersection, Direction.E));
            if (EAST_BOUND_LEFT_TOTAL > 0) {
                if (intersection == 5) {
                    EventHandler.getInstance().addScheduleEvent(new Event(time,
                            EventType.TurnRedLeft, intersection, Direction.E));
                    EventHandler.getInstance().addScheduleEvent(new Event(time + EAST_BOUND_LEFT_RED_DURATION,
                            EventType.TurnGreenLeft, intersection, Direction.E));
                    EventHandler.getInstance().addScheduleEvent(new Event(time + EAST_BOUND_LEFT_TOTAL,
                            EventType.TurnRedLeft, intersection, Direction.E));
                } else {
                    EventHandler.getInstance().addScheduleEvent(new Event(time + EAST_BOUND_THROUGH_RED_DURATION,
                            EventType.TurnGreenLeft, intersection, Direction.E));
                    EventHandler.getInstance().addScheduleEvent(new Event(time + EAST_BOUND_THROUGH_RED_DURATION + EAST_BOUND_LEFT_GREEN_DURATION,
                            EventType.TurnRedLeft, intersection, Direction.E));
                }
            }
            EventHandler.getInstance().addScheduleEvent(new Event(time + EAST_BOUND_THROUGH_RED_DURATION + EAST_BOUND_LEFT_TOTAL,
                    EventType.TurnGreenThrough, intersection, Direction.E));

            time += TOTAL_DURATION;
        }
    }
}
