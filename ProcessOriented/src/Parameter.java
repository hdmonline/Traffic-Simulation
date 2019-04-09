/**
 * Parameter.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * This class stores all the parameters that are used in this project
 */
public class Parameter {

    // Simulation time
    static final double VEHICLE_TIME = 15 * 60;
    static final double SIMULATION_TIME = VEHICLE_TIME + 5 * 60;

    // Travelling time from starting point to Intersection 1
    static final double BETWEEN_START_INTERSECTION1 = 14.136;
    // Average Travelling time between intersections
    static final double BETWEEN_INTERSECTION_12 = 26.116;
    static final double BETWEEN_INTERSECTION_23 = 36.073;
    static final double BETWEEN_INTERSECTION_35 = 55.634;
    static final double AFTER_INTERSECTION_5 = 6.136;

    // Waiting time for going through a traffic light per vehicle in (s)
    static final double W = 0.5;

    static final String INPUT_FILE = "process_input.csv";
    static final String OUTPUT_VEHICLE_FILE = "process_vehicles.csv";
    static final String OUTPUT_EVENT_FILE = "process_events.csv";

    public static double getBetweenIntersectionTime(int intersection) {
        switch(intersection) {
            case 1:
                return BETWEEN_START_INTERSECTION1;
            case 2:
                return BETWEEN_INTERSECTION_12;
            case 3:
                return BETWEEN_INTERSECTION_23;
            case 5:
                return BETWEEN_INTERSECTION_35;
            case 6:
                return AFTER_INTERSECTION_5;
            default:
                System.out.println("Error - EventHandler.handleEvent: Wrong Intersection!");
                return -1;
        }
    }
}
