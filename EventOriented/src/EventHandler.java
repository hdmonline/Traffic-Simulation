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
    private ArrayList<LinkedList<Vehicle>> northBoundVehs;
    private TrafficLight[] trafficLights;

    // Waiting time for going through a traffic light per vehicle in (s)
    private static final double W = 1.0;

    // Average Travelling time between intersections
    private static final double BETWEEN_INTERSECTION_12 = 3;
    private static final double BETWEEN_INTERSECTION_23 = 3;
    private static final double BETWEEN_INTERSECTION_35 = 3;
    private static final double AFTER_INTERSECTION_5 = 3;

    /**
     * Private constructor for this singleton class
     */
    private EventHandler() {
        // Initialize the vehicle queues for each traffic light
        for (int i = 0; i < 4; i++) {
            northBoundVehs.set(i, new LinkedList<>());
        }

        // Initialize the traffic lights
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
        switch(event.name) {
            case ArrivalSouth:
                arrivalSouth(event.intersection, event.time, event.vehicle);
                break;
            case Departure:
                departure(event.intersection, event.time, event.vehicle);
                break;
            default:
                System.out.println("Error - EventHandler.handleEvent: Wrong Event!");
        }
    }

    private void arrivalSouth(int intersection, double time, Vehicle car) {
        int index = getIntersectionIndex(intersection);
        int numVehicleToPass = northBoundVehs.get(index).size();
        TrafficLight tl = trafficLights[index];
        // Number of cars can go through the traffic light in an entire green light duration
        double greenPass = Math.floor(tl.getSouthThroughGreen() / W);
        double departureTime;

        // Tell if the light is green.
        if (!tl.isThroughGreen(time)) {
            northBoundVehs.get(index).addFirst(car);
            double numGreens = Math.floor(numVehicleToPass / greenPass);
            double resPass = numVehicleToPass % greenPass;
            departureTime = tl.nextSouthThroughGreen(time, numGreens) + resPass * W;
        } else {
            double currPass = Math.floor((tl.nextSouthThroughRed(time) - time)/ W);
            // If all cars can go through the traffic light in current green duration
            if (currPass >= numVehicleToPass) {
                departureTime = time + numVehicleToPass * W;
            } else {
                numVehicleToPass -= currPass;
                double numGreens = Math.floor(numVehicleToPass / greenPass);
                double resPass = numVehicleToPass % greenPass;
                departureTime = tl.nextSouthThroughGreen(time, numGreens) + resPass * W;
            }
        }
        ProcessEvents.getEventQueue().add(new Event(departureTime, EventName.Departure, intersection, car));
    }

    private void departure(int intersection, double time, Vehicle car) {
        ProcessEvents.getEventQueue().add(new Event(time + getBetweenIntersectionTime(intersection), EventName.ArrivalSouth, intersection, car));
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
                return BETWEEN_INTERSECTION_12;
            case 2:
                return BETWEEN_INTERSECTION_23;
            case 3:
                return BETWEEN_INTERSECTION_35;
            case 5:
                return AFTER_INTERSECTION_5;
            default:
                System.out.println("Error - EventHandler.handleEvent: Wrong Intersection!");
                return -1;
        }
    }
}
