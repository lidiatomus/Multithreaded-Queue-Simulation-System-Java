package lidia_assignement2.utilis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LogWriter {
    private BufferedWriter writer;

    public LogWriter(String fileName) {
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String text) {
        try {
            writer.write(text);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
