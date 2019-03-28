/**
 * ProcessEvents.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * The main entry point for CSE6730 Project 2
 */

import java.util.Iterator;
import java.util.PriorityQueue;

public class ProcessEvents {
    // Event queue to store all the event in the order of time
    private PriorityQueue<Event> eventQueue;
    // Current time
    private double now;

    public ProcessEvents() {
        this.now = 0;
        this.eventQueue = new PriorityQueue<>();
    }

    public static void main() {
        ProcessEvents processor = new ProcessEvents();
        EventHandler handler = EventHandler.getInstance();

        // processing loop
        Iterator itr = processor.eventQueue.iterator();
        while (itr.hasNext()) {
            Event currentEvent = processor.eventQueue.poll();
            processor.now = currentEvent.time;
            handler.handleEvent(currentEvent);
        }



    }

}
