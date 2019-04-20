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

    // Vehicle queues, First -> In, Last -> Out
    private ArrayList<LinkedList<Vehicle>> southVehsThrough;
    private ArrayList<LinkedList<Vehicle>> southVehsTurnLeft;
    private ArrayList<LinkedList<Vehicle>> southVehsTurnRight;
    private ArrayList<LinkedList<Vehicle>> eastVehs;
    private ArrayList<LinkedList<Vehicle>> westVehs;

    private TrafficLight[] trafficLights;
    // Status i.e. isGreen of triffic northbound traffic lights at the four intersections
    private boolean[] isGreenSouthThrough = new boolean[4];
    private boolean[] isGreenSouthTurnLeft = new boolean[4];
    private boolean[] isGreenEastTurnRight = new boolean[4];
    private boolean[] isGreenWestTurnLeft = new boolean[4];

    private Random random = new Random();

    /**
     * Private constructor for this singleton class
     */
    private EventHandler() {
        // Initialize the vehicle queues for each traffic light from south, east and west
        // Initialize isGreenSouthThrough in each intersection
        southVehsThrough = new ArrayList<>();
        southVehsTurnRight = new ArrayList<>();
        southVehsTurnLeft = new ArrayList<>();
        eastVehs = new ArrayList<>();
        westVehs = new ArrayList<>();

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

        // Initialize the traffic lights
        trafficLights = new TrafficLight[4];
        trafficLights[0] = new TrafficLight(1,
                10.6, 2.2, 38.3, 49.3,
                8.6, 4.2, 31.8, 55,
                9.8, 1.8, 33.8, 55);
        trafficLights[1] = new TrafficLight(2,
                0, 0, 44.7, 55.4,
                0, 0, 23.9, 76.2,
                0, 0, 23.9, 76.2);
        trafficLights[2] = new TrafficLight(3,
                0, 0, 64.1, 35.7,
                0, 0, 30.9, 69.2,
                0, 0, 30.9, 69.2);
        trafficLights[3] = new TrafficLight(5,
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

// TODO: may need to handle arrival and exit from west and east direction
    public void handleEvent(Event event) {
        switch(event.type) {
            case Arrival:
                arrival(event.intersection, event.direction, event.time, event.vehicle);
                break;
            case Departure:
                departure(event.intersection, event.time, event.vehicle, event.direction);
                break;
            case GreenSouth:
                greenSouth(event.intersection, event.direction, event.time);
                break;
            case RedSouth:
                redSouth(event.intersection, event.direction, event.time);
                break;
            case GreenEastTurnRight:
                greenEastTurnRight(event.intersection, event.time);
                break;
            case RedEastTurnRight:
                redEastTurnRight(event.intersection);
                break;
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

    private void greenSouth(int intersection, Direction direction, double time) {
        switch (direction) {
            case N:
                greenSouthThrough(intersection, time);
                break;
            case W:
                greenSouthTurnLeft(intersection, time);
                break;
            default:
                System.out.println("Error - EventHandler: greenSouth: Wrong direction!");
        }
    }

    private void redSouth(int intersection, Direction direction, double time) {
        switch (direction) {
            case N:
                redSouthThrough(intersection);
                break;
            case W:
                redSouthTurnLeft(intersection);
                break;
            default:
                System.out.println("Error - EventHandler: greenSouth: Wrong direction!");
        }
    }

    // TODO: may need to handle west/east departures
    // TODO: follow the logic of the example in the slides instead of hard coding the departure time.
    private void greenSouthThrough(int intersection, double time) {
        int index = getIntersectionIndex(intersection);
        isGreenSouthThrough[index] = true;
        LinkedList<Vehicle> vehQueue = southVehsThrough.get(index);
        if (!vehQueue.isEmpty()) {
            Vehicle firstVeh = vehQueue.getLast();
            ProcessEvents.getEventQueue().add(new Event(time, EventType.Departure, intersection,
                    Direction.N, firstVeh));
        }

        LinkedList<Vehicle> vehQueueTurnRight = southVehsTurnRight.get(index);
        if (!vehQueueTurnRight.isEmpty()) {
            Vehicle firstVehTurnRight = vehQueueTurnRight.getLast();
            ProcessEvents.getEventQueue().add(new Event(time, EventType.Exit, intersection,
                    Direction.E, firstVehTurnRight));
        }
    }

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

    private void redSouthThrough(int intersection) {
        int index = getIntersectionIndex(intersection);
        isGreenSouthThrough[index] = false;
    }

    private void redSouthTurnLeft(int intersection) {
        int index = getIntersectionIndex(intersection);
        isGreenSouthTurnLeft[index] = false;
    }

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

    private void redEastTurnRight(int intersection) {
        int index = getIntersectionIndex(intersection);
        isGreenEastTurnRight[index] = false;
    }

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

    private void redWestTurnLeft(int intersection) {
        int index = getIntersectionIndex(intersection);
        isGreenWestTurnLeft[index] = false;
    }

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

    private void arrivalSouth(int intersection, double time, Vehicle veh) {
        int index = getIntersectionIndex(intersection);
        double r;
        r = random.nextDouble();
        double exitProb[] = Parameter.getExitCumuProb(intersection);
        if (r < exitProb[0]) {
            southVehsTurnLeft.get(index).addFirst(veh);
            int numInQuene = southVehsTurnLeft.get(index).size();
            if (numInQuene == 1 && isGreenSouthTurnLeft[index]) {
                ProcessEvents.getEventQueue().add(new Event(time, EventType.Exit, intersection, Direction.W, veh));
            }
        } else if (r > exitProb[0] && r < exitProb[1]){
            southVehsTurnRight.get(index).addFirst(veh);
            int numInQuene = southVehsTurnRight.get(index).size();
            if (numInQuene == 1 && isGreenSouthThrough[index]) {
                ProcessEvents.getEventQueue().add(new Event(time, EventType.Exit, intersection, Direction.E, veh));
            }
        } else {
            southVehsThrough.get(index).addFirst(veh);
            int numInQuene = southVehsThrough.get(index).size();
            if (numInQuene == 1 && isGreenSouthThrough[index]) {
                ProcessEvents.getEventQueue().add(new Event(time, EventType.Departure, intersection, Direction.N, veh));
            }
        }
    }

    // TODO: handle arrival from other directions (west/east)
    private void arrivalWest(int intersection, double time, Vehicle veh) {
        int index = getIntersectionIndex(intersection);
        westVehs.get(index).addFirst(veh);
        int numInQueue = westVehs.get(index).size();
        if (numInQueue == 1 && isGreenWestTurnLeft[index]) {
            ProcessEvents.getEventQueue().add(new Event(time, EventType.Departure, intersection, Direction.W, veh));
        }
    }

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
    // TODO: schedule next departure if the queue is not empty.
    private void departure(int intersection, double time, Vehicle veh, Direction direction) {
        // Last departure -> exit
        switch (direction) {
            case N:
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
                        EventType.Departure, intersection, Direction.N, firstInQueue);
                ProcessEvents.getEventQueue().add(depart);
            }
        }
    }

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
     private void exitFromSouth(int intersection, double time, Vehicle veh) {
        exitFromAll(intersection, time, veh, Direction.N);
     }
    private void exitFromAll(int intersection, double time, Vehicle veh, Direction direction) {
        veh.endTime = time;
        veh.exitDirection = direction;
        veh.exitIntersection = intersection;
        ProcessEvents.addFinishedvehs(veh);
    }

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
