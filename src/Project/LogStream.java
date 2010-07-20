package Project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class LogStream {
    private static LogStream instance;
    private FileWriter sysStream;
    private FileWriter eventStream;
    public static LogStream getInstance() {
        return (instance == null)?(instance = new LogStream()):instance;
    }
    private LogStream() {
        try {
            sysStream = new FileWriter(new File("test/sys.log"));
            eventStream = new FileWriter(new File("test/event.log"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sysPrint(String str) {
        try {
            sysStream.write(str+"\n");
            sysStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void eventPrint(String str) {
        try {
            eventStream.write(str+"\n");
            eventStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
