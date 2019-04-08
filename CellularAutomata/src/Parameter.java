/**
 * Parameter.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * This class stores all the parameters that are used in this project
 */
public class Parameter {

    // Simulation time
    static final double SIMULATION_TIME = 15 * 60; // second

    static final int END_POSITION = 300;

    static final double TIME_INTERVAL = 1; // second

    static final double PROB_CHANGE_LANE = 0.7;


    // Vehicle length
    static final int VEH_LEN = 14;
    static final int INITIAL_SPEED = 3;
    static final int MAX_SPEED = 40; // feet/sec
    static final double PROB_BRAKE = 0.5;

    static final int INTERSECTION_POSITION_1 = 100;
    static final int INTERSECTION_POSITION_2 = 200;
    static final int INTERSECTION_POSITION_3 = 300;
    static final int INTERSECTION_POSITION_4 = 400;
    static final int INTERSECTION_POSITION_5 = 500;

    // Waiting time for going through a traffic light per vehicle in (s)
    static final double W = 1.0;

    static final String INPUT_FILE = "ca_input.txt";
    static final String OUTPUT_VEHICLE_FILE = "ca_vehicles.txt";
    static final String OUTPUT_EVENT_FILE = "ca_events.txt";
}
