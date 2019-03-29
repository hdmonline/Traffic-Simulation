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
    private ArrayList<Distribution> distributions = new ArrayList<>();
    private Random rand = new Random();

    /**
     * Read input file and load distributions to every intersection/direction
     */
    public void readFile() {
        // Open the file and read inter arrival interval of each intersection and direction
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader(Parameter.INPUT_FILE);
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
        // Open the file and write finished vehicles
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(Parameter.OUTPUT_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ArrayList<Vehicle> finishedVehs = ProcessEvents.getFinishedVehs();
            if (finishedVehs.size() > 0) {
                Vehicle veh;
                for (int i = 0; i < finishedVehs.size() - 1; i++) {
                    veh = finishedVehs.get(i);
                    bw.write(veh.toString());
                    bw.newLine();
                }
                veh = finishedVehs.get(finishedVehs.size() - 1);
                bw.write(veh.toString());
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
            double[] inter = distr.interval;
            double r;
            while (time < Parameter.GENERATING_VEHS_TIME) {
                r = rand.nextDouble();
                int i;
                for (i = 0; i < cumu.length; i++) {
                    if (r <= cumu[i]) {
                        break;
                    }
                }
                double lowerLimit = i == 0 ? 0 : inter[i - 1];
                double interval = rand.nextDouble() * (inter[i] - lowerLimit) + lowerLimit;
                time += interval;
                ProcessEvents.getEnteringVehs().add(new Vehicle(
                        id++, time, intersection, direction));
            }
        }
    }
}