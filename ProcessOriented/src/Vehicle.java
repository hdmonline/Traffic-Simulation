public class Vehicle implements Runnable {
    private int id;
    private double startTime;
    private double endTime;
    private Scheduler scheduler;

    // Constructors
    public Vehicle(int id) {
        this.id = id;
    }

    public Vehicle(int id, double startTime) {
        this.id = id;
        this.startTime = startTime;
        scheduler = Scheduler.getInstance();
    }

    /**
     * The process of each vehicle.
     *
     */
    @Override
    public void run() {
        
    }

    // Getters
    public int getId() {
        return id;
    }

    public double getStartTime() {
        return startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }
}
