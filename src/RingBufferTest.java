public class RingBufferTest {
    public static void main(String[] args) {
        RingBuffer<Vehicle> rb = new RingBuffer<>(10);

        try {
            for (int x = 0; x < 10; x++) {
                rb.push(new Vehicle(null));
            }

            rb.pop();
            rb.pop();

            System.out.println(rb.size());

            rb.push(new Vehicle(null));

            System.out.println(rb.size());

            rb.pop();

            System.out.println(rb.size());

            System.out.println(rb.toString());

            Object[] veh = rb.getAllItems();

            Vehicle v = (Vehicle) veh[4];

            //rb.removeSpecific(v);

            rb.push(new Vehicle(null));

            System.out.println(rb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
