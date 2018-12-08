public class RingBuffer<T> {
    private T[] buffer;

    //datafield for the next available element
    private int indexAvail;

    //datafield for the first element in queue
    private int indexFirst;

    //item counter
    private int count;

    private int buffersize;

    public RingBuffer(int bufferSize) {
        this.buffersize = bufferSize;
        this.count = 0;
        buffer =  (T[]) new Object[bufferSize];

    }

    public void push(T element) throws Exception{
        if (count == buffer.length) {
            throw new Exception("Ring buffer overflow");
        }
        buffer[indexAvail] = element;
        indexAvail = (indexAvail + 1) % buffer.length;
        count++;
    }

    public T pop() throws Exception {
        if (isEmpty()) {
            throw new Exception("Ring buffer underflow");
        }
        T item = buffer[indexFirst];
        buffer[indexFirst] = null;                  // remove the first element in queue
        count--;
        indexFirst = (indexFirst + 1) % buffer.length;
        return item;
    }

    public boolean isEmpty() {
        if(count == 0)
            return true;
        else
            return false;
    }

    public boolean isFull() {
        if(count == buffersize)
            return true;
        else
            return false;
    }

    public int size() {
        return count;
    }

    public int getIndexAvail () {
        return indexAvail;
    }

    public int maxSize() {
        return buffersize;
    }

    public String toString() {
        String s = "Empty";
        if(!isEmpty()) {
            s = "Index: "+indexFirst + "\n";
            for (int x = 0; x < buffersize; x++) {
                int index = (x + indexFirst) % buffersize;
                if(buffer[index] != null)
                    s = s + " ["+index+"] " + buffer[index].toString() + "\n";
            }
        }
        return s;
    }

    public T[] getAllItems(){
        T[] b = (T[]) new Object[buffersize];

        for(int x = 0; x < buffersize; x++) {
            int index = (x + indexFirst) % buffersize;
            b[x] = buffer[index];
        }
        return b;

    }

    public T getNextObject() {
        return buffer[indexFirst];
    }


}
