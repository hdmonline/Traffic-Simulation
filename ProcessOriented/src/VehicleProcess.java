/**
 * VehicleProcess.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * The class of VehicleProcess.
 */

public class VehicleProcess implements Runnable {
    static final Scheduler scheduler = Scheduler.getInstance();
    int id;
    double startTime;
    double endTime;
    int entranceIntersection;
    Direction entranceDirection;
    int exitIntersection;
    Direction exitDirection;
    boolean pause;
    boolean entered;

    private EventHandler eventHandler;

    public VehicleProcess(int id, double startTime, int entranceIntersection, Direction entranceDirection) {
        this.id = id;
        this.startTime = startTime;
        this.entranceIntersection = entranceIntersection;
        this.entranceDirection = entranceDirection;
        eventHandler = EventHandler.getInstance();
        pause = false;
    }

    public String toString() {
        String str = "";
        str += id + ",";
        str += String.format("%.2f", startTime) + ",";
        str += String.format("%.2f", endTime) + ",";
        str += entranceIntersection + ",";
        str += entranceDirection + ",";
        str += exitIntersection + ",";
        str += exitDirection;
        return str;
    }

    private int getIntersection(int i) {
        if (i > 0 && i < 4) {
            return i;
        } else if (i == 4) {
            return 5;
        } else {
            System.out.println("Error - VehicleProcess.getIntersection: Wrong intersection index!");
            return -1;
        }
    }

    @Override
    public synchronized void run() {
        // Iterate 4 intersections
        for (int i = 1; i < 5; i++) {
            // Get intersection number from loop index
            int intersection = getIntersection(i);
            if (entranceIntersection <= intersection) {
                Direction direction = entered ? Direction.S : entranceDirection;
                // First intersection is special
                double delay;
                if (intersection == 1) {
                    // If coming from south, it need to travel a distance to arrive intersection 1
                    delay = entranceDirection == Direction.S ? Parameter.BETWEEN_START_INTERSECTION1 : 0;
                } else {
                    delay = entered ? Parameter.getBetweenIntersectionTime(intersection) : 0;
                }
                // Schedule resume event
                eventHandler.addScheduleEvent(new Event(scheduler.getTime() + delay,
                        EventType.Resume,
                        intersection,
                        direction,
                        this));
                // Wait for resuming (arrived intersection)
                synchronized (scheduler) {
                    scheduler.notify();
                }
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                entered = true;
                eventHandler.addScheduleEvent(new Event(scheduler.getTime(), EventType.WaitUntil, intersection, direction, this));
                // Wait for being able to cross the intersection
                synchronized (scheduler) {
                    scheduler.notify();
                }
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Leave the intersection, set availableSouth to true;
                eventHandler.getAvailableNorth()[eventHandler.getIntersectionIndex(intersection)] = true;
            }
        }
        // Exit to the North
        double delay = Parameter.AFTER_INTERSECTION_5;
        eventHandler.addScheduleEvent(new Event(scheduler.getTime() + delay,
                EventType.Exit,
                5,
                Direction.N,
                this));
        // Resume the scheduler
        synchronized (scheduler) {
            scheduler.notify();
        }
    }
}
