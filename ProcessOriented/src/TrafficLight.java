/**
 * TrafficLight.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * Class for traffic lights. Yellow lights are counted into greens.
 */

public class TrafficLight {
    private int intersection;
    private final double NORTH_BOUND_THROUGH_RED_DURATION;
    private final double NORTH_BOUND_THROUGH_GREEN_DURATION;
    private final double NORTH_BOUND_LEFT_RED_DURATION;
    private final double NORTH_BOUND_LEFT_GREEN_DURATION;
    private final double NORTH_BOUND_LEFT_TOTAL;
    private final double NORTH_BOUND_THROUGH_TOTAL;
    private final double NORTH_BOUND_TOTAL;

    public TrafficLight(int intersection, double nbLtG, double nbLtR, double nbTrG, double nbTrR) {
        this.intersection = intersection;
        NORTH_BOUND_THROUGH_GREEN_DURATION = nbTrG;
        NORTH_BOUND_THROUGH_RED_DURATION = nbTrR;
        NORTH_BOUND_LEFT_GREEN_DURATION = nbLtG;
        NORTH_BOUND_LEFT_RED_DURATION = nbLtR;
        NORTH_BOUND_LEFT_TOTAL = nbLtG + nbLtR;
        NORTH_BOUND_THROUGH_TOTAL = nbTrG + nbTrR;
        NORTH_BOUND_TOTAL =  NORTH_BOUND_THROUGH_TOTAL + NORTH_BOUND_LEFT_TOTAL;
    }

    public TrafficLight(int intersection,  double nbLtG, double nbLtR, double nbTrG, double nbTrR,
                        double wbLtG, double wbLtR, double wbTrG, double wbTrR,
                        double ebLtG, double ebLtR, double ebTrG, double ebTrR) {
        this.intersection = intersection;
        NORTH_BOUND_THROUGH_GREEN_DURATION = nbTrG;
        NORTH_BOUND_THROUGH_RED_DURATION = nbTrR;
        NORTH_BOUND_LEFT_GREEN_DURATION = nbLtG;
        NORTH_BOUND_LEFT_RED_DURATION = nbLtR;
        NORTH_BOUND_LEFT_TOTAL = nbLtG + nbLtR;
        NORTH_BOUND_THROUGH_TOTAL = nbTrG + nbTrR;
        NORTH_BOUND_TOTAL =  NORTH_BOUND_THROUGH_TOTAL + NORTH_BOUND_LEFT_TOTAL;
    }

    public void generateLightEvents() {
        double time = 0;
        while (time < Parameter.SIMULATION_TIME) {
            EventHandler.getInstance().addScheduleEvent(new Event(time + NORTH_BOUND_LEFT_TOTAL, EventType.TurnGreenThrough, intersection, Direction.N));
            EventHandler.getInstance().addScheduleEvent(new Event(time + NORTH_BOUND_LEFT_TOTAL + NORTH_BOUND_THROUGH_GREEN_DURATION, EventType.TurnRedThrough, intersection, Direction.N));
            EventHandler.getInstance().addScheduleEvent(new Event(time + ));
            // TODO: generate other directions
            time += NORTH_BOUND_TOTAL;
        }
    }
}
