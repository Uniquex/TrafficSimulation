import java.util.Random;

/**
 * Threaded
 * <p>
 * generating Vehicle objects with destinations
 * <p>
 * should give cars timestamp when entered town
 * <p>
 * destinations of cars should be randomly assigned
 * weighted: 10% University, 20% Station, 30% Shopping Centre, 40% Industrial Park
 */

public class EntryPoint extends Thread {

    private String name;
    private CarPark[] destinations;
    private Road connectedRoad;
    private int vehiclesPerTime;
    private Clock timer;
    private int counter;

    public EntryPoint(Clock timer, CarPark[] destinations, Road connectedRoad, int vehiclesPerTime, String name) {
        this.destinations = destinations;
        this.connectedRoad = connectedRoad;
        this.vehiclesPerTime = vehiclesPerTime;
        this.name = name;
        this.timer = timer;
        this.counter = 0;
    }

    public Vehicle generateVehicle() {

        Vehicle vehicle = null;

        //Probability
        double d1 = 0.1;
        double d2 = 0.3;
        double d3 = 0.6;
        double d4 = 1;


        double rand = new Random().nextFloat();

        vehicle = new Vehicle(destinations[0]);

        if (rand <= d1) {
            vehicle = new Vehicle(destinations[0]);
        } else if (rand <= d2 && rand > d1) {
            vehicle = new Vehicle(destinations[1]);
        } else if (rand <= d3 && rand > d2) {
            vehicle = new Vehicle(destinations[2]);
        } else if (rand <= d4 && rand > d3) {
            vehicle = new Vehicle(destinations[3]);
        }
        //System.out.println(toString() + " generated Vehicle " + vehicle + " with destination " + vehicle.getDestination() + " Vehicles on Road: " + connectedRoad.getAmountWaiting());
        counter++;
        return vehicle;

    }

    @Override
    public void run() {
        while (timer.getTicks() != -1) {
                if (!connectedRoad.isFull()) {
                    connectedRoad.addVehicle(generateVehicle());
                    try {
                        //Vehicles per hour / 3600 -> seconds per vehicle
                        //Seconds per Vehicle / tickrate
                        //Vehicle per Tickrate * 1000 -> tickrate vehicle per ms
                        Thread.sleep(((3600 / vehiclesPerTime) / timer.getTickrate()) * 1000);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                    }
                } else
                {
                    try {
                        //Vehicles per hour / 3600 -> seconds per vehicle
                        //Seconds per Vehicle / tickrate
                        //Vehicle per Tickrate * 1000 -> tickrate vehicle per ms
                        Thread.sleep(((3600 / vehiclesPerTime) / timer.getTickrate()) * 1000);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                    }
                }
            }
        }



    @Override
    public String toString() {
        return "EntryPoint{" +
                "name='" + name + '\'' +
                '}';
    }

    public int getCounter() {
        return counter;
    }
}




