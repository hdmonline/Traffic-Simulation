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

    public VehicleProcess(int id) {
        this.id = id;
        pause = false;
        entered = false;
    }

    public VehicleProcess(int id, double startTime, int entranceIntersection, Direction entranceDirection) {
        this.id = id;
        this.startTime = startTime;
        this.entranceIntersection = entranceIntersection;
        this.entranceDirection = entranceDirection;
        pause = false;
    }

    private void moveToIntersection1() {


    }

    public String toString() {
        String str = "";
        str += id + " ";
        str += String.format("%.2f", startTime) + " ";
        str += String.format("%.2f", endTime) + " ";
        str += entranceIntersection + " ";
        str += entranceDirection + " ";
        str += exitIntersection + " ";
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
        Thread currThread = Thread.currentThread();
        // Iterate 4 intersections
        for (int i = 1; i < 5; i++) {
            // Get intersection number from loop index
            int intersection = getIntersection(i);
            if (entranceIntersection <= intersection) {
                Direction direction = entered ? Direction.S : entranceDirection;
                // First intersection is special
                double delay;
                if (entranceIntersection == 1) {
                    // If coming from south, it need to travel a distance to arrive intersection 1
                    delay = entranceDirection == Direction.S ? Parameter.BETWEEN_START_INTERSECTION1 : 0;
                } else {
                    delay = entered ? Parameter.getBetweenIntersectionTime(intersection) : 0;
                }
                // Schedule resume event
                scheduler.addScheduleEvent(new Event(scheduler.getTime() + delay,
                        EventType.Resume,
                        intersection,
                        direction,
                        this,
                        currThread));
                // Wait for resuming
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                entered = true;
                scheduler.addScheduleEvent(new Event(scheduler.getTime(), EventType.WaitUntil, intersection, direction));
                // Wait for being able to cross the intersection
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        // Exit to the North
        double delay = Parameter.AFTER_INTERSECTION_5;
        scheduler.addScheduleEvent(new Event(scheduler.getTime() + delay,
                EventType.Exit,
                5,
                Direction.N,
                this,
                currThread));
    }
}
