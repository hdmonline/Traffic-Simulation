/**
 * TrafficLight.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * Class for traffic lights. Yellow lights are counted into greens.
 */

public class TrafficLight {
    private int intersection;
    private final double SOUTH_THROUGH_RED_DURATION;
    private final double SOUTH_THROUGH_GREEN_DURATION;
    private final double SOUTH_LEFT_RED_DURATION;
    private final double SOUTH_LEFT_GREEN_DURATION;
    private final double SOUTH_LEFT_TOTAL;
    private final double SOUTH_THROUGH_TOTAL;
    private final double SOUTH_TOTAL;

    public TrafficLight(int intersection,  double southThroughRed, double southThroughGreen, double southLeftGreen, double southLeftRed) {
        this.intersection = intersection;
        SOUTH_THROUGH_GREEN_DURATION = southThroughGreen;
        SOUTH_THROUGH_RED_DURATION = southThroughRed;
        SOUTH_LEFT_GREEN_DURATION = southLeftGreen;
        SOUTH_LEFT_RED_DURATION = southLeftRed;
        SOUTH_LEFT_TOTAL = southLeftGreen + southLeftRed;
        SOUTH_THROUGH_TOTAL = southThroughGreen + southThroughRed;
        SOUTH_TOTAL =  SOUTH_THROUGH_TOTAL + SOUTH_LEFT_TOTAL;
    }

    public void generateLightEvents() {
        double time = 0;
        while (time < Parameter.SIMULATION_TIME) {
            EventHandler.getInstance().addScheduleEvent(new Event(time + SOUTH_LEFT_TOTAL, EventType.TurnGreen, intersection, Direction.S));
            EventHandler.getInstance().addScheduleEvent(new Event(time + SOUTH_LEFT_TOTAL + SOUTH_THROUGH_GREEN_DURATION, EventType.TurnRed, intersection, Direction.S));
            // TODO: generate other directions
            time += SOUTH_TOTAL;
        }
    }
}
