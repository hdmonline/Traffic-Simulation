/**
 * Parameter.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * This class stores all the parameters that are used in this project
 */
public class Parameter {

    // Simulation time
    static double VEHICLE_TIME = 15 * 60;
    static double SIMULATION_TIME = VEHICLE_TIME + 10 * 60;

    // Travelling time from starting point to Intersection 1
    static final double BETWEEN_START_INTERSECTION_1 = 14.136;
    // Average Travelling time between intersections
    static final double BETWEEN_INTERSECTION_12 = 26.116;
    static final double BETWEEN_INTERSECTION_23 = 36.073;
    static final double BETWEEN_INTERSECTION_35 = 55.634;
    static final double AFTER_INTERSECTION_5 = 6.136;

    // Probabilities for exiting on intersections
    // West -> East -> North
    static final double[] EXIT_CUMU_PROB_1 = {0.197, 0.197 + 0.066, 1},
            EXIT_CUMU_PROB_2 = {0.047, 0.047 + 0.013, 1},
            EXIT_CUMU_PROB_3 = {0.048, 0.048 + 0.034, 1},
            EXIT_CUMU_PROB_5 = {0.338, 0.338 + 0.058, 1};

    // Waiting time for going through a traffic light per vehicle in (s)
    static final double W = 1.8;

    // Traffic delay offset in seconds
    static double TRAFFIC_LIGHT_DELAY = 0;

    // File paths
    static String INPUT_FILE = "process_input.csv";
    static String OUTPUT_VEHICLE_FILE = "process_vehicles.csv";
    static String OUTPUT_EVENT_FILE = "process_events.csv";

    // Random seed
    static long RANDOM_SEED = 100;
    static boolean HAS_SEED = false;

    public static double getBetweenIntersectionTime(int intersection) {
        switch(intersection) {
            case 1:
                return BETWEEN_START_INTERSECTION_1;
            case 2:
                return BETWEEN_INTERSECTION_12;
            case 3:
                return BETWEEN_INTERSECTION_23;
            case 5:
                return BETWEEN_INTERSECTION_35;
            case 6:
                return AFTER_INTERSECTION_5;
            default:
                System.err.println("Error - Parameter.getBetweenIntersectionTime: Wrong Intersection!");
                return -1;
        }
    }

    public static double[] getExitCumuProb(int intersection) {
        switch (intersection) {
            case 1:
                return EXIT_CUMU_PROB_1;
            case 2:
                return EXIT_CUMU_PROB_2;
            case 3:
                return EXIT_CUMU_PROB_3;
            case 5:
                return EXIT_CUMU_PROB_5;
            default:
                System.err.println("Error - Parameter.getExitProb: Wrong Intersection!");
                return new double[] {-1, -1, -1};
        }
    }
}
