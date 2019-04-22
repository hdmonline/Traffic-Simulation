/**
 * Parameter.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * This class stores all the parameters that are used in this project
 */
public class Parameter {

    // Simulation parameters
    static final double SIMULATION_TIME = 15 * 60; // second
    static final int END_POSITION = 2191;
    static final double TIME_INTERVAL = 1; // second

    static final int INTERSECTION_POSITION_1 = 144;
    static final int INTERSECTION_POSITION_2 = 662;
    static final int INTERSECTION_POSITION_3 = 1204;
    static final int INTERSECTION_POSITION_4 = 1630;
    static final int INTERSECTION_POSITION_5 = 2041;

    // Vehicle parameters
    static final int VEH_LEN = 14;
    static final int INITIAL_SPEED = 23;
    static final int MAX_SPEED = 40; // feet/sec
    static final double PROB_BRAKE = 0.2;
    static final double PROB_CHANGE_LANE = 0.5;
    static final int COMFORT_ACC = 6; // feet/s^2

    // IO parameters
    static final String INPUT_FILE = "ca_input.csv";
    static final String OUTPUT_VEHICLE_FILE = "ca_vehicles.csv";
    static final String OUTPUT_EVENT_FILE = "ca_log.csv";
}
