package lidia_assignement2.model;

public class Task {
    private final int arrivalTime;
    private final int serviceTime;
    private int dispatchTime = -1; // default not set
    private int waitingTime = 0;

    public Task(int arrivalTime, int serviceTime) {
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setDispatchTime(int time) {
        this.dispatchTime = time;
    }

    public int getDispatchTime() {
        return dispatchTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    @Override
    public String toString() {
        return "(" + arrivalTime + ", " + serviceTime + ")";
    }
}
