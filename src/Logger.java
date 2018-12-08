import java.io.PrintWriter;

public class Logger{


    public Logger() {

    }

    public synchronized void writeToFile(String threadName, String content) {
        try {
            PrintWriter writer = new PrintWriter("C:\\Users\\wvitz\\Documents\\Java\\ASE_Assignment\\logs\\"+threadName+".txt", "UTF-8");
            writer.println(content);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
