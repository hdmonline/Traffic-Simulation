/**
 * Parameter.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * This class stores all the parameters that are used in this project
 */
public class Parameter {

    // Simulation time
    static final double SIMULATION_TIME = 15 * 60;

    // Travelling time from starting point to Intersection 1
    static final double BETWEEN_START_INTERSECTION1 = 14.136;
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
    static final double W = 0.3;

    // probability of turn right/left to exit at each intersection
    static final double TURN_LEFT_PROB = 0.10;
    static final double CUMUL_PROB = 0.25;

    static final String INPUT_FILE = "event_input.csv";
    static final String OUTPUT_VEHICLE_FILE = "event_vehicles.csv";
    static final String OUTPUT_EVENT_FILE = "event_events.csv";


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
                System.out.println("Error - Parameter.getExitCumuProb: No such intersection!");
                return new double[] {-1, -1, -1};
        }
    }
}
