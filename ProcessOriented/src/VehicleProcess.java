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
            System.err.println("Error - VehicleProcess.getIntersection: Wrong intersection index!");
            return -1;
        }
    }

    @Override
    public synchronized void run() {
        // Iterate 4 intersections
        boolean exitingToWestOrEast = false;
        for (int i = 1; i < 5; i++) {
            // Get intersection number from loop index
            int intersection = getIntersection(i);
            if (entranceIntersection <= intersection) {
                Direction direction = entered ? Direction.S : entranceDirection;
                // First intersection is special
                double delay;
                if (intersection == 1) {
                    // If coming from south, it need to travel a distance to arrive intersection 1
                    delay = entranceDirection == Direction.S ? Parameter.BETWEEN_START_INTERSECTION_1 : 0;
                } else {
                    delay = entered ? Parameter.getBetweenIntersectionTime(intersection) : 0;
                }
                // Schedule resume event
                eventHandler.addEvent(new Event(scheduler.getTime() + delay,
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
                boolean turningLeft = false;
                // If the vehicle is coming from south, it could be exiting to all 3 directions
                if (direction == Direction.S) {
                    double[] cumuProb = Parameter.getExitCumuProb(intersection);
                    double r = FileIo.rand.nextDouble();
                    if (r <= cumuProb[0]) {
                        // Exiting to the west
                        turningLeft = true;
                        exitingToWestOrEast = true;
                        exitIntersection = intersection;
                        exitDirection = Direction.W;
                    } else if (r <= cumuProb[1]) {
                        // Exiting to the east
                        exitingToWestOrEast = true;
                        exitIntersection = intersection;
                        exitDirection = Direction.E;
                    }
                }
                // Left turn if the vehicle is entering from west or exiting to the west
                turningLeft = turningLeft || direction == Direction.W;
                eventHandler.addEvent(new Event(scheduler.getTime(),
                        EventType.WaitUntil,
                        intersection,
                        direction,
                        this,
                        turningLeft));
                entered = true;
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
                eventHandler.setAvailable(intersection, turningLeft, direction, true);
                eventHandler.addEvent(new Event(Scheduler.getInstance().getTime(), EventType.CheckWait));
                if (exitingToWestOrEast) {
                    break;
                }
            }
        }
        // Exit
        double delay = exitingToWestOrEast ? 0 : Parameter.AFTER_INTERSECTION_5;
        exitIntersection = exitingToWestOrEast ? exitIntersection : 5;
        exitDirection = exitingToWestOrEast ? exitDirection : Direction.N;
        eventHandler.addEvent(new Event(scheduler.getTime() + delay,
                EventType.Exit,
                exitIntersection,
                exitDirection,
                this));
        // Resume the scheduler
        synchronized (scheduler) {
            scheduler.notify();
        }
    }
}
