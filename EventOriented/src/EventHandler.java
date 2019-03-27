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
    private ArrayList<LinkedList<Vehicle>> southVehicleQueues;
    private TrafficLight[] trafficLights;

    // Waiting time for going through a traffic light per vehicle in (s)
    private static final double W = 1.0;

    /**
     * Private constructor for this singleton class
     */
    private EventHandler() {
        // Initialize the vehicle queues for each traffic light
        for (int i = 0; i < 4; i++) {
            southVehicleQueues.set(i, new LinkedList<>());
        }

        // Initialize the traffic lights
        trafficLights[0] = new TrafficLight(1, 10.6, 2.2, 38.3, 49.3);
        trafficLights[1] = new TrafficLight(2, 0, 0, 44.7, 55.4);
        trafficLights[2] = new TrafficLight(3, 0, 0, 64.6, 35.7);
        trafficLights[3] = new TrafficLight(5, 15.2, 0.5, 39.8, 45.3);
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
        int index = getStreedIndex(intersection);
        int numVehicleToPass = southVehicleQueues.get(index).size();
        TrafficLight tl = trafficLights[index];
        // Number of cars can go through the traffic light in an entire green light duration
        double greenPass = Math.floor(tl.getSouthThroughGreen() / W);
        double departureTime;

        // Tell if the light is green.
        if (!tl.isThroughGreen(time)) {
            southVehicleQueues.get(index).addFirst(car);
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
        ProcessEvents.eventQueue.add(new Event(departureTime, EventName.Departure, intersection, car));
    }

    private void departure(int street, double time, Vehicle car) {

    }

    private int getStreedIndex(int street) {
        switch(street) {
            case 1:
                return 0;
            case 11:
                return 1;
            case 12:
                return 2;
            case 14:
                return 3;
            default:
                System.out.println("Error - EventHandler.handleEvent: Wrong Street!");
                return -1;
        }
    }
}
