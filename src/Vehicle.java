/**
 * No thread
 *
 * created by EntryPoint
 *
 * Ability to store destination, time entered Gridlock, time parked
 */

public class Vehicle {
    private CarPark destination;
    private long timeEntered;
    private long timeParked;
    private String objectID;

    public Vehicle(CarPark destination) {
        this.destination  = destination;
        this.objectID = String.valueOf(System.identityHashCode(this));
        this.timeEntered = System.currentTimeMillis();
    }

    public void parked() {
        this.timeParked = System.currentTimeMillis();
    }

    public CarPark getDestination() {
        return destination;
    }

    public void setDestination(CarPark destination) {
        this.destination = destination;
    }

    public String toString() {
        return objectID;
    }

    public long getTimeEntered() {
        return timeEntered;
    }


    public long getTimeParked() {
        return timeParked;
    }

}
