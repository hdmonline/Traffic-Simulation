/**
 * Parameter.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * This class stores all the parameters that are used in this project
 */
public class Parameter {

    // Simulation time
    static final double SIMULATION_TIME = 100 * 60;

    // Travelling time from starting point to Intersection 1
    static final double BETWEEN_START_INTERSECTION1 = 15;
    // Average Travelling time between intersections
    static final double BETWEEN_INTERSECTION_12 = 15;
    static final double BETWEEN_INTERSECTION_23 = 15;
    static final double BETWEEN_INTERSECTION_35 = 15;
    static final double AFTER_INTERSECTION_5 = 3;

    // Waiting time for going through a traffic light per vehicle in (s)
    static final double W = 1.0;

    static final String INPUT_FILE = "event_input.txt";
    static final String OUTPUT_VEHICLE_FILE = "event_vehicles.txt";
    static final String OUTPUT_EVENT_FILE = "event_events.txt";
}
