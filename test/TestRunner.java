package lidia_assignement2.test;

import lidia_assignement2.business.SimulationManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

public class TestRunner {

    public static void main(String[] args) {
        runTest("input_test1.txt", "log_test1.txt");
        runTest("input_test2.txt", "log_test2.txt");
        runTest("input_test3.txt", "log_test3.txt");
    }

    private static void runTest(String inputFile, String outputLogFile) {
        try {
            Properties props = new Properties();
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            props.load(reader);

            int clients = Integer.parseInt(props.getProperty("clients"));
            int queues = Integer.parseInt(props.getProperty("queues"));
            int timeLimit = Integer.parseInt(props.getProperty("timeLimit"));
            int minArrival = Integer.parseInt(props.getProperty("minArrival"));
            int maxArrival = Integer.parseInt(props.getProperty("maxArrival"));
            int minService = Integer.parseInt(props.getProperty("minService"));
            int maxService = Integer.parseInt(props.getProperty("maxService"));

            SimulationManager manager = new SimulationManager(
                    clients, queues, timeLimit,
                    minArrival, maxArrival,
                    minService, maxService,
                    outputLogFile
            );
            Thread t = new Thread(manager);
            t.start();
            t.join(); // wait for it to finish

            System.out.println("Finished " + inputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
