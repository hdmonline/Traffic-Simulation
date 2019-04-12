/**
 * TrafficLight.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * Class for traffic lights. Yellow lights are counted into greens.
 */

// TODO: add generateEvent(), move queue to this class, get rid of nextGreen() etc.
public class TrafficLight {
    private int id;
    private final double SOUTH_THROUGH_RED_DURATION;
    private final double SOUTH_THROUGH_GREEN_DURATION;
    private final double SOUTH_LEFT_RED_DURATION;
    private final double SOUTH_LEFT_GREEN_DURATION;
    private final double SOUTH_LEFT_TOTAL;
    private final double SOUTH_THROUGH_TOTAL;
    private final double TOTAL;

    public TrafficLight(int id, double southThroughRed, double southThroughGreen, double southLeftGreen, double southLeftRed) {
        this.id = id;
        SOUTH_THROUGH_GREEN_DURATION = southThroughGreen;
        SOUTH_THROUGH_RED_DURATION = southThroughRed;
        SOUTH_LEFT_GREEN_DURATION = southLeftGreen;
        SOUTH_LEFT_RED_DURATION = southLeftRed;
        SOUTH_LEFT_TOTAL = southLeftGreen + southLeftRed;
        SOUTH_THROUGH_TOTAL = southThroughGreen + southThroughRed;
        TOTAL =  SOUTH_THROUGH_TOTAL + SOUTH_LEFT_TOTAL;
    }

    /**
     * Find next green light time.
     * The period is leftGreen -> leftRed -> Green -> Red.
     *
     * @param time current time
     * @param num number of green lights to skip
     * @return the next green light time
     */

    // Generate the flow of traffic lights event
    public void generateTrafficLights() {
        double time = 0;
        while (time < Parameter.SIMULATION_TIME) {
            ProcessEvents.getEventQueue().add(new Event(time, EventType.GreenSouth, id, Direction.E));
            ProcessEvents.getEventQueue().add(new Event(time + SOUTH_LEFT_TOTAL, EventType.GreenSouth, id, Direction.S));
            ProcessEvents.getEventQueue().add(new Event(time + SOUTH_LEFT_GREEN_DURATION, EventType.RedSouth, id, Direction.E));
            ProcessEvents.getEventQueue().add(new Event(time + SOUTH_LEFT_TOTAL + SOUTH_THROUGH_GREEN_DURATION, EventType.RedSouth, id, Direction.S));
            //TODO: may need to generate traffic lights events from other direcrions
            time += TOTAL;
        }
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
        return TOTAL;
    }
}
