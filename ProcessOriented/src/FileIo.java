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
    private BufferedWriter eventWriter = null;
    private boolean wroteEvent = false;

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
                Distribution distribution = new Distribution(intersection, parseDirection(direction), numLines);
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

    public void initialEventWriter() {
        try {
            eventWriter = new BufferedWriter(new FileWriter(Parameter.OUTPUT_EVENT_FILE));
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
            if (!wroteEvent) {
                eventWriter.write(event.toString());
                wroteEvent = true;
            } else {
                eventWriter.newLine();
                eventWriter.write(event.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Write results to file
    public void writeVehicles() {
        // Open the file and write finished vehicles
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(Parameter.OUTPUT_VEHICLE_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ArrayList<VehicleProcess> finishedVehs = Scheduler.getInstance().getFinishedVehs();
            if (finishedVehs.size() > 0) {
                VehicleProcess veh;
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
            Direction direction = distr.direction;
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
                Scheduler.getInstance().getEnteringVehs().add(new VehicleProcess(
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
