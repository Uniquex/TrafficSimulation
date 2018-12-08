public class Clock extends Thread{
    private long ticks;
    private long timeframe;
    private int tickrate;
    private CarPark[] carParks;
    private Junction[] junctions;
    private Road[] roads;
    private EntryPoint[] entryPoints;
    private Thread[] threads;
    private Logger logger;

    /**
     *
     * @param timeframe timeframe in s
     * @param tickrate tickrate per second
     */
    public Clock(long timeframe, int tickrate, Logger logger) {
        this.ticks = 0;
        this.timeframe = (timeframe*1000);
        this.tickrate = tickrate;
        this.logger = logger;
    }

    public long getTicks() {
        return ticks;
    }

    public void addCarParks(CarPark[] carParks) {
        this.carParks = carParks;
    }

    public void setJunctions(Junction[] junctions) {
        this.junctions = junctions;
    }

    public void setRoads(Road[] roads) {
        this.roads = roads;
    }

    public void setEntryPoints(EntryPoint[] entryPoints) {
        this.entryPoints = entryPoints;
    }

    public static String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = ((seconds / 60) / 60) % 24;
        return String.format("%02d:%02d:%02d", h,m,s);
    }

    public void printCarParkStats(long time) {
        for(int x = 0; x < carParks.length; x++) {
            CarPark cp = carParks[x];
            String sTime =convertSecondsToHMmSs((time*tickrate)/1000);

            if(x == 0)
                System.out.println("Time: " + sTime + " min." + " " + cp.getName() + "    " + cp.getFreeCapacity() + " Spaces");
            else if (x == carParks.length-1) {
                System.out.println("      " + sTime + " min" + " " + cp.getName() + "    " + cp.getFreeCapacity() + " Spaces\n");
            }
            else
                System.out.println("      " + sTime + " min" + " " + cp.getName() + "    " + cp.getFreeCapacity() + " Spaces");

        }
    }

    public void printCarStats() {
        int cCreated = 0;
        int cParked = 0;
        int cOnRoad = 0;

        for(EntryPoint ep : entryPoints) {
            cCreated += ep.getCounter();
        }
        for(Road road : roads) {
            cOnRoad += road.getAmountWaiting();
        }
        for(CarPark cp : carParks) {
            cParked += cp.getVehicleAmount();
        }



        System.out.println(cCreated + " vehicles created " + cOnRoad + " vehicles on Road " + cParked + " vehicles parked " + (cCreated-(cOnRoad+cParked)) + " lost");
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        long timerCP = System.currentTimeMillis();

        do {
                this.ticks = System.currentTimeMillis() - startTime;
                //System.out.println((System.currentTimeMillis() - startTime) + " Tick: " + this.getTicks() + "  "+timeframe/tickrate);
                //Print CarPark stats every virtual 5 minutes
                if(System.currentTimeMillis() - timerCP > 300000/tickrate && this.ticks != -1) {
                    printCarParkStats(this.ticks);
                    timerCP = System.currentTimeMillis();
                }

        }  while((System.currentTimeMillis() - startTime) < timeframe/12);


        printCarParkStats(timeframe/12);

        this.ticks = -1;





        try {
            this.sleep(10000);
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }

        for(int i = 0; i < threads.length; i++) {
            threads[i].interrupt();
        }

        try {
            this.sleep(5000);
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
        System.out.println("\n");
        printCarStats();
    }

    public int getTickrate() {
        return tickrate;
    }

    public void setThreads(Thread[] threads) {
        this.threads = threads;
    }
}
