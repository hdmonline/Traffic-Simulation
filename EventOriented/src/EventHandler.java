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

    // Waiting time for going through a traffic light per vehicle in (s)
    private static final double W = 1.0;


    /**
     * Private constructor for this singleton class
     */
    private EventHandler() {
        // Initialize the vehicle queues for each traffic light
        southVehs = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            southVehs.add(new LinkedList<>());
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

    // TODO: add TurnRed & TurnGreen
    public void handleEvent(Event event) {
        switch(event.type) {
            case Arrival:
                arrival(event.intersection, event.direction, event.time, event.vehicle);
                break;
            case Departure:
                departure(event.intersection, event.time, event.vehicle);
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
    private void arrivalSouth(int intersection, double time, Vehicle veh) {
        int index = getIntersectionIndex(intersection);
        int numVehicleToPass = southVehs.get(index).size();
        TrafficLight tl = trafficLights[index];
        // Number of vehicles can go through the traffic light in an entire green light duration
        double greenPass = Math.floor(tl.getSouthThroughGreen() / W);
        double departureTime;

        southVehs.get(index).addFirst(veh);
        // Tell if the light is green.
        if (!tl.isThroughGreen(time)) {
            double numGreens = Math.floor(numVehicleToPass / greenPass);
            double resPass = numVehicleToPass % greenPass;
            departureTime = tl.nextSouthThroughGreen(time, numGreens) + resPass * W;
        } else {
            double currPass = Math.floor((tl.nextSouthThroughRed(time) - time)/ W);
            // If all vehicles can go through the traffic light in current green duration
            if (currPass >= numVehicleToPass) {
                departureTime = time + numVehicleToPass * W;
            } else {
                numVehicleToPass -= currPass;
                double numGreens = Math.floor(numVehicleToPass / greenPass);
                double resPass = numVehicleToPass % greenPass;
                departureTime = tl.nextSouthThroughGreen(time, numGreens) + resPass * W;
            }
        }
        ProcessEvents.getEventQueue().add(new Event(departureTime, EventType.Departure, intersection, Direction.N, veh));
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
}
