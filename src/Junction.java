import java.text.SimpleDateFormat;
import java.util.HashMap;

public class Junction extends Thread {

    /**
     * 0 1 2 3
     * N E S W
     */
    private Road[] incomingRoads;

    private HashMap<String, Road> outgoing;

    private int duration;

    private Clock clock;

    private Logger logger;


    public Junction(Clock clock, Logger logger, int durationGreen, Road[] incomingRoads, HashMap<String, Road> outgoing) {
        this.incomingRoads = incomingRoads;
        this.duration = durationGreen;
        this.outgoing = outgoing;
        this.clock = clock;
        this.logger = logger;
    }

    @Override
    public void run() {
        int x = 0;
        String log = "";

        while (clock.getTicks() != -1) {
            int counter = 0;
            int vehicleCounter = 0;
            long startTime = System.currentTimeMillis(); //fetch starting time
            Road incomingR = null;
            Road outgoingR;

            while ((System.currentTimeMillis() - startTime) < duration/clock.getTickrate()) {
                int roadidx = x % incomingRoads.length;


                incomingR = incomingRoads[roadidx];
                if (incomingR == null) {
                    System.out.println("incoming empty");
                } else {
                    if (!incomingR.isEmpty()) {
                        Vehicle v = incomingRoads[roadidx].getVehicle();

                        String dst = v.getDestination().toString();
                        outgoingR = outgoing.get(dst);

                        if (outgoingR != null && !outgoingR.isFull()) {
                            try {
                                sleep(2000 / clock.getTickrate());
                                v = incomingR.popVehicle();
                                outgoing.get(dst).addVehicle(v);
                                vehicleCounter++;
                            } catch (InterruptedException e) {
                                //e.printStackTrace();
                            }

                            //System.out.println("Putting vehicle on road: "+outgoing.get(dst).getName());

                        } else if (outgoingR != null && outgoingR.isFull()) {
                            counter++;
                        } else if (outgoingR == null) {
                            System.out.println("No Road applicable for Vehicle");
                        }


                    }


                }
            }

            if(incomingR != null) {

                if(counter == outgoing.size()) {
                    counter = 0;
                    log = log + "Time: " + new SimpleDateFormat("mm:ss").format(clock.getTicks()) + " - " + this.getName() + ": " + vehicleCounter + " cars through " + incomingR + ", " + incomingR.getAmountWaiting() + " waiting" + ". GRIDLOCK" + "\n";
                } else if(vehicleCounter >  0) {
                    log = log + "Time: " + new SimpleDateFormat("mm:ss").format(clock.getTicks()) + " - " + this.getName() + ": " + vehicleCounter + " cars through " + incomingR + ", " + incomingR.getAmountWaiting() + " waiting" + "\n";
                }

                vehicleCounter = 0;
            }
            x++;
        }
        logger.writeToFile(this.getName(), log);


    }
}