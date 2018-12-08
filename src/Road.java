public class Road {
    private RingBuffer<Vehicle> vehicles;
    private String name;
    private Clock clock;
    private Semaphore mutex;
    private Semaphore spaces;
    private Semaphore items;

    public Road(int capacity, String name, Clock clock) {
        this.vehicles = new RingBuffer<>(capacity);
        this.clock = clock;
        spaces = new Semaphore(capacity, clock);
        items = new Semaphore(0, clock);
        mutex = new Semaphore(1, clock);
        this.name = name;
    }

    public void addVehicle(Vehicle vehicle) {
        try {
            spaces.acquire();
            mutex.acquire();
            vehicles.push(vehicle);

            items.release();
            //System.out.println("items released (Add)");
            mutex.release();
            //System.out.println("Mutex released (Add)");
        } catch (InterruptedException e) {
            //e.printStackTrace();
        } catch (Exception e) {
            //System.out.println("Vehicle could not be pushed to road");
        }

        try {
            Thread.sleep((int) (Math.random() * 10));
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }

        // System.out.println("###"+toString()+" Vehicle " + vehicle + " added spaces: "+spaces+ " items: "+items);

    }

    /**
     * gets the first vehicle in the buffer
     *
     * @return Vehicle
     */

    public Vehicle popVehicle() throws NullPointerException{
        Vehicle v = null;
        try {
            items.acquire();
            mutex.acquire();
            if(!vehicles.isEmpty()) {
                try {
                    v = vehicles.pop();
                } catch (Exception e) {
                    System.out.println("Buffer underflow");
                }
                    Thread.sleep((int) (Math.random() * 10));
            } else {
                items.release();
            }

            if(v == null)
                throw new NullPointerException("No vehicle available");

            spaces.release();
            //System.out.println("******************space acquired (release)"+spaces);
            mutex.release();
            //System.out.println("Mutex released (get)");

        } catch (InterruptedException e) {
            //e.printStackTrace();
        } catch (NullPointerException ex) {

        }


        return v;
    }

    //get first vehicle witout popping it from the stack
    public Vehicle getVehicle() {
        return vehicles.getNextObject();
    }


    public boolean isEmpty() {
       return this.vehicles.isEmpty();
    }

    public boolean isFull() {
        return this.vehicles.isFull();
    }


    public int getAmountWaiting() {
        //System.out.println(items.getCounter() + " " + vehicles.getCount());
        //return this.items.getCounter();
        return this.vehicles.size();
    }

    public String toString() {
        return name;
    }
    public String getName() {
        return name;
    }
}
