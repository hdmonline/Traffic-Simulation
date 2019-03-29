/**
 * FileIo.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * Helper class for reading data, writing results and handling the input model
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class FileIo {

    private static final String INPUT_FILE = "input.txt";
    private static final String OUTPUT_FILE = "output.txt";

    private ArrayList<Distribution> distributions = new ArrayList<>();
    private Random rand = new Random();
    public static final int INITIAL_SPEED = 3;

    private static final int INTERSECTION_POSITION_1 = 100;
    private static final int INTERSECTION_POSITION_2 = 200;
    private static final int INTERSECTION_POSITION_3 = 300;
    private static final int INTERSECTION_POSITION_4 = 400;
    private static final int INTERSECTION_POSITION_5 = 500;

    /**
     * Read input file and load distributions to every intersection/direction
     */
    public void readFile() {
        // Open the file and read inter arrival interval of each intersection and direction
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(INPUT_FILE));
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
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO: Write the results to output file
     */
    public void writeResults() {
        // Open the file and write finished vehicles
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(OUTPUT_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            for (Vehicle veh : Ca.getFinishedVehs()) {
                bw.write(veh.toString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            while (time < Ca.SIMULATION_TIME * 60) {
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
                Ca.getEnteringVehs().add(new Vehicle(
                        id++, getPosition(intersection) + 1,
                        getLane(direction), INITIAL_SPEED, time,
                        intersection, direction));
            }
        }
    }

    /**
     * Get intersection position
     *
     * @param intersection the intersection
     * @return the position of the intersection
     */
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
}
