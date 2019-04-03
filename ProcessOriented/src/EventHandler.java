/**
 * EventHandler.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * A singleton class for handling all kinds of events.
 */

import java.util.ArrayList;
import java.util.LinkedList;

//TODO: Consider to include this class in the Scheduler
public class EventHandler {
    private static EventHandler instance = null;

    // Vehicle queues, First -> In, Last -> Out
    private ArrayList<LinkedList<VehicleProcess>> southVehs;
    private TrafficLight[] trafficLights;


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

    public void handleEvent(Event event) {
        switch(event.type) {
            case Enter:
                startThread(event.thread);
                break;
            case Resume:
                // TODO
                break;
            case TurnGreen:
                // TODO
                break;
            case TurnRed:
                // TODO
                break;
            default:
                System.out.println("Error - EventHandler.handleEvent: Wrong Event!");
        }
    }

    private void startThread(Thread thread) {
        thread.start();
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
}
