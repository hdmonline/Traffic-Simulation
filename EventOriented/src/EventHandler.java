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
    private boolean[] isGreenEastTurnLeft = new boolean[4];
    private boolean[] isGreenWestTurnRight = new boolean[4];

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
            isGreenEastTurnLeft[i] = false;
            isGreenWestTurnRight[i] = false;
        }

        // Initialize the traffic lights
        trafficLights = new TrafficLight[4];
        trafficLights[0] = new TrafficLight(1, 49.3, 38.3, 10.6, 2.2);
        trafficLights[1] = new TrafficLight(2, 55.4, 44.7, 0, 0);
        trafficLights[2] = new TrafficLight(3, 35.7, 64.6, 0, 0);
        trafficLights[3] = new TrafficLight(5, 46.1, 37.8, 12.4, 3.6);
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
            case GreenEastTurnLeft:
                greenEastTurnLeft(event.intersection, event.time);
                break;
            case RedEastTurnLeft:
                redEastTurnLeft(event.intersection);
                break;
            case GreenWestTurnRight:
                greenWestTurnRight(event.intersection, event.time);
                break;
            case RedWestTurnRight:
                redWestTurnRight(event.intersection);
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
            case S:
                greenSouthThrough(intersection, time);
                break;
            case E:
                greenSouthTurnLeft(intersection, time);
                break;
            default:
                System.out.println("Error - EventHandler: greenSouth: Wrong direction!");
        }
    }

    private void redSouth(int intersection, Direction direction, double time) {
        switch (direction) {
            case S:
                redSouthThrough(intersection);
                break;
            case E:
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
                    Direction.S, firstVeh));
        }

        LinkedList<Vehicle> vehQueueTurnRight = southVehsTurnRight.get(index);
        if (!vehQueueTurnRight.isEmpty()) {
            Vehicle firstVehTurnRight = vehQueueTurnRight.getLast();
            ProcessEvents.getEventQueue().add(new Event(time, EventType.Exit, intersection,
                    Direction.W, firstVehTurnRight));
        }
    }

    private void greenSouthTurnLeft(int intersection, double time) {
        int index = getIntersectionIndex(intersection);
        isGreenSouthTurnLeft[index] = true;
        LinkedList<Vehicle> vehQueue = southVehsTurnLeft.get(index);
        if (!vehQueue.isEmpty()) {
            Vehicle firstVeh = vehQueue.getLast();
            ProcessEvents.getEventQueue().add(new Event(time, EventType.Exit, intersection,
                    Direction.E, firstVeh));
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

    private void greenEastTurnLeft(int intersection, double time) {
        int index = getIntersectionIndex(intersection);
        isGreenEastTurnLeft[index] = true;
        LinkedList<Vehicle> vehQueue = eastVehs.get(index);
        if (!vehQueue.isEmpty()) {
            Vehicle firstVeh = vehQueue.getLast();
            ProcessEvents.getEventQueue().add(new Event(time, EventType.Departure, intersection,
                    Direction.E, firstVeh));
        }
    }

    private void redEastTurnLeft(int intersection) {
        int index = getIntersectionIndex(intersection);
        isGreenEastTurnLeft[index] = false;
    }

    private void greenWestTurnRight(int intersection, double time) {
        int index = getIntersectionIndex(intersection);
        isGreenWestTurnRight[index] = true;
        LinkedList<Vehicle> vehQueue = westVehs.get(index);
        if (!vehQueue.isEmpty()) {
            Vehicle firstVeh = vehQueue.getLast();
            ProcessEvents.getEventQueue().add(new Event(time, EventType.Departure, intersection,
                    Direction.W, firstVeh));
        }
    }

    private void redWestTurnRight(int intersection) {
        int index = getIntersectionIndex(intersection);
        isGreenWestTurnRight[index] = false;
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
        if (r < Parameter.TURN_LEFT_PROB) {
            southVehsTurnLeft.get(index).addFirst(veh);
            int numInQuene = southVehsTurnLeft.get(index).size();
            if (numInQuene == 1 && isGreenSouthTurnLeft[index]) {
                ProcessEvents.getEventQueue().add(new Event(time, EventType.Exit, intersection, Direction.E, veh));
            }
        } else if (r > Parameter.TURN_LEFT_PROB && r < Parameter.CUMUL_PROB){
            southVehsTurnRight.get(index).addFirst(veh);
            int numInQuene = southVehsTurnRight.get(index).size();
            if (numInQuene == 1 && isGreenSouthThrough[index]) {
                ProcessEvents.getEventQueue().add(new Event(time, EventType.Exit, intersection, Direction.W, veh));
            }
        } else {
            southVehsThrough.get(index).addFirst(veh);
            int numInQuene = southVehsThrough.get(index).size();
            if (numInQuene == 1 && isGreenSouthThrough[index]) {
                ProcessEvents.getEventQueue().add(new Event(time, EventType.Departure, intersection, Direction.S, veh));
            }
        }
    }

    // TODO: handle arrival from other directions (west/east)
    private void arrivalWest(int intersection, double time, Vehicle veh) {
        int index = getIntersectionIndex(intersection);
        westVehs.get(index).addFirst(veh);
        int numInQueue = westVehs.get(index).size();
        if (numInQueue == 1 && isGreenWestTurnRight[index]) {
            ProcessEvents.getEventQueue().add(new Event(time, EventType.Departure, intersection, Direction.W, veh));
        }
    }

    private void arrivalEast(int intersection, double time, Vehicle veh) {
        int index = getIntersectionIndex(intersection);
        eastVehs.get(index).addFirst(veh);
        int numInQueue = eastVehs.get(index).size();
        if (numInQueue == 1 && isGreenEastTurnLeft[index]) {
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

    private void departureFromSouth(int intersection, double time, Vehicle veh) {
        int nextIntersection;
        int index = getIntersectionIndex(intersection);
        LinkedList<Vehicle> queue = southVehsThrough.get(index);
        if (isGreenSouthThrough[index]) {
            if (intersection == 5) {
                Event exit = new Event(time + getBetweenIntersectionTime(intersection), EventType.Exit, intersection, Direction.S, veh);
                ProcessEvents.getEventQueue().add(exit);
            } else {
                nextIntersection = intersection == 3 ? 5 : intersection + 1;
                ProcessEvents.getEventQueue().add(new Event(time + getBetweenIntersectionTime(intersection), EventType.Arrival, nextIntersection, Direction.S, veh));
            }
            queue.removeLast();
            if (!queue.isEmpty()) {
                Vehicle firstInQueue = queue.getLast();
                Event depart = new Event(time + Parameter.W, EventType.Departure, intersection, Direction.S, firstInQueue);
                ProcessEvents.getEventQueue().add(depart);
            }
        }
    }

    private void departureFromEast(int intersection, double time, Vehicle veh) {
        int nextIntersection;
        int index = getIntersectionIndex(intersection);
        LinkedList<Vehicle> queue = eastVehs.get(index);
        if (isGreenEastTurnLeft[index]) {
            if (intersection == 5) {
                Event exit = new Event(time + getBetweenIntersectionTime(intersection), EventType.Exit, intersection, Direction.E, veh);
                ProcessEvents.getEventQueue().add(exit);
            } else {
                nextIntersection = intersection == 3 ? 5 : intersection + 1;
                ProcessEvents.getEventQueue().add(new Event(time + getBetweenIntersectionTime(intersection), EventType.Arrival, nextIntersection, Direction.E, veh));
            }
            queue.removeLast();
            if (!queue.isEmpty()) {
                Vehicle firstInQueue = queue.getLast();
                Event depart = new Event(time + Parameter.W, EventType.Departure, intersection, Direction.E, firstInQueue);
                ProcessEvents.getEventQueue().add(depart);
            }
        }
    }

    private void departureFromWest(int intersection, double time, Vehicle veh) {
        int nextIntersection;
        int index = getIntersectionIndex(intersection);
        LinkedList<Vehicle> queue = westVehs.get(index);
        if (isGreenWestTurnRight[index]) {
            if (intersection == 5) {
                Event exit = new Event(time + getBetweenIntersectionTime(intersection), EventType.Exit, intersection, Direction.W, veh);
                ProcessEvents.getEventQueue().add(exit);
            } else {
                nextIntersection = intersection == 3 ? 5 : intersection + 1;
                ProcessEvents.getEventQueue().add(new Event(time + getBetweenIntersectionTime(intersection), EventType.Arrival, nextIntersection, Direction.W, veh));
            }
            queue.removeLast();
            if (!queue.isEmpty()) {
                Vehicle firstInQueue = queue.getLast();
                Event depart = new Event(time + Parameter.W, EventType.Departure, intersection, Direction.W, firstInQueue);
                ProcessEvents.getEventQueue().add(depart);
            }
        }
    }

    private void exit(int intersection, double time, Vehicle veh, Direction direction) {
        switch (direction) {
            case S:
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
        exitFromAll(intersection, time, veh, Direction.S);
     }
    private void exitFromAll(int intersection, double time, Vehicle veh, Direction direction) {
        veh.endTime = time;
        veh.exitDirection = direction;
        veh.exitIntersection = intersection;
        ProcessEvents.addFinishedvehs(veh);
    }

    private void exitFromEast(int intersection, double time, Vehicle veh) {
        int index = getIntersectionIndex(intersection);
        LinkedList<Vehicle> queue = southVehsTurnLeft.get(index);
        if (isGreenSouthTurnLeft[index]) {
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
        LinkedList<Vehicle> queue = southVehsTurnRight.get(index);
        if (isGreenSouthThrough[index]) {
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
