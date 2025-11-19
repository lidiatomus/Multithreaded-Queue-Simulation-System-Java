package lidia_assignement2.model;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private boolean running;
    private int id;
    private Task currentTask;
    private int remainingTime = 0;

    public Server(int id) {
        this.id = id;
        this.tasks = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger(0);
        this.running = true;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    // add a new task to the queue
    public void addTask(Task newTask) {
        tasks.add(newTask);
        waitingPeriod.addAndGet(newTask.getServiceTime());
    }


    // process the tasks in the queue one by one
    @Override
    public void run() {
        while (running) {
            try {
                // wait for the next task
                currentTask = tasks.take(); // wait for next task
                remainingTime = currentTask.getServiceTime();

                // process the task
                while (remainingTime > 0) {
                    Thread.sleep(1000); // wait 1 second per unit
                    remainingTime--;
                }

                // task finished
                waitingPeriod.addAndGet(-currentTask.getServiceTime());
                currentTask = null;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Server " + id + " stopped.");
            }
        }
    }


    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }

    public int getId() {
        return id;
    }

    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    // get the status of the queue: task with its time, tasks in the queue
    public String getQueueStatus() {
        StringBuilder status = new StringBuilder();

        if (currentTask != null) {
            status.append("(")
                    .append(currentTask.getArrivalTime())
                    .append(",")
                    .append(currentTask.getServiceTime())
                    .append(")[").append(remainingTime).append("]; ");
        }

        for (Task task : tasks) {
            status.append(task.toString()).append("; ");
        }

        if (status.isEmpty()) {
            return "closed";
        }

        return status.toString();
    }

    // looks if the simulation can stop, no more clients in the queue
    public boolean isIdle() {
        return currentTask == null && tasks.isEmpty();
    }



    @Override
    public String toString() {
        return "Queue " + id + ": " + tasks.toString();
    }
}
