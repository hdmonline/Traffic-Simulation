/**
 * EventHandler.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * A singleton class for handling all kinds of events.
 */

import java.util.ArrayList;
import java.util.LinkedList;

public class EventHandler {
    private static EventHandler instance = null;

    // Vehicle queues, First -> In, Last -> Out
    private ArrayList<LinkedList<Vehicle>> southVehs;
    private TrafficLight[] trafficLights;
    // Status i.e. isGreen of triffic northbound traffic lights at the four intersections
    private boolean[] isGreenSouth = new boolean[4];
    // at 10th street, is GreenEast refers to
    private boolean[] isGreenEast = new boolean[4];
    private boolean[] isGreenWest = new boolean[4];

    /**
     * Private constructor for this singleton class
     */
    private EventHandler() {
        // Initialize the vehicle queues for each traffic light
        // Initialize isGreenSouth in each intersection
        southVehs = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            southVehs.add(new LinkedList<>());
            isGreenSouth[i] = false;
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
            case TurnGreenSouth:
                turnGreenSouth(event.intersection, event.time, event.direction);
                break;
            case TurnRedSouth:
                turnRedSouth(event.intersection, event.direction);
                break;
            case Exit:
                exit(event.intersection, event.time, event.vehicle, event.direction);
                break;
            default:
                System.out.println("Error - EventHandler.handleEvent: Wrong Event!");
        }
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

    // TODO: may need to handle west/east departures
    // TODO: follow the logic of the example in the slides instead of hard coding the departure time.
    private void turnGreenSouth(int intersection, double time, Direction direction) {
        int index = getIntersectionIndex(intersection);
        isGreenSouth[index] = true;
        LinkedList<Vehicle> vehQueue = southVehs.get(index);
        if (!vehQueue.isEmpty()) {
            Vehicle firstVeh = vehQueue.getLast();
            ProcessEvents.getEventQueue().add(new Event(time + Parameter.W, EventType.Departure, intersection,
                    Direction.N, firstVeh));
        }
    }

    private void turnRedSouth(int intersection, Direction direction) {
        int index = getIntersectionIndex(intersection);
        isGreenSouth[index] = false;
    }


    private void arrivalSouth(int intersection, double time, Vehicle veh) {
        int index = getIntersectionIndex(intersection);
        southVehs.get(index).addFirst(veh);
        int numInQuene = southVehs.get(index).size();
        double nextArrivalTime;
        if (numInQuene == 1 && isGreenSouth[index]) {
            // nextArrivalTime = getBetweenIntersectionTime(intersection) + Parameter.W;
            // ProcessEvents.getEventQueue().add(new Event(nextArrivalTime, EventType.Arrival, intersection+1, Direction.S, veh));
            ProcessEvents.getEventQueue().add(new Event(time + Parameter.W, EventType.Departure, intersection, Direction.N, veh));
        }
    }

    // TODO: handle arrival from other directions (west/east)
    private void arrivalWest(int intersection, double time, Vehicle veh) {

    }

    private void arrivalEast(int intersection, double time, Vehicle veh) {

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
        int nextIntersection;
        int index = getIntersectionIndex(intersection);
        LinkedList<Vehicle> queue = southVehs.get(index);

        if (isGreenSouth[index]) {
            if (intersection == 5) {
/*                veh.endTime = time + getBetweenIntersectionTime((intersection));
                veh.exitIntersection = intersection;
                veh.exitDirection = Direction.N;
                ProcessEvents.addFinishedvehs(veh);*/
                Event exit = new Event(time + getBetweenIntersectionTime(intersection), EventType.Exit, intersection, direction, veh);
                ProcessEvents.getEventQueue().add(exit);
            } else {
                nextIntersection = intersection == 3 ? 5 : intersection + 1;
                ProcessEvents.getEventQueue().add(new Event(time + getBetweenIntersectionTime(intersection), EventType.Arrival, nextIntersection, Direction.S, veh));
            }
            queue.removeLast();
            if (!queue.isEmpty()) {
                Vehicle firstInQueue = queue.getLast();
                Event depart = new Event(time + Parameter.W, EventType.Departure, intersection, Direction.N, firstInQueue);
                ProcessEvents.getEventQueue().add(depart);
            }
        }
    }

    private void exit(int intersection, double time, Vehicle veh, Direction direction) {
        veh.endTime = time;
        veh.exitDirection = direction;
        veh.exitIntersection = intersection;
        ProcessEvents.addFinishedvehs(veh);
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
