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
    private BufferedWriter eventWriter = null;
    static Random rand = new Random();

    /**
     * Read input file and load distributions to every intersection/direction
     */
    public void readFile() {
        // Open the file and read inter arrival interval of each intersection and direction
        try (BufferedReader br =
                     new BufferedReader(new FileReader(Parameter.INPUT_FILE))) {
            String currLine;
            int intersection, direction, numLines;
            // Get read all lines
            while ((currLine = br.readLine()) != null) {
                String[] strs = currLine.split(",", 3);
                intersection = Integer.parseInt(strs[0]);
                direction = Integer.parseInt(strs[1]);
                numLines = Integer.parseInt(strs[2]);
                Distribution distribution = new Distribution(intersection, parseDirection(direction), numLines);
                // Read distribution bins
                for (int i = 0; i < numLines; i++) {
                    currLine = br.readLine();
                    String[] pair = currLine.split(",", 2);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialEventWriter() {
        try {
            eventWriter = new BufferedWriter(new FileWriter(Parameter.OUTPUT_EVENT_FILE));
            String header = "time,type,intersection,direction,turning_left,vehicle";
            eventWriter.write(header);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeEvnetWriter() {
        try {
            eventWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeEvent(Event event) {
        if (eventWriter == null) {
            initialEventWriter();
        }
        try {
            eventWriter.newLine();
            eventWriter.write(event.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write vehicles to file
     */
    public void writeVehicles() {
        // Open the file and write finished vehicles
        try (BufferedWriter bw =
                     new BufferedWriter(new FileWriter(Parameter.OUTPUT_VEHICLE_FILE))) {
            ArrayList<VehicleProcess> finishedVehs = EventHandler.getInstance().getFinishedVehs();
            // Write header
            String header = "id,enter_time,exit_time,entrance_intersection,entrance_direction,exit_intersection,exit_direction";
            bw.write(header);
            for (VehicleProcess veh : finishedVehs) {
                bw.newLine();
                bw.write(veh.toString());
            }
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
            Direction direction = distr.direction;
            double[] cumu = distr.cumuProb;
            double[] inter = distr.interval;
            double r;
            while (time < Parameter.VEHICLE_TIME) {
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
                EventHandler.getInstance().getEnteringVehs().add(new VehicleProcess(
                        id++, time, intersection, direction));
            }
        }
    }

    private Direction parseDirection(int d) {
        switch (d) {
            case 1:
                return Direction.S;
            case 2:
                return Direction.W;
            case 3:
                return Direction.E;
            default:
                System.out.println("Error - FileIo.parseDirection: Wrong direction!");
                return null;
        }
    }
}
