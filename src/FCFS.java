public class FCFS {

    public static void main(String[] args) {
        //instantiate processes
        Process P1 = new Process("P1", new int[]{4, 5, 6, 7, 6, 4, 5, 4}, new int[]{15, 31, 26, 24, 41, 51, 16}, 0);
        Process P2 = new Process("P2", new int[]{9, 11, 15, 12, 8, 11, 9, 10, 7}, new int[]{28, 22, 21, 28, 34, 34, 29, 31}, 0);
        Process P3 = new Process("P3", new int[]{24, 12, 6, 17, 11, 22, 18}, new int[]{28, 21, 27, 21, 54, 31}, 0);
        Process P4 = new Process("P4", new int[]{15, 14, 16, 18, 14, 13, 16, 15}, new int[]{35, 41, 45, 51, 61, 54, 61}, 0);
        Process P5 = new Process("P5", new int[]{6, 5, 15, 4, 7, 4, 6, 10, 3}, new int[]{22, 21, 31, 26, 31, 18, 21, 33}, 0);
        Process P6 = new Process("P6", new int[]{22, 27, 25, 11, 19, 18, 6, 6}, new int[]{38, 41, 29, 26, 32, 22, 26}, 0);
        Process P7 = new Process("P7", new int[]{4, 7, 6, 5, 4, 7, 6, 5, 6, 9}, new int[]{36, 31, 32, 41, 42, 39, 33, 34, 21}, 0);
        Process P8 = new Process("P8", new int[]{5, 4, 6, 4, 6, 5, 4, 6, 6}, new int[]{14, 33, 31, 31, 27, 21, 19, 11}, 0);

        ReadyQueue<Process> readyQueue = new ReadyQueue<Process>();

        ReadyQueue<Process> blockingQueue = new ReadyQueue<>();

        ReadyQueue<Process> completeQueue = new ReadyQueue<>();

        //add processes to ready queue
        readyQueue.enqueue(P1);
        readyQueue.enqueue(P2);
        readyQueue.enqueue(P3);
        readyQueue.enqueue(P4);
        readyQueue.enqueue(P5);
        readyQueue.enqueue(P6);
        readyQueue.enqueue(P7);
        readyQueue.enqueue(P8);


        Process bQueueProc;

        //initialize timer
        int curTime = 0;

        //count time that cpu is idle
        int idleTime = 0;

        //get first process in queue and set its state to EXECUTING
        Process curProc = readyQueue.dequeue();
        curProc.setState("EXECUTING");
        curProc.setResponseTime(curTime);

        System.out.println("Current Time:   " + curTime);
        System.out.println("Now running:    " + curProc.getId());
        System.out.println("Current burst: " + curProc.getCurrentBurst());
        System.out.println("------------------------------------");
        System.out.println("Ready Queue:    Process    Burst");
        for(int i = 0; i<readyQueue.size(); i++) {
            System.out.println("                " + readyQueue.getItem(i).getId() + "         " + readyQueue.getItem(i).getCurrentBurst());
        }
        System.out.println("------------------------------------");
        System.out.println("Now in I/O:     Process    Remaining I/O time");
        for(int j = 0; j<blockingQueue.size(); j++) {
            System.out.println("                " + blockingQueue.getItem(j).getId() + "         " + blockingQueue.getItem(j).getCurrentIO());

        }
        System.out.println("------------------------------------");
        System.out.println("------------------------------------");


        //main loop.  Each iteration represents 1 time unit.
        while(!readyQueue.isEmpty() || blockingQueue.hasProcesses() || curProc != null) {

            //increment timer
            curTime++;


            //decrement ioBurst of every process in blocking queue
            if(blockingQueue.hasProcesses()) {

                //new queue to hold items to remove from the blocking queue
                ReadyQueue<Process> toRemove = new ReadyQueue<>();

                for(int x = 0; x<blockingQueue.size(); x++) {
                    bQueueProc = blockingQueue.getItem(x);

                    //decrement current io burst if it is greater than 0
                    if(bQueueProc.getCurrentIO() > 0) {
                        bQueueProc.decrementCurrentIO();
                    }

                    //increment io index and mark process to be removed from blocking queue if its io burst = 0
                    if(bQueueProc.getCurrentIO() <= 0) {
                        bQueueProc.incrementCurrentIOIndex();
                        toRemove.enqueue(bQueueProc);
                        //if process has completed all cpu and i/o bursts, it is complete
                        if(bQueueProc.isComplete()) {
                            bQueueProc.setTurnaroundTime(curTime);
                            bQueueProc.setWaitingTime(bQueueProc.getTurnaroundTime() - bQueueProc.getTotalBurstTime());
                            completeQueue.enqueue(bQueueProc);
                            System.out.println("Current time: " + curTime);
                            System.out.println(bQueueProc.getId() + " HAS COMPLETED");
                            System.out.println("----------------------------------");
                        } else {
                            bQueueProc.setState("READY");
                            bQueueProc.setArrivalTime(curTime);
                            readyQueue.enqueue(bQueueProc);
                            if(curProc == null) {
                                curProc = readyQueue.dequeue();
                                curProc.setResponseTime(curTime);
                            }
                        }

                    }
                }

                //remove from the blocking queue all processes that are in toRemove
                for(int y=0; y<toRemove.size(); y++) {
                    blockingQueue.remove(toRemove.getItem(y));
                }
            }

            //curProc is null when there is no process in the ready queue.
            if(curProc != null) {
                //if state is EXECUTING
                if(curProc.getState() == "EXECUTING") {
                    //if current burst > 0
                    if (curProc.getCurrentBurst() > 0) {
                        //decrement current burst
                        curProc.decrementCurrentBurst();
                    }

                    //if  current burst is 0
                    if (curProc.getCurrentBurst() <= 0) {
                        //increment process's cpu burst index
                        curProc.incrementCurrentBurstIndex();
                        //set state to BLOCKING
                        curProc.setState("BLOCKING");
                    }

                    //test if active process is complete
                    if(curProc.isComplete()) {
                        System.out.println("Current time: " + curTime);
                        System.out.println(curProc.getId() + " HAS COMPLETED");
                        System.out.println("----------------------------------");
                        curProc.setTurnaroundTime(curTime);
                        curProc.setWaitingTime(curProc.getTurnaroundTime() - curProc.getTotalBurstTime());
                        completeQueue.enqueue(curProc);

                        if(readyQueue.hasProcesses()) {
                            curProc = readyQueue.dequeue();
                            curProc.setResponseTime(curTime);
                            curProc.setState("EXECUTING");
                        } else {
                            curProc = null;
                            continue;
                        }
                    }
                }

                //if state is BLOCKING
                if(curProc.getState() == "BLOCKING") {
                    //current process's new arrival time = current time + current io time
                    // curProc.setArrivalTime(curTime + curProc.getCurrentIO());
                    blockingQueue.enqueue(curProc);

                    if(readyQueue.hasProcesses()) {
                        curProc = readyQueue.dequeue();
                        curProc.setResponseTime(curTime);
                        curProc.setState("EXECUTING");

                        displayInfo(readyQueue, blockingQueue, completeQueue, curProc, curTime);
                    } else {
                        curProc = null;
                        displayInfo(readyQueue, blockingQueue, completeQueue, curProc, curTime);
                        continue;
                    }
                }

                //if state is READY
                //add process to end of ready queue
                if(curProc.getState() == "READY") {
                    readyQueue.enqueue(curProc);
                    curProc = readyQueue.dequeue();
                    curProc.setResponseTime(curTime);
                    curProc.setState("EXECUTING");
                }
            } else {
                idleTime++;
            }
        }
        System.out.println("------------------------------------");
        System.out.println("Current time: " + curTime);
        System.out.println("Idle time: " + idleTime);
        double cpuUtil = 100 - (((double)idleTime/(double)curTime) * 100);
        double rtSum = 0;
        double wtSum = 0;
        double ttSum = 0;
        System.out.println("Cpu Utilization: " + cpuUtil + "%");
        System.out.println("Complete:    Process    RT    WT    TT");
        for(int z = 0; z<completeQueue.size(); z++) {
            System.out.println("             " + completeQueue.getItem(z).getId() + "         " + completeQueue.getItem(z).getResponseTime() + "     " +
                    completeQueue.getItem(z).getWaitingTime() + "   " + completeQueue.getItem(z).getTurnaroundTime());
            rtSum += completeQueue.getItem(z).getResponseTime();
            wtSum += completeQueue.getItem(z).getWaitingTime();
            ttSum += completeQueue.getItem(z).getTurnaroundTime();
        }
        System.out.println("Avg:                    " + rtSum/8 + "   " + wtSum/8 + "  " + ttSum/8);

    }

    public static void displayInfo(ReadyQueue<Process> readyQueue, ReadyQueue<Process> blockingQueue, ReadyQueue<Process> completeQueue, Process curProc, int curTime) {
        if(curProc == null) {
            System.out.println("Current time: " + curTime);
            System.out.println("CPU IS IDLE");
            System.out.println("------------------------------------");
        } else {
            //output status info
            System.out.println("Current Time:   " + curTime);
            System.out.println("Now running:    " + curProc.getId());
            System.out.println("Current burst: " + curProc.getCurrentBurst());
            System.out.println("Current burst index: " + curProc.getCurrentBurstIndex() + "/" + curProc.getBurstTimesSize());
            System.out.println("Current io burst index: " + curProc.getCurrentIOIndex() + "/" + curProc.getIOTimesSize());
            System.out.println("------------------------------------");
            System.out.println("Ready Queue:    Process    Burst");
            for (int i = 0; i < readyQueue.size(); i++) {
                System.out.println("                " + readyQueue.getItem(i).getId() + "         " + readyQueue.getItem(i).getCurrentBurst());
            }
            System.out.println("------------------------------------");
            System.out.println("Now in I/O:     Process    Remaining I/O time");
            for (int j = 0; j < blockingQueue.size(); j++) {
                System.out.println("                " + blockingQueue.getItem(j).getId() + "         " + blockingQueue.getItem(j).getCurrentIO());

            }
            System.out.println("------------------------------------");
            System.out.println("Complete:     Process");
            for (int k = 0; k < completeQueue.size(); k++) {
                System.out.println("                " + completeQueue.getItem(k).getId());

            }
            System.out.println("------------------------------------");
            System.out.println("------------------------------------");
        }
    }
}
