
public class Process implements Comparable<Process> {
    private String id;
    private int arrivalTime;
    private int[] burstTimes;
    private int[] ioTimes;
    private int currentBurst;
    private int currentIO;
    private Integer responseTime;
    private int waitingTime;
    private int turnaroundTime;
    private int currentBurstIndex;
    private int currentIOIndex;
    private String state;

    //Constructor

    public Process(String id, int[] burstTimes, int[] ioTimes, int arrivalTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTimes = burstTimes;
        this.ioTimes = ioTimes;
        this.currentBurstIndex = 0;
        this.currentIOIndex = 0;
        this.state="READY";
    }


    //Getters

    public int getArrivalTime() {
        return arrivalTime;
    }

    public String getId() {
        return id;
    }

    public int[] getBurstTimes() {
        return burstTimes;
    }

    public int[] getIoTimes() {
        return ioTimes;
    }

    public int getCurrentBurst() {
        return burstTimes[currentBurstIndex];
    }

    public int getCurrentIO() {
        return ioTimes[currentIOIndex];
    }

    public int getResponseTime() {
        return responseTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public String getState() {
        return state;
    }

    public int getCurrentBurstIndex() {
        return currentBurstIndex;
    }

    public int getCurrentIOIndex() {
        return currentIOIndex;
    }

    //Setters

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBurstTimes(int[] burstTimes) {
        this.burstTimes = burstTimes;
    }

    public void setIoTimes(int[] ioTimes) {
        this.ioTimes = ioTimes;
    }

    public void setCurrentBurst() {
        this.currentBurst = this.burstTimes[this.currentBurstIndex];
    }

    public void decrementCurrentBurst() { this.burstTimes[currentBurstIndex]--;}

    public void incrementCurrentBurst() {if(this.currentBurstIndex < this.burstTimes.length) this.burstTimes[currentBurstIndex]++; }

    public void incrementCurrentIO() {if(this.currentIOIndex < this.ioTimes.length) this.ioTimes[currentIOIndex]++;}

    public void decrementCurrentIO() { this.ioTimes[currentIOIndex]--;}

    public void setCurrentIO() {
        this.currentIO = ioTimes[this.currentIOIndex];
    }

    public void setResponseTime(int responseTime) {
        if(this.responseTime == null) this.responseTime = responseTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public void incrementCurrentBurstIndex() {if(this.currentBurstIndex < this.burstTimes.length-1) {this.currentBurstIndex++;} }

    public void incrementCurrentIOIndex() { if(this.currentIOIndex < this.ioTimes.length-1) {this.currentIOIndex++;} }

    public void setState(String state) {
        this.state = state;
    }

    public boolean onlyZeros() {
        for(int i : this.burstTimes) {
            if(i != 0) {
                return false;
            }
        }
        for(int j : this.ioTimes) {
            if(j != 0) {
                return false;
            }
        }
        return true;
    }


    @Override
    public int compareTo(Process comparedProc) {
        int compareArrivalTime = ((Process) comparedProc).getArrivalTime();
        return this.arrivalTime - compareArrivalTime;
    }
}
