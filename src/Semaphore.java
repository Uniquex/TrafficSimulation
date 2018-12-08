public class Semaphore {
    private int counter;
    private Clock clock;

    public Semaphore(int val, Clock clock) {
        if(val < 0)
            throw new IllegalArgumentException();
        else
            this.counter = val;

        this.clock = clock;
    }

    public void acquire() throws InterruptedException, NullPointerException{
        while(counter == 0) {
            Thread.sleep(500);
            if(clock.getTicks() == -1) {
                throw new NullPointerException("Clock has finished");
            }
        }
        counter--;
    }

    public void release() {
        counter++;
    }

    public int getCounter() {
        return counter;
    }

    public String toString(){
        return String.valueOf(counter);
    }
}
