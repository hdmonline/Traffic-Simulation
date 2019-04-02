/**
 * VehicleProcess.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * The class of VehicleProcess.
 */

public class VehicleProcess implements Runnable {
    static final Scheduler schedular = Scheduler.getInstance();
    int id;
    double startTime;
    double endTime;
    int entranceIntersection;
    Direction entranceDirection;
    int exitIntersection;
    Direction exitDirection;
    boolean pause;
    boolean arrive11;

    public VehicleProcess(int id) {
        this.id = id;
        pause = false;
        arrive11 = false;
    }

    public VehicleProcess(int id, double startTime, int entranceIntersection, Direction entranceDirection) {
        this.id = id;
        this.startTime = startTime;
        this.entranceIntersection = entranceIntersection;
        this.entranceDirection = entranceDirection;
        pause = false;
        arrive11 = false;
    }

    private synchronized void moveToIntersection1() {
        schedular.addSchedulerEvent(new ScheduleEvent(schedular.getTime(), SchedulerEventType.AdvanceTime, this));
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        arrive11 = true;
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

    @Override
    public void run() {
        moveToIntersection1();
        
    }
}
