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
    private BufferedWriter processWriter = null;

    // Random generator, can be used by other class
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

    public void initialProcessWriter() {
        try {
            processWriter = new BufferedWriter(new FileWriter(Parameter.OUTPUT_EVENT_FILE));
            String header = "time,vehicle,lane,pos,speed,leader,lagger,left_leader,left_lagger,right_leader,right_lagger,following_light";
            processWriter.write(header);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeProcessWriter() {
        try {
            processWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeProcess(double time, Vehicle veh) {
        if (processWriter == null) {
            initialProcessWriter();
        }
        try {
            processWriter.newLine();
            String str = "";
            str += String.format("%.0f", time) + ",";
            str += veh.id + ",";
            str += veh.lane + ",";
            str += veh.pos + ",";
            str += veh.speed + ",";
            str += (veh.leader == null ? "null" : veh.leader.id) + ",";
            str += (veh.lagger == null ? "null" : veh.lagger.id) + ",";
            str += (veh.leftLeader == null ? "null" : veh.leftLeader.id) + ",";
            str += (veh.leftLagger == null ? "null" : veh.leftLagger.id) + ",";
            str += (veh.rightLeader == null ? "null" : veh.rightLeader.id) + ",";
            str += (veh.rightLagger == null ? "null" : veh.rightLagger.id) + ",";
            str += veh.isFollowingLight ? veh.trafficLight.getIntersection() : "null";
            processWriter.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write the results to output file
     */
    public void writeVehicles() {
        // Open the file and write finished vehicles
        try (BufferedWriter bw =
                     new BufferedWriter(new FileWriter(Parameter.OUTPUT_VEHICLE_FILE))) {
            ArrayList<Vehicle> finishedVehs = Ca.getFinishedVehs();
            // Write header
            String header = "id,lane,entrance_time,exit_time,entrance_intersection,entrance_direction,exit_intersection,exit_direction";
            bw.write(header);
            for (Vehicle veh : finishedVehs) {
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
            while (time < Parameter.SIMULATION_TIME) {
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
                if (time > Parameter.SIMULATION_TIME) {
                    break;
                }
                Ca.getEnteringVehs().add(new Vehicle(
                        id++, Parameter.VEH_LEN, getPosition(intersection, direction) + 1,
                        getLane(direction), Parameter.INITIAL_SPEED, time,
                        intersection, direction));
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

    /**
     * Get intersection position
     *
     * @param intersection the intersection
     * @return the position of the intersection
     */
    private int getPosition(int intersection, Direction direction) {
        switch(intersection) {
            case 1:
                if (direction == Direction.S) {
                    return 0;
                }
                return Parameter.INTERSECTION_POSITION_1;
            case 2:
                return Parameter.INTERSECTION_POSITION_2;
            case 3:
                return Parameter.INTERSECTION_POSITION_3;
            case 4:
                return Parameter.INTERSECTION_POSITION_4;
            case 5:
                return Parameter.INTERSECTION_POSITION_5;
            default:
                System.out.println("Error - FileIo.getDistribution: Wrong intersection!");
                return -1;
        }
    }

    private int getLane(Direction direction) {
        switch (direction) {
            case W:
                return 1;
            case E:
                return 0;
            case S:
                return rand.nextDouble() > 0.5 ? 1 : 0;
            default:
                System.out.println("Error - FileIo.getLane: Wrong direction!");
                return -1;
        }
    }
}
