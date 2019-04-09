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

    public TrafficLight(int intersection,  double nbThroughRed, double nbThroughGreen, double nbLeftGreen, double nbLeftRed) {
        this.intersection = intersection;
        NORTH_BOUND_THROUGH_GREEN_DURATION = nbThroughGreen;
        NORTH_BOUND_THROUGH_RED_DURATION = nbThroughRed;
        NORTH_BOUND_LEFT_GREEN_DURATION = nbLeftGreen;
        NORTH_BOUND_LEFT_RED_DURATION = nbLeftRed;
        NORTH_BOUND_LEFT_TOTAL = nbLeftGreen + nbLeftRed;
        NORTH_BOUND_THROUGH_TOTAL = nbThroughGreen + nbThroughRed;
        NORTH_BOUND_TOTAL =  NORTH_BOUND_THROUGH_TOTAL + NORTH_BOUND_LEFT_TOTAL;
    }

    public void generateLightEvents() {
        double time = 0;
        while (time < Parameter.SIMULATION_TIME) {
            EventHandler.getInstance().addScheduleEvent(new Event(time + NORTH_BOUND_LEFT_TOTAL, EventType.TurnGreen, intersection, Direction.N));
            EventHandler.getInstance().addScheduleEvent(new Event(time + NORTH_BOUND_LEFT_TOTAL + NORTH_BOUND_THROUGH_GREEN_DURATION, EventType.TurnRed, intersection, Direction.N));
            // TODO: generate other directions

            time += NORTH_BOUND_TOTAL;
        }
    }
}
