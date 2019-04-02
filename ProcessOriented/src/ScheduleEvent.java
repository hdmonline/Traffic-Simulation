/**
 * ScheduleEvent.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * The class of ScheduleEvent.
 */
public class ScheduleEvent {
    double time;
    SchedulerEventType type;
    VehicleProcess veh;

    public ScheduleEvent(double time, SchedulerEventType type, VehicleProcess veh) {
        this.time = time;
        this.type = type;
        this.veh = veh;
    }
}
