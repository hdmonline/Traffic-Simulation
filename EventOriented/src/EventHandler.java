import java.util.LinkedList;

public class EventHandler {
    private static LinkedList<Vehicle> vehicleQueue10 = new LinkedList<>(),
            vehicleQueue11 = new LinkedList<>(),
            vehicleQueue12 = new LinkedList<>(),
            vehicleQueue14 = new LinkedList<>();

    public static void handleEvent(double time, EventName event, Vehicle car) {
        switch(event) {
            case TLArrival10:
                arrival10(time, car);
                break;
            case TLArrival11:
                arrival11(time, car);
                break;
            case TLArrival12:
                arrival12(time, car);
                break;
            case TLArrival14:
                arrival14(time, car);
                break;
            default:
                System.out.println("Error - handleEvent: Wrong Event!");
        }
    }

    private static void arrival10(double time, Vehicle car) {
        int numVehicleToPass = vehicleQueue10.size();
        int greenPass = Math.floor();
    }

    private static void arrival11(double time, Vehicle car) {

    }

    private static void arrival12(double time, Vehicle car) {

    }

    private static void arrival14(double time, Vehicle car) {

    }
}
