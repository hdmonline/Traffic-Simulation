/**
 * Parameter.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * This class stores all the parameters that are used in this project
 */
public class Parameter {

    // Total time to generate vehs
    static final double GENERATING_VEHS_TIME = 100 * 60;

    // Travelling time from starting point to Intersection 1
    static final double BETWEEN_START_INTERSECTION1 = 15;
    // Average Travelling time between intersections
    static final double BETWEEN_INTERSECTION_12 = 15;
    static final double BETWEEN_INTERSECTION_23 = 15;
    static final double BETWEEN_INTERSECTION_35 = 15;
    static final double AFTER_INTERSECTION_5 = 3;

    static final String INPUT_FILE = "event_input.txt";
    static final String OUTPUT_FILE = "event_result.txt";
}