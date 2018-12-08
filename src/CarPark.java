import java.text.SimpleDateFormat;
import java.util.Random;

public class CarPark extends Thread {

    private int capacity;
    private RingBuffer<Vehicle> vehicles;
    private int timeToPark;
    private String name;
    private Clock timer;
    private Road incomingRoad;
    private boolean isFull;

    //datafields for the advanced option
    private CarPark[] carParks;
    private Road outgoingRoad;


    public CarPark(Clock timer, int capacity, String name, Road incomingRoad, int timeToPark, Road outgoing) {
        this.capacity = capacity;
        this.vehicles = new RingBuffer<>(capacity);
        this.name = name;
        this.incomingRoad = incomingRoad;
        this.timeToPark = timeToPark;
        this.timer = timer;
        this.isFull = false;

        this.outgoingRoad = outgoing;

    }

    /**
     * Advanced Option
     * If carpark is full give queueing vehicles new destination
     * Requires additional roads leading from car parks away
     */
    public Vehicle giveVehicleNewDestination(Vehicle vehicle) {
        if (vehicle != null) {

            //Probability
            double d1 = 0.1;
            double d2 = 0.3;
            double d3 = 0.6;


            double rand = new Random().nextFloat();
            CarPark dest = null;
            CarPark[] tmp = carParks.clone();

            for (int x = 0; x < tmp.length; x++) {
                if (tmp[x] == this || tmp[x].getFreeCapacity() == 0) {
                    tmp[x] = null;
                    for (int y = 0; y < tmp.length - x; x++) {
                        tmp[x] = tmp[(x + 1) % tmp.length];
                    }
                }
            }

            if (rand <= d1 && tmp[0] != this) {
                dest = tmp[0];
            } else if (rand <= d2 && rand > d1 && tmp[1] != this) {
                dest = tmp[1];
            } else if (rand <= d3 && rand > d2 && tmp[2] != this) {
                dest = tmp[2];
            } else {
                dest = tmp[3];
            }

            try {
                vehicle.setDestination(dest);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            return vehicle;
        } else {
            return null;
        }
    }

    public String toString() {
        return name;
    }

    @Override
    public void run() {

        while (timer.getTicks() != -1) {

                Vehicle v = null;
                try {
                    if(!incomingRoad.isEmpty()) {
                        v = incomingRoad.popVehicle();

                        if (v != null && !vehicles.isFull()) {
                            //System.out.println(v.toString());
                            try {
                                vehicles.push(v);
                                Thread.sleep(timeToPark / timer.getTickrate());
                                v.parked();
                                //System.out.println("CarPark " + name + " filled spot #" + (vehicles.getIndexAvail() + 1) + " / " + capacity + " with Vehicle: " + v);
                            } catch (InterruptedException i) {
                                //i.printStackTrace();
                            } catch (Exception e) {
                                //e.printStackTrace();
                            }

                        } else if (vehicles.isFull() && v != null) {
                            if (!isFull) {
                                System.out.println(this + " is full rerouting to other carParks");
                                isFull = true;
                            }
                            v = giveVehicleNewDestination(v);
                            if (v != null) {
                                outgoingRoad.addVehicle(v);
                            }

                            try {
                                Thread.sleep(timeToPark / timer.getTickrate());
                            } catch (Exception e) {

                            }

                        }
                    }
                } catch (NullPointerException e) {
                    System.out.println("Null vehicle caught");
                } finally {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {

                    }
                }

        }
        try {
            System.out.println(this + "     " + vehicles.size() + " Vehicles parked, average journey time " + new SimpleDateFormat("mm:ss").format(getAverageJourneyTimer()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getVehicleAmount() {
        return vehicles.size();
    }


    public int getFreeCapacity() {
        return this.capacity - vehicles.size();
    }

    public double getAverageJourneyTimer() {
        double avg = 0;
        Object[] v = vehicles.getAllItems();
        for (int x = 0; x < vehicles.size(); x++) {
            Vehicle vx = (Vehicle) v[x];
            avg += vx.getTimeParked() - vx.getTimeEntered();
        }
        avg = avg / vehicles.size();
        avg = avg / timer.getTickrate();
        return avg * timer.getTickrate();
    }

    public void setCarParks(CarPark[] carParks) {
        this.carParks = carParks;
    }
}
