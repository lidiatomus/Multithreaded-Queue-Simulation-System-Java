package lidia_assignement2.business;

import lidia_assignement2.model.Server;
import lidia_assignement2.model.Task;
import lidia_assignement2.utilis.LogWriter;
import lidia_assignement2.ui.SimulationFrame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SimulationManager implements Runnable {
    private int timeLimit;
    private int maxProcessingTime;
    private int minProcessingTime;
    private int numberOfServers;
    private int numberOfClients;
    private int minArrivalTime;
    private int maxArrivalTime;
    private int peakHour = 0;
    private int maxLoad = 0;

    private SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;
    private List<Task> allDispatchedTasks = new ArrayList<>();

    private Scheduler scheduler;
    private List<Task> generatedTasks;
    private int currentTime;
    private LogWriter logWriter;
    private SimulationFrame frame;

    // constructor for testing
    public SimulationManager(int numberOfClients, int numberOfServers, int timeLimit, int minArrivalTime, int maxArrivalTime,  int minProcessingTime, int maxProcessingTime,  String logFileName) {
        this.numberOfClients = numberOfClients;
        this.numberOfServers = numberOfServers;
        this.timeLimit = timeLimit;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minProcessingTime = minProcessingTime;
        this.maxProcessingTime = maxProcessingTime;

        this.scheduler = new Scheduler(numberOfServers, maxProcessingTime);
        this.scheduler.changeStrategy(selectionPolicy);
        this.generatedTasks = new ArrayList<>();
        this.logWriter = new LogWriter(logFileName);
        this.allDispatchedTasks = new ArrayList<>();

        generateRandomClients();
    }

    // constructor for UI
    public SimulationManager(SimulationFrame frame) {
        this.frame = frame;

        // here we read UI inputs
        this.numberOfClients = Integer.parseInt(frame.clientsField.getText());
        this.numberOfServers = Integer.parseInt(frame.queuesField.getText());
        this.timeLimit = Integer.parseInt(frame.timeLimitField.getText());
        this.minArrivalTime = Integer.parseInt(frame.minArrivalField.getText());
        this.maxArrivalTime = Integer.parseInt(frame.maxArrivalField.getText());
        this.minProcessingTime = Integer.parseInt(frame.minServiceField.getText());
        this.maxProcessingTime = Integer.parseInt(frame.maxServiceField.getText());

        scheduler = new Scheduler(numberOfServers, maxProcessingTime);
        scheduler.changeStrategy(selectionPolicy);

        generatedTasks = new ArrayList<>();
        // here we create a log file
        logWriter = new LogWriter("simulation_log.txt");
        // we generate random clients
        generateRandomClients();
    }

    //function to generate random clients by using random arrival and service times
    private void generateRandomClients() {
        Random random = new Random();
        for (int i = 1; i <= numberOfClients; i++) {
            int arrivalTime = random.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
            int serviceTime = random.nextInt(maxProcessingTime - minProcessingTime + 1) + minProcessingTime;
            Task t = new Task(arrivalTime, serviceTime);
            generatedTasks.add(t);
        }
        // clients are sorted by arrival time
        Collections.sort(generatedTasks, (t1, t2) -> Integer.compare(t1.getArrivalTime(), t2.getArrivalTime()));
    }

    // function to check if the simulation is finished: there are no more clients and queues are empty
    private boolean isSimulationFinished() {
        if (!generatedTasks.isEmpty()) {
            return false;
        }

        for (Server server : scheduler.getServers()) {
            if (!server.isIdle()) {
                return false;
            }
        }

        return true;
    }

    // function to start the simulation
    @Override
    public void run() {
        currentTime = 0;
        while ((frame != null && currentTime <= timeLimit) || !isSimulationFinished()) // go until all clients are dealt with or time is not up
        {
            List<Task> toRemove = new ArrayList<>();
            // check if there are clients that can be dispatched(put in queues)
            for (Task t : generatedTasks) {
                if (t.getArrivalTime() <= currentTime) {
                    scheduler.dispatchTask(t, currentTime);
                    allDispatchedTasks.add(t); 
                    toRemove.add(t);
                }


            }
            generatedTasks.removeAll(toRemove);
            displayStatus();

            try {
                Thread.sleep(1000); // wait 1 second per iteration
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentTime++;
        }

        // at the end of the simulation, we write the log file and calculate the average waiting and service times and peak hour
        double[] averages = calculateAverages();
        double avgWait = averages[0];
        double avgService = averages[1];

        logWriter.write("Average waiting time: " + String.format("%.2f", avgWait));
        logWriter.write("Average service time: " + String.format("%.2f", avgService));
        logWriter.write("Peak hour: " + peakHour);
        logWriter.write("Simulation ended at time: " + currentTime);
        logWriter.close();

        showToUI("Average waiting time: " + String.format("%.2f", avgWait));
        showToUI("Average service time: " + String.format("%.2f", avgService));
        showToUI("Peak hour: " + peakHour);

    }

    private double[] calculateAverages() {
        int totalWaitingTime = 0;
        int totalServiceTime = 0;
        int count = 0;

        for (Task t : allDispatchedTasks) {
            totalWaitingTime += t.getWaitingTime();
            totalServiceTime += t.getServiceTime();
            count++;
        }

        double avgWait = count == 0 ? 0 : (double) totalWaitingTime / count;
        double avgService = count == 0 ? 0 : (double) totalServiceTime / count;

        return new double[]{avgWait, avgService};
    }

    // function to show the status of the simulation
    private void displayStatus() {
        StringBuilder log = new StringBuilder(); // a class to build a string for the log file(it s more efficient than string)
        log.append("Time ").append(currentTime).append("\n");

        log.append("Waiting clients: ");
        if (generatedTasks.isEmpty()) {
            log.append("none\n");
        } else {
            for (Task t : generatedTasks) {
                log.append(t.toString()).append("; ");
            }
            log.append("\n");
        }

        for (Server s : scheduler.getServers()) {
            log.append("Queue ").append(s.getId()).append(": ").append(s.getQueueStatus()).append("\n");
        }

        // calculate the peak hour
        int load = 0;
        for (Server s : scheduler.getServers()) {
            if (s.getCurrentTask() != null) load++;
            load += s.getTasks().size();
        }

        if (load > maxLoad) {
            maxLoad = load;
            peakHour = currentTime;
        }

        log.append("\n");
        System.out.print(log);
        logWriter.write(log.toString());
        showToUI(log.toString());
    }

    private void showToUI(String text) {
        if (frame == null) return; // here we skip UI
        SwingUtilities.invokeLater(() -> frame.logArea.append(text));
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimulationFrame frame = new SimulationFrame();
            frame.startButton.addActionListener(e -> {
                SimulationManager manager = new SimulationManager(frame);
                new Thread(manager).start();
            });
        });
    }
}
