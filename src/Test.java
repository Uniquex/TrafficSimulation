import java.util.HashMap;

public class Test {

    public static void main(String[] args) {
        /** Generation Test **/



        Logger logger = new Logger();
        //timer in seconds
        Clock timer = new Clock(3600, 12, logger);

        Road north = new Road(50, "north", timer);
        Road east = new Road(30, "east", timer);
        Road south = new Road(60, "south", timer);

        Road junction2to1 = new Road(10, "Rj2to1", timer);
        Road junction2to3 = new Road(10, "Rj2to3", timer);
        Road junction3to2 = new Road(10, "Rj3to2", timer);
        Road junction3to4 = new Road(7, "Rj3to4", timer);
        Road junction4to3 = new Road(7, "Rj4to3", timer);


        Road toUni = new Road(15, "toUni", timer);
        Road toStation = new Road(15, "toStation", timer);
        Road toSC = new Road(7, "toSC", timer);
        Road toIP = new Road(15, "toIP", timer);

        //advanced
        Road fromUni = new Road(150, "fromUni", timer);
        Road fromStation = new Road(150, "fromStation", timer);
        Road fromSC = new Road(70, "fromSC", timer);
        Road fromIP = new Road(150, "fromIP", timer);

        Road junction1to2 = new Road(10, "Rj1to2", timer);


        CarPark uni = new CarPark(timer,100, "University", toUni, 10000, fromUni);
        CarPark st = new CarPark(timer,150, "Station", toStation, 10000, fromStation);
        CarPark sc = new CarPark(timer,400, "Shopping Centre", toSC, 10000, fromSC);
        CarPark ip = new CarPark(timer,1000, "Industrial Park", toIP, 10000, fromIP);

        CarPark[] carParks = new CarPark[] {uni, st, sc, ip};

        uni.setCarParks(carParks);
        st.setCarParks(carParks);
        sc.setCarParks(carParks);
        ip.setCarParks(carParks);

        EntryPoint northE = new EntryPoint(timer, carParks, north, 550, "North");
        EntryPoint eastE = new EntryPoint(timer, carParks, east, 300, "East");
        EntryPoint southE = new EntryPoint(timer, carParks, south, 550, "South");




        int junctionDuration = 35000;

        Junction j1 = new Junction(timer, logger, junctionDuration, new Road[]{junction2to1, fromUni, fromStation}, new HashMap<>(){{
                                                                                                    put("University", toUni);
                                                                                                    put("Station", toStation);
                                                                                                    put("Industrial Park", junction1to2);
                                                                                                    put("Shopping Centre", junction1to2);}});

        Junction j2 = new Junction(timer, logger,junctionDuration, new Road[]{north, junction3to2, fromSC, junction1to2}, new HashMap<>(){{
                                                                                                    put("Shopping Centre", toSC);
                                                                                                    put("University", junction2to1);
                                                                                                    put("Station", junction2to1);
                                                                                                    put("Industrial Park", junction2to3);}});

        Junction j3 = new Junction(timer, logger, junctionDuration, new Road[]{east, junction2to3, junction4to3}, new HashMap<>(){{
                                                                                                    put("Industrial Park", junction3to4);
                                                                                                    put("Shopping Centre", junction3to2);
                                                                                                    put("University", junction2to1);
                                                                                                    put("Station", junction2to1);}});

        Junction j4 = new Junction(timer, logger,junctionDuration, new Road[]{south, junction3to4, fromIP}, new HashMap<>(){{
                                                                                                    put("Industrial Park", toIP);
                                                                                                    put("University", junction4to3);
                                                                                                    put("Shopping Centre", junction4to3);
                                                                                                    put("Station", toStation);}});

        timer.addCarParks(carParks);
        timer.setEntryPoints(new EntryPoint[]{northE, eastE, southE});
        timer.setRoads(new Road[]{north, south, east, junction1to2, junction2to1, junction2to3, junction3to2, junction3to4, junction4to3, toIP, toSC, toStation, toUni, fromIP, fromSC, fromStation, fromUni});
        timer.setJunctions(new Junction[]{j1, j2, j3, j4});

        j1.setName("Junction1");
        j2.setName("Junction2");
        j3.setName("Junction3");
        j4.setName("Junction4");
        northE.setName("NorthEntry");
        southE.setName("SouthEntry");
        eastE.setName("EastEntry");
        uni.setName("CP_University");
        sc.setName("CP_ShoppingCentre");
        st.setName("CP_Station");
        ip.setName("CP_Industrial Park");
        timer.setName("Clock");


        Thread[] tlist = new Thread[]{timer, j1, j2, j3, j4, northE, southE, eastE, uni, sc, st, ip};

        timer.setThreads(tlist);


        for(int x = 0; x < tlist.length; x++) {
            tlist[x].start();
        }



    }
}
