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

    /**
     * Private constructor for this singleton class
     */
    private EventHandler() {
        // Initialize the vehicle queues for each traffic light and isGreenSouth
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


    public void handleEvent(Event event) {
        switch(event.type) {
            case Arrival:
                arrival(event.intersection, event.direction, event.time, event.vehicle);
                break;
            case Departure:
                departure(event.intersection, event.time, event.vehicle);
                break;
            case TurnGreen:
                turnGreen(event.intersection, event.time, event.direction);
                break;
            case TurnRed:
                turnRed(event.intersection, event.direction);
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
    private void turnGreen(int intersection, double time, Direction direction) {
        int index = getIntersectionIndex(intersection);
        isGreenSouth[index] = true;
        LinkedList<Vehicle> vehQueue = southVehs.get(index);
        if (!vehQueue.isEmpty()) {
            Vehicle firstVeh = vehQueue.getLast();
            ProcessEvents.getEventQueue().add(new Event(time, EventType.Departure, intersection,
                    Direction.S, firstVeh));
        }
    }

    private void turnRed(int intersection, Direction direction) {
        int index = getIntersectionIndex(intersection);
        isGreenSouth[index] = false;
    }

   /* private void arrivalSouth(int intersection, double time, Vehicle veh) {
        int index = getIntersectionIndex(intersection);
        int numVehicleToPass = southVehs.get(index).size();
        TrafficLight tl = trafficLights[index];
        // Number of vehicles can go through the traffic light in an entire green light duration
        double greenPass = Math.floor(tl.getSouthThroughGreen() / Parameter.W);
        double departureTime;

        southVehs.get(index).addFirst(veh);
        // Tell if the light is green.
        if (!tl.isThroughGreen(time)) {
            double numGreens = Math.floor(numVehicleToPass / greenPass);
            double resPass = numVehicleToPass % greenPass;
            departureTime = tl.nextSouthThroughGreen(time, numGreens) + resPass * Parameter.W;
        } else {
            double currPass = Math.floor((tl.nextSouthThroughRed(time) - time)/ Parameter.W);
            // If all vehicles can go through the traffic light in current green duration
            if (currPass >= numVehicleToPass) {
                departureTime = time + numVehicleToPass * Parameter.W;
            } else {
                numVehicleToPass -= currPass;
                double numGreens = Math.floor(numVehicleToPass / greenPass);
                double resPass = numVehicleToPass % greenPass;
                departureTime = tl.nextSouthThroughGreen(time, numGreens) + resPass * Parameter.W;
            }
        }
        ProcessEvents.getEventQueue().add(new Event(departureTime, EventType.Departure, intersection, Direction.N, veh));
    }*/

    private void arrivalSouth(int intersection, double time, Vehicle veh) {
        int index = getIntersectionIndex(intersection);
        int numVehicleToPass = southVehs.get(index).size();
        double nextArrivalTime;
        if (numVehicleToPass == 0 && isGreenSouth[index]) {
            nextArrivalTime = getBetweenIntersectionTime(intersection);
            ProcessEvents.getEventQueue().add(new Event(nextArrivalTime, EventType.Arrival, intersection+1, Direction.N, veh));
        } else {
            southVehs.get(index).addFirst(veh);
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
/*
    private void departure(int intersection, double time, Vehicle veh) {
        // Last departure -> exit
        int nextIntersection;
        LinkedList<Vehicle> queue = southVehs.get(getIntersectionIndex(intersection));

        // Check the departing vehicle for debugging.
        Vehicle check = queue.getLast();
        assert check.equals(veh);

        queue.removeLast();
        if (intersection == 5) {
            veh.endTime = time + getBetweenIntersectionTime(intersection);
            veh.exitIntersection = intersection;
            veh.exitDirection = Direction.N;
            ProcessEvents.addFinishedvehs(veh);
            return;
        }

        nextIntersection = intersection == 3 ? 5 : intersection + 1;
        ProcessEvents.getEventQueue().add(new Event(time + getBetweenIntersectionTime(intersection), EventType.Arrival, nextIntersection, Direction.S, veh));
    }
*/

    private void departure(int intersection, double time, Vehicle veh) {
        // Last departure -> exit
        int nextIntersection;
        int index = getIntersectionIndex(intersection);
        LinkedList<Vehicle> queue = southVehs.get(index);

        if (isGreenSouth[index]) {
            if (intersection == 5) {
                veh.endTime = time + getBetweenIntersectionTime((intersection));
                veh.exitIntersection = intersection;
                veh.exitDirection = Direction.N;
                ProcessEvents.addFinishedvehs(veh);
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

        // Check the departing vehicle for debugging.
/*
        Vehicle check = queue.getLast();
        assert check.equals(veh);
*/
        queue.removeLast();
        if (intersection == 5) {
            veh.endTime = time + getBetweenIntersectionTime(intersection);
            veh.exitIntersection = intersection;
            veh.exitDirection = Direction.N;
            ProcessEvents.addFinishedvehs(veh);
            return;
        }

        nextIntersection = intersection == 3 ? 5 : intersection + 1;
        ProcessEvents.getEventQueue().add(new Event(time + getBetweenIntersectionTime(intersection), EventType.Arrival, nextIntersection, Direction.S, veh));
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
