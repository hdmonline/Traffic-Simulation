/**
 * EventHandler.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * A singleton class for handling all kinds of events.
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class EventHandler {
    private static EventHandler instance = null;

    // Vehicle queues, First -> In, First -> Out
    private ArrayList<LinkedList<Vehicle>> southVehsThrough;
    private ArrayList<LinkedList<Vehicle>> southVehsTurnLeft;
    private ArrayList<LinkedList<Vehicle>> southVehsTurnRight;
    private ArrayList<LinkedList<Vehicle>> eastVehs;
    private ArrayList<LinkedList<Vehicle>> westVehs;

    private TrafficLight[] trafficLights;
    // Status i.e. isGreen of traffic lights at the four intersections
    private boolean[] isGreenSouthThrough = new boolean[4];
    private boolean[] isGreenSouthTurnLeft = new boolean[4];
    private boolean[] isGreenEastTurnRight = new boolean[4];
    private boolean[] isGreenWestTurnLeft = new boolean[4];

    private Random random = new Random();

    /**
     * Private constructor for this singleton class
     */
    private EventHandler() {
        // Initialize the vehicle queues at each intersection from south, east and west
        // Initialize status of traffic lights in each intersection
        southVehsThrough = new ArrayList<>();
        southVehsTurnRight = new ArrayList<>();
        southVehsTurnLeft = new ArrayList<>();
        eastVehs = new ArrayList<>();
        westVehs = new ArrayList<>();
        // Initialize vehs queues arriving each intersection
        for (int i = 0; i < 4; i++) {
            southVehsThrough.add(new LinkedList<>());
            southVehsTurnRight.add(new LinkedList<>());
            southVehsTurnLeft.add(new LinkedList<>());
            eastVehs.add(new LinkedList<>());
            westVehs.add(new LinkedList<>());

            isGreenSouthThrough[i] = false;
            isGreenSouthTurnLeft[i] = false;
            isGreenEastTurnRight[i] = false;
            isGreenWestTurnLeft[i] = false;
        }

        // Initialize the traffic lights at 10th street, 11th street, 12th street and 14th street
        trafficLights = new TrafficLight[4];
        trafficLights[0] = new TrafficLight(1, 0,
                10.6, 2.2, 38.3, 49.3,
                8.6, 4.2, 31.8, 55,
                9.8, 1.8, 33.8, 55);
        trafficLights[1] = new TrafficLight(2, Parameter.TRAFFIC_LIGHT_DELAY,
                0, 0, 44.7, 55.4,
                0, 0, 23.9, 76.2,
                0, 0, 23.9, 76.2);
        trafficLights[2] = new TrafficLight(3, Parameter.TRAFFIC_LIGHT_DELAY * 2,
                0, 0, 64.1, 35.7,
                0, 0, 30.9, 69.2,
                0, 0, 30.9, 69.2);
        trafficLights[3] = new TrafficLight(5, Parameter.TRAFFIC_LIGHT_DELAY * 3,
                12.4, 3.6, 37.8, 46.1,
                0, 0, 26.1, 74,
                13.4, 60, 40.6, 60.2);
    }

    public static EventHandler getInstance() {
        if (instance == null) {
            instance = new EventHandler();
        }
        return instance;
    }

    // Handle event based on the event type
    public void handleEvent(Event event) {
        switch(event.type) {
            case Arrival:
                arrival(event.intersection, event.direction, event.time, event.vehicle);
                break;
            case Departure:
                departure(event.intersection, event.time, event.vehicle, event.direction);
                break;
            // Northbound traffic light events
            case GreenSouth:
                greenSouth(event.intersection, event.direction, event.time);
                break;
            case RedSouth:
                redSouth(event.intersection, event.direction, event.time);
                break;
            // Westbound traffic light turn right
            case GreenEastTurnRight:
                greenEastTurnRight(event.intersection, event.time);
                break;
            case RedEastTurnRight:
                redEastTurnRight(event.intersection);
                break;
            // Eastbound traffic light turn left
            case GreenWestTurnLeft:
                greenWestTurnLeft(event.intersection, event.time);
                break;
            case RedWestTurnLeft:
                redWestTurnLeft(event.intersection);
                break;
            case Exit:
                exit(event.intersection, event.time, event.vehicle, event.direction);
                break;
            default:
                System.out.println("Error - EventHandler.handleEvent: Wrong Event!");
        }
    }

    // Northbound traffic light event
    private void greenSouth(int intersection, Direction direction, double time) {
        switch (direction) {
            // Northbound through green
            case N:
                greenSouthThrough(intersection, time);
                break;
            // Northbound turn left green
            case W:
                greenSouthTurnLeft(intersection, time);
                break;
            default:
                System.out.println("Error - EventHandler: greenSouth: Wrong direction!");
        }
    }

    private void redSouth(int intersection, Direction direction, double time) {
        switch (direction) {
            // Northbound through red
            case N:
                redSouthThrough(intersection);
                break;
            // Northbound turn left red
            case W:
                redSouthTurnLeft(intersection);
                break;
            default:
                System.out.println("Error - EventHandler: greenSouth: Wrong direction!");
        }
    }

    /*
    Schedule departure event for the first vehicle in the southVehsThrough queue
    schedule Exit to East event for the first vehicle in the southVehsTurnRight queue
    */
    private void greenSouthThrough(int intersection, double time) {
        int index = getIntersectionIndex(intersection);
        isGreenSouthThrough[index] = true;
        LinkedList<Vehicle> vehQueue = southVehsThrough.get(index);
        if (!vehQueue.isEmpty()) {
            Vehicle firstVeh = vehQueue.getLast();
            ProcessEvents.getEventQueue().add(new Event(time, EventType.Departure, intersection,
                    Direction.S, firstVeh));
        }

        LinkedList<Vehicle> vehQueueTurnRight = southVehsTurnRight.get(index);
        if (!vehQueueTurnRight.isEmpty()) {
            Vehicle firstVehTurnRight = vehQueueTurnRight.getLast();
            ProcessEvents.getEventQueue().add(new Event(time, EventType.Exit, intersection,
                    Direction.E, firstVehTurnRight));
        }
    }

    // Set the northbound turn left light green, schedule exit to west event for the first veh in the queue
    private void greenSouthTurnLeft(int intersection, double time) {
        int index = getIntersectionIndex(intersection);
        isGreenSouthTurnLeft[index] = true;
        LinkedList<Vehicle> vehQueue = southVehsTurnLeft.get(index);
        if (!vehQueue.isEmpty()) {
            Vehicle firstVeh = vehQueue.getLast();
            ProcessEvents.getEventQueue().add(new Event(time, EventType.Exit, intersection,
                    Direction.W, firstVeh));
        }
    }

    // Set the northbound through light red
    private void redSouthThrough(int intersection) {
        int index = getIntersectionIndex(intersection);
        isGreenSouthThrough[index] = false;
    }

    // Set the northbound turn left light red
    private void redSouthTurnLeft(int intersection) {
        int index = getIntersectionIndex(intersection);
        isGreenSouthTurnLeft[index] = false;
    }

    // Set westbound turn right light green, schedule departure from east event for 1st veh in the queue
    private void greenEastTurnRight(int intersection, double time) {
        int index = getIntersectionIndex(intersection);
        isGreenEastTurnRight[index] = true;
        LinkedList<Vehicle> vehQueue = eastVehs.get(index);
        if (!vehQueue.isEmpty()) {
            Vehicle firstVeh = vehQueue.getLast();
            ProcessEvents.getEventQueue().add(new Event(time, EventType.Departure, intersection,
                    Direction.E, firstVeh));
        }
    }

    // Set Westbound turn right light red
    private void redEastTurnRight(int intersection) {
        int index = getIntersectionIndex(intersection);
        isGreenEastTurnRight[index] = false;
    }

    // Set eastbound turn left light green, schedule departure from west event for 1st veh in the queue
    private void greenWestTurnLeft(int intersection, double time) {
        int index = getIntersectionIndex(intersection);
        isGreenWestTurnLeft[index] = true;
        LinkedList<Vehicle> vehQueue = westVehs.get(index);
        if (!vehQueue.isEmpty()) {
            Vehicle firstVeh = vehQueue.getLast();
            ProcessEvents.getEventQueue().add(new Event(time, EventType.Departure, intersection,
                    Direction.W, firstVeh));
        }
    }

    // Set eastbound turn left light red
    private void redWestTurnLeft(int intersection) {
        int index = getIntersectionIndex(intersection);
        isGreenWestTurnLeft[index] = false;
    }

    // Vehs arrive before traffic light from south, west and east
    private void arrival(int intersection, Direction direction, double time, Vehicle veh) {
        switch (direction) {
            case S:
                arrivalSouth(intersection, time, veh);
                break;
            case W:
                arrivalWest(intersection, time, veh);
                break;
            case E:
                arrivalEast(intersection, time, veh);
                break;
            default:
                System.out.println("Error - EventHandler.arrival: Wrong direction!");
        }
    }

    // Vehs arrive from south, possible to go straight, turn left (exit) and turn right (exit)
    private void arrivalSouth(int intersection, double time, Vehicle veh) {
        int index = getIntersectionIndex(intersection);
        double r;
        r = random.nextDouble();
        double exitProb[] = Parameter.getExitCumuProb(intersection);
        // Vehs turn left and exit to West
        if (r < exitProb[0]) {
            southVehsTurnLeft.get(index).addFirst(veh);
            int numInQuene = southVehsTurnLeft.get(index).size();
            if (numInQuene == 1 && isGreenSouthTurnLeft[index]) {
                ProcessEvents.getEventQueue().add(new Event(time, EventType.Exit, intersection, Direction.W, veh));
            }
        // Vehs turn right and exit to East
        } else if (r > exitProb[0] && r < exitProb[1]){
            southVehsTurnRight.get(index).addFirst(veh);
            int numInQuene = southVehsTurnRight.get(index).size();
            if (numInQuene == 1 && isGreenSouthThrough[index]) {
                ProcessEvents.getEventQueue().add(new Event(time, EventType.Exit, intersection, Direction.E, veh));
            }
        // Vehs go straight and travel through the intersection
        } else {
            southVehsThrough.get(index).addFirst(veh);
            int numInQuene = southVehsThrough.get(index).size();
            if (numInQuene == 1 && isGreenSouthThrough[index]) {
                ProcessEvents.getEventQueue().add(new Event(time, EventType.Departure, intersection, Direction.S, veh));
            }
        }
    }

    // Vehs arrive from west, and turn left into Peachtree street if traffic light is green
    private void arrivalWest(int intersection, double time, Vehicle veh) {
        int index = getIntersectionIndex(intersection);
        westVehs.get(index).addFirst(veh);
        int numInQueue = westVehs.get(index).size();
        if (numInQueue == 1 && isGreenWestTurnLeft[index]) {
            ProcessEvents.getEventQueue().add(new Event(time, EventType.Departure, intersection, Direction.W, veh));
        }
    }

    // Vehs arrive from east, and turn right into Peachtree street if traffic light is green
    private void arrivalEast(int intersection, double time, Vehicle veh) {
        int index = getIntersectionIndex(intersection);
        eastVehs.get(index).addFirst(veh);
        int numInQueue = eastVehs.get(index).size();
        if (numInQueue == 1 && isGreenEastTurnRight[index]) {
            ProcessEvents.getEventQueue().add(new Event(time, EventType.Departure, intersection, Direction.E, veh));
        }
    }

    /**
     * Depart from current intersection to the north
     *
     * @param intersection current intersection
     * @param time current time
     * @param veh current vehicle
     */
    // Vehs depart from south, east and west to north
    private void departure(int intersection, double time, Vehicle veh, Direction direction) {
        // Last departure -> exit
        switch (direction) {
            case S:
                departureFromSouth(intersection, time, veh) ;
                break;
            case E:
                departureFromEast(intersection, time, veh);
                break;
            case W:
                departureFromWest(intersection, time, veh);
                break;
            default:
                System.out.println("Error - EventHandler.departure: No such direction!");
        }
    }

    // Departure from South to North, if arrives 14th street, then exit
    private void departureFromSouth(int intersection, double time, Vehicle veh) {
        int nextIntersection;
        int index = getIntersectionIndex(intersection);
        LinkedList<Vehicle> queue = southVehsThrough.get(index);
        if (isGreenSouthThrough[index]) {
            if (intersection == 5) {
                Event exit = new Event(time + getBetweenIntersectionTime(intersection),
                        EventType.Exit, intersection, Direction.N, veh);
                ProcessEvents.getEventQueue().add(exit);
            } else {
                nextIntersection = intersection == 3 ? 5 : intersection + 1;
                ProcessEvents.getEventQueue().add(new Event(time + getBetweenIntersectionTime(intersection),
                        EventType.Arrival, nextIntersection, Direction.S, veh));
            }
            queue.removeLast();
            if (!queue.isEmpty()) {
                Vehicle firstInQueue = queue.getLast();
                Event depart = new Event(time + Parameter.W,
                        EventType.Departure, intersection, Direction.S, firstInQueue);
                ProcessEvents.getEventQueue().add(depart);
            }
        }
    }

    // Departure from East
    private void departureFromEast(int intersection, double time, Vehicle veh) {
        int nextIntersection;
        int index = getIntersectionIndex(intersection);
        LinkedList<Vehicle> queue = eastVehs.get(index);
        if (isGreenEastTurnRight[index]) {
            if (intersection == 5) {
                Event exit = new Event(time + getBetweenIntersectionTime(intersection),
                        EventType.Exit, intersection, Direction.N, veh);
                ProcessEvents.getEventQueue().add(exit);
            } else {
                nextIntersection = intersection == 3 ? 5 : intersection + 1;
                ProcessEvents.getEventQueue().add(new Event(time + getBetweenIntersectionTime(intersection),
                        EventType.Arrival, nextIntersection, Direction.S, veh));
            }
            queue.removeLast();
            if (!queue.isEmpty()) {
                Vehicle firstInQueue = queue.getLast();
                Event depart = new Event(time + Parameter.W,
                        EventType.Departure, intersection, Direction.E, firstInQueue);
                ProcessEvents.getEventQueue().add(depart);
            }
        }
    }

    // Departure from West
    private void departureFromWest(int intersection, double time, Vehicle veh) {
        int nextIntersection;
        int index = getIntersectionIndex(intersection);
        LinkedList<Vehicle> queue = westVehs.get(index);
        if (isGreenWestTurnLeft[index]) {
            if (intersection == 5) {
                Event exit = new Event(time + getBetweenIntersectionTime(intersection),
                        EventType.Exit, intersection, Direction.N, veh);
                ProcessEvents.getEventQueue().add(exit);
            } else {
                nextIntersection = intersection == 3 ? 5 : intersection + 1;
                ProcessEvents.getEventQueue().add(new Event(time + getBetweenIntersectionTime(intersection),
                        EventType.Arrival, nextIntersection, Direction.S, veh));
            }
            queue.removeLast();
            if (!queue.isEmpty()) {
                Vehicle firstInQueue = queue.getLast();
                Event depart = new Event(time + Parameter.W,
                        EventType.Departure, intersection, Direction.W, firstInQueue);
                ProcessEvents.getEventQueue().add(depart);
            }
        }
    }

    // Exit from 14th street to north, exit from other intersections to west or east
    private void exit(int intersection, double time, Vehicle veh, Direction direction) {
        switch (direction) {
            case N:
                exitFromSouth(intersection, time, veh);
                break;
            case E:
                exitFromEast(intersection, time, veh);
                break;
            case W:
                exitFromWest(intersection, time, veh);
                break;
            default:
                System.out.println("Error - EventHandler.exit: No such direction!");
        }
    }
    // exit from 14th street to north
     private void exitFromSouth(int intersection, double time, Vehicle veh) {
        exitFromAll(intersection, time, veh, Direction.N);
     }
    private void exitFromAll(int intersection, double time, Vehicle veh, Direction direction) {
        veh.endTime = time;
        veh.exitDirection = direction;
        veh.exitIntersection = intersection;
        ProcessEvents.addFinishedvehs(veh);
    }

    // exit to east (northbound vehs turn right and exit to east)
    private void exitFromEast(int intersection, double time, Vehicle veh) {
        int index = getIntersectionIndex(intersection);
        LinkedList<Vehicle> queue = southVehsTurnRight.get(index);
        if (isGreenSouthThrough[index]) {
            exitFromAll(intersection, time, veh, Direction.E);
            queue.removeLast();
            if (!queue.isEmpty()) {
                Vehicle firstInQueue = queue.getLast();
                Event exit = new Event(time + Parameter.W, EventType.Exit, intersection, Direction.E, firstInQueue);
                ProcessEvents.getEventQueue().add(exit);
            }
        }
    }

    // exit to west (northbound vehs turn left and exit to west)
    private void exitFromWest (int intersection, double time, Vehicle veh) {
        int index = getIntersectionIndex(intersection);
        LinkedList<Vehicle> queue = southVehsTurnLeft.get(index);
        if (isGreenSouthTurnLeft[index]) {
            exitFromAll(intersection, time, veh, Direction.W);
            queue.removeLast();
            if (!queue.isEmpty()) {
                Vehicle firstInQueue = queue.getLast();
                Event exit = new Event(time + Parameter.W, EventType.Exit, intersection, Direction.W, firstInQueue);
                ProcessEvents.getEventQueue().add(exit);
            }
        }
    }

    private int getIntersectionIndex(int intersection) {
        switch(intersection) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 5:
                return 3;
            default:
                System.out.println("Error - EventHandler.handleEvent: Wrong Intersection!");
                return -1;
        }
    }

    private double getBetweenIntersectionTime(int intersection) {
        switch(intersection) {
            case 1:
                return Parameter.BETWEEN_INTERSECTION_12;
            case 2:
                return Parameter.BETWEEN_INTERSECTION_23;
            case 3:
                return Parameter.BETWEEN_INTERSECTION_35;
            case 5:
                return Parameter.AFTER_INTERSECTION_5;
            default:
                System.out.println("Error - EventHandler.handleEvent: Wrong Intersection!");
                return -1;
        }
    }

    public TrafficLight[] getTrafficLights() {
        return trafficLights;
    }
}
