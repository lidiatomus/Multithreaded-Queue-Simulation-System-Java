package lidia_assignement2.business;

import lidia_assignement2.model.Server;
import lidia_assignement2.model.Task;
import java.util.ArrayList;
import java.util.List;
/*
*  create and start threads
*  dispatch tasks to the best server by using the strategy
* */

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        this.servers = new ArrayList<>();

        // create servers/queues and start threads
        for (int i = 0; i < maxNoServers; i++) {
            Server server = new Server(i + 1);
            servers.add(server);
            Thread thread = new Thread(server);
            thread.start();
        }
    }

    // for changing strategy
    public void changeStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ShortestQueueStrategy();
        }
        if (policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new TimeStrategy();
        }
    }

    // put a task in the best server based on the strategy
    public void dispatchTask(Task t, int currentTime) {
        Server bestServer = strategy.findBestServer(servers);
        bestServer.addTask(t);
        t.setDispatchTime(currentTime); // used to know when the task was put in the queue
        t.setWaitingTime(bestServer.getWaitingPeriod() - t.getServiceTime()); // used to know how long the task waited in the queue for calculating the waiting time
    }

    // return the list of servers
    public List<Server> getServers() {
        return servers;
    }
}
