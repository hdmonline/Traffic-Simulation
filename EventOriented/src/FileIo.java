/**
 * FileIo.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * Helper class for reading data, writing results and handling the input model
 */

import com.sun.xml.internal.bind.v2.TODO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class FileIo {

    private static final String INPUT_FILE = "input.txt";
    private static final String OUTPUT_FILE = "output.txt";

    private ArrayList<Distribution> distributions = new ArrayList<>();
    private Random rand = new Random();
    // public static final int INITIAL_SPEED = 3;

    /**
    private static final int INTERSECTION_POSITION_1 = 100;
    private static final int INTERSECTION_POSITION_2 = 200;
    private static final int INTERSECTION_POSITION_3 = 300;
    private static final int INTERSECTION_POSITION_4 = 400;
    private static final int INTERSECTION_POSITION_5 = 500;
     */
    // total time to generate vehs
    private static final int GENERATING_VEHS_TIME = 15 * 60;

    /**
     * Read input file and load distributions to every intersection/direction
     */
    public void readFile() {
        // Open the file and read inter arrival interval of each intersection and direction
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader(INPUT_FILE);
            br = new BufferedReader(fr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String currLine;

        // Get read all lines
        int intersection, direction, numLines;
        try {
            while ((currLine = br.readLine()) != null) {
                String[] strs = currLine.split(" ", 3);
                intersection = Integer.parseInt(strs[0]);
                direction = Integer.parseInt(strs[1]);
                numLines = Integer.parseInt(strs[2]);
                Distribution distribution = new Distribution(intersection, direction, numLines);
                // Read distribution bins
                for (int i = 0; i < numLines; i++) {
                    currLine = br.readLine();
                    String[] pair = currLine.split(" ", 2);
                    double time = Double.parseDouble(pair[0]);
                    double prob = Double.parseDouble(pair[1]);
                    distribution.interval[i] = time;
                    distribution.prob[i] = prob;
                    if (i == 0) {
                        distribution.cumuProb[i] = prob;
                    } else {
                        distribution.cumuProb[i] = distribution.cumuProb[i - 1] + prob;
                    }
                }
                distributions.add(distribution);
            }
            // fr.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: write results to file
    public void writeResults() {

    }

    /**
     * Generate the vehicles entering from all intersections/directions.
     */
    public void generateFlow() {
        int id = 0;
        for (Distribution distr : distributions) {
            double time = 0;
            int intersection = distr.intersection;
            int direction = distr.direction;
            double[] cumu = distr.cumuProb;
            double r;
            while (time < GENERATING_VEHS_TIME) {
                r = rand.nextDouble();
                int i;
                for (i = 0; i < cumu.length; i++) {
                    if (r <= cumu[i]) {
                        break;
                    }
                }
                double lowerLimit = i == 0 ? 0 : cumu[i - 1];
                double interval = rand.nextDouble() * (cumu[i] - lowerLimit) + lowerLimit;
                time += interval;
                ProcessEvents.getEnteringVehs().add(new Vehicle(
                        id++, time, 0, intersection,
                        direction, 5, 1));
            }
        }
    }

    /**
    private int getPosition(int intersection) {
        switch(intersection) {
            case 1:
                return INTERSECTION_POSITION_1;
            case 2:
                return INTERSECTION_POSITION_2;
            case 3:
                return INTERSECTION_POSITION_3;
            case 4:
                return INTERSECTION_POSITION_4;
            case 5:
                return INTERSECTION_POSITION_5;
            default:
                System.out.println("Error - FileIo.getDistribution: Wrong intersection!");
                return -1;
        }
    }
    */

    /**
    private int getLane(int direction) {
        switch (direction) {
            case 2:
                return 1;
            case 3:
                return 0;
            case 1:
                return rand.nextDouble() > 0.5 ? 1 : 0;
            default:
                System.out.println("Error - FileIo.getLane: Wrong direction!");
                return -1;
        }
    }
    */
}
